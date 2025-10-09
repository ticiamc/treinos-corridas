// package br.com.treinos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Relatorio {

    private final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * REQ16: Gera um relatório de atividades para um usuário em um período específico.
     */
    public String gerarRelatorioAtividades(Usuario usuario, LocalDate inicio, LocalDate fim) {
        StringBuilder conteudo = new StringBuilder();
        conteudo.append("====================================================\n");
        conteudo.append("Relatório de Atividades - ").append(usuario.getNome()).append("\n");
        conteudo.append("Período: ").append(inicio.format(FORMATADOR_DATA))
                .append(" a ").append(fim.format(FORMATADOR_DATA)).append("\n");
        conteudo.append("====================================================\n\n");

        List<Treino> treinosNoPeriodo = usuario.getTreinos().stream()
                .filter(t -> !t.getDataExecucao().toLocalDate().isBefore(inicio) && !t.getDataExecucao().toLocalDate().isAfter(fim))
                .collect(Collectors.toList());

        if (treinosNoPeriodo.isEmpty()) {
            conteudo.append("Nenhuma atividade registrada no período.\n");
        } else {
            for (Treino treino : treinosNoPeriodo) {
                conteudo.append(treino.toString()).append("\n"); // Supondo que a classe Treino tem um bom toString()
            }
        }
        
        conteudo.append("\n--- Fim do Relatório ---\n");
        return conteudo.toString();
    }

    /**
     * REQ17: Gera um relatório simples de evolução de desempenho.
     */
    public String gerarRelatorioEvolucao(Usuario usuario) {
        StringBuilder conteudo = new StringBuilder();
        conteudo.append("====================================================\n");
        conteudo.append("Relatório de Evolução - ").append(usuario.getNome()).append("\n");
        conteudo.append("====================================================\n\n");

        if (usuario.getTreinos().isEmpty()) {
            conteudo.append("Nenhum treino registrado para analisar a evolução.\n");
        } else {
            double distanciaTotal = usuario.getTreinos().stream()
                .filter(t -> t instanceof Corrida)
                .mapToDouble(t -> ((Corrida) t).getDistanciaEmMetros())
                .sum();

            long tempoTotalSegundos = usuario.getTreinos().stream()
                .mapToLong(Treino::getDuracaoSegundos)
                .sum();

            conteudo.append("Total de treinos registrados: ").append(usuario.getTreinos().size()).append("\n");
            conteudo.append(String.format("Distância total percorrida (em corridas): %.2f km\n", distanciaTotal / 1000));
            conteudo.append(String.format("Tempo total de treino: %d horas %d minutos\n", tempoTotalSegundos / 3600, (tempoTotalSegundos % 3600) / 60));
        }
        
        conteudo.append("\n--- Fim do Relatório ---\n");
        return conteudo.toString();
    }
    
    /**
     * REQ18: Gera o ranking de participantes de um desafio.
     */
    public String gerarRankingDesafio(Desafio desafio) {
        StringBuilder conteudo = new StringBuilder();
        conteudo.append("====================================================\n");
        conteudo.append("Ranking do Desafio - ").append(desafio.getNome()).append("\n");
        conteudo.append("====================================================\n\n");

        if (desafio.getParticipacoes().isEmpty()) {
            conteudo.append("Nenhum participante no desafio.\n");
        } else {
            // Ordena os participantes pelo progresso (do maior para o menor)
            List<ParticipacaoDesafio> ranking = desafio.getParticipacoes().stream()
                .sorted(Comparator.comparingDouble(ParticipacaoDesafio::getProgresso).reversed())
                .collect(Collectors.toList());
            
            int posicao = 1;
            for (ParticipacaoDesafio p : ranking) {
                conteudo.append(String.format("%dº Lugar: %s - Progresso: %.2f\n", 
                    posicao++, 
                    p.getUsuario().getNome(), 
                    p.getProgresso()));
            }
        }

        conteudo.append("\n--- Fim do Relatório ---\n");
        return conteudo.toString();
    }

    /**
     * REQ19: Exporta o conteúdo gerado para um formato específico (simulação).
     */
    public void exportar(String conteudo, String formato) {
        System.out.println("--- INÍCIO DA EXPORTAÇÃO PARA " + formato.toUpperCase() + " ---");
        System.out.println(conteudo);
        System.out.println("--- FIM DA EXPORTAÇÃO ---");
        System.out.println("Arquivo " + formato.toLowerCase() + " gerado com sucesso (simulação).");
    }
}
