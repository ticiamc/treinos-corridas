package br.com.negocio.treinos;

import java.time.LocalDateTime;

/**
 * Essa é uma classe "molde" ou "modelo" para qualquer tipo de treino.
 * Ninguém cria um "Treino" genérico, mas sim cria uma "Corrida" ou "Intervalado"
 * que são tipos específicos de Treino. Por isso ela é 'abstract'.
 *
 * Ela garante que todo tipo de treino tenha pelo menos uma data e uma duração.
 */
public abstract class Treino {

    // 'protected' significa que só essa classe e suas "filhas" (Corrida, Intervalado)
    // podem acessar diretamente essa informação.
    protected LocalDateTime dataExecucao;
    protected int duracaoSegundos; // Guardar em segundos facilita muito os cálculos depois.

    public Treino(LocalDateTime dataExecucao, int duracaoSegundos) {
        if (duracaoSegundos <= 0) {
            throw new IllegalArgumentException("A duração do treino tem que ser maior que zero.");
        }
        this.dataExecucao = dataExecucao;
        this.duracaoSegundos = duracaoSegundos;
    }

    /**
     * Este é um método "obrigatório". Toda classe filha (Corrida, Intervalado, etc.)
     * DEVE ter o seu próprio jeito de calcular as calorias, pois o cálculo
     * muda dependendo do tipo de exercício.
     *
     * @param usuario Os dados do usuário (como peso) são necessários pro cálculo.
     * @return A quantidade de calorias queimadas.
     */
    public abstract double calcularCaloriasQueimadas(Usuario usuario);


    // --- Getters ---

    public LocalDateTime getDataExecucao() {
        return dataExecucao;
    }

    public int getDuracaoSegundos() {
        return duracaoSegundos;
    }
}
