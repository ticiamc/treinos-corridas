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
    protected String nomeTreino;
    protected LocalDateTime dataExecucao;
    protected int duracaoSegundos;
    protected boolean status; 

    public Treino(String nomeTreino, LocalDateTime dataExecucao, int duracaoSegundos) {
        if (duracaoSegundos <= 0) {
            throw new IllegalArgumentException("A duração do treino tem que ser maior que zero.");
        }
        this.nomeTreino = nomeTreino;
        this.dataExecucao = dataExecucao;
        this.duracaoSegundos = duracaoSegundos;
        this.status = false;
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
    
    public void setDataExecucao(LocalDateTime dataExecucao) {
        this.dataExecucao = dataExecucao;
    }

    public void setDuracaoSegundos(int duracaoSegundos) {
        this.duracaoSegundos = duracaoSegundos;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getNomeTreino() {
        return nomeTreino;
    }

    public void setNomeTreino(String nomeTreino) {
        this.nomeTreino = nomeTreino;
    }

    
}
