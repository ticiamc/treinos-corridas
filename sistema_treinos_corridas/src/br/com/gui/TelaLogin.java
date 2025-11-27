package br.com.gui;

import br.com.dados.RepositorioClientes;
import br.com.negocio.ControladorCliente;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaLogin {

    private static ControladorCliente controladorCliente;

    public static void main(String[] args) {
        // Inicializa dependências
        RepositorioClientes repo = new RepositorioClientes();
        controladorCliente = new ControladorCliente(repo);
        
        // --- DADOS MOCK (PARA TESTE) ---
        // Cria um usuário de teste para você conseguir logar
        if (controladorCliente.buscarCliente("123") == null) {
            Usuario u = new Usuario("Atleta Teste", 25, 70, 1.75, "teste@email.com", "123");
            controladorCliente.cadastrarCliente(u);
        }
        // -------------------------------

        abrirTelaLogin();
    }

    public static void abrirTelaLogin() {
        JFrame frame = new JFrame("Login - Iron Track");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30));
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("BEM-VINDO", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JTextField txtCpf = new JTextField();
        txtCpf.setBorder(BorderFactory.createTitledBorder("Digite seu CPF ou 'admin'"));

        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setBackground(new Color(74, 255, 86)); // Verde Neon
        btnEntrar.setForeground(Color.BLACK);
        
        // Link para cadastro (caso não tenha conta)
        JButton btnCadastrar = new JButton("Não tem conta? Cadastre-se");
        btnCadastrar.setBackground(new Color(50, 50, 50));
        btnCadastrar.setForeground(Color.WHITE);

        btnEntrar.addActionListener(e -> {
            String input = txtCpf.getText();

            // 1. Lógica de Admin
            if (input.equalsIgnoreCase("admin")) {
                SessaoUsuario.getInstance().logout(); // Garante logout de usuário
                frame.dispose();
                TelaComputador.abrirTelaAdmin(); // Vamos criar/adaptar isso
                return;
            }

            // 2. Lógica de Usuário Comum
            Usuario usuario = controladorCliente.buscarCliente(input);
            if (usuario != null) {
                SessaoUsuario.getInstance().login(usuario); // Salva na sessão
                frame.dispose();
                TelaPrincipalUsuario.abrirTelaUsuario(); // Nova tela só do usuário
            } else {
                JOptionPane.showMessageDialog(frame, "Usuário não encontrado.");
            }
        });
        
        btnCadastrar.addActionListener(e -> {
             // Redireciona para a tela de cadastro que você já tem em TelaComputador
             // Nota: Você pode precisar tornar o método de cadastro static ou acessível
             JOptionPane.showMessageDialog(frame, "Contate o admin para cadastro ou implemente a tela pública.");
        });

        panel.add(lblTitulo);
        panel.add(txtCpf);
        panel.add(btnEntrar);
        panel.add(btnCadastrar);

        frame.add(panel);
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}