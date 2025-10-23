package br.com.negocio.treinos;

public abstract class TreinoProgresso {
    private static String status;

    public static String verificarProgresso(Treino treino, Meta meta){
        if (treino instanceof Intervalado){
            Intervalado treinoAnalisado = (Intervalado) treino;
            if (treinoAnalisado.getSeries() < meta.getValorAlvo()*0.5){
                status = "Incompleto";
            }
            else if (treinoAnalisado.getSeries() < meta.getValorAlvo()*0.8){
                status = "Está quase";
            }
            else if (treinoAnalisado.getSeries() >= meta.getValorAlvo()){
                status = "Concluído";
                treino.setStatus(true);
            }
        }
        else if (treino instanceof Corrida){
            Corrida treinoAnalisado = (Corrida) treino;
            if (treinoAnalisado.getDistanciaEmMetros() <  meta.getValorAlvo()*0.5){
                status = "Incompleto";
            }
            else if (treinoAnalisado.getDistanciaEmMetros() < meta.getValorAlvo()*0.8){
                status = "Está quase";
            }
            else if (treinoAnalisado.getDistanciaEmMetros() >= meta.getValorAlvo()){
                status = "Concluído";
                treino.setStatus(true);
            }
        } 
        return status;
    }
}
