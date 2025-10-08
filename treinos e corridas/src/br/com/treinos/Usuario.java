package br.com.treinos;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private int idade;
    private double peso;
    private double altura;
    private String email;
    private List<Treino> treinosRegistrados;
    private List<Meta> metas;
    private List<ParticipacaoDesafio> participacoesEmDesafios;

    public Usuario(String nome, int idade, double peso, double altura, String email) {
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
        this.email = email;
        this.treinosRegistrados = new ArrayList<>();
        this.metas = new ArrayList<>();
        this.participacoesEmDesafios = new ArrayList<>();
    }

    public void adicionarTreino(Treino treino) { this.treinosRegistrados.add(treino); }
    public void adicionarMeta(Meta meta) { this.metas.add(meta); }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Treino> getTreinosRegistrados() { return treinosRegistrados; }
    public List<Meta> getMetas() { return metas; }
    public List<ParticipacaoDesafio> getParticipacoesEmDesafios() { return participacoesEmDesafios; }
}