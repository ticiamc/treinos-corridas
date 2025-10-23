package br.com.negocio.treinos;

import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter; 
import java.util.UUID; 

/**
 * Representa uma mensagem de notificação para o usuário.
 * 
 */
public class Notificacao {

    // --- Atributos  ---
    private UUID id;
    private String mensagem;
    private LocalDateTime data; // 
    private boolean lida; 

    // --- Construtor  ---
    
    public Notificacao(UUID id, String mensagem, LocalDateTime data) {
        this.id = id;
        this.mensagem = mensagem;
        this.data = data;
        this.lida = false; // Toda notificação começa como "não lida"
    }

    // --- Getters e Setters ---

    public UUID getId() {
        return id;
    }
    
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) { 
        this.data = data;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        return String.format("[%s] %s", this.data.format(formatter), this.mensagem);
    }
}