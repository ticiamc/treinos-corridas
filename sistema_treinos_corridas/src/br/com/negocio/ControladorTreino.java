package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.Corrida;
import br.com.negocio.treinos.Intervalado;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;
import java.util.Date;
import java.util.List;

public class ControladorTreino {

    private IRepositorioCliente repositorioCliente;
    private int proximoTreinoId = 1; // Simula a geração de ID para treinos

    public ControladorTreino(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    // Método de cadastro (existente)
    public void cadastrarTreino(String cpfCliente, Date data, double duracao, String tipo, Double distancia, Integer series, Double tempoDescanso) throws Exception {
        Usuario cliente = repositorioCliente.buscar(cpfCliente);
        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }

        Treino treino;
        if ("corrida".equalsIgnoreCase(tipo)) {
            if (distancia == null) {
                throw new IllegalArgumentException("Distância é obrigatória para corrida.");
            }
            treino = new Corrida(data, duracao, distancia);
        } else if ("intervalado".equalsIgnoreCase(tipo)) {
            if (series == null || tempoDescanso == null) {
                throw new IllegalArgumentException("Séries e tempo de descanso são obrigatórios para treino intervalado.");
            }
            treino = new Intervalado(data, duracao, series, tempoDescanso);
        } else {
            throw new IllegalArgumentException("Tipo de treino inválido.");
        }

        treino.setIdTreino(proximoTreinoId++); // Atribui um ID único
        cliente.adicionarTreino(treino);
        repositorioCliente.atualizar(cliente); // Persiste a mudança no usuário
    }
    
    // Método de listagem (existente)
    public List<Treino> listarTreinos(String cpfCliente) throws Exception {
         Usuario cliente = repositorioCliente.buscar(cpfCliente);
        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }
        return cliente.getTreinos();
    }

    // --- MÉTODOS NOVOS (ATUALIZAR E REMOVER) ---

    public void atualizarTreino(String cpfCliente, int idTreino, Date novaData, double novaDuracao) throws Exception {
        Usuario cliente = repositorioCliente.buscar(cpfCliente);
        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }

        Treino treino = cliente.buscarTreinoPorId(idTreino);
        if (treino == null) {
            throw new Exception("Treino não encontrado para este usuário.");
        }

        treino.setData(novaData);
        treino.setDuracao(novaDuracao);
        // Outras atualizações específicas (distância, etc.) poderiam ser adicionadas
        
        repositorioCliente.atualizar(cliente); // Salva o estado atualizado do cliente
        System.out.println("Treino atualizado com sucesso!");
    }

    public void removerTreino(String cpfCliente, int idTreino) throws Exception {
        Usuario cliente = repositorioCliente.buscar(cpfCliente);
        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }

        Treino treino = cliente.buscarTreinoPorId(idTreino);
        if (treino == null) {
            throw new Exception("Treino não encontrado para este usuário.");
        }

        cliente.removerTreino(treino);
        repositorioCliente.atualizar(cliente); // Salva o estado atualizado do cliente
        System.out.println("Treino removido com sucesso!");
    }
}
