package br.com.gui;

import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import java.awt.*;

public class TelaLogin {
    public TelaLogin() {
        if (TelaComputador.controladorCliente == null) new TelaComputador(); 
    }

    public JPanel criarPainelLogin() {
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(new Color(30, 30, 30));

        JPanel cardLogin = new JPanel(new GridLayout(5, 1, 10, 10));
        cardLogin.setBackground(new Color(40, 40, 40));
        cardLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(74, 255, 86), 2),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel lblTitulo = new JLabel("IRON TRACK", SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(74, 255, 86));
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JTextField txtCpf = new JTextField();
        txtCpf.setBorder(BorderFactory.createTitledBorder(null, "Digite seu CPF ou 'admin'", 0, 0, null, Color.WHITE));
        txtCpf.setBackground(new Color(60, 60, 60));
        txtCpf.setForeground(Color.WHITE);
        txtCpf.setCaretColor(Color.WHITE);

        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setBackground(new Color(74, 255, 86));
        
        JButton btnCadastrar = new JButton("Não tem conta? Cadastre-se");
        btnCadastrar.setBackground(new Color(40, 40, 40));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.addActionListener(e -> TelaComputador.abrirTelaCadastroUsuario());

        btnEntrar.addActionListener(e -> {
            String input = txtCpf.getText();
            if (input.equalsIgnoreCase("admin")) {
                SessaoUsuario.getInstance().logout();
                GerenciadorTelas.getInstance().carregarTela(new TelaComputador().criarPainelAdmin());
            } else {
                Usuario usuario = TelaComputador.controladorCliente.buscarCliente(input);
                if (usuario != null) {
                    SessaoUsuario.getInstance().login(usuario);
                    TelaComputador.controladorPlanoTreino.verificarLembretesDoDia(usuario);
                    GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario());
                } else {
                    JOptionPane.showMessageDialog(painelPrincipal, "Usuário não encontrado.");
                }
            }
        });

        cardLogin.add(lblTitulo);
        cardLogin.add(new JLabel("Acesso", SwingConstants.CENTER));
        cardLogin.add(txtCpf);
        cardLogin.add(btnEntrar);
        cardLogin.add(btnCadastrar);
        painelPrincipal.add(cardLogin);
        return painelPrincipal;
    }
}