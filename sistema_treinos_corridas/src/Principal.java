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
import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;


public class Principal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        IRepositorioCliente repositorioCliente = new RepositorioClientes();
        IRepositorioDesafio repositorioDesafio = new RepositorioDesafio();

        ControladorCliente controladorCliente = new ControladorCliente(repositorioCliente);
        ControladorTreino controladorTreino = new ControladorTreino();
        ControladorMeta controladorMeta = new ControladorMeta(repositorioCliente);
        ControladorPlanoTreino controladorPlanoTreino = new ControladorPlanoTreino(repositorioCliente);
        ControladorDesafio controladorDesafio = new ControladorDesafio(repositorioDesafio, repositorioCliente); 

        // --- DADOS DE EXEMPLO (CORRIGIDOS) ---
        try {
            Usuario u1 = new Usuario("Alice", 30, 60, 1.65, "alice@email.com", "cpf1");
            controladorCliente.cadastrarCliente(u1);
            controladorMeta.cadastrarMeta("cpf1", "Correr 5km", TipoMeta.DISTANCIA, 5000, LocalDate.parse("31/12/2025", formatter));
            controladorDesafio.cadastrarDesafio("Desafio de Outubro", "Correr o máximo em Outubro", LocalDate.parse("01/10/2025", formatter), LocalDate.parse("31/10/2025", formatter));
            
            // Buscar o desafio recém-criado (assumindo que é o ID 1)
            Desafio d1 = controladorDesafio.buscarDesafio(1); 
            
            
            if (d1 != null) {
                controladorDesafio.participarDesafio(d1.getIdDesafio(), u1.getCpf());
                System.out.println(">>> Usuário 'Alice' (cpf1) e 'Desafio de Outubro' (ID 1) carregados para teste <<<");
            } else {
                 System.out.println(">>> Usuário 'Alice' (cpf1) carregado. Falha ao carregar desafio de teste. <<<");
            }
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

            System.out.println("--- Relatórios e Notificações ---");
            System.out.println("21. Ver Ranking de Desafio (Automático)");
            System.out.println("22. Ver Minhas Notificações (REQ08)");
            System.out.println("23. Gerar Relatório de Atividades (REQ16)");
            System.out.println("24. Gerar Relatório de Evolução (REQ17)");

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
                        System.out.print("Idade: "); int idade = scanner.nextInt(); scanner.nextLine();
                        System.out.print("Peso: "); double peso = scanner.nextDouble(); scanner.nextLine();
                        System.out.print("Altura: "); double altura = scanner.nextDouble(); scanner.nextLine();
                        Usuario u = new Usuario(nome, idade, peso, altura, email, cpf);
                        controladorCliente.cadastrarCliente(u);
                        break;
                    }
                    case 2: {
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u != null) System.out.println("Encontrado: " + u.getNome() + " (CPF: " + u.getCpf() + ")");
                        else System.out.println("Cliente não encontrado.");
                        break;
                    }
                    case 3:
                        List<Usuario> clientes = repositorioCliente.listarTodos();
                        System.out.println("--- Clientes Cadastrados ---");
                        if(clientes.isEmpty()) System.out.println("Nenhum cliente cadastrado.");
                        for(Usuario u : clientes) {
                             System.out.println("- " + u.getNome() + " (CPF: " + u.getCpf() + ")");
                        }
                        break;
                    // ... (Cases 4 e 5 de atualizar/deletar cliente)
                    case 4: {
                        System.out.print("CPF do cliente a atualizar: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }
                        System.out.print("Novo Nome: "); String nome = scanner.nextLine();
                        System.out.print("Novo Email: "); String email = scanner.nextLine();
                        u.setNome(nome); u.setEmail(email); 
                        controladorCliente.atualizarCliente(u);
                        break;
                    }
                    case 5: {
                        System.out.print("CPF do cliente a deletar: "); String cpf = scanner.nextLine();
                        controladorCliente.removerCliente(cpf);
                        break;
                    }

                    // --- Cases 6 a 7 (Treino) ---
                    case 6: { // Cadastrar Treino
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }

                        System.out.print("Tipo (1: Corrida, 2: Intervalado): "); int tipo = scanner.nextInt(); scanner.nextLine();
                        System.out.print("Nome/Descrição do Treino: "); String nomeTreino = scanner.nextLine();
                        System.out.print("Data (dd/MM/yyyy): "); LocalDate data = LocalDate.parse(scanner.nextLine(), formatter);
                        LocalDateTime dataExecucao = data.atStartOfDay(); // Assume início do dia
                        System.out.print("Duração (minutos): "); int duracaoMin = scanner.nextInt(); scanner.nextLine();
                        int duracaoSeg = duracaoMin * 60; // Converte para segundos
                        
                        Treino t = null;
                        if (tipo == 1) {
                            System.out.print("Distância (metros): "); double dist = scanner.nextDouble(); scanner.nextLine();
                            t = new Corrida(nomeTreino, dataExecucao, duracaoSeg, dist);
                        } else if (tipo == 2) {
                            System.out.print("Séries: "); int series = scanner.nextInt(); scanner.nextLine();
                            System.out.print("Descanso (segundos): "); int desc = scanner.nextInt(); scanner.nextLine();
                            t = new Intervalado(nomeTreino, dataExecucao, duracaoSeg, series, desc);
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
                        for(Treino t : u.getTreinos()) System.out.println(t); // Usa o toString() de Corrida/Intervalado
                        break;
                    }
                    
                    // --- Cases 8 a 11 (Metas) ---
                    case 8: { // Cadastrar Meta
                        System.out.print("CPF do cliente: "); String cpfCli = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpfCli);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }

                        System.out.print("Descrição: "); String desc = scanner.nextLine();
                        System.out.print("Tipo (1: DISTANCIA, 2: TEMPO, 3: CALORIAS): "); int tipo = scanner.nextInt(); scanner.nextLine();
                        
                        TipoMeta tipoMeta;
                        double valorAlvo = 0;
                        
                        if(tipo == 1) {
                             tipoMeta = TipoMeta.DISTANCIA;
                             System.out.print("Distância (metros): "); valorAlvo = scanner.nextDouble(); scanner.nextLine();
                        } else if (tipo == 2) {
                             tipoMeta = TipoMeta.TEMPO;
                             System.out.print("Duração (minutos): "); valorAlvo = scanner.nextDouble(); scanner.nextLine();
                        } else {
                             tipoMeta = TipoMeta.CALORIAS;
                             System.out.print("Calorias (kcal): "); valorAlvo = scanner.nextDouble(); scanner.nextLine();
                        }
                        
                        System.out.print("Data Limite (dd/MM/yyyy): "); LocalDate dataFim = LocalDate.parse(scanner.nextLine(), formatter);

                        controladorMeta.cadastrarMeta(cpfCli, desc, tipoMeta, valorAlvo, dataFim);
                        break;
                    }
                    case 9: { // Listar Metas
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }
                        
                        System.out.println("Metas de " + u.getNome() + ":");
                        List<Meta> metas = controladorMeta.listarMetas(cpf); // Usa o controlador
                        if (metas.isEmpty()) System.out.println("Nenhuma meta cadastrada.");
                        for(Meta m : metas) System.out.println(m); // Usa o toString() da Meta (corrigido)
                        break;
                    }
                    case 10: { // Atualizar Meta
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        System.out.print("ID da Meta: "); int idMeta = scanner.nextInt(); scanner.nextLine();
                        System.out.print("Nova Descrição: "); String desc = scanner.nextLine();
                        System.out.print("Novo Valor Alvo: "); double valor = scanner.nextDouble(); scanner.nextLine();
                        System.out.print("Nova Data Fim (dd/MM/yyyy): "); LocalDate dataFim = LocalDate.parse(scanner.nextLine(), formatter);
                        controladorMeta.atualizarMeta(cpf, idMeta, desc, valor, dataFim);
                        break;
                    }
                    case 11: { // Deletar Meta
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        System.out.print("ID da Meta: "); int idMeta = scanner.nextInt(); scanner.nextLine();
                        controladorMeta.removerMeta(cpf, idMeta);
                        break;
                    }

                    // --- Cases 12 a 16 (Plano de Treino) ---
                    // (Implementação dos cases de Plano de Treino - Aviso: Não usado)
                    case 12: { // Cadastrar Plano
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        System.out.print("Nome do Plano: "); String nome = scanner.nextLine();
                        System.out.print("Data Início (dd/MM/yyyy): "); LocalDate dataIni = LocalDate.parse(scanner.nextLine(), formatter);
                        System.out.print("Data Fim (dd/MM/yyyy): "); LocalDate dataFim = LocalDate.parse(scanner.nextLine(), formatter);
                        controladorPlanoTreino.cadastrarPlano(cpf, nome, dataIni, dataFim);
                        break;
                    }
                    case 13: { // Listar Planos
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        List<PlanoTreino> planos = controladorPlanoTreino.listarPlanos(cpf);
                        System.out.println("Planos de " + cpf + ":");
                        if (planos.isEmpty()) System.out.println("Nenhum plano cadastrado.");
                        for(PlanoTreino p : planos) System.out.println("ID " + p.getIdPlano() + ": " + p.getNome());
                        break;
                    }
                    
                    // --- Cases 17 a 20 (Desafio) ---
                    case 17: { // Cadastrar Desafio
                         System.out.print("Nome do Desafio: "); String nome = scanner.nextLine();
                         System.out.print("Descrição: "); String desc = scanner.nextLine();
                         System.out.print("Data Início (dd/MM/yyyy): "); LocalDate dataIni = LocalDate.parse(scanner.nextLine(), formatter);
                         System.out.print("Data Fim (dd/MM/yyyy): "); LocalDate dataFim = LocalDate.parse(scanner.nextLine(), formatter);
                         controladorDesafio.cadastrarDesafio(nome, desc, dataIni, dataFim);
                         break;
                    }
                    case 18: // Listar Desafios
                        System.out.println("--- Desafios Ativos ---");
                        List<Desafio> desafios = controladorDesafio.listarDesafios();
                        if(desafios.isEmpty()) System.out.println("Nenhum desafio cadastrado.");
                        for(Desafio d : desafios) {
                            System.out.printf("ID: %d | %s (de %s até %s)\n",
                                d.getIdDesafio(), d.getNome(),
                                d.getDataInicio().format(formatter),
                                d.getDataFim().format(formatter));
                        }
                        break;
            
                    
                    case 19: { // Deletar Desafio
                        System.out.print("ID do Desafio a deletar: "); 
                        int idDesafio = scanner.nextInt(); 
                        scanner.nextLine(); // Limpar buffer
                        
                        controladorDesafio.removerDesafio(idDesafio);
                        break;
                    }

                    
                    case 20: { // Adicionar Participante
                        // CORREÇÃO: ID do desafio é INT
                        System.out.print("ID do Desafio: "); int idDesafio = scanner.nextInt(); scanner.nextLine();
                        Desafio d = controladorDesafio.buscarDesafio(idDesafio);
                        if(d == null) { System.out.println("Desafio não encontrado."); break; }

                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }

                        controladorDesafio.participarDesafio(d.getIdDesafio(), u.getCpf());
                        break;
                    }

                    case 21: { // Ver Ranking
            
                        System.out.print("ID do Desafio: "); int idDesafio = scanner.nextInt(); scanner.nextLine();
                        Desafio d = controladorDesafio.buscarDesafio(idDesafio);
                        if(d == null) { System.out.println("Desafio não encontrado."); break; }
                        Relatorio.gerarRankingDesafio(d);
                        break;
                    }

                    case 22: { // Ver Notificações
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }

                        // CORREÇÃO: Chama o método do controlador
                        controladorCliente.verNotificacoes(u);
                        break;
                    }

                    case 23: { // Relatório de Atividades
                        System.out.print("CPF do cliente: "); String cpf = scanner.nextLine();
                        Usuario u = controladorCliente.buscarCliente(cpf);
                        if (u == null) { System.out.println("Cliente não encontrado."); break; }
                        
                        Relatorio.gerarRelatorioAtividades(u);
                        break;
                    }
                    
                    case 24: { // Relatório de Evolução
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
                // e.printStackTrace(); // Descomente para depurar
                System.out.println("Por favor, tente novamente.");
                if(scanner.hasNextLine()) scanner.nextLine(); // Limpar o buffer do scanner em caso de erro de input
            }

        } while (opcao != 0);

        scanner.close();
    }
}