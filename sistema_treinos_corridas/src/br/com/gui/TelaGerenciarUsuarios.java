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
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Usuario u = (Usuario) value;
                String txt = String.format("%s (CPF: %s) - %s", u.getNome(), u.getCpf(), u.getEmail());
                return super.getListCellRendererComponent(list, txt, index, isSelected, cellHasFocus);
            }
        });
        
        painel.add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));

        JButton btnEditar = new JButton("Editar Usuário Selecionado");
        btnEditar.addActionListener(e -> {
            Usuario sel = lista.getSelectedValue();
            if(sel != null) {
                // Abre a tela de perfil já carregada com este usuário
                new TelaPerfilUsuario(sel); 
            } else {
                JOptionPane.showMessageDialog(painel, "Selecione um usuário.");
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaComputador().criarPainelAdmin()));

        botoes.add(btnVoltar);
        botoes.add(btnEditar);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }
}