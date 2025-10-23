package br.com.negocio;

import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.TreinoProgresso; // Importar a classe
import br.com.negocio.treinos.Usuario;

public class ControladorTreino {

    // (Outros métodos como buscarTreino, listarTreinos, etc. permanecem iguais)
    
    public void cadastrarTreino(Usuario cliente, Treino treino) {
        if (cliente != null && treino != null) {
            cliente.adicionarTreino(treino);
            System.out.println("Treino cadastrado com sucesso para " + cliente.getNome());

            // --- CORREÇÃO (REQ09 e REQ08) ---
            // Após cadastrar o treino, chama o método para verificar 
            // automaticamente se alguma meta foi atingida e gerar notificações.
            TreinoProgresso.verificarTodasMetas(cliente, treino);
            // --- FIM DA CORREÇÃO ---

        } else {
            System.out.println("Falha ao cadastrar treino. Usuário ou Treino nulo.");
        }
    }

    public void atualizarTreino(Usuario cliente, Treino treinoAntigo, Treino treinoNovo) {
        // Implementação do método de atualização (se necessário)
    }

    public void removerTreino(Usuario cliente, Treino treino) {
        if (cliente != null && treino != null) {
            cliente.removerTreino(treino);
            System.out.println("Treino removido com sucesso.");
        } else {
            System.out.println("Falha ao remover treino.");
        }
    }
}