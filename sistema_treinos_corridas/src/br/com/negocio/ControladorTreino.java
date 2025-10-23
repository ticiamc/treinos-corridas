package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.Corrida;
import br.com.negocio.treinos.Intervalado;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;
import java.time.LocalDateTime; // Corrigido: import java.util.Date;
import java.util.List;

public class ControladorTreino {

    private IRepositorioCliente repositorioCliente;
    // Não precisamos mais do id aqui, será controlado na classe Treino
    // private int proximoTreinoId = 1; 

    public ControladorTreino(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    // Método de cadastro (corrigido)
    public void cadastrarTreino(String cpfCliente, String nomeTreino, LocalDateTime data, double duracaoMinutos, String tipo, 
                                Double distanciaKm, Integer series, Double tempoDescansoMinutos) throws Exception {
        
        Usuario cliente = repositorioCliente.buscarElementoPorCpf(cpfCliente); // Corrigido: buscarPorCpf
        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }

        // Converte duração de minutos (double) para segundos (int)
        int duracaoSegundos = (int) (duracaoMinutos * 60);

        Treino treino;
        if ("corrida".equalsIgnoreCase(tipo)) {
            if (distanciaKm == null) {
                throw new IllegalArgumentException("Distância é obrigatória para corrida.");
            }
            // Converte km (double) para metros (double)
            double distanciaMetros = distanciaKm * 1000;
            treino = new Corrida(nomeTreino, data, duracaoSegundos, distanciaMetros);

        } else if ("intervalado".equalsIgnoreCase(tipo)) {
            if (series == null || tempoDescansoMinutos == null) {
                throw new IllegalArgumentException("Séries e tempo de descanso são obrigatórios para treino intervalado.");
            }
            // Converte tempo de descanso de minutos (double) para segundos (int)
            int tempoDescansoSeg = (int) (tempoDescansoMinutos * 60);
            treino = new Intervalado(nomeTreino, data, duracaoSegundos, series, tempoDescansoSeg);
        } else {
            throw new IllegalArgumentException("Tipo de treino inválido.");
        }

        // O ID é atribuído automaticamente no construtor do Treino
        // treino.setIdTreino(proximoTreinoId++); // Linha removida
        cliente.adicionarTreino(treino);
        repositorioCliente.atualizarElemento(cliente); // Corrigido: atualizarElemento
    }
    
    // Método de listagem (corrigido)
    public List<Treino> listarTreinos(String cpfCliente) throws Exception {
         Usuario cliente = repositorioCliente.buscarElementoPorCpf(cpfCliente); // Corrigido: buscarPorCpf
        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }
        return cliente.getTreinos();
    }

    // --- MÉTODOS NOVOS (ATUALIZAR E REMOVER) ---

    public void atualizarTreino(String cpfCliente, int idTreino, LocalDateTime novaData, double novaDuracaoMinutos) throws Exception {
        Usuario cliente = repositorioCliente.buscarElementoPorCpf(cpfCliente); // Corrigido: buscarPorCpf
        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }

        Treino treino = cliente.buscarTreinoPorId(idTreino);
        if (treino == null) {
            throw new Exception("Treino não encontrado para este usuário.");
        }

        int novaDuracaoSegundos = (int) (novaDuracaoMinutos * 60);

        treino.setDataExecucao(novaData); // Corrigido: setData -> setDataExecucao
        treino.setDuracaoSegundos(novaDuracaoSegundos); // Corrigido: setDuracao -> setDuracaoSegundos
        // Outras atualizações específicas (distância, etc.) poderiam ser adicionadas
        
        repositorioCliente.atualizarElemento(cliente); // Corrigido: atualizar -> atualizarElemento
        System.out.println("Treino atualizado com sucesso!");
    }

    public void removerTreino(String cpfCliente, int idTreino) throws Exception {
        Usuario cliente = repositorioCliente.buscarElementoPorCpf(cpfCliente); // Corrigido: buscarPorCpf
        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }

        Treino treino = cliente.buscarTreinoPorId(idTreino);
        if (treino == null) {
            throw new Exception("Treino não encontrado para este usuário.");
        }

        cliente.removerTreino(treino);
        repositorioCliente.atualizarElemento(cliente); // Corrigido: atualizar -> atualizarElemento
        System.out.println("Treino removido com sucesso!");
    }
}
