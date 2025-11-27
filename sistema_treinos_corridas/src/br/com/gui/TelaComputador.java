package br.com.gui;

import br.com.dados.*;
import br.com.negocio.*;
import br.com.negocio.treinos.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class TelaComputador {

    // Controladores Estáticos (Singleton pattern simplificado)
    public static ControladorCliente controladorCliente;
    public static ControladorTreino controladorTreino;
    public static ControladorMeta controladorMeta;
    public static ControladorDesafio controladorDesafio;
    public static ControladorPlanoTreino controladorPlanoTreino;

    public static final String CPF_LOGADO = "000.000.000-00"; 
    
    // Cores e Estilos
    private static final Color COR_FUNDO = new Color(30, 30, 30);
    private static final Color COR_DESTAQUE = new Color(74, 255, 86);
    private static final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 14);

    // Bloco de inicialização dos dados
    static {
        if (controladorCliente == null) {
            RepositorioClientes rc = new RepositorioClientes();
            IRepositorioDesafio rd = new RepositorioDesafio();
            
            controladorCliente = new ControladorCliente(rc);
            controladorTreino = new ControladorTreino(rc);
            controladorMeta = new ControladorMeta(rc);
            controladorDesafio = new ControladorDesafio(rd, rc);
            controladorPlanoTreino = new ControladorPlanoTreino(rc);

            // Dados Mock
            if(controladorCliente.buscarCliente(CPF_LOGADO) == null) {
                Usuario u = new Usuario("Admin Teste", 30, 80, 1.80, "admin@iron.com", CPF_LOGADO);
                controladorCliente.cadastrarCliente(u);
            }
        }
    }

    public TelaComputador() {}

    // --- PAINEL DO ADMIN (Retorna JPanel) ---
    public JPanel criarPainelAdmin() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Menu Lateral
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(20, 20, 20));
        menu.setPreferredSize(new Dimension(220, 0));
        menu.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel logo = new JLabel("ADMINISTRADOR");
        logo.setForeground(COR_DESTAQUE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(logo);
        menu.add(Box.createVerticalStrut(30));

        // Botões
        adicionarBotao(menu, "Novo Atleta", e -> abrirTelaCadastroUsuario());
        adicionarBotao(menu, "Registrar Treino", e -> abrirTelaCadastroTreino(null));
        adicionarBotao(menu, "Planos de Treino", e -> abrirTelaPlanos(null));
        adicionarBotao(menu, "Desafios", e -> abrirTelaDesafios(null));
        adicionarBotao(menu, "Relatórios Gerais", e -> abrirTelaRelatorios(null));
        
        menu.add(Box.createVerticalGlue());
        
        JButton btnSair = new JButton("SAIR");
        btnSair.setBackground(new Color(200, 50, 50));
        btnSair.setForeground(Color.WHITE);
        btnSair.setMaximumSize(new Dimension(200, 40));
        btnSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSair.addActionListener(e -> {
            SessaoUsuario.getInstance().logout();
            GerenciadorTelas.getInstance().carregarTela(new TelaLogin().criarPainelLogin());
        });
        menu.add(btnSair);

        // Centro (Dashboard simples)
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(COR_FUNDO);
        JLabel bemVindo = new JLabel("Painel de Gestão");
        bemVindo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        bemVindo.setForeground(Color.DARK_GRAY);
        centro.add(bemVindo);

        painel.add(menu, BorderLayout.WEST);
        painel.add(centro, BorderLayout.CENTER);
        return painel;
    }

    // --- MÉTODOS DE JANELAS AUXILIARES (POPUPS) ---

    // 1. CADASTRO USUÁRIO (REQ01)
    public static void abrirTelaCadastroUsuario() {
        JFrame f = criarFrameBase("Novo Atleta", 400, 500);
        f.setLayout(new GridLayout(7, 2, 10, 10));
        
        JTextField tNome = new JTextField(), tCpf = new JTextField(), tEmail = new JTextField();
        JTextField tIdade = new JTextField(), tPeso = new JTextField(), tAlt = new JTextField();
        
        f.add(new JLabel("Nome:")); f.add(tNome);
        f.add(new JLabel("CPF:")); f.add(tCpf);
        f.add(new JLabel("Email:")); f.add(tEmail);
        f.add(new JLabel("Idade:")); f.add(tIdade);
        f.add(new JLabel("Peso:")); f.add(tPeso);
        f.add(new JLabel("Altura:")); f.add(tAlt);
        
        JButton btn = new JButton("SALVAR");
        btn.setBackground(COR_DESTAQUE);
        btn.addActionListener(e -> {
            try {
                Usuario u = new Usuario(tNome.getText(), Integer.parseInt(tIdade.getText()),
                        Double.parseDouble(tPeso.getText()), Double.parseDouble(tAlt.getText()),
                        tEmail.getText(), tCpf.getText());
                controladorCliente.cadastrarCliente(u);
                JOptionPane.showMessageDialog(f, "Sucesso!"); f.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(f, "Erro: " + ex.getMessage()); }
        });
        f.add(new JLabel("")); f.add(btn);
        f.setVisible(true);
    }

    // 2. CADASTRO TREINO (REQ04, REQ05)
    public static void abrirTelaCadastroTreino(Usuario uLogado) {
        JFrame f = criarFrameBase("Registrar Treino", 500, 600);
        
        JTextField tCpf = new JTextField();
        if(uLogado != null) { tCpf.setText(uLogado.getCpf()); tCpf.setEditable(false); }
        
        JTextField tNome = new JTextField(), tData = new JTextField("dd/MM/yyyy"), tDur = new JTextField();
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Corrida", "Intervalado"});
        
        JPanel extra = new JPanel(new GridLayout(3, 2));
        JTextField tDist = new JTextField("0"), tSeries = new JTextField("0"), tDesc = new JTextField("0");
        
        extra.add(new JLabel("Distância (m):")); extra.add(tDist);
        extra.add(new JLabel("Séries:")); extra.add(tSeries);
        extra.add(new JLabel("Descanso (s):")); extra.add(tDesc);

        JButton btn = new JButton("REGISTRAR");
        btn.setBackground(COR_DESTAQUE);
        btn.addActionListener(e -> {
            try {
                String cpf = uLogado != null ? uLogado.getCpf() : tCpf.getText();
                LocalDate dt = LocalDate.parse(tData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int dur = Integer.parseInt(tDur.getText()) * 60;
                String tipo = (String) cbTipo.getSelectedItem();
                
                controladorTreino.cadastrarTreino(cpf, tipo, dt, dur, tNome.getText(), 
                    Double.parseDouble(tDist.getText()), Integer.parseInt(tSeries.getText()), Integer.parseInt(tDesc.getText()));
                
                JOptionPane.showMessageDialog(f, "Treino Salvo!"); f.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(f, "Erro: " + ex.getMessage()); }
        });

        f.setLayout(new GridLayout(8, 1));
        f.add(new JLabel("CPF Aluno:")); f.add(tCpf);
        f.add(new JLabel("Nome Treino:")); f.add(tNome);
        f.add(new JLabel("Data:")); f.add(tData);
        f.add(new JLabel("Duração (min):")); f.add(tDur);
        f.add(new JLabel("Tipo:")); f.add(cbTipo);
        f.add(extra);
        f.add(btn);
        f.setVisible(true);
    }

    // 3. PLANOS (REQ10, REQ11, REQ12)
    public static void abrirTelaPlanos(Usuario u) {
        Usuario alvo = (u != null) ? u : identificarUsuario();
        if(alvo == null) return;

        JFrame f = criarFrameBase("Planos de " + alvo.getNome(), 600, 400);
        String[] col = {"ID", "Nome", "Inicio", "Fim"};
        DefaultTableModel model = new DefaultTableModel(col, 0);
        
        try {
            for(PlanoTreino p : controladorPlanoTreino.listarPlanos(alvo.getCpf()))
                model.addRow(new Object[]{p.getIdPlano(), p.getNome(), p.getDataInicio(), p.getDataFim()});
        } catch(Exception e){}

        JTable tb = new JTable(model);
        f.add(new JScrollPane(tb), BorderLayout.CENTER);

        JButton btnAdd = new JButton("Criar Plano");
        btnAdd.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog("Nome do Plano:");
            if(nome != null) {
                try {
                    controladorPlanoTreino.cadastrarPlano(alvo.getCpf(), nome, LocalDate.now(), LocalDate.now().plusMonths(1));
                    f.dispose(); abrirTelaPlanos(alvo);
                } catch(Exception ex) { JOptionPane.showMessageDialog(f, "Erro: " + ex.getMessage()); }
            }
        });
        
        // Botão para adicionar treino ao plano selecionado
        JButton btnAddTreino = new JButton("Add Treino ao Plano");
        btnAddTreino.addActionListener(e -> {
            int row = tb.getSelectedRow();
            if(row >= 0) {
                String idTreino = JOptionPane.showInputDialog("ID do Treino para adicionar:");
                try {
                    controladorPlanoTreino.adicionarTreinoPlano(alvo.getCpf(), (int)tb.getValueAt(row, 0), Integer.parseInt(idTreino));
                    JOptionPane.showMessageDialog(f, "Adicionado!");
                } catch(Exception ex){ JOptionPane.showMessageDialog(f, "Erro: " + ex.getMessage());}
            }
        });

        JPanel p = new JPanel(); p.add(btnAdd); p.add(btnAddTreino);
        f.add(p, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    // 4. DESAFIOS (REQ13, REQ14, REQ15)
    public static void abrirTelaDesafios(Usuario u) {
        JFrame f = criarFrameBase("Desafios", 700, 500);
        String[] col = {"ID", "Nome", "Inicio", "Fim", "Participantes"};
        DefaultTableModel model = new DefaultTableModel(col, 0);
        
        for(Desafio d : controladorDesafio.listarDesafios())
            model.addRow(new Object[]{d.getIdDesafio(), d.getNome(), d.getDataInicio(), d.getDataFim(), d.getParticipacoes().size()});

        JTable tb = new JTable(model);
        f.add(new JScrollPane(tb), BorderLayout.CENTER);

        JPanel p = new JPanel();
        
        // Botão Criar (Só Admin ou geral)
        if(u == null) {
            JButton btnCriar = new JButton("Criar Desafio");
            btnCriar.addActionListener(e -> {
                try {
                    controladorDesafio.cadastrarDesafio(JOptionPane.showInputDialog("Nome"), "Geral", LocalDate.now(), LocalDate.now().plusMonths(1));
                    f.dispose(); abrirTelaDesafios(null);
                } catch(Exception ex){}
            });
            p.add(btnCriar);
        }

        JButton btnEntrar = new JButton("Participar");
        btnEntrar.addActionListener(e -> {
            int row = tb.getSelectedRow();
            if(row >= 0) {
                Usuario part = (u != null) ? u : identificarUsuario();
                if(part != null) {
                    try {
                        controladorDesafio.participarDesafio((int)tb.getValueAt(row, 0), part.getCpf());
                        JOptionPane.showMessageDialog(f, "Inscrito!");
                    } catch(Exception ex){ JOptionPane.showMessageDialog(f, ex.getMessage()); }
                }
            }
        });
        
        JButton btnRank = new JButton("Ver Ranking");
        btnRank.addActionListener(e -> {
            int row = tb.getSelectedRow();
            if(row >= 0) {
                try {
                    Desafio d = controladorDesafio.buscarDesafio((int)tb.getValueAt(row, 0));
                    mostrarRanking(d);
                } catch(Exception ex){}
            }
        });

        p.add(btnEntrar); p.add(btnRank);
        f.add(p, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    private static void mostrarRanking(Desafio d) {
        JFrame f = criarFrameBase("Ranking: " + d.getNome(), 400, 400);
        List<ParticipacaoDesafio> parts = d.getParticipacoes();
        // Ordena
        Collections.sort(parts, (p1, p2) -> Double.compare(p2.getProgresso(), p1.getProgresso()));
        
        DefaultTableModel m = new DefaultTableModel(new String[]{"Pos", "Nome", "Km"}, 0);
        int i=1;
        for(ParticipacaoDesafio p : parts) m.addRow(new Object[]{i++, p.getUsuario().getNome(), p.getProgresso()/1000});
        
        f.add(new JScrollPane(new JTable(m)));
        f.setVisible(true);
    }

    // 5. RELATÓRIOS E CSV (REQ16, REQ19)
    public static void abrirTelaRelatorios(Usuario u) {
        Usuario alvo = (u != null) ? u : identificarUsuario();
        if(alvo == null) return;

        JFrame f = criarFrameBase("Relatório: " + alvo.getNome(), 600, 500);
        JTextArea txt = new JTextArea();
        txt.setEditable(false);
        
        StringBuilder sb = new StringBuilder("HISTÓRICO DE TREINOS\n\n");
        for(Treino t : alvo.getTreinos()) sb.append(t.toString()).append("\n");
        txt.setText(sb.toString());
        
        f.add(new JScrollPane(txt), BorderLayout.CENTER);
        
        JButton btnCsv = new JButton("Exportar CSV");
        btnCsv.setBackground(COR_DESTAQUE);
        btnCsv.addActionListener(e -> {
            try {
                PrintWriter pw = new PrintWriter(new FileWriter("Relatorio_" + alvo.getNome() + ".csv"));
                pw.println("Data;Treino;Minutos;Kcal");
                for(Treino t : alvo.getTreinos()) 
                    pw.println(t.getDataExecucao() + ";" + t.getNomeTreino() + ";" + t.getDuracaoSegundos()/60 + ";" + t.calcularCaloriasQueimadas(alvo));
                pw.close();
                JOptionPane.showMessageDialog(f, "Arquivo CSV gerado na pasta do projeto!");
            } catch(Exception ex) { JOptionPane.showMessageDialog(f, "Erro: " + ex.getMessage()); }
        });
        
        f.add(btnCsv, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    // 6. METAS (REQ07)
    public static void abrirTelaMetas(Usuario u) {
        Usuario alvo = (u != null) ? u : identificarUsuario();
        if(alvo == null) return;
        
        JFrame f = criarFrameBase("Metas de " + alvo.getNome(), 500, 400);
        DefaultTableModel m = new DefaultTableModel(new String[]{"Descrição", "Alvo", "Status"}, 0);
        for(Meta meta : alvo.getMetas()) m.addRow(new Object[]{meta.getDescricao(), meta.getValorAlvo(), meta.getStatus()});
        
        f.add(new JScrollPane(new JTable(m)), BorderLayout.CENTER);
        
        JButton btnAdd = new JButton("Nova Meta");
        btnAdd.addActionListener(e -> {
            try {
                controladorMeta.cadastrarMeta(alvo.getCpf(), JOptionPane.showInputDialog("Descrição"), TipoMeta.DISTANCIA, 10000, LocalDate.now().plusMonths(1));
                f.dispose(); abrirTelaMetas(alvo);
            } catch(Exception ex){}
        });
        f.add(btnAdd, BorderLayout.SOUTH);
        f.setVisible(true);
    }
    
    // 7. NOTIFICAÇÕES (REQ20)
    public static void abrirTelaNotificacoes(Usuario u) {
        Usuario alvo = (u != null) ? u : identificarUsuario();
        if(alvo == null) return;
        JFrame f = criarFrameBase("Alertas", 400, 300);
        DefaultListModel<String> m = new DefaultListModel<>();
        for(Notificacao n : alvo.getNotificacoes()) m.addElement((n.isLida()?"[Lida] ":"[NOVA] ")+n.getMensagem());
        f.add(new JScrollPane(new JList<>(m)));
        f.setVisible(true);
    }

    // UTILITÁRIOS
    private static void adicionarBotao(JPanel p, String t, java.awt.event.ActionListener l) {
        JButton b = new JButton(t);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(200, 40));
        b.addActionListener(l);
        p.add(b);
        p.add(Box.createVerticalStrut(10));
    }

    private static Usuario identificarUsuario() {
        String cpf = JOptionPane.showInputDialog("Digite o CPF do Aluno:");
        if(cpf == null) return null;
        Usuario u = controladorCliente.buscarCliente(cpf);
        if(u == null) JOptionPane.showMessageDialog(null, "Não encontrado.");
        return u;
    }

    private static JFrame criarFrameBase(String titulo, int w, int h) {
        JFrame f = new JFrame(titulo);
        f.setSize(w, h);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return f;
    }
}