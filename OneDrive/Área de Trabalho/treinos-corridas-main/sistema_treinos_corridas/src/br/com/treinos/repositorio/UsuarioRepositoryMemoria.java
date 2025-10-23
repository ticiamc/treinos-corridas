package br.com.treinos.repositorio;

import br.com.treinos.Usuario;

public class UsuarioRepositoryMemoria extends RepositorioMemoriaAbstrato<Usuario> implements IUsuarioRepository {

    @Override
    protected int getId(Usuario entidade) {
        return entidade.getId();
    }

    @Override
    protected void setId(Usuario entidade, int id) {
        entidade.setId(id);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        // Simula uma busca específica que não seja por ID
        return listarTodos().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}