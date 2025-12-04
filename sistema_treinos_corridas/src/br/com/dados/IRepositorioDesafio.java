package br.com.dados;

import br.com.negocio.treinos.Desafio;
import java.util.List;
import java.util.ArrayList;

public interface IRepositorioDesafio {
    void cadastrar(Desafio desafio);
    Desafio buscar(int idDesafio);
    void atualizar(Desafio desafio);
    ArrayList<Desafio> listarTodos();
    void remover(int idDesafio);
}