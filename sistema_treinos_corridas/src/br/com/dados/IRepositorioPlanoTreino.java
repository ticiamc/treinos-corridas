package br.com.dados;
import br.com.negocio.treinos.PlanoTreino;
import java.util.List;

public interface IRepositorioPlanoTreino {
    void cadastrar(PlanoTreino plano);
    PlanoTreino buscar(int idPlano);
    void remover(int idPlano);
    List<PlanoTreino> listarTodos();
}