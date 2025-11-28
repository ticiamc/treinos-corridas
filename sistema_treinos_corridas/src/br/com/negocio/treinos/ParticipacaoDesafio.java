package br.com.negocio.treinos;

public class ParticipacaoDesafio {
    private Usuario usuario;
    private Desafio desafio;
    private double progresso; 

    public ParticipacaoDesafio(Usuario usuario, Desafio desafio) {
        this.usuario = usuario;
        this.desafio = desafio;
        this.progresso = 0;
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Desafio getDesafio() { return desafio; }
    public void setDesafio(Desafio desafio) { this.desafio = desafio; }
    public double getProgresso() { return progresso; }
    public void setProgresso(double progresso) { this.progresso = progresso; }
}