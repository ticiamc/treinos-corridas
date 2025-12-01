package br.com.gui;

import br.com.negocio.Fachada;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import java.awt.*;

// Agora apenas gera um PAINEL, não é mais um JFrame independente
public class TelaPerfilUsuario {

    private Usuario usuarioAtual;

    public TelaPerfilUsuario(Usuario usuario) {
        this.usuarioAtual = usuario;
    }

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        
        // Título
        JLabel lblTitulo = new JLabel("Meu Perfil", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(74, 255, 86));
        painel.add(lblTitulo, BorderLayout.NORTH);

        // Formulário
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBackground(new Color(45, 45, 45));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JTextField campoNome = new JTextField(usuarioAtual.getNome());
        JTextField campoIdade = new JTextField(String.valueOf(usuarioAtual.getIdade()));
        JTextField campoPeso = new JTextField(String.valueOf(usuarioAtual.getPeso()));
        JTextField campoAltura = new JTextField(String.valueOf(usuarioAtual.getAltura()));
        JTextField campoEmail = new JTextField(usuarioAtual.getEmail());
        JLabel lblCpf = new JLabel(usuarioAtual.getCpf()); 
        lblCpf.setForeground(Color.GRAY);

        adicionarCampo(form, "Nome:", campoNome);
        adicionarCampo(form, "Idade:", campoIdade);
        adicionarCampo(form, "Peso (kg):", campoPeso);
        adicionarCampo(form, "Altura (m):", campoAltura);
        adicionarCampo(form, "E-mail:", campoEmail);
        
        JLabel lCpf = new JLabel("CPF:"); lCpf.setForeground(Color.WHITE);
        form.add(lCpf); form.add(lblCpf);

        painel.add(form, BorderLayout.CENTER);

        // Botões
        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setBackground(new Color(74, 255, 86));
        btnSalvar.addActionListener(e -> {
            try {
                usuarioAtual.setNome(campoNome.getText());
                usuarioAtual.setIdade(Integer.parseInt(campoIdade.getText()));
                usuarioAtual.setPeso(Double.parseDouble(campoPeso.getText()));
                usuarioAtual.setAltura(Double.parseDouble(campoAltura.getText()));
                usuarioAtual.setEmail(campoEmail.getText());

                // Persiste via Fachada
                Fachada.getInstance().getControladorCliente().atualizarCliente(usuarioAtual);
                JOptionPane.showMessageDialog(painel, "Dados atualizados com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painel, "Erro: Verifique os dados numéricos.");
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            // Verifica se é admin ou usuario comum para voltar pra tela certa
            if(SessaoUsuario.getInstance().getUsuarioLogado() != null && 
               SessaoUsuario.getInstance().getUsuarioLogado().getCpf().equals(usuarioAtual.getCpf())) {
                GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario());
            } else {
                GerenciadorTelas.getInstance().carregarTela(new TelaGerenciarUsuarios().criarPainel());
            }
        });

        botoes.add(btnVoltar);
        botoes.add(btnSalvar);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }

    private void adicionarCampo(JPanel p, String label, JComponent campo) {
        JLabel l = new JLabel(label);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(l);
        p.add(campo);
    }
}