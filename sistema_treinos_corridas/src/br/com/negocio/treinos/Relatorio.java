package br.com.negocio.treinos;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Relatorio {
    private Relatorio() {
    }

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // --- 1. RELATÓRIO VISUAL EM TELA (TEXTO) ---
    public static String gerarRelatorioAtividadesTexto(Usuario cliente) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Relatório de Atividades: ").append(cliente.getNome()).append(" ---\n\n");
        if (cliente.getTreinos().isEmpty()) {
            sb.append("Nenhuma atividade registrada neste perfil.\n");
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Treino t : cliente.getTreinos()) {
                sb.append(String.format("Data: %s | Treino: %s (%d min)\n",
                        t.getDataExecucao().format(fmt), t.getNomeTreino(), t.getDuracaoSegundos() / 60));
            }
        }
        sb.append("\n--- Fim do Relatório ---");
        return sb.toString();
    }

    // --- 2. RELATÓRIO POR PERÍODO ---
    public static String gerarRelatorioPorPeriodo(Usuario usuario, LocalDate inicio, LocalDate fim) {
        StringBuilder sb = new StringBuilder();
        sb.append("RELATÓRIO DE ATIVIDADES POR PERÍODO\n");
        sb.append("Usuário: ").append(usuario.getNome()).append("\n");
        sb.append("Período: ").append(df.format(inicio)).append(" até ").append(df.format(fim)).append("\n\n");

        double caloriasTotal = 0;
        int totalTreinos = 0;

        for (Treino t : usuario.getTreinos()) {
            LocalDate dataTreino = t.getDataExecucao().toLocalDate();

            if (!dataTreino.isBefore(inicio) && !dataTreino.isAfter(fim)) {
                totalTreinos++;
                double kcal = t.calcularCaloriasQueimadas(usuario);
                caloriasTotal += kcal;

                sb.append("Treino: ").append(t.getNomeTreino()).append("\n");
                sb.append("Data: ").append(df.format(dataTreino)).append("\n");
                sb.append("Duração: ").append(t.getDuracaoSegundos()).append(" segundos\n");
                sb.append("Calorias: ").append(String.format("%.2f", kcal)).append("\n");
                sb.append("----------------------------------------------\n");
            }
        }

        if (totalTreinos == 0) {
            sb.append("Nenhum treino encontrado no período.\n");
        } else {
            sb.append("\nTotal de Treinos: ").append(totalTreinos).append("\n");
            sb.append("Calorias Totais: ").append(String.format("%.2f", caloriasTotal)).append("\n");
        }

        return sb.toString();
    }

    // --- 3. RELATÓRIO DE DESEMPENHO ---
    public static String gerarRelatorioDesempenho(Usuario usuario) {
        StringBuilder sb = new StringBuilder();
        sb.append("RELATÓRIO DE DESEMPENHO\n");
        sb.append("Usuário: ").append(usuario.getNome()).append("\n\n");

        double calorias = 0;
        double distanciaTotal = 0;
        int totalTreinos = usuario.getTreinos().size();

        for (Treino t : usuario.getTreinos()) {
            calorias += t.calcularCaloriasQueimadas(usuario);

            // distância simulada (caso existam treinos específicos você pode substituir)
            if (t instanceof Corrida) {
                distanciaTotal += ((Corrida) t).getDistanciaEmMetros();
            }
        }

        sb.append("Total de Treinos: ").append(totalTreinos).append("\n");
        sb.append("Calorias Queimadas: ").append(String.format("%.2f kcal", calorias)).append("\n");
        sb.append("Distância Total: ").append(String.format("%.2f km", distanciaTotal)).append("\n\n");

        if (totalTreinos > 0) {
            sb.append("Calorias médias por treino: ")
                    .append(String.format("%.2f kcal", calorias / totalTreinos)).append("\n");
        }

        return sb.toString();
    }

    
    // --- 4. RELATÓRIO DE DESAFIOS ---
    public static String gerarRelatorioDesafios(List<Desafio> desafios, Usuario usuario) {
        StringBuilder sb = new StringBuilder();


        sb.append("===== RELATÓRIO DE DESAFIOS =====\n");
        sb.append("Usuário: ").append(usuario.getNome()).append("\n\n");


        if (desafios == null || desafios.isEmpty()) {
            sb.append("Não há desafios cadastrados no sistema.\n");
            return sb.toString();
        }


        for (Desafio d : desafios) {
            sb.append("----------------------------------------------------\n");
            sb.append("Desafio: ").append(d.getNome()).append("\n");
            sb.append("Período: ").append(df.format(d.getDataInicio()))
            .append(" até ").append(df.format(d.getDataFim())).append("\n");
            sb.append("Descrição: ").append(d.getDescricao()).append("\n\n");


            // --- Identificar se o usuário participa ---
            ParticipacaoDesafio minhaPart = null;
            for (ParticipacaoDesafio p : d.getParticipacoes()) {
                if (p.getUsuario().getCpf().equals(usuario.getCpf())) {
                    minhaPart = p;
                    break;
                }
            }


            if (minhaPart == null) {
                sb.append("Status: Você NÃO está participando deste desafio.\n\n");
            } else {
                double objetivo = 1.0; // Caso queira evoluir com metas de distância
                double progresso = calcularProgressoDesafio(usuario, d);
                double km = progresso / 1000.0;


                sb.append("Status: Participando\n");
                sb.append(String.format("Seu progresso: %.2f km\n", km));


                // Concluído?
                boolean concluido = LocalDate.now().isAfter(d.getDataFim());
                sb.append("Situação: ").append(concluido ? "Concluído\n\n" : "Em andamento\n\n");
            }
        
        // --- Ranking do desafio ---
            sb.append(">> Ranking do Desafio:\n");


            if (d.getParticipacoes().isEmpty()) {
                sb.append("Nenhum participante até o momento.\n\n");
                continue;
            }


            // Atualizar progresso de todos (igual ao gerarRankingDesafioTexto)
            for (ParticipacaoDesafio p : d.getParticipacoes()) {
                p.setProgresso(calcularProgressoDesafio(p.getUsuario(), d));
            }


            // Ordenação decrescente por distância percorrida
            Collections.sort(d.getParticipacoes(),
                    (p1, p2) -> Double.compare(p2.getProgresso(), p1.getProgresso()));


            int pos = 1;
            for (ParticipacaoDesafio p : d.getParticipacoes()) {
                sb.append(String.format("%dº - %s: %.2f km\n",
                        pos,
                        p.getUsuario().getNome(),
                        p.getProgresso() / 1000.0));
                pos++;
            }


            sb.append("\n");
        }


        sb.append("===== FIM DO RELATÓRIO =====\n");
        return sb.toString();
    }


    // --- 5. EXPORTAÇÃO EXCEL XML (.XLS) - SUBSTITUI O CSV "FEIO" ---
    // Este formato permite cores, larguras de coluna e formatação que o CSV não permite.
    public static void exportarRelatorioExcelLindo(Usuario cliente, String caminhoArquivo) throws IOException {
        if (!caminhoArquivo.toLowerCase().endsWith(".xls"))
            caminhoArquivo += ".xls";

        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivo, StandardCharsets.UTF_8))) {
            writer.println("<?xml version=\"1.0\"?>");
            writer.println("<?mso-application progid=\"Excel.Sheet\"?>");
            writer.println("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
            writer.println(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"");
            writer.println(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"");
            writer.println(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"");
            writer.println(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">");

            // Estilos (Cores e Fontes)
            writer.println(" <Styles>");
            writer.println(
                    "  <Style ss:ID=\"Default\" ss:Name=\"Normal\"><Alignment ss:Vertical=\"Bottom\"/><Borders/><Font ss:FontName=\"Calibri\" ss:Size=\"11\"/></Style>");
            writer.println(
                    "  <Style ss:ID=\"sHeader\"><Font ss:FontName=\"Calibri\" ss:Size=\"12\" ss:Bold=\"1\" ss:Color=\"#000000\"/><Interior ss:Color=\"#4AFF56\" ss:Pattern=\"Solid\"/><Borders><Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/></Borders><Alignment ss:Horizontal=\"Center\"/></Style>");
            writer.println(
                    "  <Style ss:ID=\"sDate\"><NumberFormat ss:Format=\"Short Date\"/><Alignment ss:Horizontal=\"Center\"/></Style>");
            writer.println("  <Style ss:ID=\"sCenter\"><Alignment ss:Horizontal=\"Center\"/></Style>");
            writer.println(" </Styles>");

            writer.println(" <Worksheet ss:Name=\"Relatorio Iron Track\">");
            writer.println("  <Table>");
            // Larguras das Colunas (Resolve o problema do ####)
            writer.println("   <Column ss:Width=\"100\"/>"); // Data
            writer.println("   <Column ss:Width=\"60\"/>"); // Hora
            writer.println("   <Column ss:Width=\"150\"/>"); // Nome
            writer.println("   <Column ss:Width=\"80\"/>"); // Tipo
            writer.println("   <Column ss:Width=\"80\"/>"); // Duração
            writer.println("   <Column ss:Width=\"80\"/>"); // Distância
            writer.println("   <Column ss:Width=\"80\"/>"); // Kcal

            // Cabeçalho
            writer.println("   <Row>");
            String[] headers = { "Data", "Hora", "Nome do Treino", "Tipo", "Duração (min)", "Distância (km)", "Kcal" };
            for (String h : headers)
                writer.println("    <Cell ss:StyleID=\"sHeader\"><Data ss:Type=\"String\">" + h + "</Data></Cell>");
            writer.println("   </Row>");

            DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH:mm");
            Locale br = Locale.forLanguageTag("pt-BR");

            for (Treino t : cliente.getTreinos()) {
                String tipo = (t instanceof Corrida) ? "Corrida" : "Intervalado";
                double dist = (t instanceof Corrida) ? ((Corrida) t).getDistanciaEmMetros() / 1000.0 : 0;

                writer.println("   <Row>");
                writer.println("    <Cell ss:StyleID=\"sDate\"><Data ss:Type=\"String\">"
                        + t.getDataExecucao().format(fmtData) + "</Data></Cell>");
                writer.println("    <Cell ss:StyleID=\"sCenter\"><Data ss:Type=\"String\">"
                        + t.getDataExecucao().format(fmtHora) + "</Data></Cell>");
                writer.println("    <Cell><Data ss:Type=\"String\">" + t.getNomeTreino() + "</Data></Cell>");
                writer.println("    <Cell ss:StyleID=\"sCenter\"><Data ss:Type=\"String\">" + tipo + "</Data></Cell>");
                writer.println("    <Cell ss:StyleID=\"sCenter\"><Data ss:Type=\"Number\">"
                        + (t.getDuracaoSegundos() / 60) + "</Data></Cell>");
                writer.println("    <Cell ss:StyleID=\"sCenter\"><Data ss:Type=\"Number\">"
                        + String.format(Locale.US, "%.2f", dist) + "</Data></Cell>");
                writer.println("    <Cell ss:StyleID=\"sCenter\"><Data ss:Type=\"Number\">"
                        + String.format(Locale.US, "%.0f", t.calcularCaloriasQueimadas(cliente)) + "</Data></Cell>");
                writer.println("   </Row>");
            }
            writer.println("  </Table>");
            writer.println(" </Worksheet>");
            writer.println("</Workbook>");
        }
    }

    // --- 6. EXPORTAÇÃO PDF NATIVO (SIMPLIFICADO) ---
    // Escreve a estrutura binária básica do PDF sem bibliotecas externas.
    public static void exportarPDFNativo(Usuario cliente, String caminhoArquivo) throws IOException {
        if (!caminhoArquivo.toLowerCase().endsWith(".pdf"))
            caminhoArquivo += ".pdf";

        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
            StringBuilder content = new StringBuilder();

            // Título e Cabeçalho
            content.append("BT /F1 18 Tf 50 800 Td (IRON TRACK - RELATORIO DE PERFORMANCE) Tj ET\n");
            content.append("BT /F1 12 Tf 50 770 Td (Atleta: " + cliente.getNome() + ") Tj ET\n");
            content.append("BT /F1 10 Tf 50 755 Td (CPF: " + cliente.getCpf() + ") Tj ET\n");

            // Cabeçalho da Tabela
            int y = 720;
            content.append("BT /F1 10 Tf 50 " + y
                    + " Td (DATA       | TIPO        | NOME                | TEMPO   | KCAL) Tj ET\n");
            content.append("1.0 w 50 " + (y - 5) + " m 500 " + (y - 5) + " l S\n"); // Linha
            y -= 20;

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Treino t : cliente.getTreinos()) {
                if (y < 50)
                    break; // Limite de página simples (simplificação)

                String tipo = (t instanceof Corrida) ? "Corrida" : "Interval";
                String nome = t.getNomeTreino().length() > 15 ? t.getNomeTreino().substring(0, 15) : t.getNomeTreino();
                String linha = String.format("%s | %-10s | %-18s | %3d min | %.0f",
                        t.getDataExecucao().format(fmt),
                        tipo,
                        nome,
                        t.getDuracaoSegundos() / 60,
                        t.calcularCaloriasQueimadas(cliente));

                // Sanitizar parênteses para PDF
                linha = linha.replace("(", "\\(").replace(")", "\\)");

                content.append("BT /F1 10 Tf 50 " + y + " Td (" + linha + ") Tj ET\n");
                y -= 15;
            }

            // --- Estrutura Mínima do PDF (Cross-Reference e Trailer) ---
            String streamData = content.toString();
            int streamLength = streamData.length();

            String pdfHeader = "%PDF-1.4\n";
            String obj1_Catalog = "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n";
            String obj2_Pages = "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n";
            String obj3_Page = "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>\nendobj\n";
            String obj4_Font = "4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n";
            String obj5_Stream = "5 0 obj\n<< /Length " + streamLength + " >>\nstream\n" + streamData
                    + "\nendstream\nendobj\n";

            fos.write(pdfHeader.getBytes());
            fos.write(obj1_Catalog.getBytes());
            fos.write(obj2_Pages.getBytes());
            fos.write(obj3_Page.getBytes());
            fos.write(obj4_Font.getBytes());
            fos.write(obj5_Stream.getBytes());

            // Xref simples (offset fictício simplificado para compatibilidade básica)
            fos.write(
                    "xref\n0 6\n0000000000 65535 f \n0000000010 00000 n \n0000000060 00000 n \n0000000117 00000 n \n0000000260 00000 n \n0000000348 00000 n \n"
                            .getBytes());
            fos.write(("trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n450\n%%EOF").getBytes());
        }
    }

    // Métodos de Desafio mantidos
    public static String gerarRankingDesafioTexto(Desafio desafio) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Ranking: ").append(desafio.getNome()).append(" ===\n\n");
        List<ParticipacaoDesafio> participacoes = desafio.getParticipacoes();
        if (participacoes.isEmpty())
            return "Ainda não há participantes neste desafio.\n";
        for (ParticipacaoDesafio p : participacoes)
            p.setProgresso(calcularProgressoDesafio(p.getUsuario(), desafio));
        Collections.sort(participacoes, (p1, p2) -> Double.compare(p2.getProgresso(), p1.getProgresso()));
        int pos = 1;
        for (ParticipacaoDesafio p : participacoes) {
            sb.append(pos).append("º Lugar - ").append(p.getUsuario().getNome()).append(": ")
                    .append(String.format("%.2f km", p.getProgresso() / 1000.0)).append("\n");
            pos++;
        }
        return sb.toString();
    }

    private static double calcularProgressoDesafio(Usuario usuario, Desafio desafio) {
        double progressoTotal = 0;
        for (Treino treino : usuario.getTreinos()) {
            if (treino instanceof Corrida) {
                Corrida c = (Corrida) treino;
                java.time.LocalDate dataC = c.getDataExecucao().toLocalDate();
                if (!dataC.isBefore(desafio.getDataInicio()) && !dataC.isAfter(desafio.getDataFim()))
                    progressoTotal += c.getDistanciaEmMetros();
            }
        }
        return progressoTotal;
    }
}