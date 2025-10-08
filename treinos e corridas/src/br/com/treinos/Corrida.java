package br.com.treinos;

import java.time.LocalDateTime;

/**
 * Representa um treino do tipo Corrida.
 * Especialização da classe Treino
 */
public class Corrida extends Treino {

    private double distanciaEmMetros;

    public Corrida(LocalDateTime dataExecucao, int duracaoSegundos, double distanciaEmMetros) {
        super(dataExecucao, duracaoSegundos);
        if (distanciaEmMetros <= 0) {
            throw new IllegalArgumentException("A distância deve ser positiva.");
        }
        this.distanciaEmMetros = distanciaEmMetros;
    }

    /**
     * Calcula a velocidade média em km/h 
     */
    public double calcularVelocidadeMediaKmPorHora() {
        double duracaoEmHoras = getDuracaoSegundos() / 3600.0;
        double distanciaEmKm = distanciaEmMetros / 1000.0;
        if (duracaoEmHoras == 0) return 0;
        return distanciaEmKm / duracaoEmHoras;
    }

    @Override
    public double calcularCaloriasQueimadas(Usuario usuario) {
        // Fórmula de exemplo: (MET * peso * 3.5) / 200 * duração_minutos
        // MET para corrida ~8.0
        double met = 8.0;
        double duracaoMinutos = getDuracaoSegundos() / 60.0;
        return (met * usuario.getPeso() * 3.5) / 200 * duracaoMinutos;
    }
    
    // Getters e Setters
    public double getDistanciaEmMetros() {
        return distanciaEmMetros;
    }

    public void setDistanciaEmMetros(double distanciaEmMetros) {
         if (distanciaEmMetros <= 0) {
            throw new IllegalArgumentException("A distância deve ser positiva.");
        }
        this.distanciaEmMetros = distanciaEmMetros;
    }
}