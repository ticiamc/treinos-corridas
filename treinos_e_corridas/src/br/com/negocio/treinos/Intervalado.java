package br.com.negocio.treinos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um treino intervalado, aquele com picos de esforço e descanso.
 * Também herda de Treino e adiciona suas próprias características.
 */
public class Intervalado extends Treino {

    private int series; // Quantas "explosões" de exercício a pessoa fez.
    private int descansoEntreSeriesSeg; // Tempo de descanso entre as séries.

    public Intervalado(LocalDateTime dataExecucao, int duracaoSegundos, int series, int descansoEntreSeriesSeg) {
        super(dataExecucao, duracaoSegundos);
        this.series = series;
        this.descansoEntreSeriesSeg = descansoEntreSeriesSeg;
    }

    /**
     * A fórmula pra queima de calorias em treinos intervalados é diferente
     * da corrida contínua, por isso o cálculo aqui é outro.
     */
    @Override
    public double calcularCaloriasQueimadas(Usuario usuario) {
        // Fórmula simplificada para HIIT (High-Intensity Interval Training)
        // MET para HIIT é mais alto, ~8.0
        double met = 8.0;
        double tempoEmHoras = this.duracaoSegundos / 3600.0;
        return met * usuario.getPeso() * tempoEmHoras;
    }
    
    @Override
    public String toString() {
        return String.format("Treino Intervalado em %s | Duração: %d min | Séries: %d",
            this.dataExecucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            this.duracaoSegundos / 60,
            this.series
        );
    }
}
