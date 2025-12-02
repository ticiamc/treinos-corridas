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
        areaTexto.setBackground(new Color(40, 40, 40));
        areaTexto.setForeground(new Color(230, 230, 230));
        areaTexto.setBorder(new EmptyBorder(10,10,10,10));

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60,60,60)));
        painel.add(scroll, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));

        JButton btnExportarExcel = new JButton("Exportar Excel");
        estilizarBotao(btnExportarExcel, new Color(74, 255, 86), Color.BLACK);
        btnExportarExcel.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("relatorio.xls"));
            if (fc.showSaveDialog(painel) == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarRelatorioExcelLindo(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "Excel gerado com sucesso!");
                } catch (IOException ex) { JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage()); }
            }
        });

        JButton btnExportarPDF = new JButton("Exportar PDF");
        estilizarBotao(btnExportarPDF, new Color(255, 100, 100), Color.BLACK);
        btnExportarPDF.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("relatorio.pdf"));
            if (fc.showSaveDialog(painel) == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarPDFNativo(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "PDF gerado com sucesso!");
                } catch (IOException ex) { JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage()); }
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        estilizarBotao(btnVoltar, new Color(60, 60, 60), Color.WHITE);
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario()));

        botoes.add(btnVoltar);
        botoes.add(btnExportarExcel);
        botoes.add(btnExportarPDF);
        painel.add(botoes, BorderLayout.SOUTH);
        return painel;
    }
    
    private void estilizarBotao(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }
}