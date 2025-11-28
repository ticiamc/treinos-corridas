package br.com.gui.TelasPlanosTreino;

import br.com.negocio.ControladorCliente;
import br.com.negocio.treinos.Usuario;
import br.com.negocio.treinos.PlanoTreino;
import br.com.negocio.ControladorTreino;
import br.com.negocio.treinos.Treino;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TelaDetalhesPlanoTreino {

    private final ControladorCliente controladorCliente;
    private final ControladorTreino controladorTreino;
    private final PlanoTreino plano;

    public TelaDetalhesPlanoTreino(ControladorCliente controladorCliente, PlanoTreino plano) {
        this.controladorCliente = controladorCliente;
        this.controladorTreino = TelaComputador.controladorTreino;
        this.plano = plano;
    }

    public JPanel criarPainel() {
        Color corFundo = new Color(30,30,30);
        Color corCard = new Color(45,45,45);
        Color corDestaque = new Color(74,255,86);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(corFundo);
        painel.setBorder(new EmptyBorder(12,12,12,12));

        JLabel titulo = new JLabel("Detalhes do Plano", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(corDestaque);
        painel.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(corCard);
        centro.setBorder(new EmptyBorder(12,12,12,12));

        JTextField txtNome = new JTextField(plano.getNome());
        txtNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        centro.add(new JLabel("Nome:")); centro.add(txtNome);
        centro.add(Box.createVerticalStrut(8));

        JLabel lblPeriodo = new JLabel("Período: " + plano.getDataInicio() + " → " + plano.getDataFim());
        lblPeriodo.setForeground(Color.WHITE);
        centro.add(lblPeriodo);
        centro.add(Box.createVerticalStrut(12));

        centro.add(new JLabel("Treinos do Plano:"));
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Treino t : plano.getTreinosDoPlano()) {
            model.addElement(t.getNomeTreino());
        }
        JList<String> listaTreinos = new JList<>(model);
        listaTreinos.setVisibleRowCount(6);
        centro.add(new JScrollPane(listaTreinos));

        // opção para adicionar treino existente ao plano
        JPanel painelAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelAdd.setBackground(corCard);
        JComboBox<String> cbTreinos = new JComboBox<>();
        // popula com treinos disponíveis do controladorTreino (se houver)
        List<Treino> disponiveis = controladorTreino.listarTodosTreinos();
        for (Treino t : disponiveis) cbTreinos.addItem(t.getNomeTreino());

        JButton btnAddTreino = new JButton("Adicionar treino ao plano");
        btnAddTreino.addActionListener(e -> {
            int idx = cbTreinos.getSelectedIndex();
            if (idx < 0) { JOptionPane.showMessageDialog(painel, "Selecione um treino."); return; }
            Treino escolhido = disponiveis.get(idx);
            plano.adicionarTreino(escolhido);
            // persiste alterações no usuário dono do plano
            Usuario dono = plano.getUsuarioAlvo();
            if (dono != null) {
                controladorCliente.atualizarCliente(dono);
                model.addElement(escolhido.getNomeTreino());
                JOptionPane.showMessageDialog(painel, "Treino adicionado ao plano.");
            } else {
                JOptionPane.showMessageDialog(painel, "Plano sem usuário alvo.");
            }
        });

        painelAdd.add(cbTreinos);
        painelAdd.add(btnAddTreino);
        centro.add(painelAdd);

        painel.add(centro, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBackground(corFundo);

        JButton btnSalvar = new JButton("Salvar Alterações Nome");
        btnSalvar.setBackground(corDestaque);
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.addActionListener(e -> {
            plano.setNome(txtNome.getText().trim()); // note: if no setter exists, we need to workaround
            // se a classe PlanoTreino não tem setNome, usa reflexão ou recria; aqui assumimos setNome existente
            Usuario dono = plano.getUsuarioAlvo();
            if (dono != null) {
                controladorCliente.atualizarCliente(dono);
                JOptionPane.showMessageDialog(painel, "Alterações salvas.");
            } else {
                JOptionPane.showMessageDialog(painel, "Erro: plano sem usuário.");
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(60,60,60));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaListarPlanosTreino(controladorCliente).criarPainel()));

        JButton btnRemover = new JButton("Remover Plano");
        btnRemover.setBackground(new Color(180,50,50));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(painel, "Remover plano '" + plano.getNome() + "'?");
            if (ok == JOptionPane.YES_OPTION) {
                Usuario dono = plano.getUsuarioAlvo();
                if (dono != null) {
                    dono.getPlanos().remove(plano);
                    controladorCliente.atualizarCliente(dono);
                    JOptionPane.showMessageDialog(painel, "Plano removido.");
                    GerenciadorTelas.getInstance().carregarTela(new TelaListarPlanosTreino(controladorCliente).criarPainel());
                }
            }
        });

        botoes.add(btnVoltar);
        botoes.add(btnSalvar);
        botoes.add(btnRemover);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }
}
