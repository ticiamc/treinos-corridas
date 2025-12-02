package br.com.negocio.treinos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TreinoProgresso {
    private TreinoProgresso() {}

    public static void verificarTodasMetas(Usuario usuario, Treino treino) {
        for (Meta meta : usuario.getMetas()) {
            
            if (meta.getStatus().equalsIgnoreCase("Pendente")) {
                
                LocalDate dataDoTreino = treino.getDataExecucao().toLocalDate();
                if (dataDoTreino.isAfter(meta.getDataFim()) || dataDoTreino.isBefore(meta.getDataInicio())) {
                    continue; 
                }

                if (meta.getTipo() == TipoMeta.CALORIAS) {
                    meta.adicionarProgresso(treino.calcularCaloriasQueimadas(usuario));
                } else if (meta.getTipo() == TipoMeta.TEMPO) {
                    meta.adicionarProgresso(treino.getDuracaoSegundos() / 60.0);
                } else if (meta.getTipo() == TipoMeta.DISTANCIA) {
                    if (treino instanceof Corrida) {
                        meta.adicionarProgresso(((Corrida) treino).getDistanciaEmMetros());
                    }
                }

                // --- NOVO: REQ20 - ALERTA DE META PRÓXIMA (>= 90%) ---
                double porcentagem = meta.getProgressoAtual() / meta.getValorAlvo();
                
                if (porcentagem >= 0.9 && porcentagem < 1.0 && !meta.isAvisoProximidadeEnviado()) {
                    Notificacao notificacao = new Notificacao(
                        UUID.randomUUID(),
                        "Força! Você já completou 90% da meta: " + meta.getDescricao(),
                        LocalDateTime.now()
                    );
                    usuario.adicionarNotificacao(notificacao);
                    meta.setAvisoProximidadeEnviado(true); // Marca para não repetir
                }
                // -----------------------------------------------------

                if (meta.getProgressoAtual() >= meta.getValorAlvo()) {
                    meta.setStatus("Concluída"); 
                    Notificacao notificacao = new Notificacao(
                        UUID.randomUUID(),
                        "Parabéns! Você concluiu a meta: " + meta.getDescricao(),
                        LocalDateTime.now()
                    );
                    usuario.adicionarNotificacao(notificacao);
                }
            }
        }
    }
}