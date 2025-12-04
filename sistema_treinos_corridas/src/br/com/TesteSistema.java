package br.com;

import br.com.dados.*;
import br.com.negocio.*;
import br.com.negocio.treinos.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class TesteSistema {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   INICIANDO BATERIA DE TESTES COMPLETA (V2)  ");
        System.out.println("==============================================\n");

        // 1. Configuração do Ambiente
        RepositorioClientes repoCli = new RepositorioClientes();
        RepositorioDesafio repoDesafio = new RepositorioDesafio();
        RepositorioPlanoTreino repoPlano = new RepositorioPlanoTreino();

        ControladorCliente ctrlCliente = new ControladorCliente(repoCli);
        ControladorTreino ctrlTreino = new ControladorTreino(repoCli);
        ControladorMeta ctrlMeta = new ControladorMeta(repoCli);
        ControladorDesafio ctrlDesafio = new ControladorDesafio(repoDesafio, repoCli);
        ControladorPlanoTreino ctrlPlano = new ControladorPlanoTreino(repoCli, repoPlano);

        try {
            // ---------------------------------------------------------------
            // GRUPO 1: GERENCIAMENTO DE USUÁRIOS (CRUD)
            // ---------------------------------------------------------------
            System.out.println("--- GRUPO 1: USUÁRIOS ---");
            
            // Teste 1.1: Cadastro
            System.out.print("1.1 Cadastro de Usuário... ");
            Usuario u = new Usuario("Teste Silva", 25, 80.0, 1.80, "teste@email.com", "123");
            ctrlCliente.cadastrarCliente(u);
            if (ctrlCliente.buscarCliente("123") != null) System.out.println("[OK]");
            else System.out.println("[FALHOU]");

            // Teste 1.2: Atualização
            System.out.print("1.2 Atualização de Dados... ");
            u.setNome("Teste Silva Editado");
            u.setPeso(75.0); // Emagreceu
            ctrlCliente.atualizarCliente(u);
            Usuario uBusca = ctrlCliente.buscarCliente("123");
            if (uBusca.getNome().equals("Teste Silva Editado") && uBusca.getPeso() == 75.0) System.out.println("[OK]");
            else System.out.println("[FALHOU] Dados não atualizaram.");

            // ---------------------------------------------------------------
            // GRUPO 2: TREINOS E CÁLCULOS
            // ---------------------------------------------------------------
            System.out.println("\n--- GRUPO 2: TREINOS ---");

            // Teste 2.1: Cadastro e Cálculo de Calorias
            System.out.print("2.1 Cadastro e Cálculo Kcal... ");
            // 60 min, 10km. MET 7.0 * 75kg * 1h = 525 kcal
            ctrlTreino.cadastrarTreino("123", "Corrida", LocalDate.now(), 3600, "Corrida 10k", 10000, 0, 0);
            Treino t = u.getTreinos().get(0);
            double cal = t.calcularCaloriasQueimadas(u);
            if (Math.abs(cal - 525.0) < 0.1) System.out.println("[OK] (525 kcal)");
            else System.out.println("[FALHOU] Esperado 525, deu " + cal);

            // Teste 2.2: Remoção de Treino
            System.out.print("2.2 Remoção de Treino... ");
            int idTreino = t.getIdTreino();
            ctrlTreino.removerTreino("123", idTreino);
            if (u.getTreinos().isEmpty()) System.out.println("[OK]");
            else System.out.println("[FALHOU] Treino não foi removido.");

            // Recadastra treino para os próximos testes
            ctrlTreino.cadastrarTreino("123", "Corrida", LocalDate.now(), 3600, "Corrida Base", 10000, 0, 0);

            // ---------------------------------------------------------------
            // GRUPO 3: METAS E PROGRESSO
            // ---------------------------------------------------------------
            System.out.println("\n--- GRUPO 3: METAS ---");

            // Teste 3.1: Criação e Detecção Automática
            System.out.print("3.1 Meta Batida Automaticamente... ");
            // Meta: 5km. O usuário já tem um treino de 10km.
            ctrlMeta.cadastrarMeta("123", "Correr 5k", TipoMeta.DISTANCIA, 5000, LocalDate.now().plusDays(5));
            // Gatilho: Adicionar um treino pequeno para forçar a verificação (ou chamar manual)
            ctrlTreino.cadastrarTreino("123", "Corrida", LocalDate.now(), 600, "Trote", 1000, 0, 0);
            
            Meta m = u.getMetas().get(0);
            if (m.getStatus().equalsIgnoreCase("Concluída")) System.out.println("[OK]");
            else System.out.println("[FALHOU] Status: " + m.getStatus() + " (Progresso: " + m.getProgressoAtual() + ")");

            // ---------------------------------------------------------------
            // GRUPO 4: DESAFIOS E REGRAS
            // ---------------------------------------------------------------
            System.out.println("\n--- GRUPO 4: DESAFIOS ---");

            // Teste 4.1: Bloqueio de Usuário sem Histórico (REQ25)
            System.out.print("4.1 Bloqueio de Novato (REQ25)... ");
            Usuario novato = new Usuario("Novato", 20, 70, 1.70, "novato@email.com", "999");
            ctrlCliente.cadastrarCliente(novato);
            Desafio d = new Desafio("Desafio Teste", "Desc", LocalDate.now(), LocalDate.now().plusDays(10), u);
            repoDesafio.cadastrar(d); // Cadastro manual no repo para teste
            
            try {
                ctrlDesafio.participarDesafio(d.getIdDesafio(), "999");
                System.out.println("[FALHOU] O sistema permitiu novato participar!");
            } catch (Exception e) {
                System.out.println("[OK] Bloqueado: " + e.getMessage());
            }

            // Teste 4.2: Participação Válida e Ranking
            System.out.print("4.2 Participação e Ranking... ");
            ctrlDesafio.participarDesafio(d.getIdDesafio(), "123"); // Usuário com treinos
            // Usuário 123 tem 11km totais (10k + 1k)
            String ranking = Relatorio.gerarRankingDesafioTexto(d);
            if (ranking.contains("11,00 km") || ranking.contains("11.00 km")) System.out.println("[OK]");
            else System.out.println("[FALHOU] Ranking incorreto.\n" + ranking);

            // ---------------------------------------------------------------
            // GRUPO 5: PLANOS DE TREINO E VALIDAÇÕES
            // ---------------------------------------------------------------
            System.out.println("\n--- GRUPO 5: PLANOS DE TREINO ---");

            // Teste 5.1: Criação de Plano
            System.out.print("5.1 Criação de Plano... ");
            // Plano para o Mês que vem
            LocalDate inicioPlano = LocalDate.now().plusMonths(1);
            LocalDate fimPlano = inicioPlano.plusDays(30);
            ctrlPlano.cadastrarPlano("123", "Maratona Futura", inicioPlano, fimPlano);
            PlanoTreino plano = ctrlPlano.listarPlanos("123").get(0);
            if (plano != null) System.out.println("[OK]");
            else System.out.println("[FALHOU]");

            // Teste 5.2: Bloqueio de Treino Fora da Data.
            System.out.print("5.2 Bloqueio de Data Inválida no Plano... ");
            // Tenta adicionar um treino de HOJE num plano do MÊS QUE VEM
            Treino treinoHoje = u.getTreinos().get(0); // Treino de hoje
            try {
                ctrlPlano.adicionarTreinoPlano("123", plano.getIdPlano(), treinoHoje.getIdTreino());
                System.out.println("[FALHOU] permitiu treino fora da data!");
            } catch (Exception e) {
                if (e.getMessage().contains("fora da vigência")) System.out.println("[OK] Bloqueado corretamente.");
                else System.out.println("[FALHOU] Erro diferente do esperado: " + e.getMessage());
            }

            // ---------------------------------------------------------------
            // GRUPO 6: EXPORTAÇÃO E ARQUIVOS
            // ---------------------------------------------------------------
            System.out.println("\n--- GRUPO 6: EXPORTAÇÃO ---");
            
            System.out.print("6.1 Exportação para CSV... ");
            String caminhoArquivo = "teste_exportacao.csv";
            Relatorio.exportarRelatorioAtividadesCSV(u, caminhoArquivo);
            File arquivo = new File(caminhoArquivo);
            if (arquivo.exists() && arquivo.length() > 0) {
                System.out.println("[OK] Arquivo criado com sucesso.");
                arquivo.delete(); // Limpa a sujeira
            } else {
                System.out.println("[FALHOU] Arquivo não criado.");
            }

        } catch (Exception e) {
            System.out.println("\n[ERRO CRÍTICO NO MEIO DOS TESTES]:");
            e.printStackTrace();
        }
        
        System.out.println("\n==============================================");
        System.out.println("          FIM DA BATERIA DE TESTES            ");
        System.out.println("==============================================");
    }
}