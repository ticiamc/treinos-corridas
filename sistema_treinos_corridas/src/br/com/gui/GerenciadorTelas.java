package br.com.gui;

import javax.swing.*;
import java.awt.*;

public class GerenciadorTelas {

    private static GerenciadorTelas instance;
    private JFrame janelaPrincipal;

    private GerenciadorTelas() {
        // Configuração inicial da janela única
        janelaPrincipal = new JFrame("Iron Track - Sistema de Treinos");
        janelaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janelaPrincipal.setSize(1000, 700);
        janelaPrincipal.setLocationRelativeTo(null);
        janelaPrincipal.setLayout(new BorderLayout());
    }

    public static GerenciadorTelas getInstance() {
        if (instance == null) {
            instance = new GerenciadorTelas();
        }
        if (instance == null) instance = new GerenciadorTelas();
        return instance;
    }

    public void iniciar() {
        janelaPrincipal.setVisible(true);
        // Começa carregando o Login
        carregarTela(new TelaLogin().criarPainelLogin()); 
    }

    // Este é o método que troca a tela sem fechar a janela
    public void carregarTela(JPanel novoPainel) {
        janelaPrincipal.getContentPane().removeAll(); // Remove a tela anterior
        janelaPrincipal.getContentPane().add(novoPainel, BorderLayout.CENTER); // Adiciona a nova
        janelaPrincipal.revalidate(); // Recalcula o layout
        janelaPrincipal.repaint();    // Redesenha a tela
    }
    
    // Método auxiliar para pegar a janela se precisar de dialogos modais
    public JFrame getJanelaPrincipal() {
        return janelaPrincipal;
    }
}