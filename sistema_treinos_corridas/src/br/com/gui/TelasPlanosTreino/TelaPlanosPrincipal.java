package br.com.gui.TelasPlanosTreino;

import br.com.gui.GerenciadorTelas;
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
        Color corCard = new Color(45, 45, 45);
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

        JButton btnCriar = new JButton("Criar Plano");
        btnCriar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCriar.setBackground(new Color(50,50,50));
        btnCriar.setForeground(Color.WHITE);
        btnCriar.setFocusPainted(false);
        btnCriar.addActionListener(e -> {
            TelaCriarPlanoTreino criar = new TelaCriarPlanoTreino(controlador);
            GerenciadorTelas.getInstance().carregarTela(criar.criarPainel());
        });

        JButton btnListar = new JButton("Listar Planos");
        btnListar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnListar.setBackground(new Color(50,50,50));
        btnListar.setForeground(Color.WHITE);
        btnListar.setFocusPainted(false);
        btnListar.addActionListener(e -> {
            TelaListarPlanosTreino listar = new TelaListarPlanosTreino(controlador);
            GerenciadorTelas.getInstance().carregarTela(listar.criarPainel());
        });

        gbc.gridy = 0; centro.add(btnCriar, gbc);
        gbc.gridy = 1; centro.add(btnListar, gbc);

        painel.add(centro, BorderLayout.CENTER);
        return painel;
    }
}