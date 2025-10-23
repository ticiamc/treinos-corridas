// Imports das camadas de Dados, Negócio e Modelo
import br.com.dados.IRepositorioCliente;
import br.com.dados.IRepositorioDesafio;
import br.com.dados.RepositorioClientes;
import br.com.dados.RepositorioDesafio;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorDesafio;
import br.com.negocio.ControladorMeta;
import br.com.negocio.ControladorPlanoTreino;
import br.com.negocio.ControladorTreino;
import br.com.negocio.treinos.*; // Importa todas as classes do pacote 'treinos'

import java.util.Date; // Para usar a data atual
import java.util.Scanner; // Para ler a entrada do usuário

/**
 * Classe Principal (Camada de Interface/Apresentação - modo Console).
 * Contém o método main() que inicia o sistema, exibe o menu
 * e gerencia a interação com o usuário.
 */
public class Principal {
    
    /**
     * Ponto de entrada do programa.
     */
    public static void main(String[] args) {
        // --- 1. Inicialização ---
        Scanner scanner = new Scanner(System.in);
        
        // Inicializa os Repositórios (Camada de Dados)
        IRepositorioCliente repositorioCliente = new RepositorioClientes();
        IRepositorioDesafio repositorioDesafio = new RepositorioDesafio();

        // Inicializa os Controladores (Camada de Negócio), injetando os repositórios
        ControladorCliente controladorCliente = new ControladorCliente(repositorioCliente);
        ControladorTreino controladorTreino = new ControladorTreino();
        ControladorMeta controladorMeta = new ControladorMeta();
        ControladorPlanoTreino controladorPlanoTreino = new ControladorPlanoTreino();
        ControladorDesafio controladorDesafio = new ControladorDesafio(repositorioDesafio, repositorioCliente);

        int opcao; // Variável para guardar a escolha do menu

        // --- 2. Loop Principal do Menu ---
        do {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Buscar Cliente");
            System.out.println("3. Atualizar Cliente");
            System.out.println("4. Remover Cliente");
            System.out.println("--- Treinos e Metas ---");
            System.out.println("5. Cadastrar Treino (Executado)");
            System.out.println("6. Cadastrar Meta");
            System.out.println("7. Cadastrar Plano de Treino");
            System.out.println("8. Cadastrar Desafio");
            System.out.println("9. Ranking do Desafio");
            System.out.println("--- Relatórios e Notificações ---");
            System.out.println("10. Gerar Relatório de Atividades");
            System.out.println("11. Gerar Relatório de Evolução");
            System.out.println("12. Exportar Relatório de Atividades");
            System.out.println("13. Ver Minhas Notificações");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner (consome o "Enter")

            // --- 3. Processamento da Opção ---
            switch (opcao) {
                case 1: { // Cadastrar Cliente
                    System.out.println("--- Cadastro de Cliente ---");
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Idade: ");
                    int idade = scanner.nextInt();
                    System.out.print("Peso (kg): ");
                    double peso = scanner.nextDouble();
                    System.out.print("Altura (m): ");
                    double altura = scanner.nextDouble();
                    scanner.nextLine(); // Limpar buffer
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();
                    // Cria o objeto e envia para o controlador
                    Usuario novoCliente = new Usuario(nome, idade, peso, altura, email, cpf);
                    controladorCliente.cadastrarCliente(novoCliente);
                    break;
                }
                case 2: { // Buscar Cliente
                    System.out.print("Digite o CPF do cliente: ");
                    String cpf = scanner.nextLine();
                    Usuario cliente = controladorCliente.buscarCliente(cpf);
                    if (cliente != null) {
                        System.out.println("Cliente encontrado: " + cliente.getNome() + ", Email: " + cliente.getEmail());
                    } else {
                        System.out.println("Cliente não encontrado.");
                    }
                    break;
                }
                case 3: { // Atualizar Cliente
                    System.out.print("Digite o CPF do cliente a ser atualizado: ");
                    String cpf = scanner.nextLine();
                    Usuario cliente = controladorCliente.buscarCliente(cpf);
                    if (cliente != null) {
                        System.out.print("Digite o novo Nome (deixe em branco para manter [" + cliente.getNome() + "]): ");
                        String nome = scanner.nextLine();
                        if (!nome.isEmpty()) {
                            cliente.setNome(nome); // Exemplo de atualização (só do nome)
                        }
                        controladorCliente.atualizarCliente(cliente);
                    } else {
                        System.out.println("Cliente não encontrado.");
                    }
                    break;
                }
                case 4: { // Remover Cliente
                    System.out.print("Digite o CPF do cliente a ser removido: ");
                    String cpf = scanner.nextLine();
                    controladorCliente.removerCliente(cpf);
                    break;
                }
                case 5: { // Cadastrar Treino
                    System.out.print("Digite o CPF do usuário que realizou o treino: ");
                    String cpf = scanner.nextLine();
                    Usuario usuario = controladorCliente.buscarCliente(cpf);
                    if (usuario != null) {
                        System.out.print("Tipo de Treino (1: Corrida, 2: Intervalado): ");
                        int tipo = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nome do Treino (ex: 'Corrida na praia'): ");
                        String nome = scanner.nextLine();
                        System.out.print("Duração (em minutos): ");
                        int duracao = scanner.nextInt();
                        scanner.nextLine();

                        Treino treino = null;
                        if (tipo == 1) {
                            System.out.print("Distância (em metros): ");
                            double dist = scanner.nextDouble();
                            scanner.nextLine();
                            treino = new Corrida(new Date(), duracao, nome, dist);
                        } else if (tipo == 2) {
                            System.out.print("Número de Séries: ");
                            int series = scanner.nextInt();
                            System.out.print("Descanso entre séries (segundos): ");
                            int descanso = scanner.nextInt();
                            scanner.nextLine();
                            treino = new Intervalado(new Date(), duracao, nome, series, descanso);
                        }
                        
                        if (treino != null) {
                            // O controlador agora chama o TreinoProgresso automaticamente
                            controladorTreino.cadastrarTreino(usuario, treino);
                        } else {
                            System.out.println("Tipo de treino inválido.");
                        }
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                    break;
                }
                case 6: { // Cadastrar Meta
                    System.out.print("Digite o CPF do usuário: ");
                    String cpf = scanner.nextLine();
                    Usuario usuario = controladorCliente.buscarCliente(cpf);
                    if (usuario != null) {
                        System.out.print("Descrição da Meta (ex: 'Correr 5km'): ");
                        String desc = scanner.nextLine();
                        System.out.print("Tipo (1: Distância, 2: Tempo): ");
                        int tipo = scanner.nextInt();
                        scanner.nextLine();
                        
                        Meta meta = null;
                        if (tipo == 1) {
                            System.out.print("Distância da Meta (em metros): ");
                            double dist = scanner.nextDouble();
                            scanner.nextLine();
                            meta = new Meta(desc, TipoMeta.DISTANCIA);
                            meta.setDistancia(dist); 
                        } else if (tipo == 2) {
                             System.out.print("Tempo da Meta (em minutos): ");
                             double tempo = scanner.nextDouble();
                             scanner.nextLine();
                             meta = new Meta(desc, TipoMeta.TEMPO);
                             meta.setTempo(tempo);
                        }

                        if(meta != null) {
                            controladorMeta.cadastrarMeta(usuario, meta);
                        } else {
                            System.out.println("Tipo de meta inválido.");
                        }
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                    break;
                }
                case 7: { // Cadastrar Plano de Treino
                    System.out.print("Digite o CPF do usuário: ");
                    String cpf = scanner.nextLine();
                    Usuario usuario = controladorCliente.buscarCliente(cpf);
                    if (usuario != null) {
                        System.out.print("Nome do Plano (ex: 'Plano Maratona'): ");
                        String nome = scanner.nextLine();
                        // Simulação de datas
                        PlanoTreino plano = new PlanoTreino(nome, new Date(), new Date());
                        // Simplificação: Adicionando um treino de corrida ao plano
                        plano.adicionarTreino(new Corrida(new Date(), 30, "Corrida Leve (Planejada)", 3000));
                        controladorPlanoTreino.cadastrarPlanoTreino(usuario, plano);
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                    break;
                }
                case 8: { // Cadastrar Desafio
                    System.out.print("Nome do Desafio (ex: 'Desafio de Dezembro'): ");
                    String nome = scanner.nextLine();
                    Desafio desafio = new Desafio(nome, new Date(), new Date());
                    controladorDesafio.cadastrarDesafio(desafio);
                    break;
                }
                case 9: { // Ranking do Desafio
                    System.out.print("Nome do Desafio: ");
                    String nome = scanner.nextLine();
                    Desafio desafio = controladorDesafio.buscarDesafio(nome);
                    if (desafio != null) {
                        // Simulação de geração de ranking
                        controladorDesafio.gerarRanking(desafio);
                    } else {
                        System.out.println("Desafio não encontrado.");
                    }
                    break;
                }

                case 10: { //Relatório de Atividades
                    System.out.print("Digite o CPF do usuário para o relatório: ");
                    String cpf = scanner.nextLine();
                    Usuario u = controladorCliente.buscarCliente(cpf);
                    if (u != null) {
                        Relatorio rel = new Relatorio(u);
                        rel.gerarRelatorioAtividades();
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                    break;
                }

                case 11: { //Relatório de Evolução
                    System.out.print("Digite o CPF do usuário para o relatório: ");
                    String cpf = scanner.nextLine();
                    Usuario u = controladorCliente.buscarCliente(cpf);
                    if (u != null) {
                        Relatorio rel = new Relatorio(u);
                        rel.gerarRelatorioEvolucao();
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                    break;
                }

                case 12: { //Exportar Relatório
                    System.out.print("Digite o CPF do usuário para exportar: ");
                    String cpf = scanner.nextLine();
                    Usuario u = controladorCliente.buscarCliente(cpf);
                    if (u != null) {
                        System.out.print("Digite o nome do arquivo (ex: relatorio.txt): ");
                        String nomeArquivo = scanner.nextLine();
                        Relatorio rel = new Relatorio(u);
                        rel.exportar(nomeArquivo); // Cria um arquivo .txt
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                    break;
                }

                case 13: { // Ver Notificações
                    System.out.print("Digite o CPF do usuário para ver as notificações: ");
                    String cpf = scanner.nextLine();
                    Usuario u = controladorCliente.buscarCliente(cpf);
                    if (u != null) {
                        controladorCliente.verNotificacoes(u); 
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                    break;
                }
                

                case 0: // Sair
                    System.out.println("Saindo do sistema...");
                    break;
                default: // Opção Inválida
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }
        } while (opcao != 0); // O loop continua enquanto a opção não for 0

        // --- 4. Encerramento ---
        scanner.close(); // Fecha o recurso do scanner
    }
}