

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import br.com.dados.RepositorioClientes;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorTreino;
import br.com.negocio.treinos.Corrida;
import br.com.negocio.treinos.Intervalado;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;

// Essa classe não faz parte do sistema em si, mas serve pra gente testar se as outras classes estão funcionando como deveriam.
 
public class Principal {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        RepositorioClientes repositorio = new RepositorioClientes();
        int option = 0;
        
        while (option != 22) {
            System.out.println("\n==========================");
            System.out.println("    Sistema de Treinos    ");
            System.out.println("==========================");
            System.out.print("\nMENU: \n[ 1] Cadastrar cliente \n[ 2] Ver clientes cadastrados \n[ 3] Atualizar Cliente \n[ 4] Deletar Cliente" + 
                             "\n[ 5] Cadastrar treino \n[ 6] Ver treinos \n[ 7] Atualizar treino \n[ 8] Deletar treino" + 
                             "\n[ 9] Cadastrar plano de treino \n[10] Ver planos de treino \n[11] Atualizar plano de treino \n[12] Deletar plano de treino" + 
                             "\n[13] Cadastrar Desafio \n[14] Ver desafios \n[15] Deletar desafio" + 
                             "\n[16] Cadastrar Meta \n[17] Ver metas \n[18] Atualizar metas \n[19] Deletar metas" + 
                             "\n[20] Gerar relatório \n[21] Notificações \n[22] Sair do Sistema \nSua escolha: ");
            option = scan.nextInt();
            scan.nextLine();
    
            switch (option) {
                case 1:
                    System.out.print("Nome do cliente: ");
                    String nome = scan.nextLine();
                    
                    System.out.print("E-mail do cliente: ");
                    String email = scan.nextLine();

                    System.out.print("Idade do cliente: ");
                    int idade = scan.nextInt();

                    System.out.print("Peso do cliente: ");
                    float peso = scan.nextFloat();
                    
                    System.out.print("Altura do cliente: ");
                    float altura = scan.nextFloat();

                    ControladorCliente.cadastrarUsuario(nome, idade, peso, altura, email, repositorio);

                    break;
                case 2:
                    if (repositorio.getClientes().size() > 0){
                        System.out.printf("%-20s %-6s %-8s %-8s %-25s%n", "Nome", "Idade", "Peso", "Altura", "E-mail");
                        System.out.println("--------------------------------------------------------------------------");
                        for (Usuario u: repositorio.getClientes()){
                            System.out.printf("%-20s %-6d %-8.2f %-8.2f %-25s%n", u.getNome(), u.getIdade(),
                            u.getPeso(), u.getAltura(), u.getEmail());
                        }
                    }
                    else{
                        System.out.println("\nBanco Vazio! Nenhum usuário cadastrado!\n");
                    }
                    break;
                case 3:
                    if (repositorio.getClientes().size() > 0){
                        System.out.print("Nome antigo do cliente: ");
                        String old_nome = scan.nextLine();

                        System.out.print("Nome atual do cliente: ");
                        String new_nome = scan.nextLine();
                        
                        System.out.print("E-mail do cliente: ");
                        String new_email = scan.nextLine();

                        System.out.print("Idade do cliente: ");
                        int new_idade = scan.nextInt();

                        System.out.print("Peso do cliente: ");
                        float new_peso = scan.nextFloat();
                        
                        System.out.print("Altura do cliente: ");
                        float new_altura = scan.nextFloat();

                        ControladorCliente.atualizarUsuario(old_nome, repositorio, new_nome, new_idade, new_peso, new_altura, new_email);
                    }
                    else{
                        System.out.println("\nBanco Vazio! Nenhum usuário cadastrado!\n");
                    }
                    break;
                case 4:
                    System.out.print("Nome do cliente: ");
                    String username = scan.nextLine();

                    if (repositorio.getClientes().size() > 0){
                        ControladorCliente.deletarCliente(username, repositorio);
                    }
                    else{
                        System.out.println("\nBanco Vazio! Nenhum usuário cadastrado!\n");
                    }
                    break;
                case 5:
                    System.out.print("Nome do cliente: ");
                    String username2 = scan.nextLine();

                    System.out.print("Digite o nome do Treino: ");
                    String nomeTreino = scan.nextLine();

                    System.out.print("Digite a data e hora (formato: dd/MM/yyyy HH:mm): ");
                    String date = scan.nextLine();
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime dataHora = LocalDateTime.parse(date, formato);

                    System.out.print("Digite a duração do treino em segundos: ");
                    int count = scan.nextInt();

                    System.out.print("Escolha o tipo de treino (1 - Intervalado 2 - Corrida): ");
                    int choice = scan.nextInt();

                    if (repositorio.getClientes().size() > 0){
                        if (choice == 1){
                            System.out.print("Digite a quantidade de series: ");
                            int series = scan.nextInt();
    
                            System.out.print("Digite o tempo de descanso: ");
                            int descanso = scan.nextInt();
    
                            Intervalado intervalado = new Intervalado(nomeTreino, dataHora, count, series, descanso);
                            ControladorTreino.adicionarTreino(username2, intervalado, repositorio);
                        }
                        else if (choice == 2){
                            System.out.print("Digite a distância a ser percorrida (m): ");
                            int distancia = scan.nextInt();
    
                            Corrida corrida = new Corrida(nomeTreino, dataHora, count, distancia);
                            ControladorTreino.adicionarTreino(username2, corrida, repositorio);
                        }
                        else{
                            System.out.println("\nErro!!! Digite 1 ou 2!");
                        }
                    }
                    else{
                        System.out.println("\nBanco Vazio! Nenhum usuário cadastrado!\n");
                    }


                    break;
                case 6:
                    System.out.print("Nome do cliente: ");
                    String username3 = scan.nextLine();

                    Usuario user = repositorio.buscarElemento(username3);
                    if (user.equals(null)){
                        System.out.print("\nCliente não encontrado!\n");
                    }
                    else{
                        for (Treino t: user.getTreinos()){
                            System.out.println(t.getNomeTreino());
                        }
                    }
                    break;
                case 7:
                    System.out.print("Nome do cliente: ");
                    String username4 = scan.nextLine();

                    System.out.print("Digite o nome antigo do Treino: ");
                    String old_nomeTreino = scan.nextLine();


                    System.out.print("Digite o nome do Treino: ");
                    String new_nomeTreino = scan.nextLine();

                    System.out.print("Digite a data e hora (formato: dd/MM/yyyy HH:mm): ");
                    String new_date = scan.nextLine();

                    System.out.print("Digite a duração do treino em segundos: ");
                    int new_count = scan.nextInt();

                    ControladorTreino.atualizarTreino(username4, old_nomeTreino, new_nomeTreino, new_date, new_count, repositorio);
                    break;
                case 8:
                    // Remover Treino
                    System.out.print("Nome do cliente: ");
                    String username5 = scan.nextLine();

                    System.out.print("Digite o nome do Treino: ");
                    String nomeTreino2 = scan.nextLine();

                    ControladorTreino.deletarTreino(username5, nomeTreino2, repositorio);
                    break;
                case 22:
                    // Sair do Sistema
                    scan.close();
                    break;
                default:
                    System.out.println("\nDADO INVÁLIDO!!! Digite novamente!\n");
                    break;
                }
        }
    }
}
