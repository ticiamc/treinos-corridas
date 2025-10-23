package br.com.treinos;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a pessoa que vai usar o sistema.
 * Guarda os dados pessoais e também a lista de treinos que ela já fez.
 */
public class Usuario {
    private String nome;
    private int idade;
    private double peso;
    private double altura;
    private String email;
    // A "gaveta" de treinos do usuário. Cada treino que ele conclui é guardado aqui.
    // Isso é essencial para depois gerar relatórios e ver o progresso.
    private List<Treino> treinos;
    private List<Meta> metas;
    private List<PlanoTreino> planos;
    private List<Desafio> desafios;
    private int id;

    public Usuario(String nome, int idade, double peso, double altura, String email) {
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
        this.email = email;
        // Quando um novo usuário é criado, suas listas começam vazias.
        this.treinos = new ArrayList<>();
        this.metas = new ArrayList<>();
        this.planos = new ArrayList<>();
        this.desafios = new ArrayList<>();
    }

    /**
     * "Salva" um treino recém-feito na lista pessoal do usuário.
     * @param treino O objeto do treino (pode ser uma Corrida, um Intervalado, etc.).
     */
    public void adicionarTreino(Treino treino) {
        this.treinos.add(treino);
    }

    public void adicionarMeta(Meta meta) {
        this.metas.add(meta);
    }

    public void adicionarPlanoTreino(PlanoTreino plano) {
        this.planos.add(plano);
    }

    public void adicionarDesafio(Desafio desafio) {
        this.desafios.add(desafio);
    }


    // --- Getters e Setters ---

    public int getId() { 
    return id;
    }

    public void setId(int id) { 
    this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Treino> getTreinos() {
        return treinos;
    }
}
