import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import br.com.dados.IRepositorioCliente;
import br.com.dados.IRepositorioDesafio;
import br.com.dados.RepositorioClientes;
import br.com.dados.RepositorioDesafio;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorDesafio;
import br.com.negocio.ControladorMeta;
import br.com.negocio.ControladorPlanoTreino;
import br.com.negocio.ControladorTreino;
import br.com.negocio.treinos.Desafio;
import br.com.negocio.treinos.Meta;
import br.com.negocio.treinos.PlanoTreino;
import br.com.negocio.treinos.Relatorio;
import br.com.negocio.treinos.TipoMeta;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;

public class Principal {

    // Formato de data padrão
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    // Controladores
    private static ControladorCliente controladorCliente;
    private static ControladorTreino controladorTreino;
    private static ControladorMeta controladorMeta;
    private static ControladorPlanoTreino controladorPlanoTreino;
    private static ControladorDesafio controladorDesafio;
    
    // Relatórios
    private static Relatorio relatorio = new Relatorio();
    
    // Scanner global
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Inicialização das camadas
        IRepositorioCliente repositorioCliente = new RepositorioClientes();
        IRepositorioDesafio repositorioDesafio = new RepositorioDesafio();

        controladorCliente = new ControladorCliente(repositorioCliente);
        controladorTreino = new ControladorTreino(repositorioCliente);
        controladorMeta = new ControladorMeta(repositorioCliente);
        controladorPlanoTreino = new ControladorPlanoTreino(repositorioCliente);
        controladorDesafio = new ControladorDesafio(repositorioDesafio, repositorioCliente);

        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                
                switch (opcao) {
                    case 1:
                        cadastrarCliente();
                        break;
                    case 2:
                        buscarCliente();
                        break;
                    case 3:
                        atualizarCliente();
                        break;
                    case 4:
                        removerCliente();
                        break;
                    case 5:
                        cadastrarTreino();
                        break;
                    case 6:
                        listarTreinos();
                        break;
                    case 7:
                        atualizarTreino();
                        break;
                    case 8:
                        removerTreino();
                        break;
                    case 9:
                        cadastrarMeta();
                        break;
                    case 10:
                        listarMetas();
                        break;
                    case 11:
                        atualizarMeta();
                        break;
                    case 12:
                        removerMeta();
                        break;
                    case 13:
                        cadastrarPlanoTreino();
                        break;
                    case 14:
                        listarPlanosTreino();
                        break;
                    case 15:
                        removerPlanoTreino();
                        break;
                    case 16:
                        adicionarTreinoPlano();
                        break;
                    case 17:
                        removerTreinoPlano();
                        break;
                    case 18:
                        cadastrarDesafio();
                        break;
                    case 19:
                        listarDesafios();
                        break;
                    case 20:
                        participarDesafio();
                        break;
                    case 21:
                        registrarProgressoDesafio();
                        break;
                    case 22:
                        verRankingDesafio();
                        break;
                    case 0:
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Entrada inválida, por favor digite um número.");
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
            }
            
            if(opcao != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n--- Sistema de Treinos e Corridas ---");
        System.out.println("--- CRUD Cliente ---");
        System.out.println("1. Cadastrar Cliente");
        System.out.println("2. Buscar Cliente");
        System.out.println("3. Atualizar Cliente");
        System.out.println("4. Remover Cliente");
        System.out.println("--- CRUD Treino ---");
        System.out.println("5. Cadastrar Treino");
        System.out.println("6. Listar Treinos por Cliente");
        System.out.println("7. Atualizar Treino");
        System.out.println("8. Remover Treino");
        System.out.println("--- CRUD Meta ---");
        System.out.println("9. Cadastrar Meta");
        System.out.println("10. Listar Metas por Cliente");
        System.out.println("11. Atualizar Meta");
        System.out.println("12. Remover Meta");
        System.out.println("--- CRUD Plano de Treino ---");
        System.out.println("13. Cadastrar Plano de Treino");
        System.out.println("14. Listar Planos por Cliente");
        System.out.println("15. Remover Plano de Treino");
        System.out.println("16. Adicionar Treino ao Plano");
        System.out.println("17. Remover Treino do Plano");
        System.out.println("--- CRUD Desafio ---");
        System.out.println("18. Cadastrar Desafio");
        System.out.println("19. Listar Desafios");
        System.out.println("20. Participar de Desafio");
        System.out.println("21. Registrar Progresso em Desafio");
        System.out.println("22. Ver Ranking de Desafio");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    // --- Métodos Cliente ---
    private static void cadastrarCliente() throws Exception {
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Idade: ");
        int idade = Integer.parseInt(scanner.nextLine());
        System.out.print("Peso: ");
        double peso = Double.parseDouble(scanner.nextLine());
        System.out.print("Altura: ");
        double altura = Double.parseDouble(scanner.nextLine());

        Usuario novoUsuario = new Usuario(nome, idade, peso, altura, email, cpf);
        controladorCliente.cadastrar(novoUsuario);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static void buscarCliente() throws Exception {
        System.out.print("CPF do cliente a buscar: ");
        String cpf = scanner.nextLine();
        Usuario usuario = controladorCliente.buscar(cpf);
        System.out.println("Cliente encontrado: " + usuario);
    }

    private static void atualizarCliente() throws Exception {
        System.out.print("CPF do cliente a atualizar: ");
        String cpf = scanner.nextLine();
        Usuario usuario = controladorCliente.buscar(cpf); // Busca primeiro
        
        System.out.print("Novo Nome (" + usuario.getNome() + "): ");
        String nome = scanner.nextLine();
        System.out.print("Novo Email (" + usuario.getEmail() + "): ");
        String email = scanner.nextLine();
        
        // Atualiza o objeto
        usuario.setNome(nome);
        usuario.setEmail(email);
        // Outros campos podem ser adicionados

        controladorCliente.atualizar(usuario);
        System.out.println("Cliente atualizado com sucesso!");
    }

    private static void removerCliente() throws Exception {
        System.out.print("CPF do cliente a remover: ");
        String cpf = scanner.nextLine();
        controladorCliente.remover(cpf);
        System.out.println("Cliente removido com sucesso!");
    }

    // --- Métodos Treino ---
    private static void cadastrarTreino() throws Exception {
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        System.out.print("Data (dd/MM/yyyy): ");
        Date data = sdf.parse(scanner.nextLine());
        System.out.print("Duração (minutos): ");
        double duracao = Double.parseDouble(scanner.nextLine());
        System.out.print("Tipo (corrida/intervalado): ");
        String tipo = scanner.nextLine();

        Double distancia = null;
        Integer series = null;
        Double tempoDescanso = null;

        if ("corrida".equalsIgnoreCase(tipo)) {
            System.out.print("Distância (km): ");
            distancia = Double.parseDouble(scanner.nextLine());
        } else if ("intervalado".equalsIgnoreCase(tipo)) {
            System.out.print("Séries: ");
            series = Integer.parseInt(scanner.nextLine());
            System.out.print("Tempo de Descanso (minutos): ");
            tempoDescanso = Double.parseDouble(scanner.nextLine());
        }

        controladorTreino.cadastrarTreino(cpf, data, duracao, tipo, distancia, series, tempoDescanso);
        System.out.println("Treino cadastrado com sucesso!");
    }

    private static void listarTreinos() throws Exception {
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        List<Treino> treinos = controladorTreino.listarTreinos(cpf);
        if (treinos.isEmpty()) {
            System.out.println("Nenhum treino cadastrado para este cliente.");
            return;
        }
        System.out.println("Treinos do cliente:");
        for (Treino treino : treinos) {
            System.out.println(treino);
        }
    }

    private static void atualizarTreino() throws Exception {
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        System.out.print("ID do treino a atualizar: ");
        int idTreino = Integer.parseInt(scanner.nextLine());
        System.out.print("Nova data (dd/MM/yyyy): ");
        Date novaData = sdf.parse(scanner.nextLine());
        System.out.print("Nova duração (minutos): ");
        double novaDuracao = Double.parseDouble(scanner.nextLine());

        controladorTreino.atualizarTreino(cpf, idTreino, novaData, novaDuracao);
    }

    private static void removerTreino() throws Exception {
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        System.out.print("ID do treino a remover: ");
        int idTreino = Integer.parseInt(scanner.nextLine());
        controladorTreino.removerTreino(cpf, idTreino);
    }
    
    // --- Métodos Meta ---
    private static void cadastrarMeta() throws Exception {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        System.out.print("Descrição da meta: ");
        String desc = scanner.nextLine();
        System.out.print("Tipo (DISTANCIA, TEMPO, CALORIAS): ");
        TipoMeta tipo = TipoMeta.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Valor Alvo (km, min, kcal): ");
        double valor = Double.parseDouble(scanner.nextLine());
        System.out.print("Data Limite (dd/MM/yyyy): ");
        Date data = sdf.parse(scanner.nextLine());
        
        controladorMeta.cadastrarMeta(cpf, desc, tipo, valor, data);
    }

    private static void listarMetas() throws Exception {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        List<Meta> metas = controladorMeta.listarMetas(cpf);
        if(metas.isEmpty()) {
            System.out.println("Nenhuma meta cadastrada.");
            return;
        }
        for(Meta meta : metas) {
            System.out.println(meta);
        }
    }

    private static void atualizarMeta() throws Exception {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        System.out.print("ID da meta a atualizar: ");
        int idMeta = Integer.parseInt(scanner.nextLine());
        System.out.print("Nova Descrição: ");
        String desc = scanner.nextLine();
        System.out.print("Novo Valor Alvo: ");
        double valor = Double.parseDouble(scanner.nextLine());
        System.out.print("Nova Data Limite (dd/MM/yyyy): ");
        Date data = sdf.parse(scanner.nextLine());
        
        controladorMeta.atualizarMeta(cpf, idMeta, desc, valor, data);
    }

    private static void removerMeta() throws Exception {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        System.out.print("ID da meta a remover: ");
        int idMeta = Integer.parseInt(scanner.nextLine());
        controladorMeta.removerMeta(cpf, idMeta);
    }

    // --- Métodos Plano de Treino ---
    private static void cadastrarPlanoTreino() throws Exception {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        System.out.print("Nome do plano: ");
        String nome = scanner.nextLine();
        System.out.print("Data Início (dd/MM/yyyy): ");
        Date dataInicio = sdf.parse(scanner.nextLine());
        System.out.print("Data Fim (dd/MM/yyyy): ");
        Date dataFim = sdf.parse(scanner.nextLine());
        
        controladorPlanoTreino.cadastrarPlano(cpf, nome, dataInicio, dataFim);
    }

    private static void listarPlanosTreino() throws Exception {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        List<PlanoTreino> planos = controladorPlanoTreino.listarPlanos(cpf);
        if(planos.isEmpty()) {
            System.out.println("Nenhum plano cadastrado.");
            return;
        }
        for(PlanoTreino plano : planos) {
            System.out.println(plano);
            System.out.println("  Treinos no plano:");
            if(plano.getTreinos().isEmpty()) {
                System.out.println("    Nenhum treino adicionado.");
            }
            for(Treino t : plano.getTreinos()) {
                System.out.println("    - " + t);
            }
        }
    }
    
    private static void removerPlanoTreino() throws Exception {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        System.out.print("ID do plano a remover: ");
        int idPlano = Integer.parseInt(scanner.nextLine());
        controladorPlanoTreino.removerPlano(cpf, idPlano);
    }

    private static void adicionarTreinoPlano() throws Exception {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        System.out.print("ID do plano: ");
        int idPlano = Integer.parseInt(scanner.nextLine());
        System.out.print("ID do treino (deve estar cadastrado no usuário): ");
        int idTreino = Integer.parseInt(scanner.nextLine());
        
        controladorPlanoTreino.adicionarTreinoPlano(cpf, idPlano, idTreino);
    }
    
    private static void removerTreinoPlano() throws Exception {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        System.out.print("ID do plano: ");
        int idPlano = Integer.parseInt(scanner.nextLine());
        System.out.print("ID do treino a remover do plano: ");
        int idTreino = Integer.parseInt(scanner.nextLine());
        
        controladorPlanoTreino.removerTreinoPlano(cpf, idPlano, idTreino);
    }

    // --- Métodos Desafio ---
    private static void cadastrarDesafio() throws Exception {
        System.out.print("Nome do desafio: ");
        String nome = scanner.nextLine();
        System.out.print("Descrição: ");
        String desc = scanner.nextLine();
        System.out.print("Data Início (dd/MM/yyyy): ");
        Date dataInicio = sdf.parse(scanner.nextLine());
        System.out.print("Data Fim (dd/MM/yyyy): ");
        Date dataFim = sdf.parse(scanner.nextLine());
        
        controladorDesafio.cadastrarDesafio(nome, desc, dataInicio, dataFim);
    }

    private static void listarDesafios() {
        List<Desafio> desafios = controladorDesafio.listarDesafios();
        if(desafios.isEmpty()) {
            System.out.println("Nenhum desafio cadastrado no sistema.");
            return;
        }
        for(Desafio d : desafios) {
            System.out.println(d);
        }
    }

    private static void participarDesafio() throws Exception {
        System.out.print("ID do desafio: ");
        int idDesafio = Integer.parseInt(scanner.nextLine());
        System.out.print("CPF do usuário participante: ");
        String cpf = scanner.nextLine();
        
        controladorDesafio.participarDesafio(idDesafio, cpf);
    }

    private static void registrarProgressoDesafio() throws Exception {
        System.out.print("ID do desafio: ");
        int idDesafio = Integer.parseInt(scanner.nextLine());
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        System.out.print("Progresso a adicionar (ex: km corridos): ");
        double progresso = Double.parseDouble(scanner.nextLine());
        
        controladorDesafio.registrarProgresso(idDesafio, cpf, progresso);
    }
    
    private static void verRankingDesafio() throws Exception {
        System.out.print("ID do desafio para ver o ranking: ");
        int idDesafio = Integer.parseInt(scanner.nextLine());
        Desafio desafio = controladorDesafio.buscarDesafio(idDesafio);
        
        // O método gerarRankingDesafio está na classe Relatorio
        relatorio.gerarRankingDesafio(desafio);
    }
}
