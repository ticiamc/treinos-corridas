package br.com.treinos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um desafio para competir com outros usuários
 */
public class Desafio {
    
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<ParticipacaoDesafio> participacoes;

    public Desafio(String nome, String descricao, LocalDate dataInicio, LocalDate dataFim) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.participacoes = new ArrayList<>();
    }

    public void adicionarParticipante(Usuario usuario) {
        ParticipacaoDesafio novaParticipacao = new ParticipacaoDesafio(usuario, this);
        this.participacoes.add(novaParticipacao);
        usuario.getParticipacoesEmDesafios().add(novaParticipacao);
    }
    
    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public List<ParticipacaoDesafio> getParticipacoes() { return participacoes; }
}