package br.com.dados;

import br.com.negocio.treinos.Desafio;
import java.util.List;

public interface IRepositorioDesafio {
    void cadastrar(Desafio desafio);
    Desafio buscar(int idDesafio);
    void atualizar(Desafio desafio);
    List<Desafio> listarTodos();
    void remover(int idDesafio);
}