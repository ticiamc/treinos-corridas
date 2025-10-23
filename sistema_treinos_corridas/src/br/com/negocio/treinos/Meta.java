package br.com.negocio.treinos;

import java.time.LocalDate;

public class Meta {
    
    private static int proximoId = 1; // Contador estático para IDs
    private int idMeta;
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
        this.idMeta = proximoId++; // Atribui ID único
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
    
    public int getIdMeta() {
        return idMeta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorAlvo() {
        return valorAlvo;
    }

    public void setValorAlvo(double valorAlvo) {
        this.valorAlvo = valorAlvo;
    }
    
    public TipoMeta getTipoMeta() {
        return tipoMeta;
    }

    public void setTipoMeta(TipoMeta tipoMeta) {
        this.tipoMeta = tipoMeta;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    // Data de início não deve ser alterada após criação, geralmente.
    // public void setDataInicio(LocalDate dataInicio) {
    //     this.dataInicio = dataInicio;
    // }
   
    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        if (this.dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de término.");
        }
        this.dataFim = dataFim;
    }
    
    public double getProgressoAtual() {
        return progressoAtual;
    }

    // O progresso é atualizado apenas por atualizarProgresso
    // public void setProgressoAtual(double progressoAtual) {
    //     this.progressoAtual = progressoAtual;
    // }
}
