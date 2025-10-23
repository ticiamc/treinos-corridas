package br.com.negocio;

import br.com.dados.RepositorioClientes;
import br.com.negocio.treinos.Usuario;

public abstract class ControladorCliente {
    // Métodos para clientes
    public static void cadastrarUsuario(String nome, int idade, float peso, float altura, String email, RepositorioClientes repositorio){
        Usuario user = new Usuario(nome, idade, peso, altura, email);
        repositorio.adicionarElemento(user);
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

    public static void deletarCliente(String nome, RepositorioClientes repositorio){
        Usuario cliente = repositorio.buscarElemento(nome);
        if (cliente != null){
            repositorio.removerElemento(nome);
        }
        else{
            System.out.println("\nUsuário não encontrado!\n");
        }
    }
}
