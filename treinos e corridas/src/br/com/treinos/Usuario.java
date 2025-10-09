package br.com.treinos;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private int idade;
    private double peso;
    private double altura;
    private String email;
    
    // ADICIONADO: Lista para armazenar os treinos do usuário
    private List<Treino> treinos;

    public Usuario(String nome, int idade, double peso, double altura, String email) {
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
        this.email = email;
        // ADICIONADO: Inicializa a lista no construtor
        this.treinos = new ArrayList<>(); 
    }

    // ADICIONADO: Método para registrar um treino na lista do usuário
    public void adicionarTreino(Treino treino) {
        this.treinos.add(treino);
    }
    
    // --- Getters e Setters existentes ---
    public String getNome() {
        return nome;
    }
    
    public List<Treino> getTreinos() {
        return treinos;
    }
    
    // ... outros getters e setters
}
