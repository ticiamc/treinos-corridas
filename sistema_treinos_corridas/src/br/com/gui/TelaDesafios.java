package br.com.gui;
import br.com.negocio.ControladorDesafio;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Desafio;
import br.com.negocio.treinos.Relatorio;
import br.com.negocio.treinos.Usuario;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
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
        painel.setBackground(new Color(30,30,30));
        
        JLabel titulo = new JLabel("Central de Desafios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(74, 255, 86));
        painel.add(titulo, BorderLayout.NORTH);
        
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Todos os Desafios", criarPainelLista(false));
        if(usuarioLogado != null) abas.addTab("Meus Desafios", criarPainelLista(true));
        
        JPanel rodape = new JPanel();
        rodape.setBackground(new Color(30,30,30));
        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario()));
        rodape.add(btnVoltar);
        
        painel.add(abas, BorderLayout.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);
        return painel;
    }
    
    private JPanel criarPainelLista(boolean apenasMeus) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(40,40,40));
        
        DefaultListModel<Desafio> model = new DefaultListModel<>();
        List<Desafio> lista = controladorDesafio.listarDesafios();
        
        for(Desafio d : lista) {
            boolean participa = d.getParticipacoes().stream().anyMatch(par -> par.getUsuario().getCpf().equals(usuarioLogado.getCpf()));
            if (apenasMeus && participa) model.addElement(d);
            else if (!apenasMeus) model.addElement(d);
        }
        
        JList<Desafio> jList = new JList<>(model);
        jList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Desafio d = (Desafio) value;
                String txt = "ID " + d.getIdDesafio() + ": " + d.getNome() + " (" + d.getDataInicio() + " a " + d.getDataFim() + ")";
                return super.getListCellRendererComponent(list, txt, index, isSelected, cellHasFocus);
            }
        });
        
        p.add(new JScrollPane(jList), BorderLayout.CENTER);
        JPanel botoes = new JPanel();
        
        if(!apenasMeus) {
            JButton btnEntrar = new JButton("Participar do Desafio");
            btnEntrar.addActionListener(e -> {
                Desafio sel = jList.getSelectedValue();
                if(sel != null) {
                    try {
                        controladorDesafio.participarDesafio(sel.getIdDesafio(), usuarioLogado.getCpf());
                        JOptionPane.showMessageDialog(p, "Você entrou no desafio!");
                    } catch(Exception ex) { JOptionPane.showMessageDialog(p, "Erro: " + ex.getMessage()); }
                }
            });
            botoes.add(btnEntrar);
        } else {
            JButton btnRanking = new JButton("Ver Ranking");
            btnRanking.addActionListener(e -> {
                Desafio sel = jList.getSelectedValue();
                if(sel != null) JOptionPane.showMessageDialog(p, new JTextArea(Relatorio.gerarRankingDesafioTexto(sel)));
            });
            botoes.add(btnRanking);
        }
        p.add(botoes, BorderLayout.SOUTH);
        return p;
    }
    
    public static void abrirDialogoCriarDesafio() {
        JDialog d = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Criar Novo Desafio", true);
        d.setLayout(new GridLayout(5,2));
        d.setSize(400,250);
        d.setLocationRelativeTo(null);
        
        JTextField nome = new JTextField();
        JTextField desc = new JTextField();
        JTextField ini = new JTextField(LocalDate.now().toString());
        JTextField fim = new JTextField(LocalDate.now().plusMonths(1).toString());
        
        d.add(new JLabel(" Nome:")); d.add(nome);
        d.add(new JLabel(" Descrição:")); d.add(desc);
        d.add(new JLabel(" Início (aaaa-mm-dd):")); d.add(ini);
        d.add(new JLabel(" Fim (aaaa-mm-dd):")); d.add(fim);
        
        JButton btn = new JButton("Criar");
        btn.addActionListener(e -> {
            try {
                TelaComputador.controladorDesafio.cadastrarDesafio(nome.getText(), desc.getText(), LocalDate.parse(ini.getText()), LocalDate.parse(fim.getText()));
                JOptionPane.showMessageDialog(d, "Desafio criado!");
                d.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(d, "Erro: " + ex.getMessage()); }
        });
        d.add(btn);
        d.setVisible(true);
    }
}