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
import java.time.format.DateTimeFormatter;

public class TelaListarPlanosTreino {
    private final ControladorCliente controlador;

    public TelaListarPlanosTreino(ControladorCliente controlador) { this.controlador = controlador; }

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(new EmptyBorder(12,12,12,12));
        
        JLabel lblTitulo = new JLabel("Meus Planos de Treino", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(74, 255, 86));
        painel.add(lblTitulo, BorderLayout.NORTH);

        DefaultListModel<PlanoTreino> model = new DefaultListModel<>();
        Usuario u = SessaoUsuario.getInstance().getUsuarioLogado();
        if(u != null) { for(PlanoTreino p : u.getPlanos()) model.addElement(p); }
        
        JList<PlanoTreino> lista = new JList<>(model);
        lista.setBackground(new Color(50, 50, 50));
        lista.setForeground(Color.WHITE);
        
        // --- RENDERIZADOR ATUALIZADO (CORES) ---
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PlanoTreino) {
                    PlanoTreino p = (PlanoTreino) value;
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String texto = String.format("%s (%s a %s)", 
                        p.getNome(), 
                        p.getDataInicio().format(fmt), 
                        p.getDataFim().format(fmt)
                    );
                    setText(texto);
                }
                
                if (isSelected) {
                    setBackground(new Color(74, 255, 86));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(new Color(50, 50, 50));
                    setForeground(Color.WHITE);
                }
                
                return this;
            }
        });
        // ----------------------------------------------
        
        painel.add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));
        
        JButton btnDetalhes = new JButton("Ver Detalhes");
        btnDetalhes.setBackground(new Color(74, 255, 86));
        btnDetalhes.setForeground(Color.BLACK);
        
        btnDetalhes.addActionListener(e -> {
            PlanoTreino sel = lista.getSelectedValue();
            if(sel != null) GerenciadorTelas.getInstance().carregarTela(new TelaDetalhesPlanoTreino(controlador, sel).criarPainel());
        });
        
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        
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
        btnVoltar.setBackground(new Color(60, 60, 60));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPlanosPrincipal(controlador).criarPainel()));

        botoes.add(btnVoltar);
        botoes.add(btnDetalhes);
        botoes.add(btnExcluir);
        painel.add(botoes, BorderLayout.SOUTH);
        return painel;
    }
}