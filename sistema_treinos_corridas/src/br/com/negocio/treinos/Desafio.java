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
    private List<ParticipacaoDesafio> participacoes;

    public Desafio(String nome, String descricao, LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio.isAfter(dataFim)) throw new IllegalArgumentException("Início depois do fim.");
        this.idDesafio = proximoId++;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.participacoes = new ArrayList<>();
    }

    public void adicionarParticipante(Usuario usuario) {
        boolean jaParticipa = participacoes.stream().anyMatch(p -> p.getUsuario().equals(usuario));
        if (!jaParticipa) {
            this.participacoes.add(new ParticipacaoDesafio(usuario, this));
        }
    }

    public int getIdDesafio() { return idDesafio; }
    public String getNome() { return nome; }
    public List<ParticipacaoDesafio> getParticipacoes() { return participacoes; }
    public String getDescricao() { return descricao; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
}