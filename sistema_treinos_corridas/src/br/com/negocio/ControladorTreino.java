package br.com.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime; 
import java.util.List; 
import java.util.UUID;

import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.Corrida;
import br.com.negocio.treinos.Intervalado;
import br.com.negocio.treinos.Meta; 
import br.com.negocio.treinos.Notificacao; 
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.TreinoProgresso; 
import br.com.negocio.treinos.Usuario;

/**
 * Controlador (Camada de Negócio) responsável pelas regras
 * relacionadas aos Treinos de um usuário.
 */
public class ControladorTreino {

    // O controlador "conversa" com o repositório através da INTERFACE
    private IRepositorioCliente repositorioCliente;

    /**
     * Construtor que recebe a implementação do repositório (Injeção de Dependência).
     * O Principal.java é quem decide qual repositório será usado (ex: RepositorioClientes).
     */
    public ControladorTreino(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    /**
     * Método principal para cadastrar um novo treino (Corrida ou Intervalado)
     * para um usuário e verificar automaticamente o progresso das metas.
     */
    public void cadastrarTreino(String cpfUsuario, String tipoTreino, LocalDate data, int duracaoSegundos, String descricao, double distancia, int series, int descanso) {
        // 1. Busca o usuário no repositório
        Usuario usuario = repositorioCliente.buscar(cpfUsuario);
        
        if (usuario != null) {
            Treino treino; // Objeto 'Treino' que será criado
            
            // 2. Cria o tipo de treino correto
            if ("Corrida".equalsIgnoreCase(tipoTreino)) {
                treino = new Corrida(UUID.randomUUID(), data, duracaoSegundos, descricao, distancia);
            } else if ("Intervalado".equalsIgnoreCase(tipoTreino)) {
                treino = new Intervalado(UUID.randomUUID(), data, duracaoSegundos, descricao, series, descanso);
            } else {
                System.out.println("ERRO: Tipo de treino inválido.");
                return;
            }
            
            // 3. Adiciona o treino ao usuário e salva
            usuario.adicionarTreino(treino);
            repositorioCliente.atualizar(usuario); // Persiste a mudança no repositório
            System.out.println("SUCESSO: Treino cadastrado para " + usuario.getNome() + ".");           
            System.out.println("[SISTEMA] Verificando progresso de metas...");
            
            // 4. Pega a lista de metas do usuário
            List<Meta> metasDoUsuario = usuario.getMetas();
            
            if (metasDoUsuario.isEmpty()) {
                System.out.println("[SISTEMA] Usuário não possui metas ativas.");
                return; // Sai da verificação se não há metas
            }

            // 5. Itera (faz um loop) por CADA meta do usuário
            for (Meta meta : metasDoUsuario) {
                
                // Armazena o status da meta ANTES da verificação
                String statusAntes = meta.getStatus(); 
                
                // 6. Chama a lógica de verificação (do TreinoProgresso.java)
                TreinoProgresso.verificarProgresso(treino, meta);

                // 7. Verifica se a meta foi CONCLUÍDA 
                if ("Pendente".equalsIgnoreCase(statusAntes) && "Concluído".equalsIgnoreCase(meta.getStatus())) {
                    System.out.println("!!! META ATINGIDA !!! -> " + meta.getDescricao());
                    
                    // 8. Cria a notificação
                    Notificacao notificacao = new Notificacao(
                        UUID.randomUUID(),
                        "Parabéns! Você atingiu sua meta: " + meta.getDescricao(),
                        LocalDateTime.now()
                    );
                    
                    // 9. Adiciona a notificação ao usuário
                    usuario.adicionarNotificacao(notificacao);
                    repositorioCliente.atualizar(usuario); // Salva a notificação
                    
                    System.out.println("[SISTEMA] Notificação gerada para o usuário.");
                }
            }
            
            
        } else {
            System.out.println("ERRO: Usuário com CPF " + cpfUsuario + " não encontrado.");
        }
    }

    /**
     * Busca um treino específico na lista de um usuário.
     */
    public Treino buscarTreino(Usuario usuario, UUID idTreino) {
        if (usuario != null) {
            for (Treino treino : usuario.getTreinos()) {
                if (treino.getId().equals(idTreino)) {
                    return treino;
                }
            }
        }
        return null; // Não encontrou
    }

    /**
     * Remove um treino da lista de um usuário.
     * (Chamado pela Opção 7 do Principal.java)
     */
    public void removerTreino(String cpfUsuario, UUID idTreino) {
        Usuario usuario = repositorioCliente.buscar(cpfUsuario);
        if (usuario != null) {
            Treino treinoParaRemover = buscarTreino(usuario, idTreino);
            if (treinoParaRemover != null) {
                usuario.removerTreino(treinoParaRemover);
                repositorioCliente.atualizar(usuario); // Salva a remoção
                System.out.println("SUCESSO: Treino removido.");
            } else {
                System.out.println("ERRO: Treino com ID " + idTreino + " não encontrado.");
            }
        } else {
            System.out.println("ERRO: Usuário não encontrado.");
        }
    }

    /**
     * Lista todos os treinos de um usuário.
     * (Chamado pela Opção 6 do Principal.java)
     */
    public void listarTreinos(String cpfUsuario) {
        Usuario usuario = repositorioCliente.buscar(cpfUsuario);
        if (usuario != null) {
            System.out.println("--- Treinos de " + usuario.getNome() + " ---");
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