package br.com.treinos;

import java.time.LocalDateTime;

/**
 * Representa uma notificação para o usuário 
 */
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

    // Getters e Setters
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public boolean isLida() { return lida; }
    public Usuario getDestinatario() { return destinatario; }
}