package br.com.treinos;

import java.time.LocalDateTime;

public class Notificacao {
    private String mensagem;
    private LocalDateTime dataEnvio;
    private boolean lida;
    private Usuario destinatario;
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getMensagem() {
        return mensagem;
    }

    public boolean isLida() {
        return lida;
    }

    
    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

   
    public Usuario getDestinatario() {
        return destinatario;
    }
}