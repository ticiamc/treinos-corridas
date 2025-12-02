package br.com.gui.TelasPlanosTreino;

import br.com.gui.GerenciadorTelas;
import br.com.gui.TelaComputador;
import br.com.negocio.ControladorCliente;
import br.com.negocio.treinos.PlanoTreino;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaDetalhesPlanoTreino {

    private final ControladorCliente controladorCliente;
    private final PlanoTreino plano;

    public TelaDetalhesPlanoTreino(ControladorCliente controladorCliente, PlanoTreino plano) {
        this.controladorCliente = controladorCliente;
        this.plano = plano;
    }

    public JPanel criarPainel() {
        Color corFundo = new Color(30,30,30);
        Color corCard = new Color(45,45,45);
        Color corDestaque = new Color(74,255,86);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(corFundo);
        painel.setBorder(new EmptyBorder(12,12,12,12));

        JLabel titulo = new JLabel("Plano: " + plano.getNome(), SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(corDestaque);
        painel.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(corCard);
        
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Treino t : plano.getTreinosDoPlano()) {
            model.addElement(t.toString());
        }
        JList<String> listaTreinos = new JList<>(model);
        listaTreinos.setBackground(new Color(50,50,50));
        listaTreinos.setForeground(Color.WHITE);
        centro.add(new JScrollPane(listaTreinos), BorderLayout.CENTER);

        JPanel painelAdd = new JPanel(new GridLayout(2, 1, 5, 5));
        painelAdd.setBackground(corFundo);
        painelAdd.setBorder(new EmptyBorder(10,0,0,0));

        // --- Adicionar Treino Existente ---
        JPanel pnlExistente = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlExistente.setBackground(corCard);
        JComboBox<String> cbTreinos = new JComboBox<>();
        List<Treino> disponiveis = plano.getUsuarioAlvo().getTreinos();
        for (Treino t : disponiveis) cbTreinos.addItem("ID " + t.getIdTreino() + " - " + t.getNomeTreino());
        
        JButton btnAddExistente = new JButton("Vincular Treino Existente");
        btnAddExistente.addActionListener(e -> {
            int idx = cbTreinos.getSelectedIndex();
            if (idx >= 0) {
                Treino escolhido = disponiveis.get(idx);
                // --- VALIDAÇÃO DE DATA NA GUI ---
                LocalDate dTreino = escolhido.getDataExecucao().toLocalDate();
                if (dTreino.isBefore(plano.getDataInicio()) || dTreino.isAfter(plano.getDataFim())) {
                    JOptionPane.showMessageDialog(painel, "Erro: A data deste treino (" + dTreino + ") está fora do período do plano.");
                    return;
                }
                // --------------------------------
                plano.adicionarTreino(escolhido);
                model.addElement(escolhido.toString());
                controladorCliente.atualizarCliente(plano.getUsuarioAlvo());
                JOptionPane.showMessageDialog(painel, "Treino vinculado ao plano.");
            }
        });
        pnlExistente.add(cbTreinos);
        pnlExistente.add(btnAddExistente);
        
        JButton btnNovoTreino = new JButton("Registrar NOVO Treino neste Plano");
        btnNovoTreino.setBackground(corDestaque);
        btnNovoTreino.setForeground(Color.BLACK);
        btnNovoTreino.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNovoTreino.addActionListener(e -> abrirDialogoNovoTreino(painel, model));

        painelAdd.add(pnlExistente);
        painelAdd.add(btnNovoTreino);
        
        centro.add(painelAdd, BorderLayout.SOUTH);
        painel.add(centro, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(60,60,60));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaListarPlanosTreino(controladorCliente).criarPainel()));
        
        painel.add(btnVoltar, BorderLayout.SOUTH);

        return painel;
    }

    private void abrirDialogoNovoTreino(JPanel parent, DefaultListModel<String> model) {
        JDialog d = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Novo Treino no Plano", true);
        d.setLayout(new GridLayout(6,2, 5, 5));
        d.setSize(400, 350);
        d.setLocationRelativeTo(parent);

        JTextField txtNome = new JTextField("Treino do Plano");
        // Sugere a data de INÍCIO do plano se hoje estiver fora
        LocalDate sugestaoData = LocalDate.now();
        if (sugestaoData.isBefore(plano.getDataInicio()) || sugestaoData.isAfter(plano.getDataFim())) {
            sugestaoData = plano.getDataInicio();
        }
        JTextField txtData = new JTextField(sugestaoData.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        JTextField txtDuracao = new JTextField("60");
        String[] tipos = {"Corrida", "Intervalado"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);
        JTextField txtExtra = new JTextField(); 
        JLabel lblExtra = new JLabel("Distância (m):");

        cbTipo.addActionListener(e -> {
            if(cbTipo.getSelectedItem().equals("Corrida")) lblExtra.setText("Distância (m):");
            else lblExtra.setText("Séries:");
        });

        d.add(new JLabel("Nome:")); d.add(txtNome);
        d.add(new JLabel("Data (dd/MM/yyyy):")); d.add(txtData);
        d.add(new JLabel("Duração (min):")); d.add(txtDuracao);
        d.add(new JLabel("Tipo:")); d.add(cbTipo);
        d.add(lblExtra); d.add(txtExtra);

        JButton btnSalvar = new JButton("Salvar e Adicionar");
        btnSalvar.addActionListener(e -> {
            try {
                Usuario u = plano.getUsuarioAlvo();
                String nome = txtNome.getText();
                LocalDate data = LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                
                // --- VALIDAÇÃO DE DATA NA GUI ---
                if (data.isBefore(plano.getDataInicio()) || data.isAfter(plano.getDataFim())) {
                    throw new Exception("Data fora da vigência do plano (" + 
                        plano.getDataInicio() + " a " + plano.getDataFim() + ")");
                }
                // --------------------------------
                
                int duracao = Integer.parseInt(txtDuracao.getText()) * 60;
                String tipo = (String) cbTipo.getSelectedItem();
                
                if(tipo.equals("Corrida")) {
                    double dist = Double.parseDouble(txtExtra.getText());
                    TelaComputador.controladorTreino.cadastrarTreino(u.getCpf(), "Corrida", data, duracao, nome, dist, 0, 0);
                } else {
                    int series = Integer.parseInt(txtExtra.getText());
                    TelaComputador.controladorTreino.cadastrarTreino(u.getCpf(), "Intervalado", data, duracao, nome, 0, series, 0);
                }

                Treino novoTreino = u.getTreinos().get(u.getTreinos().size() - 1);
                plano.adicionarTreino(novoTreino);
                controladorCliente.atualizarCliente(u);
                
                model.addElement(novoTreino.toString());
                JOptionPane.showMessageDialog(d, "Treino registrado e adicionado ao plano!");
                d.dispose();

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(d, "Erro: " + ex.getMessage());
            }
        });
        d.add(new JLabel("")); d.add(btnSalvar);
        d.setVisible(true);
    }
}