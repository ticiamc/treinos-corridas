package br.com.treinos;

/**
 * Classe de associação que representa a participação de um Usuário em um Desafio 
 */
public class ParticipacaoDesafio {

    private double progresso;
    private Usuario usuario;
    private Desafio desafio;

    public ParticipacaoDesafio(Usuario usuario, Desafio desafio) {
        this.usuario = usuario;
        this.desafio = desafio;
        this.progresso = 0.0;
    }

    public void atualizarProgresso(double valor) {
        this.progresso += valor;
    }

    // Getters e Setters
    public double getProgresso() { return progresso; }
    public void setProgresso(double progresso) { this.progresso = progresso; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Desafio getDesafio() { return desafio; }
    public void setDesafio(Desafio desafio) { this.desafio = desafio; }
}