package br.com.dados;

import br.com.negocio.treinos.Desafio;
import java.util.ArrayList;
import java.util.List;

public class RepositorioDesafio implements IRepositorioDesafio {

    private static RepositorioDesafio instancia;

    public static synchronized RepositorioDesafio getInstance() {
        if(instancia == null)
            instancia = new RepositorioDesafio();
        return instancia;
    }
    
    private ArrayList<Desafio> desafios;
    
    public RepositorioDesafio() { this.desafios = new ArrayList<>(); }

    @Override
    public void cadastrar(Desafio desafio) { if (desafio != null) this.desafios.add(desafio); }

    @Override
    public Desafio buscar(int idDesafio) {
        for (Desafio d : desafios) {
            if (d.getIdDesafio() == idDesafio) return d;
        }
        return null;
    }

    @Override
    public void atualizar(Desafio desafio) {
        for (int i = 0; i < desafios.size(); i++) {
            if (desafios.get(i).getIdDesafio() == desafio.getIdDesafio()) {
                desafios.set(i, desafio);
                break;
            }
        }
    }

    @Override
    public ArrayList<Desafio> listarTodos() { return new ArrayList<>(desafios); }

    @Override
    public void remover(int idDesafio) { this.desafios.removeIf(d -> d.getIdDesafio() == idDesafio); }
}
