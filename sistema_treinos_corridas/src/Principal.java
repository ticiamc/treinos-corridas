

import java.util.Scanner;

import br.com.dados.RepositorioClientes;
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
            System.out.print("\nMENU: \n[1] Cadastrar cliente \n[2] Ver clientes cadastrados \n[3] Atualizar Clientes \n[4] Cadastrar plano de treino \n[5] Cadastrar treino \n[6] Cadastrar Desafio \n[7] Cadastrar Meta \n[8] Gerar relatório \n[9] Sair do Sistema \nSua escolha: ");
            option = scan.nextInt();
    
            switch (option) {
                case 1:
                    Usuario cliente1 = new Usuario("Pedro", 39, 85.8, 1.79, "pedrinho123@gmail.com");
                    repositorio.adicionarElemento(cliente1);
                    break;
                case 2:
                    for (Usuario u: repositorio.getClientes()){
                        System.out.printf("\nNome: %s \nIdade: %d", u.getNome(), u.getIdade());
                    }
                    break;
                case 3:
                    // Atualizar Clientes
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
