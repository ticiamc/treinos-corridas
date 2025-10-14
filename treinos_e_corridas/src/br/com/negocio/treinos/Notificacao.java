package br.com.negocio.treinos;

import java.time.LocalDateTime;

public class Notificacao {
    private String mensagem;
    private LocalDateTime dataEnvio;
    private boolean lida;
    private Usuario destinatario;

    public Notificacao(String mensagem, Usuario destinatario) {
        this.mensagem = mensagem;
        this.destinatario = destinatario;
        this.dataEnvio = LocalDateTime.now();
        this.lida = false;
    }

    public void marcarComoLida() {
        this.lida = true;
    }

    // --- Getters e Setters ---

    public String getMensagem() {
        return mensagem;
    }

    public boolean isLida() {
        return lida;
    }
}
