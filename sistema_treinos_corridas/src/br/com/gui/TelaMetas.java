package br.com.gui;
import br.com.negocio.ControladorMeta;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Meta;
import br.com.negocio.treinos.TipoMeta;
import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaMetas {
    private ControladorMeta controladorMeta;
    private Usuario usuarioLogado;
    private JPanel painelPrincipal;
    private DefaultListModel<String> listModel;

    public TelaMetas() {
        if (TelaComputador.controladorMeta == null) {
            TelaComputador.controladorMeta = new ControladorMeta(TelaComputador.controladorCliente.getRepositorio());
        }
        this.controladorMeta = TelaComputador.controladorMeta;
        this.usuarioLogado = SessaoUsuario.getInstance().getUsuarioLogado();
    }

    public JPanel criarPainel() {
        painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(30, 30, 30));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Minhas Metas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(74, 255, 86));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        JList<String> listaMetas = new JList<>(listModel);
        listaMetas.setBackground(new Color(50, 50, 50));
        listaMetas.setForeground(Color.WHITE);
        listaMetas.setFont(new Font("Consolas", Font.PLAIN, 12));
        atualizarLista();
        painelPrincipal.add(new JScrollPane(listaMetas), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(30, 30, 30));
        
        JButton btnNova = new JButton("Nova Meta");
        btnNova.addActionListener(e -> abrirDialogoNovaMeta());
        JButton btnExcluir = new JButton("Excluir Selecionada");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.addActionListener(e -> {
            int idx = listaMetas.getSelectedIndex();
            if (idx != -1) {
                try {
                    Meta m = usuarioLogado.getMetas().get(idx);
                    controladorMeta.removerMeta(usuarioLogado.getCpf(), m.getIdMeta());
                    atualizarLista();
                } catch (Exception ex) { JOptionPane.showMessageDialog(painelPrincipal, ex.getMessage()); }
            }
        });
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario()));

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnNova);
        painelBotoes.add(btnExcluir);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        return painelPrincipal;
    }

    private void atualizarLista() {
        listModel.clear();
        if (usuarioLogado != null) {
            for (Meta m : usuarioLogado.getMetas()) listModel.addElement(m.toString());
        }
    }

    private void abrirDialogoNovaMeta() {
        JDialog dialog = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Cadastrar Meta", true);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(painelPrincipal);

        JTextField txtDesc = new JTextField();
        JComboBox<TipoMeta> cbTipo = new JComboBox<>(TipoMeta.values());
        JTextField txtValor = new JTextField();
        JTextField txtData = new JTextField(LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        dialog.add(new JLabel(" Descrição:")); dialog.add(txtDesc);
        dialog.add(new JLabel(" Tipo:")); dialog.add(cbTipo);
        dialog.add(new JLabel(" Alvo (km, min ou kcal):")); dialog.add(txtValor);
        dialog.add(new JLabel(" Data Limite (dd/MM/yyyy):")); dialog.add(txtData);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> {
            try {
                String desc = txtDesc.getText();
                double val = Double.parseDouble(txtValor.getText().replace(",", "."));
                LocalDate data = LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                TipoMeta tipo = (TipoMeta) cbTipo.getSelectedItem();
                if(tipo == TipoMeta.DISTANCIA) val = val * 1000;
                controladorMeta.cadastrarMeta(usuarioLogado.getCpf(), desc, tipo, val, data);
                atualizarLista();
                dialog.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, ex.getMessage()); }
        });
        dialog.add(new JLabel("")); dialog.add(btnSalvar);
        dialog.setVisible(true);
    }
}