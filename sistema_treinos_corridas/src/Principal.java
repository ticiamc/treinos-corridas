import br.com.dados.IRepositorioCliente;
import br.com.dados.IRepositorioDesafio;
import br.com.dados.RepositorioClientes;
import br.com.dados.RepositorioDesafio;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorDesafio;
import br.com.negocio.ControladorMeta;
import br.com.negocio.ControladorPlanoTreino;
import br.com.negocio.ControladorTreino;
import br.com.negocio.treinos.*; // Importa todas as classes

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        IRepositorioCliente repositorioCliente = new RepositorioClientes();
        IRepositorioDesafio repositorioDesafio = new RepositorioDesafio();

        ControladorCliente controladorCliente = new ControladorCliente(repositorioCliente);
        ControladorTreino controladorTreino = new ControladorTreino();
        ControladorMeta controladorMeta = new ControladorMeta();
        ControladorPlanoTreino controladorPlanoTreino = new ControladorPlanoTreino();
        ControladorDesafio controladorDesafio = new ControladorDesafio(repositorioDesafio);

        // --- DADOS DE EXEMPLO (para facilitar testes) ---
        try {
            Usuario u1 = new Usuario(UUID.randomUUID(), "cpf1", "Alice", 30, 60, 1.65, "alice@email.com", "senha1");
            controladorCliente.cadastrarCliente(u1);
            Meta m1 = new Meta(UUID.randomUUID(), "Correr 5km", TipoMeta.DISTANCIA, 5000, 0, "Pendente");
            controladorMeta.cadastrarMeta(u1, m1);
            
            Desafio d1 = new Desafio(UUID.randomUUID(), "Desafio de Outubro", LocalDate.parse("01/10/2025", formatter), LocalDate.parse("31/10/2025", formatter));
            controladorDesafio.cadastrarDesafio(d1);
            
            controladorDesafio.adicionarParticipante(d1, u1);
            System.out.println(">>> Usuário 'Alice' (cpf1) e 'Desafio de Outubro' carregados para teste <<<");
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados de teste: " + e.getMessage());
        }
        // --- FIM DOS DADOS DE EXEMPLO ---

        int opcao = -1;
        do {
            System.out.println("\n--- SISTEMA DE TREINOS E CORRIDAS ---");
            System.out.println("--- Clientes ---");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Buscar Cliente (por CPF)");
            System.out.println("3. Listar Clientes");
            System.out.println("4. Atualizar Cliente");
            System.out.println("5. Deletar Cliente");
            System.out.println("--- Treinos (Cliente) ---");
            System.out.println("6. Cadastrar Treino (Corrida/Intervalado)");
            System.out.println("7. Listar Treinos do Cliente");
            System.out.println("--- Metas (Cliente) ---");
            System.out.println("8. Cadastrar Meta");
            System.out.println("9. Listar Metas do Cliente");
            System.out.println("10. Atualizar Meta");
            System.out.println("11. Deletar Meta");
            System.out.println("--- Planos de Treino (Cliente) ---");
            System.out.println("12. Cadastrar Plano de Treino");
            System.out.println("13. Listar Planos do Cliente");
            System.out.println("14. Adicionar Treino a um Plano");
            System.out.println("15. Atualizar Plano");
            System.out.println("16. Deletar Plano");
            System.out.println("--- Desafios ---");
            System.out.println("17. Cadastrar Desafio");
            System.out.println("18. Listar Desafios");
            System.out.println("19. Deletar Desafio");
            System.out.println("20. Adicionar Participante a Desafio");

            // --- MENU CORRIGIDO E RENUMERADO ---
            System.out.println("--- Relatórios e Notificações ---");
            System.out.println("21. Ver Ranking de Desafio (Automático)"); // Antiga 22
            System.out.println("22. Ver Minhas Notificações (REQ08)"); // NOVO
            System.out.println("23. Gerar Relatório de Atividades (REQ16)"); // NOVO
            System.out.println("24. Gerar Relatório de Evolução (REQ17)"); // NOVO
            // A antiga opção 21 (Registrar Progresso Manual) foi REMOVIDA.

            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                switch (opcao) {
                    // --- Cases 1 a 5 (Cliente) ---
                    case 1: {
                        System.out.print("CPF: "); String cpf = scanner.nextLine();
                        System.out.print("Nome: "); String nome = scanner.nextLine();
                        System.out.print("Email: "); String email = scanner.nextLine();
                        //... (pedir outros dados)
                        Usuario u = new Usuario(UUID.randomUUID(), cpf, nome, 30, 70, 1.75, email, "123");
                        controladorCliente.cadastrarCliente(u);
                        break;
                    }
                    case 2: {
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u != null) System.out.println("Encontrado: " + u);
                        else System.out.println("Cliente não encontrado.");
                        break;
                    }
                    case 3:
                        controladorCliente.listarClientes();
                        break;
                    // ... (Cases 4 e 5 de atualizar/deletar cliente)

                    // --- Cases 6 a 7 (Treino) ---
                    case 6: { // Cadastrar Treino
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }

                        System.out.print("Tipo (1: Corrida, 2: Intervalado): "); int tipo = scanner.nextInt(); scanner.nextLine();
                        System.out.print("Data (dd/MM/yyyy): "); LocalDate data = LocalDate.parse(scanner.nextLine(), formatter);
                        System.out.print("Duração (minutos): "); int duracao = scanner.nextInt(); scanner.nextLine();
                        
                        Treino t = null;
                        if (tipo == 1) {
                            System.out.print("Distância (metros): "); double dist = scanner.nextDouble(); scanner.nextLine();
                            t = new Corrida(UUID.randomUUID(), data, duracao, "Corrida", dist);
                        } else if (tipo == 2) {
                            // ... (lógica para treino intervalado)
                            System.out.println("Tipo Intervalado ainda não implementado no menu.");
                        }

                        if (t != null) {
                            // Este método agora verifica metas e cria notificações automaticamente
                            controladorTreino.cadastrarTreino(u, t);
                        }
                        break;
                    }
                    case 7: { // Listar Treinos
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }
                        
                        System.out.println("Treinos de " + u.getNome() + ":");
                        if (u.getTreinos().isEmpty()) System.out.println("Nenhum treino cadastrado.");
                        for(Treino t : u.getTreinos()) System.out.println(t);
                        break;
                    }
                    
                    // --- Cases 8 a 11 (Metas) ---
                    case 8: { // Cadastrar Meta
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }

                        System.out.print("Descrição: "); String desc = scanner.nextLine();
                        System.out.print("Tipo (1: DISTANCIA, 2: TEMPO): "); int tipo = scanner.nextInt(); scanner.nextLine();
                        TipoMeta tipoMeta = (tipo == 1) ? TipoMeta.DISTANCIA : TipoMeta.TEMPO;
                        
                        double dist = 0; int duracao = 0;
                        if(tipoMeta == TipoMeta.DISTANCIA) {
                             System.out.print("Distância (metros): "); dist = scanner.nextDouble(); scanner.nextLine();
                        } else {
                             System.out.print("Duração (minutos): "); duracao = scanner.nextInt(); scanner.nextLine();
                        }
                        
                        Meta m = new Meta(UUID.randomUUID(), desc, tipoMeta, dist, duracao, "Pendente");
                        controladorMeta.cadastrarMeta(u, m);
                        break;
                    }
                    case 9: { // Listar Metas
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }
                        
                        System.out.println("Metas de " + u.getNome() + ":");
                        if (u.getMetas().isEmpty()) System.out.println("Nenhuma meta cadastrada.");
                        for(Meta m : u.getMetas()) System.out.println(m);
                        break;
                    }
                    // ... (Cases 10 e 11 de atualizar/deletar meta)

                    // --- Cases 12 a 16 (Plano de Treino) ---
                    // ... (Implementação dos cases de Plano de Treino)

                    // --- Cases 17 a 20 (Desafio) ---
                    case 17: { // Cadastrar Desafio
                         System.out.print("Descrição: "); String desc = scanner.nextLine();
                         System.out.print("Data Início (dd/MM/yyyy): "); LocalDate dataIni = LocalDate.parse(scanner.nextLine(), formatter);
                         System.out.print("Data Fim (dd/MM/yyyy): "); LocalDate dataFim = LocalDate.parse(scanner.nextLine(), formatter);
                         Desafio d = new Desafio(UUID.randomUUID(), desc, dataIni, dataFim);
                         controladorDesafio.cadastrarDesafio(d);
                         break;
                    }
                    case 18: // Listar Desafios
                        controladorDesafio.listarDesafios();
                        break;
                    // ... (Case 19 de deletar desafio)
                    case 20: { // Adicionar Participante
                        System.out.print("ID do Desafio: "); UUID idDesafio = UUID.fromString(scanner.nextLine());
                        Desafio d = controladorDesafio.buscarDesafio(idDesafio);
                        if(d == null) { System.out.println("Desafio não encontrado."); break; }

                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }

                        controladorDesafio.adicionarParticipante(d, u);
                        break;
                    }

                    // --- CASES CORRIGIDOS / ADICIONADOS ---

                    // (Antiga opção 21 REMOVIDA)
                    
                    case 21: { // Antiga 22 - Ver Ranking
                        System.out.print("ID do Desafio: "); UUID idDesafio = UUID.fromString(scanner.nextLine());
                        Desafio d = controladorDesafio.buscarDesafio(idDesafio);
                        if(d == null) { System.out.println("Desafio não encontrado."); break; }
                        
                        // O Relatório agora calcula o ranking automaticamente
                        Relatorio.gerarRankingDesafio(d);
                        break;
                    }

                    case 22: { // NOVO - Ver Notificações (REQ08)
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }

                        List<Notificacao> notificacoes = u.getNotificacoes();
                        System.out.println("--- Notificações de " + u.getNome() + " ---");
                        if (notificacoes.isEmpty()) {
                            System.out.println("Nenhuma notificação encontrada.");
                        } else {
                            for (Notificacao n : notificacoes) {
                                System.out.println(n); // Usa o toString() da Notificacao
                            }
                            // Opcional: Adicionar lógica para limpar notificações lidas
                            // u.limparNotificacoes();
                        }
                        break;
                    }

                    case 23: { // NOVO - Relatório de Atividades (REQ16)
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }
                        
                        Relatorio.gerarRelatorioAtividades(u);
                        break;
                    }
                    
                    case 24: { // NOVO - Relatório de Evolução (REQ17)
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }
                        
                        Relatorio.gerarRelatorioEvolucao(u);
                        break;
                    }

                    case 0:
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("ERRO INESPERADO: " + e.getMessage());
                System.out.println("Por favor, tente novamente.");
                // e.printStackTrace(); // Descomente para depurar
                scanner.nextLine(); // Limpar o buffer do scanner em caso de erro
            }

        } while (opcao != 0);

        scanner.close();
    }
}