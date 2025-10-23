package br.com.negocio.treinos;

/**
 * Classe utilitária para verificar o progresso de um treino em relação a uma meta.
 * (Corrigido para usar os getters corretos e valores da meta)
 */
public abstract class TreinoProgresso {
    // private static String status; // Não deve ser estático, pois muda para cada chamada

    public static String verificarProgresso(Treino treino, Meta meta){
        String status = "Indefinido";
        double valorAlvo = meta.getValorAlvo();
        double valorAtual = 0;

        if (treino instanceof Intervalado && (meta.getTipoMeta() == TipoMeta.TEMPO || meta.getTipoMeta() == TipoMeta.CALORIAS)){
            // Exemplo: Meta de TEMPO (em minutos) vs Duração do treino (em segundos)
            if(meta.getTipoMeta() == TipoMeta.TEMPO) {
                 valorAtual = treino.getDuracaoSegundos() / 60.0; // Converte para minutos
            }
            // Aqui poderia ter lógica para calorias ou séries, se a meta for desse tipo
            
        }
        else if (treino instanceof Corrida && meta.getTipoMeta() == TipoMeta.DISTANCIA){
            Corrida treinoAnalisado = (Corrida) treino;
            // Exemplo: Meta de DISTANCIA (em km) vs Distância da corrida (em metros)
            valorAtual = treinoAnalisado.getDistanciaEmMetros() / 1000.0; // Converte para km
        } 
        
        // Lógica de progresso genérica
        if (valorAtual == 0) {
            status = "Não aplicável ou Incompleto";
        } else if (valorAtual < valorAlvo * 0.5){
            status = "Incompleto";
        }
        else if (valorAtual < valorAlvo * 0.99){ // Quase lá
            status = "Está quase";
        }
        else if (valorAtual >= valorAlvo){
            status = "Concluído";
            treino.setStatus(true); // Marca o treino como concluído
            meta.atualizarProgresso(valorAtual); // Atualiza o progresso na meta
        }
        
        return status;
    }
}
