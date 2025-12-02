package br.com.gui;

import br.com.negocio.treinos.Desafio;
import br.com.negocio.treinos.Relatorio;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TelaGerenciarDesafios {

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Administração de Desafios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(74, 255, 86));
        painel.add(titulo, BorderLayout.NORTH);

        // Lista de Desafios
        DefaultListModel<Desafio> model = new DefaultListModel<>();
        List<Desafio> todos = TelaComputador.controladorDesafio.listarDesafios();
        for(Desafio d : todos) model.addElement(d);

        JList<Desafio> lista = new JList<>(model);
        lista.setBackground(new Color(50, 50, 50));
        lista.setForeground(Color.WHITE);
        
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Desafio d = (Desafio) value;
                String status = LocalDate.now().isAfter(d.getDataFim()) ? "[CONCLUÍDO] " : "[EM ANDAMENTO] ";
                setText(status + d.getNome() + " (Part.: " + d.getParticipacoes().size() + ")");
                
                if (isSelected) {
                    setBackground(new Color(74, 255, 86));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(new Color(50, 50, 50));
                    setForeground(LocalDate.now().isAfter(d.getDataFim()) ? Color.GRAY : Color.WHITE);
                }
                return this;
            }
        });

        JScrollPane scrollLista = new JScrollPane(lista);
        scrollLista.setPreferredSize(new Dimension(250, 0));
        scrollLista.setBorder(BorderFactory.createLineBorder(new Color(60,60,60)));
        painel.add(scrollLista, BorderLayout.WEST);
        
        // Área de Detalhes
        JTextArea areaDetalhes = new JTextArea();
        areaDetalhes.setEditable(false);
        areaDetalhes.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaDetalhes.setBackground(new Color(40, 40, 40)); // Fundo escuro
        areaDetalhes.setForeground(new Color(220, 220, 220)); // Texto claro
        areaDetalhes.setBorder(new EmptyBorder(10,10,10,10));
        
        painel.add(new JScrollPane(areaDetalhes), BorderLayout.CENTER);

        // Botões de Ação
        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));

        JButton btnVerDetalhes = criarBotao("Ver Detalhes");
        btnVerDetalhes.addActionListener(e -> {
            Desafio sel = lista.getSelectedValue();
            if(sel != null) {
                StringBuilder sb = new StringBuilder();
                boolean concluido = LocalDate.now().isAfter(sel.getDataFim());
                
                sb.append("DESAFIO: ").append(sel.getNome()).append("\n");
                sb.append("Status: ").append(concluido ? "ENCERRADO" : "ATIVO").append("\n");
                sb.append("Período: ").append(sel.getDataInicio()).append(" até ").append(sel.getDataFim()).append("\n");
                sb.append("Descrição: ").append(sel.getDescricao()).append("\n\n");
                
                String ranking = Relatorio.gerarRankingDesafioTexto(sel);
                sb.append(ranking);
                
                if (concluido && !sel.getParticipacoes().isEmpty()) {
                    String vencedor = sel.getParticipacoes().get(0).getUsuario().getNome();
                    sb.append("\n🏆 VENCEDOR: ").append(vencedor);
                }
                areaDetalhes.setText(sb.toString());
            }
        });

        JButton btnEditar = criarBotao("Editar");
        btnEditar.addActionListener(e -> {
            Desafio sel = lista.getSelectedValue();
            if(sel != null) abrirDialogoEditar(sel, areaDetalhes);
            else JOptionPane.showMessageDialog(painel, "Selecione um desafio.");
        });
        
        JButton btnCriar = criarBotao("Criar Novo");
        btnCriar.setBackground(new Color(74, 255, 86));
        btnCriar.setForeground(Color.BLACK);
        btnCriar.addActionListener(e -> TelaDesafios.abrirDialogoCriarDesafio());

        JButton btnVoltar = criarBotao("Voltar");
        btnVoltar.setBackground(new Color(60, 60, 60));
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaComputador().criarPainelAdmin()));

        botoes.add(btnVoltar);
        botoes.add(btnCriar);
        botoes.add(btnVerDetalhes);
        botoes.add(btnEditar);
        
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }
    
    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(new Color(50, 50, 50));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void abrirDialogoEditar(Desafio d, JTextArea areaLog) {
        // Mantém o diálogo padrão, mas pode ser estilizado se necessário
        JDialog diag = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Editar Desafio", true);
        diag.setLayout(new GridLayout(5,2));
        diag.setSize(400, 250);
        diag.setLocationRelativeTo(null);
        
        JTextField nome = new JTextField(d.getNome());
        JTextField desc = new JTextField(d.getDescricao());
        JTextField ini = new JTextField(d.getDataInicio().toString());
        JTextField fim = new JTextField(d.getDataFim().toString());
        
        diag.add(new JLabel("Nome:")); diag.add(nome);
        diag.add(new JLabel("Descrição:")); diag.add(desc);
        diag.add(new JLabel("Início:")); diag.add(ini);
        diag.add(new JLabel("Fim:")); diag.add(fim);
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> {
            try {
                TelaComputador.controladorDesafio.atualizarDesafio(d.getIdDesafio(), nome.getText(), desc.getText(), LocalDate.parse(ini.getText()), LocalDate.parse(fim.getText()));
                JOptionPane.showMessageDialog(diag, "Atualizado! (Recarregue a tela para ver na lista)");
                areaLog.setText("Desafio atualizado. Selecione novamente para ver detalhes.");
                diag.dispose();
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(diag, "Erro: " + ex.getMessage());
            }
        });
        diag.add(btnSalvar);
        diag.setVisible(true);
    }
}