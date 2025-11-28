package br.com.gui.TelasPlanosTreino;

import br.com.gui.GerenciadorTelas;
import br.com.negocio.ControladorCliente;
import br.com.negocio.treinos.PlanoTreino;
import br.com.negocio.treinos.Treino;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TelaDetalhesPlanoTreino {
    private final ControladorCliente controladorCliente;
    private final PlanoTreino plano;

    public TelaDetalhesPlanoTreino(ControladorCliente controladorCliente, PlanoTreino plano) {
        this.controladorCliente = controladorCliente;
        this.plano = plano;
    }

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30,30,30));
        painel.setBorder(new EmptyBorder(12,12,12,12));

        JLabel titulo = new JLabel("Plano: " + plano.getNome(), SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        painel.add(titulo, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (Treino t : plano.getTreinosDoPlano()) model.addElement(t.getNomeTreino());
        JList<String> listaTreinos = new JList<>(model);
        painel.add(new JScrollPane(listaTreinos), BorderLayout.CENTER);

        JPanel painelAdd = new JPanel();
        JComboBox<String> cbTreinos = new JComboBox<>();
        List<Treino> disponiveis = plano.getUsuarioAlvo().getTreinos();
        for (Treino t : disponiveis) cbTreinos.addItem("ID " + t.getIdTreino() + " - " + t.getNomeTreino());

        JButton btnAdd = new JButton("Adicionar Treino");
        btnAdd.addActionListener(e -> {
            int idx = cbTreinos.getSelectedIndex();
            if (idx >= 0) {
                Treino escolhido = disponiveis.get(idx);
                plano.adicionarTreino(escolhido);
                model.addElement(escolhido.getNomeTreino());
                controladorCliente.atualizarCliente(plano.getUsuarioAlvo());
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaListarPlanosTreino(controladorCliente).criarPainel()));

        painelAdd.add(cbTreinos);
        painelAdd.add(btnAdd);
        painelAdd.add(btnVoltar);
        painel.add(painelAdd, BorderLayout.SOUTH);

        return painel;
    }
}