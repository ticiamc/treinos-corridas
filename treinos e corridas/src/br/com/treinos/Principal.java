package br.com.treinos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Principal {
    public static void main(String[] args) {
        System.out.println("Sistema de Treinos e Corridas iniciado!");

        Usuario usuario = new Usuario("José Leonardo da Silva", 30, 75.5, 1.80, "zeleonardo@gmail.com");
        System.out.println("Usuário criado: " + usuario.getNome());

        Corrida corrida1 = new Corrida(LocalDateTime.now(), 1800, 5000); // 30 min, 5km
        usuario.adicionarTreino(corrida1);
        System.out.println("Corrida de 5km registrada!");
        System.out.printf("Calorias queimadas: %.2f kcal\n", corrida1.calcularCaloriasQueimadas(usuario));
        System.out.printf("Velocidade média: %.2f km/h\n", corrida1.calcularVelocidadeMediaKmPorHora());

        Meta metaDistancia = new Meta("Correr 100km no mês", 100000, TipoMeta.DISTANCIA, LocalDate.now(), LocalDate.now().plusMonths(1));
        usuario.adicionarMeta(metaDistancia);
        System.out.println("Meta criada: " + metaDistancia.getDescricao());

        metaDistancia.atualizarProgresso(corrida1.getDistanciaEmMetros());
        System.out.printf("Progresso da meta: %.2f / %.2f metros\n", metaDistancia.getProgressoAtual(), metaDistancia.getValorAlvo());

        Desafio desafioVerao = new Desafio("Desafio de Verão", "Correr a maior distância em 1 semana", LocalDate.now(), LocalDate.now().plusWeeks(1));
        desafioVerao.adicionarParticipante(usuario);
        System.out.println("Usuário " + usuario.getNome() + " entrou no desafio: " + desafioVerao.getNome());
    }
}