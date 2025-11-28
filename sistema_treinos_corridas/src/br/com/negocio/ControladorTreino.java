package br.com.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.dados.IRepositorioCliente;
import br.com.excecoes.*;
import br.com.negocio.treinos.*;

public class ControladorTreino {
    private IRepositorioCliente repositorioCliente;

    public ControladorTreino(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    public void cadastrarTreino(String cpfUsuario, String tipoTreino, LocalDate data, int duracaoSegundos, 
                                String descricao, double distancia, int series, int descanso) 
                                throws UsuarioNaoEncontradoException, ValorInvalidoException, CampoVazioException {
        
        if (cpfUsuario == null || cpfUsuario.trim().isEmpty()) throw new CampoVazioException("CPF do Usuário");
        if (descricao == null || descricao.trim().isEmpty()) throw new CampoVazioException("Nome/Descrição do Treino");
        if (duracaoSegundos <= 0) throw new ValorInvalidoException("A duração deve ser maior que zero.");

        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) throw new UsuarioNaoEncontradoException(cpfUsuario);

        Treino treino;
        LocalDateTime dataExecucao = data.atStartOfDay();

        if ("Corrida".equalsIgnoreCase(tipoTreino)) {
            if (distancia <= 0) throw new ValorInvalidoException("A distância deve ser maior que zero.");
            treino = new Corrida(descricao, dataExecucao, duracaoSegundos, distancia);
        } else if ("Intervalado".equalsIgnoreCase(tipoTreino)) {
            if (series <= 0) throw new ValorInvalidoException("Séries deve ser > 0.");
            if (descanso < 0) throw new ValorInvalidoException("Descanso não pode ser negativo.");
            treino = new Intervalado(descricao, dataExecucao, duracaoSegundos, series, descanso);
        } else {
            throw new ValorInvalidoException("Tipo de treino inválido: " + tipoTreino);
        }
        
        usuario.adicionarTreino(treino);
        TreinoProgresso.verificarTodasMetas(usuario, treino);
        repositorioCliente.atualizarElemento(usuario);
    }

    public Treino buscarTreino(Usuario usuario, int idTreino) {
        if (usuario != null) return usuario.buscarTreinoPorId(idTreino);
        return null;
    }

    public void removerTreino(String cpfUsuario, int idTreino) {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario); 
        if (usuario != null) {
            Treino treinoParaRemover = buscarTreino(usuario, idTreino);
            if (treinoParaRemover != null) {
                usuario.removerTreino(treinoParaRemover);
                repositorioCliente.atualizarElemento(usuario);
            }
        }
    }

    public void listarTreinos(String cpfUsuario) {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario); 
        if (usuario != null) {
            for (Treino t : usuario.getTreinos()) System.out.println(t);
        }
    }
}