package br.com.negocio;

import br.com.negocio.treinos.Usuario;

public class SessaoUsuario {
    
    private static SessaoUsuario instance;
    private Usuario usuarioLogado;

    private SessaoUsuario() {}

    public static SessaoUsuario getInstance() {
        if (instance == null) {
            instance = new SessaoUsuario();
        }
        return instance;
    }

    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
    // Verifica se tem alguém logado (que não seja admin)
    public boolean isUsuarioLogado() {
        return usuarioLogado != null;
    }
}