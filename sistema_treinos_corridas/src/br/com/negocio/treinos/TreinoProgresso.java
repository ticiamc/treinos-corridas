package br.com.negocio.treinos;

import java.util.Date;
import java.util.List;

/**
 * Classe utilitária (com métodos estáticos) responsável por processar
 * o progresso de um usuário quando um novo treino é registrado.
 * * Principal função: Verificar se o novo treino ajudou a bater alguma meta.
 */
public class TreinoProgresso {

    //Método auxiliar privado para marcar o status do treino.
    private static void verificarStatusTreino(Treino treino) {
        // Marca o treino como "concluído" ou "contabilizado"
        treino.setStatus(true); 
    }

    /**
     * Verifica o progresso de TODAS as metas do usuário com base no novo treino.
     * Se uma meta for atingida, marca o treino como 'status=true' (concluído)
     * e ADICIONA UMA NOTIFICAÇÃO ao usuário (REQ08).
     */
    public static void verificarProgressoMetas(Usuario usuario, Treino treino) {
        
        List<Meta> metas = usuario.getMetas();

        // Itera todas as metas do usuário
        for (Meta meta : metas) {
            
            // Só processa metas que ainda NÃO FORAM CONCLUÍDAS
            if (!meta.isConcluida()) {
                
                boolean metaAtingida = false; // Flag para controlar se a meta foi batida

                // --- Lógica PARA METAS DE DISTÂNCIA ---
                if (meta.getTipoMeta() == TipoMeta.DISTANCIA) {
                    
                    // Verifica se o treino é do tipo Corrida
                    if (treino instanceof Corrida) {
                        Corrida corrida = (Corrida) treino;
                        // Verifica se a distância da *corrida atual* é maior ou igual à meta
                        if (corrida.getDistanciaEmMetros() >= meta.getDistancia()) {
                            metaAtingida = true;
                        }
                    }
                } 
                
                // --- LÓGICA PARA METAS DE TEMPO ---
                else if (meta.getTipoMeta() == TipoMeta.TEMPO) {
                    // Verifica se a duração do *treino atual* (qualquer tipo) 
                    // é maior ou igual ao tempo da meta.
                    if (treino.getDuracaoEmMinutos() >= meta.getTempo()) {
                        metaAtingida = true;
                    }
                }

                // --- Se a meta foi atingida (seja por tempo ou distância) ---
                if (metaAtingida) {
                    // 1. Marca o treino como "contabilizado"
                    verificarStatusTreino(treino); 
                    
                    // 2. Marca a meta como concluída
                    meta.setConcluida(true);
                    
                    // 3. Cria a Notificação
                    String msg = "Parabéns! Você atingiu sua meta: " + meta.getDescricao();
                    Notificacao notificacao = new Notificacao(msg, new Date());
                    
                    // 4. Adiciona a notificação à lista do usuário
                    usuario.adicionarNotificacao(notificacao);
                    
                    // Avisa no console que a notificação foi gerada
                    System.out.println(">>> [NOTIFICAÇÃO GERADA] Meta atingida: " + meta.getDescricao());
                }
            } 
        } // Fim do loop for (passa para a próxima meta)
    }
}