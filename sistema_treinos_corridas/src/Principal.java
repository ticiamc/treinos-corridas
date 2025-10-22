

import java.util.Scanner;
import br.com.dados.RepositorioClientes;
import br.com.negocio.ControladorCliente;
import br.com.negocio.treinos.Usuario;

// Essa classe não faz parte do sistema em si, mas serve pra gente testar se as outras classes estão funcionando como deveriam.
 
public class Principal {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        RepositorioClientes repositorio = new RepositorioClientes();
        int option = 0;
        
        while (option != 9) {
            System.out.println("\n==========================");
            System.out.println("    Sistema de Treinos    ");
            System.out.println("==========================");
            System.out.print("\nMENU: \n[ 1] Cadastrar cliente \n[ 2] Ver clientes cadastrados \n[ 3] Atualizar Cliente \n[ 4] Deletar Cliente" + 
                             "\n[ 5] Cadastrar plano de treino \n[ 6] Ver planos de treino \n[ 7] Atualizar plano de treino \n[ 8] Deletar plano de treino" + 
                             "\n[ 9] Cadastrar treino \n[10] Ver treinos \n[11] Atualizar treino \n[12] Deletar treino" + 
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
                    

                    Usuario user = new Usuario(nome, idade, peso, altura, email);

                    ControladorCliente.cadastrarUsuario(user, repositorio);

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
                    // Cadastrar plano de Treino
                    break;
                case 5:
                    // Cadastrar Treino
                    break;
                case 6:
                    // Cadastrar Desafio
                    break;
                case 7:
                    // Cadastrar Meta
                    break;
                case 8:
                    // Gerar Relatório
                    break;
                case 9:
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
