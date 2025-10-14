package br.com.negocio.treinos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanoTreino {
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Usuario usuarioAlvo;
    private List<Treino> treinosDoPlano;

    public PlanoTreino(String nome, LocalDate dataInicio, LocalDate dataFim, Usuario usuarioAlvo) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.usuarioAlvo = usuarioAlvo;
        this.treinosDoPlano = new ArrayList<>();
    }

    public void adicionarTreino(Treino treino) {
        this.treinosDoPlano.add(treino);
    }

    // --- Getters e Setters ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
