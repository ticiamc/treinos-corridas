package br.com.gui;

import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import java.awt.*;

public class TelaPrincipalUsuario {

    public static void abrirTelaUsuario() {
        // Recupera quem está logado
        Usuario logado = SessaoUsuario.getInstance().getUsuarioLogado();
        
        if (logado == null) {
            JOptionPane.showMessageDialog(null, "Erro de sessão. Faça login novamente.");
            return;
        }

        JFrame frame = new JFrame("Área do Atleta - " + logado.getNome());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30));
        frame.setLayout(new BorderLayout());

        // --- MENU LATERAL SIMPLES ---
        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(6, 1, 10, 10));
        menu.setBackground(new Color(20, 20, 20));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnTreino = criarBotao("Registrar Treino");
        JButton btnHistorico = criarBotao("Meu Histórico");
        JButton btnMetas = criarBotao("Minhas Metas");
        JButton btnDesafios = criarBotao("Desafios");
        JButton btnNotificacoes = criarBotao("Notificações");
        JButton btnSair = criarBotao("Sair");
        btnSair.setBackground(new Color(200, 50, 50)); // Vermelho

        // --- AÇÕES ---
        
        // REQ04 e REQ05: Registrar Treino
        btnTreino.addActionListener(e -> {
            // Aqui chamamos a tela de cadastro de treino que você já tem
            // MAS, precisamos adaptá-la para não pedir CPF, pois já sabemos quem é.
            // Veja o passo 4 abaixo.
            TelaComputador.abrirTelaCadastroTreinoUsuarioLogado(); 
        });

        // REQ16 e REQ17: Relatórios
        btnHistorico.addActionListener(e -> {
            TelaComputador.abrirTelaHistorico(logado);
        });
        
        // REQ20: Notificações
        btnNotificacoes.addActionListener(e -> {
            TelaComputador.abrirTelaNotificacoes(); // Essa tela já usa SessaoUsuario se adaptarmos
        });

        btnSair.addActionListener(e -> {
            SessaoUsuario.getInstance().logout();
            frame.dispose();
            TelaLogin.abrirTelaLogin();
        });

        menu.add(btnTreino);
        menu.add(btnHistorico);
        menu.add(btnMetas);
        menu.add(btnDesafios);
        menu.add(btnNotificacoes);
        menu.add(btnSair);

        frame.add(menu, BorderLayout.WEST);
        
        // Painel Central (Dashboard)
        JLabel lblBemVindo = new JLabel("<html>Olá, " + logado.getNome() + "!<br>Bora treinar?</html>", SwingConstants.CENTER);
        lblBemVindo.setForeground(Color.WHITE);
        lblBemVindo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        frame.add(lblBemVindo, BorderLayout.CENTER);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(50, 50, 50));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }
}