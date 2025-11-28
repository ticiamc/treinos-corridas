package br.com.gui;

import br.com.negocio.ControladorCliente;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaPerfilUsuario extends JFrame {

    // Referência ao controlador
    private ControladorCliente controlador;

    // Componentes da tela
    private JTextField campoCpfBusca;
    private JTextField campoNome;
    private JTextField campoIdade;
    private JTextField campoPeso;
    private JTextField campoAltura;
    private JTextField campoEmail;

    private JButton botaoBuscar;
    private JButton botaoSalvar;

    private Usuario usuarioAtual; // armazena o usuário encontrado

    public TelaPerfilUsuario(ControladorCliente controlador) {
        this.controlador = controlador;

        setTitle("Perfil do Usuário");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel superior (busca)
        JPanel painelBusca = new JPanel(new GridLayout(1, 3));
        painelBusca.add(new JLabel("CPF:"));
        campoCpfBusca = new JTextField();
        painelBusca.add(campoCpfBusca);

        botaoBuscar = new JButton("Buscar");
        painelBusca.add(botaoBuscar);

        add(painelBusca, BorderLayout.NORTH);

        // Painel central (dados do usuário)
        JPanel painelDados = new JPanel(new GridLayout(6, 2, 5, 5));

        painelDados.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelDados.add(campoNome);

        painelDados.add(new JLabel("Idade:"));
        campoIdade = new JTextField();
        painelDados.add(campoIdade);

        painelDados.add(new JLabel("Peso:"));
        campoPeso = new JTextField();
        painelDados.add(campoPeso);

        painelDados.add(new JLabel("Altura:"));
        campoAltura = new JTextField();
        painelDados.add(campoAltura);

        painelDados.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        painelDados.add(campoEmail);

        add(painelDados, BorderLayout.CENTER);

        // Botão salvar
        botaoSalvar = new JButton("Salvar Alterações");
        add(botaoSalvar, BorderLayout.SOUTH);

        // Eventos
        configurarEventos();

        setVisible(true);
    }

    private void configurarEventos() {

        // --- Botão Buscar ---
        botaoBuscar.addActionListener(e -> {
            String cpf = campoCpfBusca.getText().trim();

            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite um CPF para buscar.");
                return;
            }

            usuarioAtual = controlador.buscarCliente(cpf);

            if (usuarioAtual == null) {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado.");
                limparCampos();
                return;
            }

            carregarUsuarioNosCampos();
        });

        // --- Botão Salvar ---
        botaoSalvar.addActionListener(e -> {
            if (usuarioAtual == null) {
                JOptionPane.showMessageDialog(this, "Nenhum usuário carregado.");
                return;
            }

            try {
                usuarioAtual.setNome(campoNome.getText());
                usuarioAtual.setEmail(campoEmail.getText());
                usuarioAtual.setIdade(Integer.parseInt(campoIdade.getText()));
                usuarioAtual.setPeso(Double.parseDouble(campoPeso.getText()));
                usuarioAtual.setAltura(Double.parseDouble(campoAltura.getText()));

                controlador.atualizarCliente(usuarioAtual);

                JOptionPane.showMessageDialog(this, 
                        "Dados atualizados com sucesso!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                        "Erro ao atualizar. Verifique os dados digitados.");
            }
        });
    }

    // Preenche os campos com os dados encontrados
    private void carregarUsuarioNosCampos() {
        campoNome.setText(usuarioAtual.getNome());
        campoIdade.setText(String.valueOf(usuarioAtual.getIdade()));
        campoPeso.setText(String.valueOf(usuarioAtual.getPeso()));
        campoAltura.setText(String.valueOf(usuarioAtual.getAltura()));
        campoEmail.setText(usuarioAtual.getEmail());
    }

    // Limpa campos quando erro ou usuário não encontrado
    private void limparCampos() {
        campoNome.setText("");
        campoIdade.setText("");
        campoPeso.setText("");
        campoAltura.setText("");
        campoEmail.setText("");
    }
}
