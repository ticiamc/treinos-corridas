package br.com.negocio.treinos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Intervalado extends Treino {

    private int series;
    private int descansoEntreSeriesSeg; 

    public Intervalado(String nomeTreino, LocalDateTime dataExecucao, int duracaoSegundos, int series, int descansoEntreSeriesSeg) {
        super(nomeTreino, dataExecucao, duracaoSegundos);
        this.series = series;
        this.descansoEntreSeriesSeg = descansoEntreSeriesSeg;
    }

    @Override
    public double calcularCaloriasQueimadas(Usuario usuario) {
        double met = 8.0;
        double tempoEmHoras = this.duracaoSegundos / 3600.0;
        return met * usuario.getPeso() * tempoEmHoras;
    }
    
    //  Substitui o método toString() por uma versão personalizada.
    @Override
    public String toString() {
        return String.format("%s (Intervalado) em %s | Duração: %d min | Séries: %d | Descanso: %d seg",
            this.nomeTreino, // Adicionado nome do treino
            this.dataExecucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            this.duracaoSegundos / 60,
            this.series,
            this.descansoEntreSeriesSeg
        );
    }

    // --- Getters --- permitem a outras partes do código ler o valor de um atributo que está protegido como privado dentro de uma classe

    public int getSeries() {
        return series;
    }

    
    public int getDescansoEntreSeriesSeg() {
        return descansoEntreSeriesSeg;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public void setDescansoEntreSeriesSeg(int descansoEntreSeriesSeg) {
        this.descansoEntreSeriesSeg = descansoEntreSeriesSeg;
    }
}
