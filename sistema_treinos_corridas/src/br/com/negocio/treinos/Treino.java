package br.com.negocio.treinos;

import java.time.LocalDateTime;

public abstract class Treino {
    private static int proximoId = 1;
    protected int idTreino;
    protected String nomeTreino;
    protected LocalDateTime dataExecucao;
    protected int duracaoSegundos;
    protected boolean status;

    public Treino(String nomeTreino, LocalDateTime dataExecucao, int duracaoSegundos) {
        if (duracaoSegundos <= 0) throw new IllegalArgumentException("Duração deve ser maior que zero.");
        this.idTreino = proximoId++;
        this.nomeTreino = nomeTreino;
        this.dataExecucao = dataExecucao;
        this.duracaoSegundos = duracaoSegundos;
        this.status = false;
    }

    public abstract double calcularCaloriasQueimadas(Usuario usuario);

    public int getIdTreino() { return idTreino; }
    public LocalDateTime getDataExecucao() { return dataExecucao; }
    public void setDataExecucao(LocalDateTime dataExecucao) { this.dataExecucao = dataExecucao; }
    public int getDuracaoSegundos() { return duracaoSegundos; }
    public void setDuracaoSegundos(int duracaoSegundos) { this.duracaoSegundos = duracaoSegundos; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public String getNomeTreino() { return nomeTreino; }
    public void setNomeTreino(String nomeTreino) { this.nomeTreino = nomeTreino; }
}