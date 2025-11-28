package br.com.gui.TelasPlanosTreino;

import br.com.gui.GerenciadorTelas;
import br.com.gui.TelaComputador;
import br.com.negocio.ControladorCliente;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class TelaCriarPlanoTreino {
    private final ControladorCliente controlador;

    public TelaCriarPlanoTreino(ControladorCliente controlador) { this.controlador = controlador; }

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));
        painel.setBorder(new EmptyBorder(20,20,20,20));

        JPanel form = new JPanel(new GridLayout(5,2,10,10));
        form.add(new JLabel("Nome:")); JTextField txtNome = new JTextField(); form.add(txtNome);
        form.add(new JLabel("Início (aaaa-mm-dd):")); JTextField txtInicio = new JTextField(LocalDate.now().toString()); form.add(txtInicio);
        form.add(new JLabel("Fim (aaaa-mm-dd):")); JTextField txtFim = new JTextField(LocalDate.now().plusWeeks(4).toString()); form.add(txtFim);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> {
            try {
                Usuario u = SessaoUsuario.getInstance().getUsuarioLogado();
                if (u == null) { JOptionPane.showMessageDialog(painel, "Login necessário."); return; }
                
                TelaComputador.controladorPlanoTreino.cadastrarPlano(u.getCpf(), txtNome.getText(), LocalDate.parse(txtInicio.getText()), LocalDate.parse(txtFim.getText()));
                JOptionPane.showMessageDialog(painel, "Plano criado!");
                GerenciadorTelas.getInstance().carregarTela(new TelaListarPlanosTreino(controlador).criarPainel());
            } catch (Exception ex) { JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage()); }
        });

        painel.add(form, BorderLayout.CENTER);
        painel.add(btnSalvar, BorderLayout.SOUTH);
        return painel;
    }
}