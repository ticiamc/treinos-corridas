package br.com.negocio.treinos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Meta {
    private static int proximoId = 1;
    private int idMeta;
    private String descricao;
    private TipoMeta tipoMeta;
    private double valorAlvo; 
    private double progressoAtual; // Campo que guarda o acumulado
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
        this.progressoAtual = 0.0;
        this.status = "Pendente";
    }

    public void adicionarProgresso(double valor) {
        if (!this.status.equalsIgnoreCase("Concluída")) {
            this.progressoAtual += valor;
        }
    }

    public int getIdMeta() { return idMeta; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public TipoMeta getTipo() { return tipoMeta; }
    public void setTipoMeta(TipoMeta tipoMeta) { this.tipoMeta = tipoMeta; }
    
    public double getValorAlvo() { return valorAlvo; }
    public void setValorAlvo(double valorAlvo) { this.valorAlvo = valorAlvo; }
    
    public double getProgressoAtual() { return progressoAtual; }

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
        String alvoStr = "";
        String progressoStr = "";
        
        // Formatação inteligente dependendo do tipo
        if (tipoMeta == TipoMeta.DISTANCIA) {
            alvoStr = String.format("%.1f km", valorAlvo / 1000.0);
            progressoStr = String.format("%.1f km", progressoAtual / 1000.0);
        } else if (tipoMeta == TipoMeta.TEMPO) {
            alvoStr = String.format("%.0f min", valorAlvo);
            progressoStr = String.format("%.0f min", progressoAtual);
        } else {
            alvoStr = String.format("%.0f kcal", valorAlvo);
            progressoStr = String.format("%.0f kcal", progressoAtual);
        }
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // Exibe: [PENDENTE] Correr Maratona | 5.0 km de 42.0 km | Prazo: 30/12/2025
        return String.format("[%s] %s | %s de %s | Prazo: %s",
            status.toUpperCase(),
            descricao,
            progressoStr, // Quanto já fez
            alvoStr,      // Qual é o objetivo
            dataFim.format(fmt)
        );
    }
}