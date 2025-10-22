package br.com.dados;

import br.com.negocio.treinos.Usuario;

public interface IRepositorioCliente {
    // CRUD
    void adicionarElemento(Usuario cliente);
    Usuario buscarElemento(String nome);
    void atualizarElemento(String nome, Usuario cliente);
    void removerElemento(String nome);
}
