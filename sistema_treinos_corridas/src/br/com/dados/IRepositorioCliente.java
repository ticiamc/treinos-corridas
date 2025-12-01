package br.com.dados;

import br.com.negocio.treinos.Usuario;
import java.util.List;

public interface IRepositorioCliente {
    void adicionarElemento(Usuario cliente);
    Usuario buscarElementoPorCpf(String cpf);
    Usuario buscarElementoPorNome(String nome);
    void atualizarElemento(Usuario cliente);
    void removerElemento(String cpf);
    List<Usuario> listarTodos();
}