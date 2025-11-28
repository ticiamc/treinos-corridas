package br.com.negocio.treinos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Desafio {
    private static int proximoId = 1;
    private int idDesafio;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Usuario criador; 
    private List<ParticipacaoDesafio> participacoes;

    
    public Desafio(String nome, String descricao, LocalDate dataInicio, LocalDate dataFim, Usuario criador) {
        if (dataInicio.isAfter(dataFim)) throw new IllegalArgumentException("Início depois do fim.");
        this.idDesafio = proximoId++;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.criador = criador;
        this.participacoes = new ArrayList<>();
    }

    public void adicionarParticipante(Usuario usuario) {
        boolean jaParticipa = participacoes.stream().anyMatch(p -> p.getUsuario().equals(usuario));
        if (!jaParticipa) {
            this.participacoes.add(new ParticipacaoDesafio(usuario, this));
        }
    }

    // Getters e Setters
    public int getIdDesafio() { return idDesafio; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    
    public Usuario getCriador() { return criador; }
    
    public List<ParticipacaoDesafio> getParticipacoes() { return participacoes; }
}