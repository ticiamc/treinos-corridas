package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.dados.IRepositorioDesafio;
import br.com.negocio.treinos.Desafio;
import br.com.negocio.treinos.ParticipacaoDesafio;
import br.com.negocio.treinos.Usuario;
import java.time.LocalDate; 
import java.util.List;

public class ControladorDesafio {

    private IRepositorioDesafio repositorioDesafio;
    private IRepositorioCliente repositorioCliente;

    public ControladorDesafio(IRepositorioDesafio repositorioDesafio, IRepositorioCliente repositorioCliente) {
        this.repositorioDesafio = repositorioDesafio;
        this.repositorioCliente = repositorioCliente;
    }

    public void cadastrarDesafio(String nome, String descricao, LocalDate dataInicio, LocalDate dataFim) throws Exception {
        Desafio desafio = new Desafio(nome, descricao, dataInicio, dataFim);
        repositorioDesafio.cadastrar(desafio);
        System.out.println("Desafio cadastrado com sucesso! ID: " + desafio.getIdDesafio());
    }

    public List<Desafio> listarDesafios() {
        return repositorioDesafio.listarTodos();
    }

    public void participarDesafio(int idDesafio, String cpfUsuario) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Usuário não encontrado.");
        }

        Desafio desafio = repositorioDesafio.buscar(idDesafio);
        if (desafio == null) {
            throw new Exception("Desafio não encontrado.");
        }
        
        // --- [REQ25] REGRA DE NEGÓCIO: Atividade Mínima Prévia ---
        if (usuario.getTreinos().isEmpty()) {
            throw new Exception("Regra do Desafio: Você precisa ter ao menos 1 treino registrado para competir!");
        }
        // ---------------------------------------------------------
        
        desafio.adicionarParticipante(usuario);
        repositorioDesafio.atualizar(desafio);
    }

    public void registrarProgresso(int idDesafio, String cpfUsuario, double progresso) throws Exception {
        Desafio desafio = repositorioDesafio.buscar(idDesafio);
        if (desafio == null) {
            throw new Exception("Desafio não encontrado.");
        }

        ParticipacaoDesafio participacao = null;
        for (ParticipacaoDesafio p : desafio.getParticipacoes()) {
            if (p.getUsuario().getCpf().equals(cpfUsuario)) {
                participacao = p;
                break;
            }
        }

        if (participacao == null) {
            throw new Exception("Usuário não está participando deste desafio.");
        }

        participacao.setProgresso(participacao.getProgresso() + progresso);
        repositorioDesafio.atualizar(desafio);
        System.out.println("Progresso registrado com sucesso! Novo progresso: " + participacao.getProgresso());
    }

    public Desafio buscarDesafio(int idDesafio) throws Exception {
        Desafio desafio = repositorioDesafio.buscar(idDesafio);
        if (desafio == null) {
            throw new Exception("Desafio não encontrado.");
        }
        return desafio;
    }
    
    public void removerDesafio(int idDesafio) {
        Desafio desafio = repositorioDesafio.buscar(idDesafio);
        
        if (desafio != null) {
            repositorioDesafio.remover(idDesafio);
            System.out.println("Desafio '" + desafio.getNome() + "' removido com sucesso!");
        } else {
            System.out.println("Desafio com ID " + idDesafio + " não encontrado.");
        }
    }
}