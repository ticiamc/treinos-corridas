package br.com.treinos;

import java.time.LocalDate;
import java.util.List;

/**
 * Representa um plano de treino com uma sequência de atividades 
 */
public class PlanoTreino {

    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<Treino> sequenciaDeTreinos;
    private Usuario usuarioAlvo;

    public PlanoTreino(String nome, LocalDate dataInicio, LocalDate dataFim, List<Treino> sequenciaDeTreinos, Usuario usuarioAlvo) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.sequenciaDeTreinos = sequenciaDeTreinos;
        this.usuarioAlvo = usuarioAlvo;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public List<Treino> getSequenciaDeTreinos() { return sequenciaDeTreinos; }
    public void setSequenciaDeTreinos(List<Treino> sequenciaDeTreinos) { this.sequenciaDeTreinos = sequenciaDeTreinos; }
    public Usuario getUsuarioAlvo() { return usuarioAlvo; }
    public void setUsuarioAlvo(Usuario usuarioAlvo) { this.usuarioAlvo = usuarioAlvo; }
}