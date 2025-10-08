package br.com.treinos;

import java.time.LocalDateTime;

/**
 * Representa um treino do tipo Intervalado.
 * Especialização da classe Treino 
 */
public class Intervalado extends Treino {

    private int series;
    private int descansoEntreSeriesSeg;

    public Intervalado(LocalDateTime dataExecucao, int duracaoSegundos, int series, int descansoEntreSeriesSeg) {
        super(dataExecucao, duracaoSegundos);
        this.series = series;
        this.descansoEntreSeriesSeg = descansoEntreSeriesSeg;
    }

    @Override
    public double calcularCaloriasQueimadas(Usuario usuario) {
        // Fórmula de exemplo para treino intervalado (HIIT)
        // MET para HIIT ~12.0
        double met = 12.0;
        double duracaoMinutos = getDuracaoSegundos() / 60.0;
        return (met * usuario.getPeso() * 3.5) / 200 * duracaoMinutos;
    }

    // Getters e Setters
    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getDescansoEntreSeriesSeg() {
        return descansoEntreSeriesSeg;
    }

    public void setDescansoEntreSeriesSeg(int descansoEntreSeriesSeg) {
        this.descansoEntreSeriesSeg = descansoEntreSeriesSeg;
    }
}