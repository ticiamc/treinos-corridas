package br.com.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.Corrida;
import br.com.negocio.treinos.Intervalado;
import br.com.negocio.treinos.Meta;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.TreinoProgresso;
import br.com.negocio.treinos.Usuario;

/**
 * Controlador (Camada de Negócio) responsável pelas regras
 * relacionadas aos Treinos de um usuário.
 */
public class ControladorTreino {

    private IRepositorioCliente repositorioCliente;

    public ControladorTreino(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    /**
     * Método principal para cadastrar um novo treino (Corrida ou Intervalado)
     * para um usuário e verificar automaticamente o progresso das metas.
     */
   
    // Recebe os dados brutos do Principal.java
    public void cadastrarTreino(String cpfUsuario, String tipoTreino, LocalDate data, int duracaoSegundos, String descricao, double distancia, int series, int descanso) {
        
        // 1. Busca o usuário no repositório
       
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        
        if (usuario != null) {
            Treino treino; // Objeto 'Treino' que será criado
            LocalDateTime dataExecucao = data.atStartOfDay(); // Converte LocalDate para LocalDateTime

            // 2. Cria o tipo de treino correto
            if ("Corrida".equalsIgnoreCase(tipoTreino)) {
                treino = new Corrida(descricao, dataExecucao, duracaoSegundos, distancia);
            } else if ("Intervalado".equalsIgnoreCase(tipoTreino)) {
                treino = new Intervalado(descricao, dataExecucao, duracaoSegundos, series, descanso);
            } else {
                System.out.println("ERRO: Tipo de treino inválido.");
                return;
            }
            
            // 3. Adiciona o treino ao usuário e salva
            usuario.adicionarTreino(treino);
            repositorioCliente.atualizarElemento(usuario); // Persiste a mudança no repositório
            System.out.println("SUCESSO: Treino cadastrado para " + usuario.getNome() + ".");
            System.out.println("[SISTEMA] Verificando progresso de metas...");

           
            // 4. Pega a lista de metas do usuário
            List<Meta> metasDoUsuario = usuario.getMetas();
            
            if (metasDoUsuario.isEmpty()) {
                System.out.println("[SISTEMA] Usuário não possui metas ativas.");
                // Não precisa 'return', pois a verificação de progresso abaixo lidará com a lista vazia.
            }

            // 5. Chama a lógica de verificação (do TreinoProgresso.java)
            // A classe TreinoProgresso lida com o loop, a verificação e a notificação.
            TreinoProgresso.verificarTodasMetas(usuario, treino);
            
            // 6. Atualiza o usuário no repositório (CASO A VERIFICAÇÃO TENHA GERADO NOTIFICAÇÕES)
            // A classe TreinoProgresso adiciona a notificação ao objeto 'usuario'.
            repositorioCliente.atualizarElemento(usuario);
            
            
        } else {
            System.out.println("ERRO: Usuário com CPF " + cpfUsuario + " não encontrado.");
        }
    }

    /**
     * Busca um treino específico na lista de um usuário.
     */
    public Treino buscarTreino(Usuario usuario, int idTreino) {
        if (usuario != null) {
            return usuario.buscarTreinoPorId(idTreino);
        }
        return null; // Não encontrou
    }

    //Remove um treino da lista de um usuário.
     
    public void removerTreino(String cpfUsuario, int idTreino) {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario); 
        if (usuario != null) {
            Treino treinoParaRemover = buscarTreino(usuario, idTreino);
            if (treinoParaRemover != null) {
                usuario.removerTreino(treinoParaRemover);
                repositorioCliente.atualizarElemento(usuario); // Salva a remoção
                System.out.println("SUCESSO: Treino removido.");
            } else {
                System.out.println("ERRO: Treino com ID " + idTreino + " não encontrado.");
            }
        } else {
            System.out.println("ERRO: Usuário não encontrado.");
        }
    }

    //Lista todos os treinos de um usuário.
    
    public void listarTreinos(String cpfUsuario) {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario); 
        if (usuario != null) {
            System.out.println("--- Treinos de " + usuario.getNome() + " (CPF: " + usuario.getCpf() + ") ---");
            List<Treino> treinos = usuario.getTreinos();
            if (treinos.isEmpty()) {
                System.out.println("Nenhum treino cadastrado.");
            } else {
                for (Treino t : treinos) {
                    // O método .toString() de Corrida/Intervalado será chamado
                    System.out.println(t);
                }
            }
        } else {
            System.out.println("ERRO: Usuário não encontrado.");
        }
    }
}
