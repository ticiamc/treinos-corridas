package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.PlanoTreino;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para Planos de Treino.
 * (Arquivo criado pois não estava presente no projeto original)
 */
public class ControladorPlanoTreino {

    private IRepositorioCliente repositorioCliente;

    public ControladorPlanoTreino(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    public void cadastrarPlano(String cpfUsuario, String nome, LocalDate dataInicio, LocalDate dataFim) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }

        PlanoTreino plano = new PlanoTreino(nome, dataInicio, dataFim, usuario);
        usuario.adicionarPlanoTreino(plano);
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Plano de treino cadastrado com sucesso! ID: " + plano.getIdPlano());
    }

    public List<PlanoTreino> listarPlanos(String cpfUsuario) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }
        return usuario.getPlanos();
    }

    public void removerPlano(String cpfUsuario, int idPlano) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }

        PlanoTreino plano = usuario.buscarPlanoPorId(idPlano);
        if (plano == null) {
            throw new Exception("Plano de treino não encontrado.");
        }

        usuario.removerPlanoTreino(plano);
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Plano de treino removido com sucesso!");
    }

    public void adicionarTreinoPlano(String cpfUsuario, int idPlano, int idTreino) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }

        PlanoTreino plano = usuario.buscarPlanoPorId(idPlano);
        if (plano == null) {
            throw new Exception("Plano de treino não encontrado.");
        }

        Treino treino = usuario.buscarTreinoPorId(idTreino);
        if (treino == null) {
            throw new Exception("Treino não encontrado na lista principal do usuário.");
        }

        plano.adicionarTreino(treino);
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Treino adicionado ao plano com sucesso!");
    }

    public void removerTreinoPlano(String cpfUsuario, int idPlano, int idTreino) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }

        PlanoTreino plano = usuario.buscarPlanoPorId(idPlano);
        if (plano == null) {
            throw new Exception("Plano de treino não encontrado.");
        }

        Treino treinoParaRemover = null;
        for (Treino t : plano.getTreinosDoPlano()) {
            if (t.getIdTreino() == idTreino) {
                treinoParaRemover = t;
                break;
            }
        }

        if (treinoParaRemover == null) {
            throw new Exception("Treino não encontrado *dentro* deste plano.");
        }

        plano.removerTreino(treinoParaRemover); // Método adicionado em PlanoTreino
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Treino removido do plano com sucesso!");
    }
}
