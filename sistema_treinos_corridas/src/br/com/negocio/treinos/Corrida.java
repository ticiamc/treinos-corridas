package br.com.negocio.treinos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Corrida extends Treino {
    private double distanciaEmMetros;

    public Corrida(String nomeTreino, LocalDateTime dataExecucao, int duracaoSegundos, double distanciaEmMetros) {
        super(nomeTreino, dataExecucao, duracaoSegundos);
        if (distanciaEmMetros <= 0) throw new IllegalArgumentException("Distância deve ser maior que zero.");
        this.distanciaEmMetros = distanciaEmMetros;
    }

    public double calcularVelocidadeMediaKmPorHora() {
        if (this.duracaoSegundos == 0) return 0;
        double distanciaEmKm = this.distanciaEmMetros / 1000.0;
        double tempoEmHoras = this.duracaoSegundos / 3600.0;
        return distanciaEmKm / tempoEmHoras;
    }

    @Override
    public double calcularCaloriasQueimadas(Usuario usuario) {
        double met = 7.0;
        double tempoEmHoras = this.duracaoSegundos / 3600.0;
        return met * usuario.getPeso() * tempoEmHoras;
    }

    @Override
    public String toString() {
        return String.format("%s (Corrida) em %s | Distância: %.2f km | Duração: %d min | Vel. Média: %.2f km/h",
            this.nomeTreino,
            this.dataExecucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            this.distanciaEmMetros / 1000,
            this.duracaoSegundos / 60,
            this.calcularVelocidadeMediaKmPorHora()
        );
    }

    public double getDistanciaEmMetros() { return distanciaEmMetros; }
    public void setDistanciaEmMetros(double distanciaEmMetros) { this.distanciaEmMetros = distanciaEmMetros; }
}