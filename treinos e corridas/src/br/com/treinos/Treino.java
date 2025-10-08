package br.com.treinos;

import java.time.LocalDateTime;

public abstract class Treino {
    private LocalDateTime dataExecucao;
    private int duracaoSegundos;

    public Treino(LocalDateTime dataExecucao, int duracaoSegundos) {
        if (duracaoSegundos <= 0) {
            throw new IllegalArgumentException("A duração do treino deve ser positiva.");
        }
        this.dataExecucao = dataExecucao;
        this.duracaoSegundos = duracaoSegundos;
    }

    public abstract double calcularCaloriasQueimadas(Usuario usuario);

    // Getters e Setters
    public LocalDateTime getDataExecucao() { return dataExecucao; }
    public int getDuracaoSegundos() { return duracaoSegundos; }
}