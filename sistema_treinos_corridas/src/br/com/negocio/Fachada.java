package br.com.negocio;

import br.com.dados.*;
import br.com.negocio.treinos.Usuario;

// Padrão Singleton: Garante uma única instância controladora para todo o sistema
public class Fachada {

    private static Fachada instance;

    private ControladorCliente controladorCliente;
    private ControladorTreino controladorTreino;
    private ControladorDesafio controladorDesafio;
    private ControladorMeta controladorMeta;
    private ControladorPlanoTreino controladorPlanoTreino;

    // Inicialização dos Repositórios e Controladores
    private Fachada() {
        IRepositorioCliente repoCliente = new RepositorioClientes();
        IRepositorioDesafio repoDesafio = RepositorioDesafio.getInstance();
        IRepositorioPlanoTreino repoPlano = new RepositorioPlanoTreino();

        this.controladorCliente = new ControladorCliente(repoCliente);
        this.controladorTreino = new ControladorTreino(repoCliente);
        this.controladorDesafio = new ControladorDesafio(repoDesafio, repoCliente);
        this.controladorMeta = new ControladorMeta(repoCliente);
        this.controladorPlanoTreino = new ControladorPlanoTreino(repoCliente, repoPlano);
        

        // Inicializa dados de teste (opcional)
        inicializarDadosTeste();
    }

    public static Fachada getInstance() {
        if (instance == null) {
            instance = new Fachada();
        }
        return instance;
    }

    private void inicializarDadosTeste() {
        if(controladorCliente.buscarCliente("000.000.000-00") == null) {
            Usuario userTeste = new Usuario("Usuário Teste", 25, 70, 1.75, "teste@email.com", "000.000.000-00");
            controladorCliente.cadastrarCliente(userTeste);
        }
    }

    // --- Métodos de acesso aos Controladores ---
    
    public ControladorCliente getControladorCliente() { return controladorCliente; }
    public ControladorTreino getControladorTreino() { return controladorTreino; }
    public ControladorDesafio getControladorDesafio() { return controladorDesafio; }
    public ControladorMeta getControladorMeta() { return controladorMeta; }
    public ControladorPlanoTreino getControladorPlanoTreino() { return controladorPlanoTreino; }
}