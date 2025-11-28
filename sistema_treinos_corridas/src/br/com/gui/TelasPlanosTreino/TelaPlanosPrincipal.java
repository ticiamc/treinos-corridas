package br.com.gui.TelasPlanosTreino;

import br.com.gui.GerenciadorTelas;
import br.com.negocio.ControladorCliente;
import javax.swing.*;
import java.awt.*;

public class TelaPlanosPrincipal {
    private final ControladorCliente controlador;

    public TelaPlanosPrincipal(ControladorCliente controlador) { this.controlador = controlador; }

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Planos de Treino", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(74, 255, 86));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(2, 1, 10, 10));
        centro.setBackground(new Color(30, 30, 30));
        
        JButton btnCriar = new JButton("Criar Plano");
        btnCriar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaCriarPlanoTreino(controlador).criarPainel()));
        
        JButton btnListar = new JButton("Listar Planos");
        btnListar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaListarPlanosTreino(controlador).criarPainel()));

        centro.add(btnCriar);
        centro.add(btnListar);
        painel.add(centro, BorderLayout.CENTER);
        return painel;
    }
}