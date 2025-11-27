package br.com.gui;

import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaLogin {

    public TelaLogin() {
        // Garante que os dados/controladores estáticos sejam carregados
        if (TelaComputador.controladorCliente == null) {
            new TelaComputador(); 
        }
    }

    public JPanel criarPainelLogin() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(30, 30, 30));

        JPanel card = new JPanel(new GridLayout(5, 1, 10, 10));
        card.setBackground(new Color(45, 45, 45));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(74, 255, 86), 2),
            BorderFactory.createEmptyBorder(30, 50, 30, 50)
        ));

        JLabel titulo = new JLabel("IRON TRACK", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(74, 255, 86));

        JTextField txtInput = new JTextField();
        txtInput.setBorder(BorderFactory.createTitledBorder("CPF ou 'admin'"));

        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setBackground(new Color(74, 255, 86));
        btnEntrar.setForeground(Color.BLACK);

        JButton btnCadastro = new JButton("Cadastrar Novo Atleta");
        btnCadastro.setBackground(Color.GRAY);
        btnCadastro.setForeground(Color.WHITE);

        btnEntrar.addActionListener(e -> {
            String in = txtInput.getText();
            if(in.equalsIgnoreCase("admin")) {
                SessaoUsuario.getInstance().logout();
                GerenciadorTelas.getInstance().carregarTela(new TelaComputador().criarPainelAdmin());
            } else {
                Usuario u = TelaComputador.controladorCliente.buscarCliente(in);
                if(u != null) {
                    SessaoUsuario.getInstance().login(u);
                    
                    // [REQ22] Verifica lembretes automaticamente ao logar
                    TelaComputador.controladorPlanoTreino.verificarLembretesDoDia(u);
                    
                    GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario());
                } else {
                    JOptionPane.showMessageDialog(painel, "Usuário não encontrado.");
                }
            }
        });

        btnCadastro.addActionListener(e -> TelaComputador.abrirTelaCadastroUsuario());

        card.add(titulo);
        card.add(new JLabel("Login do Sistema", SwingConstants.CENTER));
        card.add(txtInput);
        card.add(btnEntrar);
        card.add(btnCadastro);
        painel.add(card);
        return painel;
    }
}