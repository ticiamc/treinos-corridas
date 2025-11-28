package br.com.negocio.treinos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanoTreino {
    private static int proximoId = 1;
    private int idPlano;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Usuario usuarioAlvo;
    private List<Treino> treinosDoPlano;

    public PlanoTreino(String nome, LocalDate dataInicio, LocalDate dataFim, Usuario usuarioAlvo) {
        this.idPlano = proximoId++;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.usuarioAlvo = usuarioAlvo;
        this.treinosDoPlano = new ArrayList<>();
    }

    public void adicionarTreino(Treino treino) {
        if (treino != null && !this.treinosDoPlano.contains(treino)) {
            this.treinosDoPlano.add(treino);
        }
    }

    public void removerTreino(Treino treino) {
        if (treino != null) this.treinosDoPlano.remove(treino);
    }

    public int getIdPlano() { return idPlano; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public Usuario getUsuarioAlvo() { return usuarioAlvo; }
    public List<Treino> getTreinosDoPlano() { return treinosDoPlano; }
}