package br.com.negocio.treinos;

/**
 * Representa uma Meta (objetivo) que um Usuário deseja alcançar.
 * A meta pode ser de diferentes tipos (DISTANCIA, TEMPO, etc.),
 * definidos pelo enum TipoMeta.
 */
public class Meta {

    // --- Atributos ---
    private String descricao; //(ex: "Correr 10km")
    private TipoMeta tipoMeta; //(DISTANCIA, TEMPO)
    private double distancia; //(em metros)
    private double tempo; //(em minutos)
    private boolean concluida; // Status que indica se a meta já foi batida

    // --- Construtor ---
   
    public Meta(String descricao, TipoMeta tipoMeta) {
        this.descricao = descricao;
        this.tipoMeta = tipoMeta;
        this.concluida = false; // Toda meta começa como "não concluída"
    }

    // --- Getters e Setters ---
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoMeta getTipoMeta() {
        return tipoMeta;
    }

    public void setTipoMeta(TipoMeta tipoMeta) {
        this.tipoMeta = tipoMeta;
    }

    public double getDistancia() {
        return distancia;
    }

    // Define o valor-alvo para metas de DISTANCIA (em metros).
     
    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getTempo() {
        return tempo;
    }

    // Define o valor-alvo para metas de TEMPO (em minutos).
    
    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}
