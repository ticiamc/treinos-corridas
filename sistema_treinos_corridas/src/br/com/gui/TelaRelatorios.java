package br.com.gui;
import br.com.negocio.treinos.Relatorio;
import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class TelaRelatorios {
    private Usuario usuario;
    public TelaRelatorios(Usuario usuario) { this.usuario = usuario; }

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Relatórios e Exportação", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(74, 255, 86));
        painel.add(titulo, BorderLayout.NORTH);

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaTexto.setText(Relatorio.gerarRelatorioAtividadesTexto(usuario));
        
        // --- ESTILIZAÇÃO DO TEXT AREA ---
        areaTexto.setBackground(new Color(40, 40, 40));
        areaTexto.setForeground(new Color(230, 230, 230));
        areaTexto.setBorder(new EmptyBorder(10,10,10,10));
        // --------------------------------

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60,60,60)));
        painel.add(scroll, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));

        JButton btnExportar = new JButton("Exportar para CSV");
        btnExportar.setBackground(new Color(74, 255, 86));
        btnExportar.setForeground(Color.BLACK);
        btnExportar.setFocusPainted(false);
        
        btnExportar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("meu_historico_treinos.csv"));
            int res = fc.showSaveDialog(painel);
            if (res == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarRelatorioAtividadesCSV(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "Arquivo exportado com sucesso!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage());
                }
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(60, 60, 60));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario()));

        botoes.add(btnVoltar);
        botoes.add(btnExportar);
        painel.add(botoes, BorderLayout.SOUTH);
        return painel;
    }
}