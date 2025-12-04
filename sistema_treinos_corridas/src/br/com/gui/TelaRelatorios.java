package br.com.gui;

import br.com.negocio.treinos.*;
import br.com.negocio.ControladorDesafio;
import br.com.negocio.Fachada;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class TelaRelatorios {

    private Usuario usuario;

    public TelaRelatorios(Usuario usuario) {
        this.usuario = usuario;
    }

    // ============================
    //  MÉTODO PRINCIPAL (MENU)
    // ============================
    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Relatórios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(74, 255, 86));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel botoes = new JPanel(new GridLayout(3, 1, 15, 15));
        botoes.setBackground(new Color(30, 30, 30));
        botoes.setBorder(new EmptyBorder(40, 60, 40, 60));

        // BOTÃO 1
        JButton bPeriodo = criarBotao("Relatório por Período (Atividades)");
        bPeriodo.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(criarPainelPeriodo()));

        // BOTÃO 2
        JButton bDesempenho = criarBotao("Relatório de Desempenho");
        bDesempenho.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(criarPainelDesempenho()));

        // BOTÃO 3
        JButton bDesafios = criarBotao("Relatório de Desafios");
        bDesafios.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(criarPainelDesafios()));
        
        JButton btnVoltar = new JButton("Voltar");
        estilizarBotao(btnVoltar, new Color(60, 60, 60), Color.WHITE);
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario()));
        
        botoes.add(bPeriodo);
        botoes.add(bDesempenho);
        botoes.add(bDesafios);

        painel.add(botoes, BorderLayout.CENTER);
        painel.add(btnVoltar, BorderLayout.SOUTH);

        return painel;
    }
    
    // ==========================================
    //  COMPONENTE COMUM — CRIAÇÃO DE BOTÕES
    // ==========================================
    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(74, 255, 86));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    private void estilizarBotao(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }

    // =======================================================
    //  TELA 1 — RELATÓRIO POR PERÍODO (REQ16)
    // =======================================================
    private JPanel criarPainelPeriodo() {
        JPanel painel = criarBase("Relatório por Período");

        JPanel centro = new JPanel();
        centro.setBackground(new Color(30, 30, 30));
        centro.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lInicio = criarTexto("Data de Início (AAAA-MM-DD):");
        JLabel lFim = criarTexto("Data de Fim (AAAA-MM-DD):");

        JTextField tInicio = new JTextField();
        JTextField tFim = new JTextField();

        centro.add(lInicio); centro.add(tInicio);
        centro.add(lFim);    centro.add(tFim);

        JButton gerar = criarBotao("Gerar Relatório");
        JTextArea resposta = new JTextArea();
        resposta.setEditable(false);
        resposta.setBackground(new Color(20, 20, 20));
        resposta.setForeground(Color.WHITE);
        resposta.setFont(new Font("Consolas", Font.PLAIN, 12));

        gerar.addActionListener(e -> {
            try {
                LocalDate d1 = LocalDate.parse(tInicio.getText());
                LocalDate d2 = LocalDate.parse(tFim.getText());

                String texto = Relatorio.gerarRelatorioPorPeriodo(usuario, d1, d2);
                resposta.setText(texto);
            } catch (Exception ex) {
                resposta.setText("Erro na data. Use o formato correto: AAAA-MM-DD.");
            }
        });

        // EXPORTAÇÃO
        JButton exportarExel = criarBotao("Exportar Exel");
        exportarExel.addActionListener(e -> { JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("relatorio.xls"));
            if (fc.showSaveDialog(painel) == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarRelatorioExcelLindo(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "Excel gerado com sucesso!");
                } catch (IOException ex) { JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage()); }
            }
        });

        JButton exportarPdf = criarBotao("Exportar PDF");
        exportarPdf.addActionListener(e -> { JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("relatorio.pdf"));
            if (fc.showSaveDialog(painel) == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarPDFNativo(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "PDF gerado com sucesso!");
                } catch (IOException ex) { JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage()); }
            }
        });

        JButton voltar = criarBotao("Voltar");
        voltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(criarPainel()));

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));
        botoes.add(gerar);
        botoes.add(exportarExel);
        botoes.add(exportarPdf);
        botoes.add(voltar);

        painel.add(centro, BorderLayout.NORTH);
        painel.add(new JScrollPane(resposta), BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }

    // =======================================================
    //  TELA 2 — RELATÓRIO DE DESEMPENHO (REQ17)
    // =======================================================
    private JPanel criarPainelDesempenho() {
        JPanel painel = criarBase("Relatório de Desempenho");

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(new Color(20, 20, 20));
        area.setForeground(Color.WHITE);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));

        area.setText(Relatorio.gerarRelatorioDesempenho(usuario));

        JButton exportarExel = criarBotao("Exportar Exel");
        exportarExel.addActionListener(e -> { JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("relatorio.xls"));
            if (fc.showSaveDialog(painel) == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarRelatorioExcelLindo(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "Excel gerado com sucesso!");
                } catch (IOException ex) { JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage()); }
            }
        });

        JButton exportarPdf = criarBotao("Exportar PDF");
        exportarPdf.addActionListener(e -> { JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("relatorio.pdf"));
            if (fc.showSaveDialog(painel) == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarPDFNativo(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "PDF gerado com sucesso!");
                } catch (IOException ex) { JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage()); }
            }
        });

        JButton voltar = criarBotao("Voltar");
        voltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(criarPainel()));

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));
        botoes.add(voltar);
        botoes.add(exportarExel);
        botoes.add(exportarPdf);

        painel.add(new JScrollPane(area), BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }

    // =======================================================
    //  TELA 3 — RELATÓRIO DE DESAFIOS (REQ18)
    // =======================================================
    private JPanel criarPainelDesafios() {
        JPanel painel = criarBase("Relatório de Desafios");

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(new Color(20, 20, 20));
        area.setForeground(Color.WHITE);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));

        java.util.List<Desafio> desafios = Fachada.getInstance().getControladorDesafio().listarDesafios();

        String texto = Relatorio.gerarRelatorioDesafios(desafios, usuario);
        area.setText(texto);

        JButton exportarExel = criarBotao("Exportar Excel");
        exportarExel.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("relatorio_desafios.xls"));
            if (fc.showSaveDialog(painel) == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarRelatorioExcelLindo(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "Excel gerado com sucesso!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage());
                }
            }
        });

        JButton exportarPdf = criarBotao("Exportar PDF");
        exportarPdf.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("relatorio_desafios.pdf"));
            if (fc.showSaveDialog(painel) == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarPDFNativo(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "PDF gerado com sucesso!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage());
                }
            }
        });

        JButton voltar = criarBotao("Voltar");
        voltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(criarPainel()));

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));
        botoes.add(voltar);
        botoes.add(exportarExel);
        botoes.add(exportarPdf);

        painel.add(new JScrollPane(area), BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }

    

    // Painel base estilizado
    private JPanel criarBase(String tituloTexto) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel(tituloTexto, SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(74, 255, 86));
        painel.add(titulo, BorderLayout.NORTH);

        return painel;
    }

    private JLabel criarTexto(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(Color.WHITE);
        return l;
    }

    private interface TextoProvider {
        String getTexto();
    }
}

