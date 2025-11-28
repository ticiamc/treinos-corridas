package br.com.negocio.treinos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class TreinoProgresso {
    private TreinoProgresso() {}

    public static void verificarTodasMetas(Usuario usuario, Treino treino) {
        for (Meta meta : usuario.getMetas()) {
            
            // Só verifica metas que ainda estão "Pendente"
            if (meta.getStatus().equalsIgnoreCase("Pendente")) {
                
                //  Validação de Prazo 
                LocalDate dataDoTreino = treino.getDataExecucao().toLocalDate();
                LocalDate prazoFinal = meta.getDataFim();
                
                // Se o treino foi realizado DEPOIS do prazo da meta, ele não conta.
                if (dataDoTreino.isAfter(prazoFinal)) {
                    continue; // Pula para a próxima meta, ignorando esta
                }
                // -------------------------------------------------

                boolean metaBatida = false;

                // Lógica de verificação dos valores 
                if (treino instanceof Corrida) {
                    Corrida corrida = (Corrida) treino;
                    if (meta.getTipo() == TipoMeta.DISTANCIA) {
                        if (corrida.getDistanciaEmMetros() >= meta.getDistancia()) metaBatida = true;
                    } else if (meta.getTipo() == TipoMeta.TEMPO) {
                        if ((corrida.getDuracaoSegundos() / 60.0) >= meta.getTempo()) metaBatida = true;
                    }
                } else if (treino instanceof Intervalado) {
                    Intervalado intervalado = (Intervalado) treino;
                    if (meta.getTipo() == TipoMeta.TEMPO) {
                        if ((intervalado.getDuracaoSegundos() / 60.0) >= meta.getTempo()) metaBatida = true;
                    }
                }

                if (metaBatida) {
                    meta.setStatus("Concluída"); 
                    Notificacao notificacao = new Notificacao(
                        java.util.UUID.randomUUID(),
                        "Parabéns! Você atingiu sua meta: " + meta.getDescricao(),
                        LocalDateTime.now()
                    );
                    usuario.adicionarNotificacao(notificacao);
                }
            }
        }
    }
}