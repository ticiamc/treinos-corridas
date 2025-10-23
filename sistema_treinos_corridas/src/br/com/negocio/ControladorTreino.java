package br.com.negocio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import br.com.dados.RepositorioClientes;
import br.com.negocio.treinos.Corrida;
import br.com.negocio.treinos.Intervalado;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;

public abstract class ControladorTreino {
    public static void adicionarTreino(String username, Treino treino, RepositorioClientes repositorio){
        Usuario user = repositorio.buscarElemento(username);
        if (user != null){
            user.adicionarTreino(treino);
        }
        else{
            System.out.println("\nUsuário não encontrado!\n");
        }
    }

    public static void atualizarTreino(String username, String treino, String novo_nome, String data, int duracao, RepositorioClientes repositorio){
        Scanner scan = new Scanner(System.in);
        Usuario user = repositorio.buscarElemento(username);
        if (user != null){
            for (Treino t: user.getTreinos()){
                if (t.getNomeTreino().equalsIgnoreCase(treino)){
                    t.setNomeTreino(novo_nome);
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime dataHora = LocalDateTime.parse(data, formato);
                    t.setDataExecucao(dataHora);
                    t.setDuracaoSegundos(duracao);

                    if (t instanceof Intervalado){
                        Intervalado new_t = (Intervalado) t;
                        System.out.print("Digite a quantidade de series: ");
                        int series = scan.nextInt();

                        System.out.print("Digite o tempo de descanso: ");
                        int descanso = scan.nextInt();

                        new_t.setSeries(series);
                        new_t.setDescansoEntreSeriesSeg(descanso);
                    }
                    else if (t instanceof Corrida){
                        Corrida new_t = (Corrida) t;

                        System.out.print("Digite a distância a ser percorrida (m): ");
                        int distancia = scan.nextInt();

                        new_t.setDistanciaEmMetros(distancia);
                    }
                }
            }
        }
        else{
            System.out.println("\nCliente não encontrado!\n");
        }

        scan.close();
    }

    public static void deletarTreino(String nome, String nome_treino, RepositorioClientes repositorio){
        Usuario user = repositorio.buscarElemento(nome);

        if (user != null){
            for (Treino t: user.getTreinos()){
                if (t.getNomeTreino().equalsIgnoreCase(nome_treino)){
                    user.getTreinos().remove(t);
                }
            }
        }
        else{
            System.out.println("\nTreino não encontrado!\n");
        }
    }
}
