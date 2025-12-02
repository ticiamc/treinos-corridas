package br.com.gui;

import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaPerfilUsuario extends JFrame {
    private JTextField campoCpfBusca, campoNome, campoIdade, campoPeso, campoAltura, campoEmail;
    private JButton botaoBuscar, botaoSalvar;
    private Usuario usuarioAtual;

    public TelaPerfilUsuario() {
        initUI();
    }

    public TelaPerfilUsuario(Usuario usuarioAlvo) {
        initUI();
        this.usuarioAtual = usuarioAlvo;
        carregarUsuarioNosCampos();
        campoCpfBusca.setText(usuarioAlvo.getCpf());
        campoCpfBusca.setEditable(false);
        botaoBuscar.setEnabled(false);
    }

    private void initUI() {
        setTitle("Perfil do Usuário");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Fundo escuro para o JFrame
        getContentPane().setBackground(new Color(30, 30, 30));

        JPanel painelBusca = new JPanel(new GridLayout(1, 3, 10, 10));
        painelBusca.setBackground(new Color(30, 30, 30));
        painelBusca.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setForeground(Color.WHITE);
        painelBusca.add(lblCpf);
        
        campoCpfBusca = criarTextField();
        painelBusca.add(campoCpfBusca);
        
        botaoBuscar = new JButton("Buscar");
        estilizarBotao(botaoBuscar, new Color(74, 255, 86), Color.BLACK);
        painelBusca.add(botaoBuscar);
        
        add(painelBusca, BorderLayout.NORTH);

        JPanel painelDados = new JPanel(new GridLayout(6, 2, 10, 15));
        painelDados.setBackground(new Color(40, 40, 40)); // Card mais claro
        painelDados.setBorder(new EmptyBorder(20, 40, 20, 40));

        painelDados.add(criarLabel("Nome:")); campoNome = criarTextField(); painelDados.add(campoNome);
        painelDados.add(criarLabel("Idade:")); campoIdade = criarTextField(); painelDados.add(campoIdade);
        painelDados.add(criarLabel("Peso:")); campoPeso = criarTextField(); painelDados.add(campoPeso);
        painelDados.add(criarLabel("Altura:")); campoAltura = criarTextField(); painelDados.add(campoAltura);
        painelDados.add(criarLabel("Email:")); campoEmail = criarTextField(); painelDados.add(campoEmail);
        add(painelDados, BorderLayout.CENTER);

        JPanel painelSul = new JPanel();
        painelSul.setBackground(new Color(30, 30, 30));
        painelSul.setBorder(new EmptyBorder(10,0,20,0));
        
        botaoSalvar = new JButton("Salvar Alterações");
        estilizarBotao(botaoSalvar, new Color(74, 255, 86), Color.BLACK);
        botaoSalvar.setPreferredSize(new Dimension(200, 40));
        painelSul.add(botaoSalvar);
        
        add(painelSul, BorderLayout.SOUTH);

        configurarAcoes();
        setVisible(true);
    }

    private void configurarAcoes() {
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
    }

    private JTextField criarTextField() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(60, 60, 60));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(new Color(74, 255, 86));
        tf.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        return tf;
    }
    
    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }
    
    private void estilizarBotao(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }

    private void carregarUsuarioNosCampos() {
        campoNome.setText(usuarioAtual.getNome());
        campoIdade.setText(String.valueOf(usuarioAtual.getIdade()));
        campoPeso.setText(String.valueOf(usuarioAtual.getPeso()));
        campoAltura.setText(String.valueOf(usuarioAtual.getAltura()));
        campoEmail.setText(usuarioAtual.getEmail());
    }
}