package br.com.negocio.treinos;

import java.time.LocalDate;

public class Meta {
    private String descricao;
    private double valorAlvo;
    private TipoMeta tipoMeta;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private double progressoAtual;

    public Meta(String descricao, double valorAlvo, TipoMeta tipoMeta, LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de término.");
        }
        this.descricao = descricao;
        this.valorAlvo = valorAlvo;
        this.tipoMeta = tipoMeta;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.progressoAtual = 0;
    }

    public void atualizarProgresso(double valor) {
        this.progressoAtual += valor;
        if (this.progressoAtual >= valorAlvo) {
            System.out.println("Parabéns! Meta '" + descricao + "' atingida!");
        }
    }

    // --- Getters e Setters ---
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorAlvo() {
        return valorAlvo;
    }
}
