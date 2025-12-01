package br.com.negocio.treinos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class TreinoProgresso {
    private TreinoProgresso() {}

    public static void verificarTodasMetas(Usuario usuario, Treino treino) {
        for (Meta meta : usuario.getMetas()) {
            
            // Só verifica metas Pendentes
            if (meta.getStatus().equalsIgnoreCase("Pendente")) {
                
                // 1. Validação de Prazo e Início
                LocalDate dataDoTreino = treino.getDataExecucao().toLocalDate();
                if (dataDoTreino.isAfter(meta.getDataFim()) || dataDoTreino.isBefore(meta.getDataInicio())) {
                    continue; // Treino fora do período da meta
                }

                // 2. Atualização do Progresso (Acumulativo)
                if (meta.getTipo() == TipoMeta.CALORIAS) {
                    // Qualquer treino gera calorias
                    meta.adicionarProgresso(treino.calcularCaloriasQueimadas(usuario));
                
                } else if (meta.getTipo() == TipoMeta.TEMPO) {
                    // Qualquer treino tem duração (convertendo segundos para minutos)
                    meta.adicionarProgresso(treino.getDuracaoSegundos() / 60.0);
                
                } else if (meta.getTipo() == TipoMeta.DISTANCIA) {
                    // Apenas Corrida tem distância
                    if (treino instanceof Corrida) {
                        meta.adicionarProgresso(((Corrida) treino).getDistanciaEmMetros());
                    }
                }

                // 3. Verificação de Conclusão
                if (meta.getProgressoAtual() >= meta.getValorAlvo()) {
                    meta.setStatus("Concluída"); 
                    Notificacao notificacao = new Notificacao(
                        java.util.UUID.randomUUID(),
                        "Parabéns! Você concluiu a meta: " + meta.getDescricao(),
                        LocalDateTime.now()
                    );
                    usuario.adicionarNotificacao(notificacao);
                }
            }
        }
    }
}