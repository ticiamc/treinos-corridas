package br.com.negocio.treinos;

import java.time.LocalDateTime;

/**
 * Classe utilitária (final) para verificar o progresso de treinos.
 * Não deve ser instanciada (por isso o construtor privado).
 * Substitui a antiga classe 'abstract'.
 */
public final class TreinoProgresso {

    /**
     * Construtor privado para impedir que esta classe utilitária seja instanciada.
     */
    private TreinoProgresso() {}

    /**
     * Verifica se um treino específico (que acabou de ser cadastrado) 
     * é suficiente para completar QUALQUER meta pendente do usuário.
     * * Se uma meta for completada:
     * 1. Altera o status da Meta para "Concluída".
     * 2. Cria e adiciona uma Notificação para o usuário (REQ08).
     *
     * @param usuario O usuário que completou o treino.
     * @param treino  O treino que foi registrado.
     */
    public static void verificarTodasMetas(Usuario usuario, Treino treino) {
        // Itera por todas as metas cadastradas do usuário
        for (Meta meta : usuario.getMetas()) {
            
            // Só verifica metas que ainda estão "Pendente"
            if (meta.getStatus().equalsIgnoreCase("Pendente")) {
                boolean metaBatida = false;

                // Lógica de verificação (baseada na sua implementação original)
                // Se o treino for uma Corrida...
                if (treino instanceof Corrida) {
                    Corrida corrida = (Corrida) treino;
                    
                    // ...e a meta for de DISTANCIA
                    if (meta.getTipo() == TipoMeta.DISTANCIA) {
                        if (corrida.getDistanciaEmMetros() >= meta.getDistancia()) {
                            metaBatida = true;
                        }
                    } 
                    // ...e a meta for de TEMPO
                    else if (meta.getTipo() == TipoMeta.TEMPO) {
                        // (Sua classe Meta não tem getTempo(), mas tem getDuracao(). Usando getDuracao())
                        // Assumindo que a duração da meta está em minutos
                        if (corrida.getDuracao() >= meta.getDuracao()) { 
                            metaBatida = true;
                        }
                    }
                } 
                // (Aqui poderia entrar a lógica para Intervalado, etc.)

                
                // Se a meta foi batida, atualize tudo!
                if (metaBatida) {
                    meta.setStatus("Concluída"); // REQ09
                    System.out.println("[SISTEMA] Meta '" + meta.getDescricao() + "' foi CONCLUÍDA!");

                    // REQ08: Criar e adicionar a notificação
                    Notificacao notificacao = new Notificacao(
                        java.util.UUID.randomUUID(),
                        "Parabéns! Você atingiu sua meta: " + meta.getDescricao(),
                        LocalDateTime.now()
                    );
                    usuario.adicionarNotificacao(notificacao); // Adiciona a notificação ao usuário
                    System.out.println("[SISTEMA] Notificação gerada para o usuário " + usuario.getNome());
                }
            }
        }
    }
}