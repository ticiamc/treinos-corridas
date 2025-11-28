package br.com.negocio.treinos;

import java.time.LocalDate;

public class Meta {
    private static int proximoId = 1;
    private int idMeta;
    private String descricao;
    private TipoMeta tipoMeta;
    private double valorAlvo; 
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status;

    public Meta(String descricao, double valorAlvo, TipoMeta tipoMeta, LocalDate dataInicio, LocalDate dataFim) {
        this.idMeta = proximoId++;
        this.descricao = descricao;
        this.valorAlvo = valorAlvo;
        this.tipoMeta = tipoMeta;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = "Pendente";
    }

    public int getIdMeta() { return idMeta; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public TipoMeta getTipo() { return tipoMeta; }
    public void setTipoMeta(TipoMeta tipoMeta) { this.tipoMeta = tipoMeta; }
    public double getValorAlvo() { return valorAlvo; }
    public void setValorAlvo(double valorAlvo) { this.valorAlvo = valorAlvo; }
    
    public double getDistancia() { return (tipoMeta == TipoMeta.DISTANCIA) ? valorAlvo : 0; }
    public double getTempo() { return (tipoMeta == TipoMeta.TEMPO) ? valorAlvo : 0; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        String alvo = "";
        if (tipoMeta == TipoMeta.DISTANCIA) alvo = String.format("%.2f km", valorAlvo / 1000.0);
        else if (tipoMeta == TipoMeta.TEMPO) alvo = String.format("%.0f min", valorAlvo);
        else alvo = String.format("%.0f kcal", valorAlvo);
        
        return String.format("ID: %d | Meta: %s | Alvo: %s | Status: %s | Prazo: %s",
            idMeta, descricao, alvo, status,
            dataFim.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }
}