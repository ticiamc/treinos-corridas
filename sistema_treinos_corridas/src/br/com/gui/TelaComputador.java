package br.com.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.*;

public class TelaComputador {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> criarUI());
    }

    public static void criarUI(){
        JFrame janela = new JFrame("Minha janela");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        janela.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.weightx = 1;
        gbc.weighty = 1;
        

        // Painel de Texto com a marca
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        
        JLabel painel_marca = new JLabel("TOTALPAS POBRE", SwingConstants.CENTER);
        painel_marca.setFont(new Font("Arial", Font.BOLD, 30));
        painel_marca.setOpaque(true);
        painel_marca.setBackground(Color.BLACK);
        painel_marca.setBorder(BorderFactory.createLineBorder(new Color(74, 255, 86), 10));
        painel_marca.setForeground(new Color(74, 255, 86));
        janela.add(painel_marca, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 1;

        // Botão Cadastro
        gbc.gridx = 1; 
        gbc.gridy = 0;
        JButton botao1 = new JButton("Cadastrar");
        botao1.setFont(new Font("Arial", Font.BOLD, 20)); 
        janela.add(botao1, gbc);

        // Botão Acessar Perfil do usuário
        gbc.gridx = 2; 
        gbc.gridy = 0;
        JButton botao2 = new JButton("Acessar Perfil");
        botao2.setFont(new Font("Arial", Font.BOLD, 20));  
        janela.add(botao2, gbc);

        // Botão de Notificações
        gbc.gridx = 1; 
        gbc.gridy = 1;
        JButton botao3 = new JButton("Notificações");
        botao3.setFont(new Font("Arial", Font.BOLD, 20));  
        janela.add(botao3, gbc);

        // Botão de Relatórios
        gbc.gridx = 2; 
        gbc.gridy = 1;
        JButton botao4 = new JButton("Relatórios");
        botao4.setFont(new Font("Arial", Font.BOLD, 20));  
        janela.add(botao4, gbc);

        // Botão de Desafios
        gbc.gridx = 1; 
        gbc.gridy = 2;
        JButton botao5 = new JButton("Desafios");
        botao5.setFont(new Font("Arial", Font.BOLD, 20));  
        janela.add(botao5, gbc);
        
        // Botão de Planos de Treinos
        gbc.gridx = 2; 
        gbc.gridy = 2;
        JButton botao6 = new JButton("Planos de Treino"); 
        botao6.setFont(new Font("Arial", Font.BOLD, 20)); 
        janela.add(botao6, gbc);

        // Botão de Metas
        gbc.gridx = 1; 
        gbc.gridy = 3;
        JButton botao7 = new JButton("Metas"); 
        botao7.setFont(new Font("Arial", Font.BOLD, 20)); 
        janela.add(botao7, gbc);

        // Botão de Treinos
        gbc.gridx = 2; 
        gbc.gridy = 3;
        JButton botao8 = new JButton("Treinos"); 
        botao8.setFont(new Font("Arial", Font.BOLD, 20)); 
        janela.add(botao8, gbc);



        janela.setSize(1000, 700);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }
}
