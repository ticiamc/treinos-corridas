package br.com.treinos;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Essa classe não faz parte do sistema em si, mas serve pra gente testar
 * se as outras classes estão funcionando como deveriam.
 * É como um "laboratório" onde a gente cria objetos e chama os métodos.
 */
public class Principal {
    public static void main(String[] args) {
        // Bloco de código para simular a utilização do sistema.

        // 1. Criando usuários
        Usuario usuario1 = new Usuario("Sarah Lima", 30, 75.5, 1.80, "sarah@abc.com");
        Usuario usuario2 = new Usuario("Leticia Marques", 25, 68.0, 1.70, "leticia@abc.com");

        // 2. Registrando alguns treinos para a Sarah
        Corrida c1 = new Corrida(LocalDateTime.now().minusDays(10), 3600, 10000); // 10km
        Intervalado i1 = new Intervalado(LocalDateTime.now().minusDays(5), 2700, 8, 120);
        Corrida c2 = new Corrida(LocalDateTime.now().minusDays(1), 3500, 10500); // 10.5km
        usuario1.adicionarTreino(c1);
        usuario1.adicionarTreino(i1);
        usuario1.adicionarTreino(c2);
        
        // Registrando treinos para Leticia
        Corrida c3 = new Corrida(LocalDateTime.now().minusDays(8), 2000, 5000);
        usuario2.adicionarTreino(c3);

        // 3. Criando um desafio e adicionando os participantes
        Desafio desafioDezembro = new Desafio("Dezembro em Movimento", "Correr a maior distância no mês", LocalDate.now().withDayOfMonth(1), LocalDate.now().withDayOfMonth(31));
        desafioDezembro.adicionarParticipante(usuario1);
        desafioDezembro.adicionarParticipante(usuario2);
        
        // 4. Simulando a atualização do progresso no desafio
        // Num sistema real, isso seria automático ao registrar um treino.
        desafioDezembro.getParticipacoes().get(0).setProgresso(20.5); // Sarah correu 20.5 km
        desafioDezembro.getParticipacoes().get(1).setProgresso(15.0); // Leticia correu 15.0 km

        // 5. Instanciando a classe de relatórios pra gerar as análises
        Relatorio relatorio = new Relatorio();

        System.out.println("### GERANDO RELATÓRIO DE ATIVIDADES PARA SARAH ###");
        String relatorioAtividades = relatorio.gerarRelatorioAtividades(usuario1, LocalDate.now().minusDays(30), LocalDate.now());
        relatorio.exportar(relatorioAtividades, "PDF");
        
        System.out.println("\n");
        
        System.out.println("### GERANDO RELATÓRIO DE EVOLUÇÃO PARA SARAH ###");
        String relatorioEvolucao = relatorio.gerarRelatorioEvolucao(usuario1);
        relatorio.exportar(relatorioEvolucao, "CSV");

        System.out.println("\n");

        System.out.println("### GERANDO RANKING DO DESAFIO ###");
        String ranking = relatorio.gerarRankingDesafio(desafioDezembro);
        relatorio.exportar(ranking, "PDF");
    }
}
