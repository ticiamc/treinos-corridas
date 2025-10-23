package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.Usuario;

/**
 * Classe corrigida:
 * - Não é mais abstract
 * - Não usa métodos static
 * - Mantém uma instância do repositório (injeção de dependência)
 * - Busca usuários por CPF
 */
public class ControladorCliente {
    
    private IRepositorioCliente repositorioCliente;

    public ControladorCliente(IRepositorioCliente repositorio) {
        this.repositorioCliente = repositorio;
    }

    public void cadastrar(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Usuário inválido.");
        }
        if (repositorioCliente.buscarElementoPorCpf(usuario.getCpf()) != null) {
            throw new Exception("Usuário com este CPF já cadastrado.");
        }
        repositorioCliente.adicionarElemento(usuario);
    }

    public Usuario buscar(String cpf) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpf);
        if (usuario == null) {
            throw new Exception("Usuário não encontrado.");
        }
        return usuario;
    }

    public void atualizar(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Usuário inválido.");
        }
        if (repositorioCliente.buscarElementoPorCpf(usuario.getCpf()) == null) {
            throw new Exception("Usuário não encontrado para atualização.");
        }
        repositorioCliente.atualizarElemento(usuario);
    }

    public void remover(String cpf) throws Exception {
        if (cpf == null || cpf.isEmpty()) {
            throw new Exception("CPF inválido.");
        }
        if (repositorioCliente.buscarElementoPorCpf(cpf) == null) {
            throw new Exception("Usuário não encontrado para remoção.");
        }
        repositorioCliente.removerElemento(cpf);
    }
}
