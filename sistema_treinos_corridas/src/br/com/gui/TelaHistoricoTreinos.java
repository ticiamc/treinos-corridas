package br.com.gui;

import br.com.negocio.treinos.Corrida;
import br.com.negocio.treinos.Intervalado;
import br.com.negocio.treinos.Relatorio;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaHistoricoTreinos {

    private Usuario usuario;

    public TelaHistoricoTreinos(Usuario usuario) {
        this.usuario = usuario;
    }

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Histórico de Atividades", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(74, 255, 86));
        painel.add(titulo, BorderLayout.NORTH);

        String[] colunas = {"Data/Hora", "Modalidade", "Nome", "Duração", "Detalhes (Km/Séries)", "Kcal"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        List<Treino> treinos = usuario.getTreinos();
        treinos.sort((t1, t2) -> t2.getDataExecucao().compareTo(t1.getDataExecucao()));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Treino t : treinos) {
            String tipo = (t instanceof Corrida) ? "Corrida" : "Intervalado";
            String duracao = (t.getDuracaoSegundos() / 60) + " min";
            String calorias = String.format("%.0f", t.calcularCaloriasQueimadas(usuario));
            String detalhes = (t instanceof Corrida) ? String.format("%.2f km", ((Corrida) t).getDistanciaEmMetros() / 1000.0) : ((Intervalado) t).getSeries() + " séries";
            model.addRow(new Object[]{t.getDataExecucao().format(fmt), tipo, t.getNomeTreino(), duracao, detalhes, calorias});
        }

        JTable tabela = new JTable(model);
        estilizarTabela(tabela);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(new Color(30,30,30));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        painel.add(scroll, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));

        // BOTÃO EXCEL (.XLS) - Visual bonito
        JButton btnExportarExcel = new JButton("Exportar Excel (Visual)");
        estilizarBotao(btnExportarExcel, new Color(74, 255, 86), Color.BLACK);
        btnExportarExcel.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("historico_treinos.xls"));
            if (fc.showSaveDialog(painel) == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarRelatorioExcelLindo(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "Arquivo Excel gerado com sucesso!");
                } catch (IOException ex) { JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage()); }
            }
        });

        // BOTÃO PDF REAL - Binário Nativo
        JButton btnExportarPDF = new JButton("Exportar PDF");
        estilizarBotao(btnExportarPDF, new Color(255, 100, 100), Color.BLACK);
        btnExportarPDF.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("historico_treinos.pdf"));
            if (fc.showSaveDialog(painel) == JFileChooser.APPROVE_OPTION) {
                try {
                    Relatorio.exportarPDFNativo(usuario, fc.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(painel, "Arquivo PDF gerado com sucesso! (Compatível com Acrobat)");
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

    private void estilizarTabela(JTable tabela) {
        tabela.setBackground(new Color(45, 45, 45));
        tabela.setForeground(Color.WHITE);
        tabela.setGridColor(new Color(60, 60, 60));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setRowHeight(30);
        tabela.setFillsViewportHeight(true);
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(new Color(20, 20, 20));
        header.setForeground(new Color(74, 255, 86));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i=0; i<tabela.getColumnCount(); i++) tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    
    private void estilizarBotao(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }
}