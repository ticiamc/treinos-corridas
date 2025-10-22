package br.com.negocio;

import java.util.Scanner;

import br.com.dados.RepositorioClientes;
import br.com.negocio.treinos.Usuario;

public abstract class ControladorCliente {
    public static void cadastrarUsuario(Usuario cliente, RepositorioClientes repositorio){
        Scanner scan = new Scanner(System.in);
        repositorio.adicionarElemento(cliente);
    }

    public static void atualizarUsuario(String nome, RepositorioClientes repositorioClientes, String new_name, int new_idade, float new_peso, float new_altura, String new_email){
        Usuario clienteEncontrado = repositorioClientes.buscarElemento(nome);
        if (clienteEncontrado != null){
            clienteEncontrado.setNome(new_name);
            clienteEncontrado.setIdade(new_idade);
            clienteEncontrado.setPeso(new_peso);
            clienteEncontrado.setAltura(new_altura);
            clienteEncontrado.setEmail(new_email);
        }
        else{
            System.out.println("\nUsuário não encontrado!\n");
        }

    }
}
