package br.com.gui.TelasPlanosTreino;

import br.com.negocio.ControladorCliente;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;
import br.com.negocio.treinos.PlanoTreino;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TelaListarPlanosTreino {

    private final ControladorCliente controlador;

    public TelaListarPlanosTreino(ControladorCliente controlador) {
        this.controlador = controlador;
    }

    public JPanel criarPainel() {
        Color corFundo = new Color(30, 30, 30);
        Color corCard = new Color(45, 45, 45);
        Color corDestaque = new Color(74, 255, 86);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(corFundo);
        painel.setBorder(new EmptyBorder(12,12,12,12));

        JLabel titulo = new JLabel("Meus Planos de Treino", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(corDestaque);
        painel.add(titulo, BorderLayout.NORTH);

        // Área central com lista
        DefaultListModel<PlanoTreino> model = new DefaultListModel<>();
        JList<PlanoTreino> lista = new JList<>(model);
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                PlanoTreino p = (PlanoTreino) value;
                String txt = p.getNome() + "  —  " + p.getDataInicio() + " a " + p.getDataFim();
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, txt, index, isSelected, cellHasFocus);
                lbl.setOpaque(true);
                lbl.setBackground(isSelected ? corDestaque : corCard);
                lbl.setForeground(isSelected ? Color.BLACK : Color.WHITE);
                lbl.setBorder(new EmptyBorder(8,8,8,8));
                return lbl;
            }
        });

        // Preenche modelo com planos do usuário logado (ou pergunta CPF)
        Usuario usuario = SessaoUsuario.getInstance().getUsuarioLogado();
        if (usuario == null) {
            String cpf = JOptionPane.showInputDialog(null, "Digite CPF para listar planos:");
            if (cpf != null && !cpf.trim().isEmpty()) {
                usuario = controlador.buscarCliente(cpf.trim());
            }
        }

        if (usuario == null) {
            model.addElement(new PlanoTreino("Nenhum usuário selecionado", null, null, null));
        } else {
            List<PlanoTreino> planos = usuario.getPlanos();
            if (planos.isEmpty()) {
                model.addElement(new PlanoTreino("Nenhum plano encontrado", null, null, usuario));
            } else {
                for (PlanoTreino p : planos) model.addElement(p);
            }
        }

        JScrollPane sc = new JScrollPane(lista);
        sc.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        painel.add(sc, BorderLayout.CENTER);

        // Botões de ação
        JPanel botoes = new JPanel();
        botoes.setBackground(corFundo);

        JButton btnDetalhes = new JButton("Ver Detalhes / Editar");
        btnDetalhes.setBackground(new Color(50,50,50));
        btnDetalhes.setForeground(Color.WHITE);
        btnDetalhes.addActionListener(e -> {
            PlanoTreino selecionado = lista.getSelectedValue();
            if (selecionado == null) {
                JOptionPane.showMessageDialog(painel, "Selecione um plano.");
                return;
            }
            // abre painel de detalhes (edição mínima)
            TelaDetalhesPlanoTreino detalhes = new TelaDetalhesPlanoTreino(controlador, selecionado);
            GerenciadorTelas.getInstance().carregarTela(detalhes.criarPainel());
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(60,60,60));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPlanosPrincipal(controlador).criarPainel()));

        JButton btnExcluir = new JButton("Excluir Plano");
        btnExcluir.setBackground(new Color(180,50,50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(e -> {
            PlanoTreino sel = lista.getSelectedValue();
            if (sel == null) { JOptionPane.showMessageDialog(painel, "Selecione um plano."); return; }
            int ok = JOptionPane.showConfirmDialog(painel, "Excluir o plano \"" + sel.getNome() + "\"?");
            if (ok == JOptionPane.YES_OPTION) {
                Usuario u = sel.getUsuarioAlvo();
                if (u != null) {
                    u.getPlanos().remove(sel);
                    controlador.atualizarCliente(u);
                    model.removeElement(sel);
                    JOptionPane.showMessageDialog(painel, "Plano excluído.");
                }
            }
        });

        botoes.add(btnVoltar);
        botoes.add(btnDetalhes);
        botoes.add(btnExcluir);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }
}

