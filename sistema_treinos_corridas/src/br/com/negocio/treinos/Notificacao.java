package br.com.negocio.treinos;

import java.util.Date;

/**
 * Representa uma mensagem de notificação para o usuário.
 * Usada para informar sobre metas atingidas, desafios, etc.
 */
public class Notificacao {

    // --- Atributos ---
    private String mensagem;
    private Date data; 
    private boolean lida; 

    // --- Construtor ---
    
    public Notificacao(String mensagem, Date data) {
        this.mensagem = mensagem;
        this.data = data;
        this.lida = false; // Toda notificação começa como "não lida"
    }

    // --- Getters e Setters ---

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }
}