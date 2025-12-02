package br.com.gui;
import javax.swing.*;
import java.awt.*;

public class GerenciadorTelas {
    private static GerenciadorTelas instance;
    private JFrame janelaPrincipal;

    private GerenciadorTelas() {
        janelaPrincipal = new JFrame("Iron Track - Sistema de Treinos");
        janelaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janelaPrincipal.setSize(1000, 700);
        janelaPrincipal.setLocationRelativeTo(null);
        janelaPrincipal.setLayout(new BorderLayout());
    }

    public static GerenciadorTelas getInstance() {
        if (instance == null) instance = new GerenciadorTelas();
        return instance;
    }

    public void iniciar() {
        janelaPrincipal.setVisible(true);
        carregarTela(new TelaLogin().criarPainelLogin()); 
    }

    public void carregarTela(JPanel novoPainel) {
        janelaPrincipal.getContentPane().removeAll();
        janelaPrincipal.getContentPane().add(novoPainel, BorderLayout.CENTER);
        janelaPrincipal.revalidate();
        janelaPrincipal.repaint();
    }
    
    public JFrame getJanelaPrincipal() { return janelaPrincipal; }
}