package br.com.treinos.repositorio;

import br.com.treinos.Desafio;

public class DesafioRepositoryMemoria extends RepositorioMemoriaAbstrato<Desafio> implements IDesafioRepository {

    @Override
    protected int getId(Desafio entidade) {
        return entidade.getId();
    }

    @Override
    protected void setId(Desafio entidade, int id) {
        entidade.setId(id);
    }
}