package br.com.negocio;

import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.TreinoProgresso;
import br.com.negocio.treinos.Usuario;

/**
 * Controlador para operações relacionadas a Treinos.
 */
public class ControladorTreino {
    
    //Cadastra um novo treino para um usuário e chama o TreinoProgresso para verificar se metas foram atingidas
     
    public void cadastrarTreino(Usuario usuario, Treino treino) {
        if (usuario != null && treino != null) {
            // 1. Adiciona o treino (Corrida, Intervalado, etc.) à lista do usuário
            usuario.adicionarTreino(treino);
            System.out.println("Treino cadastrado com sucesso!");

            // 2. Chama a classe de progresso
            // Isso verifica automaticamente se o treino bateu metas e gera notificações
            TreinoProgresso.verificarProgressoMetas(usuario, treino);
            
        } else {
            System.out.println("Erro ao cadastrar treino (usuário ou treino nulo).");
        }
    }

    // Lista todos os treinos cadastrados para um usuário específico.
    public void listarTreinos(Usuario usuario) {
        if (usuario != null && !usuario.getTreinos().isEmpty()) {
            System.out.println("--- Treinos de " + usuario.getNome() + " ---");
            for (Treino t : usuario.getTreinos()) {
                System.out.println("Data: " + t.getData() + ", Nome: " + t.getNome() + ", Duração: " + t.getDuracaoEmMinutos() + " min");
            }
        } else if (usuario != null) {
            System.out.println(usuario.getNome() + " não possui treinos cadastrados.");
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }
}