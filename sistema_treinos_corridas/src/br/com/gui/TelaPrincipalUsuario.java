package br.com.gui;

import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaPrincipalUsuario {

    private static final Color COR_FUNDO = new Color(30, 30, 30);
    private static final Color COR_MENU = new Color(20, 20, 20);
    private static final Color COR_DESTAQUE = new Color(74, 255, 86);

    public JPanel criarPainelUsuario() {
        // Recupera o usuário da sessão
        Usuario logado = SessaoUsuario.getInstance().getUsuarioLogado();
        String nomeUsuario = (logado != null) ? logado.getNome() : "Visitante";

        JPanel painelUsuario = new JPanel(new BorderLayout());
        painelUsuario.setBackground(COR_FUNDO);

        // --- MENU LATERAL ---
        JPanel menuLateral = new JPanel();
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBackground(COR_MENU);
        menuLateral.setPreferredSize(new Dimension(200, 0));
        menuLateral.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Saudação
        JLabel lblOla = new JLabel("Olá, " + nomeUsuario.split(" ")[0]);
        lblOla.setForeground(COR_DESTAQUE);
        lblOla.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblOla.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        menuLateral.add(lblOla);
        menuLateral.add(Box.createVerticalStrut(30)); // Espaço

        // Botões do Menu
        adicionarBotaoMenu(menuLateral, "Registrar Treino", e -> {
            TelaComputador.abrirTelaCadastroTreinoUsuarioLogado();
        });
        
        menuLateral.add(Box.createVerticalStrut(10));
        
        adicionarBotaoMenu(menuLateral, "Meu Histórico", e -> {
            if(logado != null) TelaComputador.abrirTelaHistorico(logado);
        });

        menuLateral.add(Box.createVerticalStrut(10));

        adicionarBotaoMenu(menuLateral, "Minhas Metas", e -> TelaComputador.TelaMetas());
        
        menuLateral.add(Box.createVerticalStrut(10));

        adicionarBotaoMenu(menuLateral, "Notificações", e -> TelaComputador.abrirTelaNotificacoes());

        menuLateral.add(Box.createVerticalStrut(10));

        // --- CORREÇÃO AQUI ---
        // Antes estava: e -> TelaPerfilUsuario()
        // Correção: new TelaPerfilUsuario(...)
        adicionarBotaoMenu(menuLateral, "Meu Perfil", e -> {
            new TelaPerfilUsuario(TelaComputador.controladorCliente);
        });

        // Espaço elástico para empurrar o Sair para baixo
        menuLateral.add(Box.createVerticalGlue());

        // Botão Sair
        JButton btnSair = new JButton("SAIR / LOGOUT");
        btnSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSair.setBackground(new Color(200, 50, 50));
        btnSair.setForeground(Color.WHITE);
        btnSair.setMaximumSize(new Dimension(180, 40));
        btnSair.addActionListener(e -> {
            SessaoUsuario.getInstance().logout();
            GerenciadorTelas.getInstance().carregarTela(new TelaLogin().criarPainelLogin());
        });
        menuLateral.add(btnSair);

        // --- ÁREA CENTRAL (Dashboard Simples) ---
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBackground(COR_FUNDO);
        
        JLabel lblBemVindo = new JLabel("<html><center>Bem-vindo à sua<br>Área do Atleta</center></html>");
        lblBemVindo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBemVindo.setForeground(Color.DARK_GRAY);
        
        painelCentral.add(lblBemVindo);

        painelUsuario.add(menuLateral, BorderLayout.WEST);
        painelUsuario.add(painelCentral, BorderLayout.CENTER);

        return painelUsuario;
    }

    private void adicionarBotaoMenu(JPanel painel, String texto, java.awt.event.ActionListener acao) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setBackground(new Color(50, 50, 50));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(acao);
        painel.add(btn);
    }
}