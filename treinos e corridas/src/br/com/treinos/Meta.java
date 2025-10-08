package br.com.treinos;

import java.time.LocalDate;

/**
 * Representa uma meta a ser alcançada pelo usuário.
 * Corresponde aos requisitos de Definição de Metas
 */
public class Meta {

    private String descricao;
    private double valorAlvo;
    private TipoMeta tipoMeta;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private double progressoAtual;

    public Meta(String descricao, double valorAlvo, TipoMeta tipoMeta, LocalDate dataInicio, LocalDate dataFim) {
        // Validação de período
        if (dataInicio.isAfter(dataFim) || dataFim.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Período da meta é inválido.");
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
    }
    
    public boolean metaAtingida() {
        return this.progressoAtual >= this.valorAlvo;
    }

    // Getters e Setters
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public double getValorAlvo() { return valorAlvo; }
    public void setValorAlvo(double valorAlvo) { this.valorAlvo = valorAlvo; }
    public TipoMeta getTipoMeta() { return tipoMeta; }
    public void setTipoMeta(TipoMeta tipoMeta) { this.tipoMeta = tipoMeta; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public double getProgressoAtual() { return progressoAtual; }
}