package br.com.gui;

import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import java.awt.*;

public class TelaPerfilUsuario extends JFrame {
    private JTextField campoCpfBusca, campoNome, campoIdade, campoPeso, campoAltura, campoEmail;
    private JButton botaoBuscar, botaoSalvar;
    private Usuario usuarioAtual;

    // Construtor padrão
    public TelaPerfilUsuario() {
        initUI();
    }

    // Construtor para Admin (Edição direta)
    public TelaPerfilUsuario(Usuario usuarioAlvo) {
        initUI();
        this.usuarioAtual = usuarioAlvo;
        carregarUsuarioNosCampos();
        // Desativa busca pois já estamos editando um específico
        campoCpfBusca.setText(usuarioAlvo.getCpf());
        campoCpfBusca.setEditable(false);
        botaoBuscar.setEnabled(false);
    }

    private void initUI() {
        setTitle("Perfil do Usuário");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelBusca = new JPanel(new GridLayout(1, 3));
        painelBusca.add(new JLabel("CPF:"));
        campoCpfBusca = new JTextField();
        painelBusca.add(campoCpfBusca);
        botaoBuscar = new JButton("Buscar");
        painelBusca.add(botaoBuscar);
        add(painelBusca, BorderLayout.NORTH);

        JPanel painelDados = new JPanel(new GridLayout(6, 2, 5, 5));
        painelDados.add(new JLabel("Nome:")); campoNome = new JTextField(); painelDados.add(campoNome);
        painelDados.add(new JLabel("Idade:")); campoIdade = new JTextField(); painelDados.add(campoIdade);
        painelDados.add(new JLabel("Peso:")); campoPeso = new JTextField(); painelDados.add(campoPeso);
        painelDados.add(new JLabel("Altura:")); campoAltura = new JTextField(); painelDados.add(campoAltura);
        painelDados.add(new JLabel("Email:")); campoEmail = new JTextField(); painelDados.add(campoEmail);
        add(painelDados, BorderLayout.CENTER);

        botaoSalvar = new JButton("Salvar Alterações");
        add(botaoSalvar, BorderLayout.SOUTH);

        botaoBuscar.addActionListener(e -> {
            usuarioAtual = TelaComputador.controladorCliente.buscarCliente(campoCpfBusca.getText().trim());
            if (usuarioAtual == null) JOptionPane.showMessageDialog(this, "Usuário não encontrado.");
            else carregarUsuarioNosCampos();
        });

        botaoSalvar.addActionListener(e -> {
            if (usuarioAtual != null) {
                try {
                    usuarioAtual.setNome(campoNome.getText());
                    usuarioAtual.setEmail(campoEmail.getText());
                    usuarioAtual.setIdade(Integer.parseInt(campoIdade.getText()));
                    usuarioAtual.setPeso(Double.parseDouble(campoPeso.getText()));
                    usuarioAtual.setAltura(Double.parseDouble(campoAltura.getText()));
                    TelaComputador.controladorCliente.atualizarCliente(usuarioAtual);
                    JOptionPane.showMessageDialog(this, "Dados atualizados!");
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro ao atualizar."); }
            }
        });
        setVisible(true);
    }

    private void carregarUsuarioNosCampos() {
        campoNome.setText(usuarioAtual.getNome());
        campoIdade.setText(String.valueOf(usuarioAtual.getIdade()));
        campoPeso.setText(String.valueOf(usuarioAtual.getPeso()));
        campoAltura.setText(String.valueOf(usuarioAtual.getAltura()));
        campoEmail.setText(usuarioAtual.getEmail());
    }
}