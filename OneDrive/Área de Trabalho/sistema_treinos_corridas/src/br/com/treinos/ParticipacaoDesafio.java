package br.com.treinos;

/**
 * Essa classe é como uma "ficha de inscrição" num desafio.
 * Ela existe pra conectar duas informações: QUEM (o usuário) e ONDE (o desafio).
 * Além disso, ela guarda o progresso daquela pessoa naquele desafio específico.
 */
public class ParticipacaoDesafio {
    
    private Usuario usuario;
    private Desafio desafio;
    private double progresso; // Ex: quantos km o usuário já correu para este desafio.

    public ParticipacaoDesafio(Usuario usuario, Desafio desafio) {
        this.usuario = usuario;
        this.desafio = desafio;
        this.progresso = 0; // Todo mundo começa com zero de progresso.
    }

    // --- Getters e Setters ---

    public Usuario getUsuario() {
        return usuario;
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public double getProgresso() {
        return progresso;
    }

    public void setProgresso(double progresso) {
        this.progresso = progresso;
    }
}
