package br.com.gui;

import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TelaGerenciarUsuarios {

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gerenciamento de Usuários", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(74, 255, 86));
        painel.add(titulo, BorderLayout.NORTH);

        // Lista de Usuários
        DefaultListModel<Usuario> model = new DefaultListModel<>();
        List<Usuario> todos = TelaComputador.controladorCliente.getRepositorio().listarTodos();
        for(Usuario u : todos) model.addElement(u);

        JList<Usuario> lista = new JList<>(model);
        lista.setBackground(new Color(50, 50, 50)); // Fundo da lista
        lista.setForeground(Color.WHITE);           // Texto da lista
        
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Usuario u = (Usuario) value;
                String txt = String.format("%s (CPF: %s) - %s", u.getNome(), u.getCpf(), u.getEmail());
                setText(txt);
                
                // Estilização do item
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
        
        JScrollPane scroll = new JScrollPane(lista);
        scroll.getViewport().setBackground(new Color(30, 30, 30));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        painel.add(scroll, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));

        JButton btnEditar = new JButton("Editar Usuário Selecionado");
        estilizarBotao(btnEditar, new Color(74, 255, 86), Color.BLACK);
        
        btnEditar.addActionListener(e -> {
            Usuario sel = lista.getSelectedValue();
            if(sel != null) {
                new TelaPerfilUsuario(sel); 
            } else {
                JOptionPane.showMessageDialog(painel, "Selecione um usuário.");
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        estilizarBotao(btnVoltar, new Color(60, 60, 60), Color.WHITE);
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaComputador().criarPainelAdmin()));

        botoes.add(btnVoltar);
        botoes.add(btnEditar);
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