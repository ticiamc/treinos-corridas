package br.com.gui.TelasPlanosTreino;

import br.com.gui.GerenciadorTelas;
import br.com.gui.TelaComputador;
import br.com.negocio.ControladorCliente;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;
import br.com.negocio.treinos.PlanoTreino;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaListarPlanosTreino {
    private final ControladorCliente controlador;

    public TelaListarPlanosTreino(ControladorCliente controlador) { this.controlador = controlador; }

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(new EmptyBorder(12,12,12,12));

        DefaultListModel<PlanoTreino> model = new DefaultListModel<>();
        Usuario u = SessaoUsuario.getInstance().getUsuarioLogado();
        if(u != null) { for(PlanoTreino p : u.getPlanos()) model.addElement(p); }
        
        JList<PlanoTreino> lista = new JList<>(model);
        
        // Renderizador para mostrar nome do plano na lista
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PlanoTreino) {
                    setText(((PlanoTreino)value).getNome());
                }
                return this;
            }
        });
        
        painel.add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton btnDetalhes = new JButton("Ver Detalhes");
        btnDetalhes.addActionListener(e -> {
            PlanoTreino sel = lista.getSelectedValue();
            if(sel != null) GerenciadorTelas.getInstance().carregarTela(new TelaDetalhesPlanoTreino(controlador, sel).criarPainel());
        });
        
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> {
            PlanoTreino sel = lista.getSelectedValue();
            if(sel != null) {
                try {
                    TelaComputador.controladorPlanoTreino.removerPlano(u.getCpf(), sel.getIdPlano());
                    model.removeElement(sel);
                } catch(Exception ex) { JOptionPane.showMessageDialog(painel, ex.getMessage()); }
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPlanosPrincipal(controlador).criarPainel()));

        botoes.add(btnVoltar);
        botoes.add(btnDetalhes);
        botoes.add(btnExcluir);
        painel.add(botoes, BorderLayout.SOUTH);
        return painel;
    }
}