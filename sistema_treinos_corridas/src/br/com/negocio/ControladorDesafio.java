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

    // Atualizado para receber o criador
    public void cadastrarDesafio(String nome, String descricao, LocalDate dataInicio, LocalDate dataFim, Usuario criador) throws Exception {
        if (dataInicio.isAfter(dataFim)) throw new Exception("Data de início não pode ser posterior à data final.");
        Desafio desafio = new Desafio(nome, descricao, dataInicio, dataFim, criador);
        repositorioDesafio.cadastrar(desafio);
    }

    // Novo método para editar desafio existente
    public void atualizarDesafio(int idDesafio, String nome, String descricao, LocalDate inicio, LocalDate fim) throws Exception {
        Desafio d = repositorioDesafio.buscar(idDesafio);
        if (d == null) throw new Exception("Desafio não encontrado.");
        if (inicio.isAfter(fim)) throw new Exception("Data de início inválida.");
        
        d.setNome(nome);
        d.setDescricao(descricao);
        d.setDataInicio(inicio);
        d.setDataFim(fim);
        
        repositorioDesafio.atualizar(d);
    }

    public List<Desafio> listarDesafios() { return repositorioDesafio.listarTodos(); }

    public void participarDesafio(int idDesafio, String cpfUsuario) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) throw new Exception("Usuário não encontrado.");

        if (usuario.getTreinos().isEmpty()) {
            throw new Exception("Requisito REQ25: Para participar de desafios, você precisa ter pelo menos um treino registrado.");
        }

        Desafio desafio = repositorioDesafio.buscar(idDesafio);
        if (desafio == null) throw new Exception("Desafio não encontrado.");
        
        desafio.adicionarParticipante(usuario);
        repositorioDesafio.atualizar(desafio);
    }
    
    public void registrarProgresso(int idDesafio, String cpfUsuario, double progresso) throws Exception {
        Desafio desafio = repositorioDesafio.buscar(idDesafio);
        if (desafio == null) throw new Exception("Desafio não encontrado.");

        ParticipacaoDesafio participacao = null;
        for (ParticipacaoDesafio p : desafio.getParticipacoes()) {
            if (p.getUsuario().getCpf().equals(cpfUsuario)) {
                participacao = p;
                break;
            }
        }

        if (participacao == null) throw new Exception("Participação não encontrada.");
        participacao.setProgresso(participacao.getProgresso() + progresso);
        repositorioDesafio.atualizar(desafio);
    }

    public void removerDesafio(int idDesafio) { repositorioDesafio.remover(idDesafio); }
}