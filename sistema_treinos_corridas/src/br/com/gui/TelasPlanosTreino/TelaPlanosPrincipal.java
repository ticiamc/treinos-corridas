package br.com.gui.TelasPlanosTreino;

import br.com.gui.GerenciadorTelas;
import br.com.gui.TelaPrincipalUsuario; 
import br.com.negocio.ControladorCliente;
import javax.swing.*;
import java.awt.*;

public class TelaPlanosPrincipal {

    private final ControladorCliente controlador;

    public TelaPlanosPrincipal(ControladorCliente controlador) {
        this.controlador = controlador;
    }

    public JPanel criarPainel() {
        Color corFundo = new Color(30, 30, 30);
        Color corDestaque = new Color(74, 255, 86);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(corFundo);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Planos de Treino", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(corDestaque);
        titulo.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setBackground(corFundo);
        centro.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JButton btnCriar = criarBotao("Criar Plano");
        btnCriar.addActionListener(e -> {
            TelaCriarPlanoTreino criar = new TelaCriarPlanoTreino(controlador);
            GerenciadorTelas.getInstance().carregarTela(criar.criarPainel());
        });

        JButton btnListar = criarBotao("Meus Planos");
        btnListar.addActionListener(e -> {
            TelaListarPlanosTreino listar = new TelaListarPlanosTreino(controlador);
            GerenciadorTelas.getInstance().carregarTela(listar.criarPainel());
        });
        
        JButton btnVoltar = criarBotao("Voltar ao Menu Principal");
        btnVoltar.setBackground(new Color(60,60,60)); 
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario()));

        gbc.gridy = 0; centro.add(btnCriar, gbc);
        gbc.gridy = 1; centro.add(btnListar, gbc);
        gbc.gridy = 2; centro.add(btnVoltar, gbc);

        painel.add(centro, BorderLayout.CENTER);
        return painel;
    }
    
    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(50,50,50));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }
}