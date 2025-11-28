package br.com.dados;
import br.com.negocio.treinos.Usuario;
import java.util.ArrayList;
import java.util.List;

public class RepositorioClientes implements IRepositorioCliente{
    private ArrayList<Usuario> clientes;
    
    public RepositorioClientes() {
        this.clientes = new ArrayList<Usuario>(); 
    }
    
    @Override
    public void adicionarElemento(Usuario cliente){ this.clientes.add(cliente); }

    @Override
    public Usuario buscarElementoPorCpf(String cpf){
        for (Usuario u: clientes){
            if (u.getCpf().equals(cpf)) return u;
        }
        return null;
    }

    @Override
    public Usuario buscarElementoPorNome(String nome){
        for (Usuario u: clientes){
            if (u.getNome().equalsIgnoreCase(nome)) return u;
        }
        return null;
    }

    @Override
    public void atualizarElemento(Usuario cliente){
        for (int i = 0; i < clientes.size(); i++){
            if (clientes.get(i).getCpf().equals(cliente.getCpf())){
                clientes.set(i, cliente);
                break;
            }
        }
    }

    @Override
    public void removerElemento(String cpf){
        this.clientes.removeIf(u -> u.getCpf().equals(cpf));
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(clientes);
    }
}