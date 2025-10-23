package br.com.negocio.treinos;

import java.time.LocalDate;
// O import java.util.UUID foi removido pois não é usado (corrigindo o warning da imagem)

/**
 * Representa uma Meta (objetivo) que um Usuário deseja alcançar.
 */
public class Meta {

    private static int proximoId = 1; // Contador estático para IDs
    
    private int idMeta;
    private String descricao;
    private TipoMeta tipoMeta;
    private double valorAlvo; // Valor principal (pode ser distância em metros ou tempo em minutos)
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status; // Ex: "Pendente", "Concluída"

    // Construtor usado pelo ControladorMeta
    public Meta(String descricao, double valorAlvo, TipoMeta tipoMeta, LocalDate dataInicio, LocalDate dataFim) {
        this.idMeta = proximoId++;
        this.descricao = descricao;
        this.valorAlvo = valorAlvo;
        this.tipoMeta = tipoMeta;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = "Pendente"; // Toda meta começa como pendente
    }

    // --- Getters e Setters (Necessários pelos Controladores) ---

    public int getIdMeta() {
        return idMeta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoMeta getTipo() { // Usado por TreinoProgresso
        return tipoMeta;
    }

    public void setTipoMeta(TipoMeta tipoMeta) {
        this.tipoMeta = tipoMeta;
    }

    public double getValorAlvo() {
        return valorAlvo;
    }

    public void setValorAlvo(double valorAlvo) {
        this.valorAlvo = valorAlvo;
    }
    
    // Métodos específicos para TreinoProgresso
    
    // Retorna o alvo (se for meta de distância)
    public double getDistancia() {
        return (tipoMeta == TipoMeta.DISTANCIA) ? valorAlvo : 0;
    }
    
    // Retorna o alvo (se for meta de tempo)
    public double getTempo() {
        return (tipoMeta == TipoMeta.TEMPO) ? valorAlvo : 0;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getStatus() { // Usado por TreinoProgresso
        return status;
    }

    public void setStatus(String status) { // Usado por TreinoProgresso
        this.status = status;
    }

    @Override
    public String toString() {
        String alvo = "";
        if (tipoMeta == TipoMeta.DISTANCIA) {
            alvo = String.format("%.2f km", valorAlvo / 1000.0);
        } else if (tipoMeta == TipoMeta.TEMPO) {
            alvo = String.format("%.0f min", valorAlvo);
        } else {
            alvo = String.format("%.0f kcal", valorAlvo);
        }
        
        return String.format("ID: %d | Meta: %s | Alvo: %s | Status: %s | Prazo: %s",
            idMeta,
            descricao,
            alvo,
            status,
            dataFim.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }
}