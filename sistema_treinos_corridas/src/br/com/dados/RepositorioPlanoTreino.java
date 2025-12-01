package br.com.dados;
import br.com.negocio.treinos.PlanoTreino;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPlanoTreino implements IRepositorioPlanoTreino {
    private List<PlanoTreino> planos;

    public RepositorioPlanoTreino() { this.planos = new ArrayList<>(); }

    @Override
    public void cadastrar(PlanoTreino plano) { if (plano != null) this.planos.add(plano); }

    @Override
    public PlanoTreino buscar(int idPlano) {
        for (PlanoTreino p : planos) {
            if (p.getIdPlano() == idPlano) return p;
        }
        return null;
    }

    @Override
    public void remover(int idPlano) { planos.removeIf(p -> p.getIdPlano() == idPlano); }

    @Override
    public List<PlanoTreino> listarTodos() { return new ArrayList<>(planos); }
}