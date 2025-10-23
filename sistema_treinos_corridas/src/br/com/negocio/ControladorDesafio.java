package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.dados.IRepositorioDesafio;
import br.com.negocio.treinos.Desafio;
import br.com.negocio.treinos.ParticipacaoDesafio;
import br.com.negocio.treinos.Usuario;
import java.util.Date;
import java.util.List;

public class ControladorDesafio {

    private IRepositorioDesafio repositorioDesafio;
    private IRepositorioCliente repositorioCliente;

    public ControladorDesafio(IRepositorioDesafio repositorioDesafio, IRepositorioCliente repositorioCliente) {
        this.repositorioDesafio = repositorioDesafio;
        this.repositorioCliente = repositorioCliente;
    }

    public void cadastrarDesafio(String nome, String descricao, Date dataInicio, Date dataFim) throws Exception {
        Desafio desafio = new Desafio(nome, descricao, dataInicio, dataFim);
        repositorioDesafio.cadastrar(desafio);
        System.out.println("Desafio cadastrado com sucesso! ID: " + desafio.getIdDesafio());
    }

    public List<Desafio> listarDesafios() {
        return repositorioDesafio.listarTodos();
    }

    public void participarDesafio(int idDesafio, String cpfUsuario) throws Exception {
        Usuario usuario = repositorioCliente.buscar(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Usuário não encontrado.");
        }

        Desafio desafio = repositorioDesafio.buscar(idDesafio);
        if (desafio == null) {
            throw new Exception("Desafio não encontrado.");
        }
        
        // Verifica se já está participando
        for(ParticipacaoDesafio p : desafio.getParticipacoes()) {
            if(p.getUsuario().getCpf().equals(cpfUsuario)) {
                throw new Exception("Usuário já está participando deste desafio.");
            }
        }

        ParticipacaoDesafio participacao = new ParticipacaoDesafio(usuario, desafio);
        desafio.adicionarParticipacao(participacao);
        
        repositorioDesafio.atualizar(desafio);
        System.out.println("Usuário " + usuario.getNome() + " inscrito no desafio " + desafio.getNome());
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

        participacao.setProgresso(participacao.getProgresso() + progresso); // Acumula o progresso
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
}