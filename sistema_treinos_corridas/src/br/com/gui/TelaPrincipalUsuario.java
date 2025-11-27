package br.com.gui;

import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaPrincipalUsuario {

    public JPanel criarPainelUsuario() {
        Usuario logado = SessaoUsuario.getInstance().getUsuarioLogado();
        
        JPanel painel = new JPanel(new BorderLayout());
        
        // Menu
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(30, 30, 30));
        menu.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        JLabel lbl = new JLabel("Olá, " + (logado!=null?logado.getNome():"Atleta"));
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        menu.add(lbl);
        menu.add(Box.createVerticalStrut(20));

        adicionarBotao(menu, "Registrar Treino", e -> TelaComputador.abrirTelaCadastroTreino(logado));
        adicionarBotao(menu, "Meus Planos", e -> TelaComputador.abrirTelaPlanos(logado));
        adicionarBotao(menu, "Meus Desafios", e -> TelaComputador.abrirTelaDesafios(logado));
        adicionarBotao(menu, "Minhas Metas", e -> TelaComputador.abrirTelaMetas(logado));
        adicionarBotao(menu, "Relatório / CSV", e -> TelaComputador.abrirTelaRelatorios(logado));
        adicionarBotao(menu, "Notificações", e -> TelaComputador.abrirTelaNotificacoes(logado));

        menu.add(Box.createVerticalGlue());
        JButton btnSair = new JButton("SAIR");
        btnSair.setBackground(new Color(200, 50, 50));
        btnSair.setForeground(Color.WHITE);
        btnSair.addActionListener(e -> {
            SessaoUsuario.getInstance().logout();
            GerenciadorTelas.getInstance().carregarTela(new TelaLogin().criarPainelLogin());
        });
        menu.add(btnSair);

        // Centro
        JPanel centro = new JPanel(new GridBagLayout());
        JLabel l = new JLabel("Área do Atleta");
        l.setFont(new Font("Segoe UI", Font.BOLD, 30));
        centro.add(l);

        painel.add(menu, BorderLayout.WEST);
        painel.add(centro, BorderLayout.CENTER);
        return painel;
    }

    private void adicionarBotao(JPanel p, String t, java.awt.event.ActionListener l) {
        JButton b = new JButton(t);
        b.setMaximumSize(new Dimension(180, 40));
        b.addActionListener(l);
        p.add(b);
        p.add(Box.createVerticalStrut(10));
    }
}