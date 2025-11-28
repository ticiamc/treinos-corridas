package br.com.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.dados.IRepositorioCliente;
import br.com.excecoes.CampoVazioException;
import br.com.excecoes.UsuarioNaoEncontradoException;
import br.com.excecoes.ValorInvalidoException;
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
     * Método principal para cadastrar um novo treino com validações e exceções.
     */
    public void cadastrarTreino(String cpfUsuario, String tipoTreino, LocalDate data, int duracaoSegundos, 
                                String descricao, double distancia, int series, int descanso) 
                                throws UsuarioNaoEncontradoException, ValorInvalidoException, CampoVazioException {
        
        // 1. Validações Básicas
        if (cpfUsuario == null || cpfUsuario.trim().isEmpty()) {
            throw new CampoVazioException("CPF do Usuário");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new CampoVazioException("Nome/Descrição do Treino");
        }
        
        // 2. Validação de Valores Numéricos Gerais
        if (duracaoSegundos <= 0) {
            throw new ValorInvalidoException("A duração do treino deve ser maior que zero.");
        }

        // 3. Busca e Validação do Usuário
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException(cpfUsuario);
        }

        Treino treino;
        LocalDateTime dataExecucao = data.atStartOfDay();

        // 4. Criação do Treino com Validação Específica
        if ("Corrida".equalsIgnoreCase(tipoTreino)) {
            if (distancia <= 0) {
                throw new ValorInvalidoException("A distância da corrida deve ser maior que zero.");
            }
            treino = new Corrida(descricao, dataExecucao, duracaoSegundos, distancia);

        } else if ("Intervalado".equalsIgnoreCase(tipoTreino)) {
            if (series <= 0) {
                throw new ValorInvalidoException("O número de séries deve ser maior que zero.");
            }
            if (descanso < 0) {
                throw new ValorInvalidoException("O tempo de descanso não pode ser negativo.");
            }
            treino = new Intervalado(descricao, dataExecucao, duracaoSegundos, series, descanso);
        } else {
            throw new ValorInvalidoException("Tipo de treino inválido: " + tipoTreino);
        }
        
        // 5. Persistência e Regras de Negócio
        usuario.adicionarTreino(treino);
        
        // Verifica metas automaticamente
        TreinoProgresso.verificarTodasMetas(usuario, treino);
        
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("SUCESSO: Treino cadastrado para " + usuario.getNome());
    }

    /**
     * Busca um treino específico na lista de um usuário.
     */
    public Treino buscarTreino(Usuario usuario, int idTreino) {
        if (usuario != null) {
            return usuario.buscarTreinoPorId(idTreino);
        }
        return null;
    }

    /**
     * Remove um treino da lista de um usuário.
     */
    public void removerTreino(String cpfUsuario, int idTreino) {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario); 
        if (usuario != null) {
            Treino treinoParaRemover = buscarTreino(usuario, idTreino);
            if (treinoParaRemover != null) {
                usuario.removerTreino(treinoParaRemover);
                repositorioCliente.atualizarElemento(usuario);
                System.out.println("SUCESSO: Treino removido.");
            } else {
                System.out.println("ERRO: Treino com ID " + idTreino + " não encontrado.");
            }
        } else {
            System.out.println("ERRO: Usuário não encontrado.");
        }
    }

    /**
     * Lista todos os treinos de um usuário no console.
     */
    public void listarTreinos(String cpfUsuario) {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario); 
        if (usuario != null) {
            System.out.println("--- Treinos de " + usuario.getNome() + " (CPF: " + usuario.getCpf() + ") ---");
            List<Treino> treinos = usuario.getTreinos();
            if (treinos.isEmpty()) {
                System.out.println("Nenhum treino cadastrado.");
            } else {
                for (Treino t : treinos) {
                    System.out.println(t);
                }
            }
        } else {
            System.out.println("ERRO: Usuário não encontrado.");
        }
    }
    
    public List<Treino> listarTodosTreinos() {
        // Método auxiliar caso precise listar de todos (cuidado com performance)
        // Aqui apenas retornando null pois depende da implementação do repositório
        return null; 
    }
}