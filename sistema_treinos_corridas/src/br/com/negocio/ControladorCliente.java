package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.Notificacao;
import br.com.negocio.treinos.Usuario;
import java.util.List;

public class ControladorCliente {
    private IRepositorioCliente repositorioCliente;

    public ControladorCliente(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }
    
    public IRepositorioCliente getRepositorio() { return repositorioCliente; }

    public void cadastrarCliente(Usuario cliente) {
        if (repositorioCliente.buscarElementoPorCpf(cliente.getCpf()) == null) {
            repositorioCliente.adicionarElemento(cliente);
        }
    }

    public Usuario buscarCliente(String cpf) { return repositorioCliente.buscarElementoPorCpf(cpf); }

    public void removerCliente(String cpf) {
        if (repositorioCliente.buscarElementoPorCpf(cpf) != null) repositorioCliente.removerElemento(cpf);
    }

    public void atualizarCliente(Usuario cliente) {
        if (repositorioCliente.buscarElementoPorCpf(cliente.getCpf()) != null) repositorioCliente.atualizarElemento(cliente);
    }

    public void verNotificacoes(Usuario usuario) {
        if (usuario == null) return;
        List<Notificacao> notificacoes = usuario.getNotificacoes();
        notificacoes.removeIf(Notificacao::isLida);
    }
}