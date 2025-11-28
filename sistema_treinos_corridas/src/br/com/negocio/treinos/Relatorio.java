package br.com.negocio.treinos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

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
                String detalhes = "";
                if (t instanceof Corrida) detalhes = String.format("%.2f km", ((Corrida) t).getDistanciaEmMetros() / 1000.0);
                else if (t instanceof Intervalado) detalhes = ((Intervalado) t).getSeries() + " séries";
                
                sb.append(String.format("Data: %s | Treino: %s | %s | %d min\n",
                    t.getDataExecucao().format(fmt), t.getNomeTreino(), detalhes, t.getDuracaoSegundos() / 60));
            }
        }
        sb.append("\n--- Fim do Relatório ---");
        return sb.toString();
    }

    public static void exportarRelatorioAtividadesCSV(Usuario cliente, String caminhoArquivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivo))) {
            writer.println("Data,Nome do Treino,Tipo,Duracao(min),Calorias,Detalhes");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Treino t : cliente.getTreinos()) {
                String tipo = (t instanceof Corrida) ? "Corrida" : "Intervalado";
                String detalhes = (t instanceof Corrida) 
                        ? String.valueOf(((Corrida) t).getDistanciaEmMetros()) 
                        : String.valueOf(((Intervalado) t).getSeries());
                writer.printf("%s,%s,%s,%d,%.2f,%s%n",
                        t.getDataExecucao().format(fmt), t.getNomeTreino(), tipo,
                        t.getDuracaoSegundos() / 60, t.calcularCaloriasQueimadas(cliente), detalhes);
            }
        }
    }

    public static String gerarRankingDesafioTexto(Desafio desafio) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Ranking: ").append(desafio.getNome()).append(" ===\n\n");
        List<ParticipacaoDesafio> participacoes = desafio.getParticipacoes();

        if (participacoes.isEmpty()) {
            sb.append("Ainda não há participantes neste desafio.\n");
            return sb.toString();
        }
        
        for (ParticipacaoDesafio p : participacoes) {
            p.setProgresso(calcularProgressoDesafio(p.getUsuario(), desafio));
        }
        Collections.sort(participacoes, (p1, p2) -> Double.compare(p2.getProgresso(), p1.getProgresso()));

        int pos = 1;
        for (ParticipacaoDesafio p : participacoes) {
            sb.append(pos).append("º Lugar - ").append(p.getUsuario().getNome())
              .append(": ").append(String.format("%.2f km", p.getProgresso() / 1000.0)).append("\n");
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
                if (!dataC.isBefore(desafio.getDataInicio()) && !dataC.isAfter(desafio.getDataFim())) {
                    progressoTotal += c.getDistanciaEmMetros();
                }
            }
        }
        return progressoTotal;
    }
}