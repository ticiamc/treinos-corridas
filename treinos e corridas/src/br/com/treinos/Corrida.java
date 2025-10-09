package br.com.treinos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa o treino de corrida.
 * Ela "herda" tudo que a classe Treino tem (data e duração) e adiciona
 * o que é específico da corrida: a distância.
 */
public class Corrida extends Treino {

    private double distanciaEmMetros;

    public Corrida(LocalDateTime dataExecucao, int duracaoSegundos, double distanciaEmMetros) {
        // 'super' chama o construtor da classe "mãe" (Treino) pra guardar a data e duração.
        super(dataExecucao, duracaoSegundos);
        if (distanciaEmMetros <= 0) {
            throw new IllegalArgumentException("A distância da corrida tem que ser maior que zero.");
        }
        this.distanciaEmMetros = distanciaEmMetros;
    }

    /**
     * Faz a conta pra descobrir a velocidade média em km/h.
     * A fórmula é: (distância em km) / (tempo em horas).
     */
    public double calcularVelocidadeMediaKmPorHora() {
        if (this.duracaoSegundos == 0) {
            return 0; // Evita divisão por zero se o treino não teve duração.
        }
        double distanciaEmKm = this.distanciaEmMetros / 1000.0;
        double tempoEmHoras = this.duracaoSegundos / 3600.0;
        return distanciaEmKm / tempoEmHoras;
    }

    /**
     * Implementação da "obrigação" que a classe Treino nos deu.
     * O cálculo aqui é uma simplificação. Em um app real, a fórmula seria mais complexa.
     */
    @Override
    public double calcularCaloriasQueimadas(Usuario usuario) {
        // Fórmula simplificada: (MET da corrida * peso da pessoa * tempo em horas)
        // MET (Metabolic Equivalent of Task) para corrida é ~7.0
        double met = 7.0;
        double tempoEmHoras = this.duracaoSegundos / 3600.0;
        return met * usuario.getPeso() * tempoEmHoras;
    }

    @Override
    public String toString() {
        return String.format("Corrida em %s | Distância: %.2f km | Duração: %d min | Velocidade Média: %.2f km/h",
            this.dataExecucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            this.distanciaEmMetros / 1000,
            this.duracaoSegundos / 60,
            this.calcularVelocidadeMediaKmPorHora()
        );
    }

    // --- Getter ---
    public double getDistanciaEmMetros() {
        return distanciaEmMetros;
    }
}
