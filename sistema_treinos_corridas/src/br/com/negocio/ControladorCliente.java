package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.Notificacao;
import br.com.negocio.treinos.Usuario;
import java.util.List;

/**
 * Controlador (Camada de Negócio) para operações relacionadas a Clientes (Usuários).
 * Faz a ponte entre a Interface (Principal.java) e os Dados (RepositorioCliente).
 */
public class ControladorCliente {

    
    private IRepositorioCliente repositorioCliente;

    // --- Construtor ---
   
    public ControladorCliente(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    // --- Métodos de CRUD (Create, Read, Update, Delete) ---

    /**
     * Cadastra um novo cliente, se o CPF não existir.
     */
    public void cadastrarCliente(Usuario cliente) {
        if (repositorioCliente.buscarElementoPorCpf(cliente.getCpf()) == null) {
            repositorioCliente.adicionarElemento(cliente);
            System.out.println("Cliente cadastrado com sucesso!");
        } else {
            System.out.println("CPF já cadastrado.");
        }
    }

    //Busca um cliente pelo CPF.
    public Usuario buscarCliente(String cpf) {
        return repositorioCliente.buscarElementoPorCpf(cpf);
    }

    // Remove um cliente pelo CPF.
    public void removerCliente(String cpf) {
        if (repositorioCliente.buscarElementoPorCpf(cpf) != null) {
            repositorioCliente.removerElemento(cpf);
            System.out.println("Cliente removido com sucesso!");
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    // Atualiza os dados de um cliente existente.
    public void atualizarCliente(Usuario cliente) {
        if (repositorioCliente.buscarElementoPorCpf(cliente.getCpf()) != null) {
            repositorioCliente.atualizarElemento(cliente);
            System.out.println("Cliente atualizado com sucesso!");
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    // --- Métodos de Negócio Específicos ---

    /**
     * Exibe as notificações NÃO LIDAS de um usuário e, em seguida,
     * limpa as notificações que foram lidas.
     */
    public void verNotificacoes(Usuario usuario) {
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        List<Notificacao> notificacoes = usuario.getNotificacoes();
        int notificacoesNovas = 0; // Contador de notificações não lidas

        System.out.println("--- Notificações de " + usuario.getNome() + " ---");
        
        for (Notificacao n : notificacoes) {
            // Mostra apenas notificações que ainda não foram lidas
            if (!n.isLida()) {
                System.out.println(n); 
                n.setLida(true); // Marca a notificação como lida
                notificacoesNovas++;
            }
        }

        if (notificacoesNovas == 0) {
            System.out.println(usuario.getNome() + " não tem novas notificações.");
        }
        System.out.println("-------------------------------------");
        
        // --- LÓGICA PARA LIMPAR NOTIFICAÇÕES ---
        // Remove da lista todas as notificações que estão marcadas como "lida".
        // Isso impede que a lista cresça indefinidamente.
        boolean removidas = notificacoes.removeIf(Notificacao::isLida);
        
        if (removidas && notificacoesNovas > 0) {
            // (notificacoesNovas > 0) é para não exibir a msg se não tinha nada novo
            System.out.println(">>> Notificações lidas foram arquivadas (limpas).");
        }
    }
}