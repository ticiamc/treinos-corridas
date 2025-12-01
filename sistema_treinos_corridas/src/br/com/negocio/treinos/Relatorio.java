package br.com.negocio.treinos;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

// Importações do iText (Necessário adicionar o .jar do iText ao projeto)
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

public class Relatorio {
    private Relatorio() {}

    public static String gerarRelatorioAtividadesTexto(Usuario cliente) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Relatório de Atividades: ").append(cliente.getNome()).append(" ---\n\n");
        
        if (cliente.getTreinos().isEmpty()) {
            sb.append("Nenhuma atividade registrada neste perfil.\n");
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Treino t : cliente.getTreinos()) {
                String detalhes = getDetalhesTreino(t);
                sb.append(String.format("Data: %s | Treino: %s | %s | %d min\n",
                    t.getDataExecucao().format(fmt), t.getNomeTreino(), detalhes, t.getDuracaoSegundos() / 60));
            }
        }
        sb.append("\n--- Fim do Relatório ---");
        return sb.toString();
    }

    public static void exportarRelatorioAtividadesCSV(Usuario cliente, String caminhoArquivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivo))) {
            writer.println("Data,Nome do Treino,Tipo,Duracao(min),Calorias,Detalhes,VelocidadeMedia(km/h)");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Treino t : cliente.getTreinos()) {
                String tipo = (t instanceof Corrida) ? "Corrida" : "Intervalado";
                String detalhes = "";
                String velocidade = "0.0";

                if (t instanceof Corrida) {
                    Corrida c = (Corrida) t;
                    detalhes = String.valueOf(c.getDistanciaEmMetros());
                    velocidade = String.format("%.2f", c.calcularVelocidadeMediaKmPorHora()).replace(',', '.');
                } else if (t instanceof Intervalado) {
                    detalhes = String.valueOf(((Intervalado) t).getSeries());
                }

                writer.printf("%s,%s,%s,%d,%.2f,%s,%s%n",
                        t.getDataExecucao().format(fmt), t.getNomeTreino(), tipo,
                        t.getDuracaoSegundos() / 60, t.calcularCaloriasQueimadas(cliente), detalhes, velocidade);
            }
        }
    }

    // --- NOVO: Método para Exportar PDF (Requisito de Lib Externa) ---
    public static void exportarRelatorioAtividadesPDF(Usuario cliente, String caminhoArquivo) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
        document.open();

        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font fontCorpo = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Relatório de Treinos - Iron Track", fontTitulo));
        document.add(new Paragraph("Atleta: " + cliente.getNome(), fontCorpo));
        document.add(new Paragraph("Gerado em: " + java.time.LocalDate.now(), fontCorpo));
        document.add(new Paragraph("--------------------------------------------------"));
        document.add(new Paragraph(" ")); // Espaço

        if (cliente.getTreinos().isEmpty()) {
            document.add(new Paragraph("Nenhum treino registrado.", fontCorpo));
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Treino t : cliente.getTreinos()) {
                String linha = String.format("%s - %s (%d min) - %s", 
                    t.getDataExecucao().format(fmt), 
                    t.getNomeTreino(), 
                    t.getDuracaoSegundos() / 60,
                    getDetalhesTreino(t));
                
                document.add(new Paragraph(linha, fontCorpo));
            }
        }
        document.close();
    }

    private static String getDetalhesTreino(Treino t) {
        if (t instanceof Corrida) {
            Corrida c = (Corrida) t;
            return String.format("%.2f km | Vel: %.1f km/h", c.getDistanciaEmMetros() / 1000.0, c.calcularVelocidadeMediaKmPorHora());
        } else if (t instanceof Intervalado) {
            return ((Intervalado) t).getSeries() + " séries";
        }
        return "";
    }

    public static String gerarRankingDesafioTexto(Desafio desafio) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Ranking: ").append(desafio.getNome()).append(" ===\n\n");
        List<ParticipacaoDesafio> participacoes = desafio.getParticipacoes();

        if (participacoes.isEmpty()) {
            sb.append("Ainda não há participantes neste desafio.\n");
            return sb.toString();
        }
        
        // Ordenação por progresso
        participacoes.sort((p1, p2) -> Double.compare(p2.getProgresso(), p1.getProgresso()));

        int pos = 1;
        for (ParticipacaoDesafio p : participacoes) {
            sb.append(pos).append("º Lugar - ").append(p.getUsuario().getNome())
              .append(": ").append(String.format("%.2f km", p.getProgresso() / 1000.0)).append("\n");
            pos++;
        }
        return sb.toString();
    }
}