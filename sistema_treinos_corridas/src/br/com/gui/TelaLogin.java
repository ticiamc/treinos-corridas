package br.com.gui;

import br.com.dados.RepositorioClientes;
import br.com.negocio.ControladorCliente;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaLogin {

    private ControladorCliente controladorCliente;

    public TelaLogin() {
        // Inicializa dependências necessárias para o login
        this.controladorCliente = new ControladorCliente(new RepositorioClientes());
        
        // Mock de dados para teste rápido (opcional)
        if (controladorCliente.buscarCliente("123") == null) {
            Usuario u = new Usuario("Atleta Teste", 25, 70, 1.75, "teste@email.com", "123");
            controladorCliente.cadastrarCliente(u);
        }
    }

    /**
     * Retorna o PAINEL de login para ser exibido na janela principal.
     */
    public JPanel criarPainelLogin() {
        // Painel de fundo principal
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(new Color(30, 30, 30));

        // Card centralizado (Caixa de Login)
        JPanel cardLogin = new JPanel(new GridLayout(5, 1, 10, 10));
        cardLogin.setBackground(new Color(40, 40, 40));
        cardLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(74, 255, 86), 2),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        // Título
        JLabel lblTitulo = new JLabel("IRON TRACK", SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(74, 255, 86)); // Verde Neon
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel lblSub = new JLabel("Acesso ao Sistema", SwingConstants.CENTER);
        lblSub.setForeground(Color.GRAY);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Campo de CPF
        JTextField txtCpf = new JTextField();
        txtCpf.setBorder(BorderFactory.createTitledBorder(null, "Digite seu CPF ou 'admin'", 0, 0, null, Color.WHITE));
        txtCpf.setBackground(new Color(60, 60, 60));
        txtCpf.setForeground(Color.WHITE);
        txtCpf.setCaretColor(Color.WHITE);

        // Botão Entrar
        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setBackground(new Color(74, 255, 86));
        btnEntrar.setForeground(Color.BLACK);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Link Cadastrar
        JButton btnCadastrar = new JButton("Não tem conta? Cadastre-se");
        btnCadastrar.setBackground(new Color(40, 40, 40));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setBorderPainted(false);
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- AÇÕES ---
        
        // 1. Ação do Botão Entrar
        btnEntrar.addActionListener(e -> {
            String input = txtCpf.getText();

            // Caminho do ADMIN
            if (input.equalsIgnoreCase("admin")) {
                SessaoUsuario.getInstance().logout();
                // Navega para o painel Admin
                GerenciadorTelas.getInstance().carregarTela(new TelaComputador().criarPainelAdmin());
            } 
            // Caminho do USUÁRIO
            else {
                Usuario usuario = controladorCliente.buscarCliente(input);
                if (usuario != null) {
                    SessaoUsuario.getInstance().login(usuario);
                    // Navega para o painel do Usuário
                    GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario());
                } else {
                    JOptionPane.showMessageDialog(painelPrincipal, "Usuário não encontrado.");
                }
            }
        });

        // 2. [CORREÇÃO] Ação do Botão Cadastrar
        // Chama a tela de cadastro que está definida estática na TelaComputador
        btnCadastrar.addActionListener(e -> {
            TelaComputador.abrirTelaCadastroUsuario();
        });

        // Adiciona componentes ao card
        cardLogin.add(lblTitulo);
        cardLogin.add(lblSub);
        cardLogin.add(txtCpf);
        cardLogin.add(btnEntrar);
        cardLogin.add(btnCadastrar);

        painelPrincipal.add(cardLogin);
        return painelPrincipal;
    }
}