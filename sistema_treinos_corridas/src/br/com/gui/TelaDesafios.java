package br.com.gui;

import br.com.negocio.ControladorDesafio;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Desafio;
import br.com.negocio.treinos.Relatorio;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaDesafios {

    private ControladorDesafio controladorDesafio;
    private Usuario usuarioLogado;

    public TelaDesafios() {
        this.controladorDesafio = TelaComputador.controladorDesafio;
        this.usuarioLogado = SessaoUsuario.getInstance().getUsuarioLogado();
    }

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 30));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(30, 30, 30));

        JLabel titulo = new JLabel("Central de Desafios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(74, 255, 86));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topo.add(titulo, BorderLayout.CENTER);

        if (usuarioLogado != null) {
            JButton btnCriar = new JButton("+ Criar Novo");
            estilizarBotao(btnCriar, new Color(74, 255, 86), Color.BLACK);
            btnCriar.addActionListener(e -> abrirDialogoCriarDesafio());
            topo.add(btnCriar, BorderLayout.EAST);
        }
        painel.add(topo, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Todos os Desafios", criarPainelLista(false));
        if (usuarioLogado != null) {
            abas.addTab("Meus Desafios (Participando)", criarPainelLista(true));
        }

        JPanel rodape = new JPanel();
        rodape.setBackground(new Color(30, 30, 30));
        JButton btnVoltar = new JButton("Voltar ao Menu");
        estilizarBotao(btnVoltar, new Color(50, 50, 50), Color.WHITE);
        btnVoltar.addActionListener(
                e -> GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario()));
        rodape.add(btnVoltar);

        painel.add(abas, BorderLayout.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelLista(boolean apenasMeus) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(40, 40, 40));

        DefaultListModel<Desafio> model = new DefaultListModel<>();
        List<Desafio> lista = controladorDesafio.listarDesafios();

        for (Desafio d : lista) {
            boolean participa = d.getParticipacoes().stream()
                    .anyMatch(par -> par.getUsuario().getCpf().equals(usuarioLogado.getCpf()));

            if (apenasMeus && participa)
                model.addElement(d);
            else if (!apenasMeus)
                model.addElement(d);
        }

        JList<Desafio> jList = new JList<>(model);
        jList.setBackground(new Color(50, 50, 50));
        jList.setForeground(Color.WHITE);

        jList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Desafio d = (Desafio) value;
                String criador = (d.getCriador() == null) ? "Admin" : d.getCriador().getNome();

                boolean encerrado = LocalDate.now().isAfter(d.getDataFim());
                String status = encerrado ? "[ENCERRADO] " : "[ATIVO] ";

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String txt = String.format("%s %s | %s a %s | Criador: %s",
                        status, d.getNome(),
                        d.getDataInicio().format(fmt),
                        d.getDataFim().format(fmt),
                        criador);

                Component c = super.getListCellRendererComponent(list, txt, index, isSelected, cellHasFocus);

                if (isSelected) {
                    setBackground(new Color(74, 255, 86));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(new Color(50, 50, 50));
                    setForeground(encerrado ? Color.GRAY : Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(jList);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        p.add(scroll, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(40, 40, 40));

        if (!apenasMeus) {
            JButton btnEntrar = new JButton("Participar");
            estilizarBotao(btnEntrar, new Color(74, 255, 86), Color.BLACK);
            btnEntrar.addActionListener(e -> {
                Desafio sel = jList.getSelectedValue();
                if (sel != null) {
                    if (LocalDate.now().isAfter(sel.getDataFim())) {
                        JOptionPane.showMessageDialog(p, "Este desafio já foi encerrado!");
                        return;
                    }
                    try {
                        controladorDesafio.participarDesafio(sel.getIdDesafio(), usuarioLogado.getCpf());
                        JOptionPane.showMessageDialog(p, "Você entrou no desafio!");
                        GerenciadorTelas.getInstance().carregarTela(new TelaDesafios().criarPainel());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(p, ex.getMessage());
                    }
                }
            });
            botoes.add(btnEntrar);
        }

        JButton btnRanking = new JButton("Ver Ranking");
        estilizarBotao(btnRanking, new Color(60, 60, 60), Color.WHITE);
        btnRanking.addActionListener(e -> {
            Desafio sel = jList.getSelectedValue();
            if (sel != null)
                JOptionPane.showMessageDialog(p, new JTextArea(Relatorio.gerarRankingDesafioTexto(sel)));
        });
        botoes.add(btnRanking);

        JButton btnEditar = new JButton("Editar (Dono)");
        estilizarBotao(btnEditar, new Color(255, 200, 0), Color.BLACK);
        btnEditar.addActionListener(e -> {
            Desafio sel = jList.getSelectedValue();
            if (sel == null) {
                JOptionPane.showMessageDialog(p, "Selecione um desafio.");
                return;
            }
            boolean isDono = sel.getCriador() != null && sel.getCriador().getCpf().equals(usuarioLogado.getCpf());

            if (isDono) {
                abrirDialogoEditarDesafio(sel);
            } else {
                JOptionPane.showMessageDialog(p, "Apenas o criador deste desafio pode editá-lo.");
            }
        });
        botoes.add(btnEditar);

        JButton btnExcluir = new JButton("Excluir");
        estilizarBotao(btnExcluir, new Color(180, 50, 50), Color.WHITE);
        btnExcluir.addActionListener(e -> {
            Desafio sel = jList.getSelectedValue();
            if (sel == null)
                return;
            boolean isDono = sel.getCriador() != null && sel.getCriador().getCpf().equals(usuarioLogado.getCpf());

            if (isDono) {
                int opt = JOptionPane.showConfirmDialog(p, "Tem certeza que deseja excluir?");
                if (opt == JOptionPane.YES_OPTION) {
                    controladorDesafio.removerDesafio(sel.getIdDesafio());
                    model.removeElement(sel);
                }
            } else {
                JOptionPane.showMessageDialog(p, "Sem permissão.");
            }
        });
        botoes.add(btnExcluir);

        p.add(botoes, BorderLayout.SOUTH);
        return p;
    }

    public static void abrirDialogoCriarDesafio() {
        JDialog d = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Criar Desafio", true);
        d.getContentPane().setBackground(new Color(30, 30, 30));
        d.setLayout(new GridLayout(5, 2, 10, 10));
        ((JPanel) d.getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        JTextField nome = criarInputEstilizado();
        JTextField desc = criarInputEstilizado();

        JTextField ini = criarInputEstilizado();
        ini.setText(LocalDate.now().format(fmt));

        JTextField fim = criarInputEstilizado();
        fim.setText(LocalDate.now().plusMonths(1).format(fmt));

        d.add(criarLabel("Nome:"));
        d.add(nome);
        d.add(criarLabel("Descrição:"));
        d.add(desc);

        d.add(criarLabel("Início (dd/MM/yyyy):"));
        d.add(ini);
        d.add(criarLabel("Fim (dd/MM/yyyy):"));
        d.add(fim);

        JButton btn = new JButton("Criar");
        estilizarBotao(btn, new Color(74, 255, 86), Color.BLACK);

        btn.addActionListener(e -> {
            try {
                Usuario criador = SessaoUsuario.getInstance().getUsuarioLogado();

                TelaComputador.controladorDesafio.cadastrarDesafio(
                        nome.getText(), desc.getText(),
                        LocalDate.parse(ini.getText(), fmt),
                        LocalDate.parse(fim.getText(), fmt),
                        criador);

                JOptionPane.showMessageDialog(d, "Desafio criado!");
                d.dispose();
                GerenciadorTelas.getInstance().carregarTela(new TelaDesafios().criarPainel());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Erro: " + ex.getMessage());
            }
        });
        d.add(new JLabel(""));
        d.add(btn);
        d.setSize(450, 300);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

    private void abrirDialogoEditarDesafio(Desafio desafio) {
        JDialog d = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Editar Desafio", true);
        d.getContentPane().setBackground(new Color(30, 30, 30));
        d.setLayout(new GridLayout(5, 2, 10, 10));
        ((JPanel) d.getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField nome = criarInputEstilizado();
        nome.setText(desafio.getNome());
        JTextField desc = criarInputEstilizado();
        desc.setText(desafio.getDescricao());
        JTextField ini = criarInputEstilizado();
        ini.setText(desafio.getDataInicio().toString());
        JTextField fim = criarInputEstilizado();
        fim.setText(desafio.getDataFim().toString());

        d.add(criarLabel("Nome:"));
        d.add(nome);
        d.add(criarLabel("Descrição:"));
        d.add(desc);
        d.add(criarLabel("Início (aaaa-mm-dd):"));
        d.add(ini);
        d.add(criarLabel("Fim (aaaa-mm-dd):"));
        d.add(fim);

        JButton btn = new JButton("Salvar Alterações");
        estilizarBotao(btn, new Color(74, 255, 86), Color.BLACK);

        btn.addActionListener(e -> {
            try {
                TelaComputador.controladorDesafio.atualizarDesafio(
                        desafio.getIdDesafio(),
                        nome.getText(), desc.getText(),
                        LocalDate.parse(ini.getText()), LocalDate.parse(fim.getText()));
                JOptionPane.showMessageDialog(d, "Atualizado com sucesso!");
                d.dispose();
                GerenciadorTelas.getInstance().carregarTela(new TelaDesafios().criarPainel());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Erro: " + ex.getMessage());
            }
        });
        d.add(new JLabel(""));
        d.add(btn);
        d.setSize(450, 300);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

    // --- Métodos Auxiliares Locais ---
    private static void estilizarBotao(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }

    private static JTextField criarInputEstilizado() {
        JTextField campo = new JTextField();
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(new Color(74, 255, 86));
        campo.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        return campo;
    }

    private static JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }
}