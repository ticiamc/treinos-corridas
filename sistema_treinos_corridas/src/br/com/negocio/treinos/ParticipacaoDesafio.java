package br.com.negocio.treinos;

/**
 * Classe de associação que representa a inscrição (participação) 
 * de um Usuário em um Desafio.
 */
public class ParticipacaoDesafio {

    // --- Atributos ---
    private Usuario usuario;
    private Desafio desafio;
    
    // Usado para armazenar o "score" do usuário
    private double progresso; 

    // --- Construtor ---
    public ParticipacaoDesafio(Usuario usuario, Desafio desafio) {
        this.usuario = usuario;
        this.desafio = desafio;
        this.progresso = 0; // Inicia com 0
    }

    // --- Getters e Setters ---
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }
    
    public double getProgresso() {
        return progresso;
    }

   
    public void setProgresso(double progresso) {
        this.progresso = progresso;
    }
}