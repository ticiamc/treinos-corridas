]package br.com.negocio.treinos;

/**
 * Classe de associação que representa a inscrição (participação) 
 * de um Usuário em um Desafio.
 */
public class ParticipacaoDesafio {

    // --- Atributos ---
    private Usuario usuario;
    private Desafio desafio;
    
    //Usado para armazenar o "score" do usuário
    //no momento em que o ranking é gerado.
    private double pontuacao; 

    // --- Construtor ---
    public ParticipacaoDesafio(Usuario usuario, Desafio desafio) {
        this.usuario = usuario;
        this.desafio = desafio;
        this.pontuacao = 0; // Inicia com 0
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
    
    // Obtém a pontuação calculada do usuário para este desafio
    public double getPontuacao() {
        return pontuacao;
    }

    //Define a pontuação calculada do usuário.
    public void setPontuacao(double pontuacao) {
        this.pontuacao = pontuacao;
    }
}