import java.time.LocalDate;
import java.time.LocalDateTime;

public class Principal {
    public static void main(String[] args) {
        // 1. Crie usuários e treinos
        Usuario usuario1 = new Usuario("Fulano da Silva", 30, 75.5, 1.80, "fulano@abc.com");
        
        Corrida c1 = new Corrida(LocalDateTime.now().minusDays(5), 3600, 10000); // 10km em 1h
        Intervalado i1 = new Intervalado(LocalDateTime.now().minusDays(2), 2700, 5, 120); // 45min
        usuario1.adicionarTreino(c1);
        usuario1.adicionarTreino(i1);

        // 2. Crie um desafio
        Desafio desafioMes = new Desafio("Maratona de Outubro", "Correr a maior distância no mês", LocalDate.now().withDayOfMonth(1), LocalDate.now().withDayOfMonth(31));
        desafioMes.adicionarParticipante(usuario1);
        // ... adicione outros participantes
        
        // Atualize o progresso de um participante
        // (a lógica de atualização do progresso estaria em outro lugar, aqui simulamos)
        desafioMes.getParticipacoes().get(0).setProgresso(150.5); // Fulano correu 150.5 km

        // 3. Gere os relatórios
        Relatorio relatorio = new Relatorio();

        // Gerando Relatório de Atividades (REQ16)
        String relatorioAtividades = relatorio.gerarRelatorioAtividades(usuario1, LocalDate.now().minusDays(30), LocalDate.now());
        relatorio.exportar(relatorioAtividades, "PDF");
        
        System.out.println("\n");

        // Gerando Relatório de Evolução (REQ17)
        String relatorioEvolucao = relatorio.gerarRelatorioEvolucao(usuario1);
        relatorio.exportar(relatorioEvolucao, "CSV");
        
        System.out.println("\n");
        
        // Gerando Ranking do Desafio (REQ18)
        String ranking = relatorio.gerarRankingDesafio(desafioMes);
        relatorio.exportar(ranking, "PDF");
    }
}
