package br.com.gui;

import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;
import br.com.negocio.treinos.Corrida;
import br.com.negocio.treinos.Treino;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaPrincipalUsuario {
    public JPanel criarPainelUsuario() {
        Usuario logado = SessaoUsuario.getInstance().getUsuarioLogado();
        String nome = (logado != null) ? logado.getNome() : "Visitante";

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30,30,30));

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(20,20,20));
        menu.setBorder(new EmptyBorder(20,10,20,10));
        menu.setPreferredSize(new Dimension(200,0));

        JLabel lbl = new JLabel("Olá, " + nome.split(" ")[0]);
        lbl.setForeground(new Color(74,255,86));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menu.add(lbl);
        menu.add(Box.createVerticalStrut(20));

        // 1. Registrar Treino
        adicionarBotao(menu, "Registrar Treino", e -> TelaComputador.abrirTelaCadastroTreinoUsuarioLogado(() -> {
            GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario());
        }));
        menu.add(Box.createVerticalStrut(10));

        // 2. Planos de Treino
        adicionarBotao(menu, "Planos de Treino", e -> {
             GerenciadorTelas.getInstance().carregarTela(new br.com.gui.TelasPlanosTreino.TelaPlanosPrincipal(TelaComputador.controladorCliente).criarPainel());
        });
        menu.add(Box.createVerticalStrut(10));

        // 3. Minhas Metas
        adicionarBotao(menu, "Minhas Metas", e -> TelaComputador.TelaMetas());
        menu.add(Box.createVerticalStrut(10));
        
        // 4. Desafios
        adicionarBotao(menu, "Desafios", e -> TelaComputador.TelaDesafios());
        menu.add(Box.createVerticalStrut(10));

        // 5. Notificações
        adicionarBotao(menu, "Notificações", e -> TelaComputador.abrirTelaNotificacoes());
        menu.add(Box.createVerticalStrut(10));

        // 6. Meu Histórico
        adicionarBotao(menu, "Meu Histórico", e -> {
            if(logado != null) GerenciadorTelas.getInstance().carregarTela(new TelaHistoricoTreinos(logado).criarPainel());
        });
        menu.add(Box.createVerticalStrut(10));

        // 7. Meu Perfil (Por último)
        adicionarBotao(menu, "Meu Perfil", e -> new TelaPerfilUsuario());
        
        // ----------------------------------------------

        menu.add(Box.createVerticalGlue());
        JButton sair = new JButton("Sair");
        sair.setBackground(Color.RED);
        sair.setForeground(Color.WHITE);
        sair.addActionListener(e -> {
            SessaoUsuario.getInstance().logout();
            GerenciadorTelas.getInstance().carregarTela(new TelaLogin().criarPainelLogin());
        });
        menu.add(sair);

        painel.add(menu, BorderLayout.WEST);
        
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(new Color(30,30,30));
        
        JLabel lblBemVindo = new JLabel("<html><center>Painel do Atleta<br>Bem-vindo ao Iron Track!</center></html>", SwingConstants.CENTER);
        lblBemVindo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblBemVindo.setForeground(Color.GRAY);
        lblBemVindo.setBorder(new EmptyBorder(30,0,30,0));
        centro.add(lblBemVindo, BorderLayout.NORTH);
        
        if (logado != null) {
            JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            statsPanel.setBackground(new Color(30,30,30));
            
            int totalTreinos = logado.getTreinos().size();
            double totalKm = 0;
            for(Treino t : logado.getTreinos()) {
                if(t instanceof Corrida) totalKm += ((Corrida)t).getDistanciaEmMetros();
            }
            totalKm = totalKm / 1000.0;
            
            statsPanel.add(criarCardEstatistica("Treinos Realizados", String.valueOf(totalTreinos)));
            statsPanel.add(criarCardEstatistica("Distância Total", String.format("%.1f km", totalKm)));
            centro.add(statsPanel, BorderLayout.CENTER);
        }
        painel.add(centro, BorderLayout.CENTER);
        return painel;
    }

    private void adicionarBotao(JPanel p, String t, java.awt.event.ActionListener l) {
        JButton b = new JButton(t);
        b.setMaximumSize(new Dimension(180, 40));
        b.setBackground(new Color(50,50,50));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.addActionListener(l);
        p.add(b);
    }
    
    private JPanel criarCardEstatistica(String titulo, String valor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(45,45,45));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(74,255,86), 1),
            new EmptyBorder(15, 25, 15, 25)
        ));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(Color.WHITE);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(74,255,86));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(lblValor);
        card.add(Box.createVerticalStrut(5));
        card.add(lblTitulo);
        return card;
    }
}