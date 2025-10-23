package br.com.dados;

import br.com.negocio.treinos.Desafio;
import java.util.List;

/**
 * Interface para o repositório de Desafios.
 * (Arquivo criado pois não estava presente no projeto original)
 */
public interface IRepositorioDesafio {
    
    void cadastrar(Desafio desafio);
    
    Desafio buscar(int idDesafio);
    
    void atualizar(Desafio desafio);
    
    List<Desafio> listarTodos();
    
    void remover(int idDesafio);
}
