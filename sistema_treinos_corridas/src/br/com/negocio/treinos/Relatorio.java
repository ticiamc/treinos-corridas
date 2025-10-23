package br.com.negocio.treinos;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Relatorio {

    private Relatorio() {
        // Classe utilitária, não deve ser instanciada
    }

    // Gera um relatório simples de todas as atividades de um cliente.
    public static void gerarRelatorioAtividades(Usuario cliente) {
        System.out.println("--- Relatório de Atividades para: " + cliente.getNome() + " ---");
        if (cliente.getTreinos().isEmpty()) {
            System.out.println("Nenhum treino registrado.");
            return;
        }
        for (Treino t : cliente.getTreinos()) {
            System.out.println(t); // O toString() do Treino/Corrida/Intervalado será usado
        }
        System.out.println("--- Fim do Relatório ---");
    }

    // Gera um relatório de evolução (ex: total de km).
    public static void gerarRelatorioEvolucao(Usuario cliente) {
        System.out.println("--- Relatório de Evolução para: " + cliente.getNome() + " ---");
        double distanciaTotal = 0;
        int totalCorridas = 0;

        for (Treino t : cliente.getTreinos()) {
            if (t instanceof Corrida) {
                distanciaTotal += ((Corrida) t).getDistanciaEmMetros();
                totalCorridas++;
            }
        }
        System.out.println("Corridas totais: " + totalCorridas);
        System.out.println("Distância total percorrida: " + (distanciaTotal / 1000) + " km");
        System.out.println("--- Fim do Relatório ---");
    }

    /**
     * Gera o ranking de um desafio.
     * CALCULA o progresso somando as corridas do usuário que estão dentro do período de datas do desafio.
     */
    public static void gerarRankingDesafio(Desafio desafio) {
        System.out.println("--- Ranking do Desafio: " + desafio.getDescricao() + " ---");
        List<ParticipacaoDesafio> participacoes = desafio.getParticipacoes();

        if (participacoes.isEmpty()) {
            System.out.println("Nenhum usuário participando deste desafio.");
            return;
        }

        // 1. Calcular o progresso real de cada participante
        for (ParticipacaoDesafio p : participacoes) {
            Usuario u = p.getUsuario(); 
            // Calcula o progresso real e o armazena no objeto ParticipacaoDesafio
            double progressoCalculado = calcularProgressoDesafio(u, desafio);
            p.setProgresso(progressoCalculado); 
        }

        // 2. Ordenar a lista
        Collections.sort(participacoes, new Comparator<ParticipacaoDesafio>() {
            @Override
            public int compare(ParticipacaoDesafio p1, ParticipacaoDesafio p2) {
                // Compara em ordem decrescente (do maior progresso para o menor)
                // Esta chamada agora está correta (getProgresso)
                return Double.compare(p2.getProgresso(), p1.getProgresso());
            }
        });

        // 3. Exibir o ranking
        int pos = 1;
        for (ParticipacaoDesafio p : participacoes) {
            System.out.printf("%dº Lugar: %s - Progresso: %.2f km\n",
                    pos,
                    p.getUsuario().getNome(),
                    (p.getProgresso() / 1000) // Converte de metros para km
            );
            pos++;
        }
        System.out.println("--- Fim do Ranking ---");
    }

    /**
     * --- MÉTODO AUXILIAR NOVO ---
     * Calcula o progresso (distância) de um usuário em um desafio.
     * Soma todas as distâncias de Corridas do usuário que ocorreram
     * ENTRE a data de início e fim do desafio.
     */
    private static double calcularProgressoDesafio(Usuario usuario, Desafio desafio) {
        double progressoTotal = 0;
        LocalDate inicioDesafio = desafio.getDataInicio();
        LocalDate fimDesafio = desafio.getDataFim();

        for (Treino treino : usuario.getTreinos()) {
            // Verifica se o treino é uma Corrida
            if (treino instanceof Corrida) {
                Corrida corrida = (Corrida) treino;
                
                LocalDate dataCorrida = corrida.getDataExecucao().toLocalDate();

                // Verifica se a data da corrida está dentro do período do desafio
                // (assume-se que as datas são inclusivas)
                if (!dataCorrida.isBefore(inicioDesafio) && !dataCorrida.isAfter(fimDesafio)) {
                    progressoTotal += corrida.getDistanciaEmMetros();
                }
            }
        }
        return progressoTotal;
    }
}