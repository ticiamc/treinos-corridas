package br.com.dados;
import br.com.negocio.treinos.Usuario;
import java.util.ArrayList;

public class RepositorioClientes implements IRepositorioCliente{
    private ArrayList<Usuario> clientes;
    
    public RepositorioClientes() {
        this.clientes = new ArrayList<Usuario>(); 
    }
    

    public ArrayList<Usuario> getClientes() {
        return clientes;
    }

    //Create
    @Override
    public void adicionarElemento(Usuario cliente){
        this.clientes.add(cliente);
    }

    // Read
    @Override
    public Usuario buscarElemento(String nome){
        for (Usuario u: clientes){
            if (u.getNome().equalsIgnoreCase(nome)){
                return u;
            }
        }

        return null;
    }

    // Update
    @Override
    public void atualizarElemento(String nome, Usuario cliente){
        for (int i = 0; i < clientes.size(); i++){
            if (clientes.get(i).getNome().equalsIgnoreCase(nome)){
                clientes.set(i, cliente);
                break;
            }
        }
    }

    // Delete
    @Override
    public void removerElemento(String nome){
        this.clientes.removeIf(u -> u.getNome().equalsIgnoreCase(nome));
    }





}
