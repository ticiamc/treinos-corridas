package br.com.treinos.repositorio;

import java.util.List;

/**
 * Interface genérica (contrato) para operações de CRUD.
 * Define 'o que' um repositório deve fazer, não 'como'.
 * @param <T> O tipo da entidade (ex: Usuario, Desafio)
 */
public interface IRepository<T> {
    T salvar(T entidade);
    T buscarPorId(int id);
    List<T> listarTodos();
    T atualizar(T entidade);
    void excluir(int id);
}