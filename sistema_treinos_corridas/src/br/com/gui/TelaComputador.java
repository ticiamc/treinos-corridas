package br.com.gui;

import br.com.dados.IRepositorioDesafio;
import br.com.dados.RepositorioClientes;
import br.com.dados.RepositorioDesafio;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorDesafio;
import br.com.negocio.ControladorMeta;
import br.com.negocio.ControladorTreino;
import br.com.negocio.treinos.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

public class TelaComputador {

    // --- CONTROLADORES E DADOS ---
    public static ControladorCliente controladorCliente;
    public static ControladorTreino controladorTreino;
    public static ControladorMeta controladorMeta;
    public static ControladorDesafio controladorDesafio;

    // CPF fixo para facilitar testes (fallback)
    public static final String CPF_LOGADO = "000.000.000-00";

    // --- PALETA DE CORES ---
    private static final Color COR_FUNDO = new Color(30, 30, 30);
    private static final Color COR_PAINEL_LATERAL = new Color(20, 20, 20);
    private static final Color COR_DESTAQUE = new Color(74, 255, 86); // Verde Neon
    private static final Color COR_BOTAO_FUNDO = new Color(50, 50, 50);
    private static final Color COR_TEXTO = new Color(240, 240, 240);

    // Fontes
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 14);

    public static void main(String[] args) {
        // Inicialização dos Repositórios
        RepositorioClientes repoClientes = new RepositorioClientes();
        IRepositorioDesafio repoDesafio = new RepositorioDesafio();

        // Inicialização dos Controladores
        controladorCliente = new ControladorCliente(repoClientes);
        controladorTreino = new ControladorTreino(repoClientes);
        controladorMeta = new ControladorMeta(repoClientes);
        controladorDesafio = new ControladorDesafio(repoDesafio, repoClientes);

        // Usuário de teste inicial
        if (controladorCliente.buscarCliente(CPF_LOGADO) == null) {
            Usuario userTeste = new Usuario("Usuário Teste", 25, 70, 1.75, "teste@email.com", CPF_LOGADO);
            controladorCliente.cadastrarCliente(userTeste);
        }

        SwingUtilities.invokeLater(() -> criarUI());
    }

    public static void criarUI() {
        JFrame janela = new JFrame("Iron Track - Performance Dashboard");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.getContentPane().setBackground(COR_FUNDO);
        janela.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // --- PAINEL LATERAL (Esquerda) ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 4;
        gbc.weightx = 0.3; gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel painelLateral = new JPanel(new BorderLayout());
        painelLateral.setBackground(COR_PAINEL_LATERAL);
        painelLateral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_DESTAQUE, 2),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitulo = new JLabel("<html><center>IRON<br>TRACK</center></html>", SwingConstants.CENTER);
        lblTitulo.setFont(FONTE_TITULO);
        lblTitulo.setForeground(COR_DESTAQUE);

        JLabel lblSubtitulo = new JLabel("<html><center><br>Performance &<br>Evolução</center></html>", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);

        painelLateral.add(lblTitulo, BorderLayout.CENTER);
        painelLateral.add(lblSubtitulo, BorderLayout.SOUTH);

        janela.add(painelLateral, gbc);

        // --- GRID DE BOTÕES (Direita) ---
        gbc.gridheight = 1; gbc.weightx = 0.35;
        gbc.insets = new Insets(10, 5, 10, 5);

        // Botões Linha 1
        gbc.gridx = 1;
        JButton btnCadastrar = criarBotaoEstilizado("Novo Aluno");
        btnCadastrar.addActionListener(e -> abrirTelaCadastroUsuario());
        janela.add(btnCadastrar, configuracaoGrid(1, 0));

        JButton btnNotificacoes = criarBotaoEstilizado("Notificações");
        btnNotificacoes.addActionListener(e -> abrirTelaNotificacoes());
        janela.add(btnNotificacoes, configuracaoGrid(1, 1));

        JButton btnDesafios = criarBotaoEstilizado("Desafios");
        btnDesafios.addActionListener(e -> abrirTelaDesafios());
        janela.add(btnDesafios, configuracaoGrid(1, 2));

        JButton btnMetas = criarBotaoEstilizado("Metas");
        btnMetas.addActionListener(e -> abrirTelaMetas());
        janela.add(btnMetas, configuracaoGrid(1, 3));

        // Botões Linha 2
        gbc.gridx = 2;
        janela.add(criarBotaoEstilizado("Perfil"), configuracaoGrid(2, 0));
        janela.add(criarBotaoEstilizado("Relatórios"), configuracaoGrid(2, 1));
        janela.add(criarBotaoEstilizado("Planos"), configuracaoGrid(2, 2));

        // Botão Destaque: TREINOS
        JButton btnTreinos = criarBotaoEstilizado("REGISTRAR TREINO");
        btnTreinos.setForeground(Color.BLACK);
        btnTreinos.setBackground(COR_DESTAQUE);
        btnTreinos.addActionListener(e -> abrirTelaCadastroTreino());
        
        // Efeito hover manual para o botão destaque
        btnTreinos.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnTreinos.setBackground(Color.WHITE); }
            public void mouseExited(MouseEvent e) { btnTreinos.setBackground(COR_DESTAQUE); }
        });

        janela.add(btnTreinos, configuracaoGrid(2, 3));

        janela.setSize(1000, 650);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    // ========================================================================
    // TELA: METAS
    // ========================================================================
    public static void abrirTelaMetas() {
        Usuario u = identificarUsuario();
        if (u == null) return;

        JFrame tela = new JFrame("Metas de " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        // Tabela
        String[] colunas = {"Descricao", "Tipo", "Alvo", "Prazo", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        try {
            List<Meta> metas = controladorMeta.listarMetas(u.getCpf());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Meta m : metas) {
                String alvoFmt = "";
                if(m.getTipo() == TipoMeta.DISTANCIA) alvoFmt = (m.getValorAlvo()/1000) + " km";
                else if(m.getTipo() == TipoMeta.TEMPO) alvoFmt = m.getValorAlvo() + " min";
                else alvoFmt = m.getValorAlvo() + " kcal";

                model.addRow(new Object[]{
                    m.getDescricao(), m.getTipo(), alvoFmt, m.getDataFim().format(fmt), m.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar metas.");
        }

        JTable tabela = new JTable(model);
        estilizarTabela(tabela);
        tela.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botão Nova Meta
        JButton btnNova = criarBotaoEstilizado("Nova Meta");
        btnNova.setBackground(COR_DESTAQUE);
        btnNova.setForeground(Color.BLACK);

        Usuario finalU = u;
        btnNova.addActionListener(e -> {
            JTextField txtDesc = new JTextField();
            JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"DISTANCIA (m)", "TEMPO (min)", "CALORIAS (kcal)"});
            JTextField txtValor = new JTextField();
            JTextField txtData = new JTextField("dd/MM/yyyy");

            Object[] msg = {"Descrição:", txtDesc, "Tipo:", cmbTipo, "Valor Alvo:", txtValor, "Prazo:", txtData};

            if (JOptionPane.showConfirmDialog(null, msg, "Definir Meta", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String desc = txtDesc.getText();
                    double val = Double.parseDouble(txtValor.getText().replace(",", "."));
                    LocalDate data = LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    
                    TipoMeta tm = TipoMeta.DISTANCIA;
                    if(cmbTipo.getSelectedIndex() == 1) tm = TipoMeta.TEMPO;
                    if(cmbTipo.getSelectedIndex() == 2) tm = TipoMeta.CALORIAS;

                    controladorMeta.cadastrarMeta(finalU.getCpf(), desc, tm, val, data);
                    tela.dispose(); abrirTelaMetas(); // Refresh
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage());
                }
            }
        });

        JPanel pnlSul = new JPanel();
        pnlSul.setBackground(COR_FUNDO);
        pnlSul.add(btnNova);
        tela.add(pnlSul, BorderLayout.SOUTH);

        tela.setSize(600, 450);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // ========================================================================
    // TELA: DESAFIOS
    // ========================================================================
    public static void abrirTelaDesafios() {
        JFrame tela = new JFrame("Desafios da Comunidade");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        String[] colunas = {"ID", "Nome", "Início", "Fim", "Participantes"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Desafio> lista = controladorDesafio.listarDesafios();
        
        for (Desafio d : lista) {
            model.addRow(new Object[]{
                d.getIdDesafio(), d.getNome(), 
                d.getDataInicio().format(fmt), d.getDataFim().format(fmt),
                d.getParticipacoes().size()
            });
        }

        JTable tabela = new JTable(model);
        estilizarTabela(tabela);
        tela.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel pnlBotoes = new JPanel();
        pnlBotoes.setBackground(COR_FUNDO);
        
        JButton btnCriar = criarBotaoEstilizado("Criar Desafio");
        JButton btnEntrar = criarBotaoEstilizado("Participar");
        JButton btnRank = criarBotaoEstilizado("Ranking");
        btnRank.setBackground(COR_DESTAQUE);
        btnRank.setForeground(Color.BLACK);

        pnlBotoes.add(btnCriar);
        pnlBotoes.add(btnEntrar);
        pnlBotoes.add(btnRank);
        tela.add(pnlBotoes, BorderLayout.SOUTH);

        // Ação Criar
        btnCriar.addActionListener(e -> {
            JTextField txtNome = new JTextField();
            JTextField txtDesc = new JTextField();
            JTextField txtIni = new JTextField();
            JTextField txtFim = new JTextField();
            Object[] msg = {"Nome:", txtNome, "Descrição:", txtDesc, "Início (dd/MM/yyyy):", txtIni, "Fim (dd/MM/yyyy):", txtFim};
            
            if (JOptionPane.showConfirmDialog(null, msg, "Novo Desafio", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    LocalDate ini = LocalDate.parse(txtIni.getText(), fmt);
                    LocalDate fim = LocalDate.parse(txtFim.getText(), fmt);
                    controladorDesafio.cadastrarDesafio(txtNome.getText(), txtDesc.getText(), ini, fim);
                    tela.dispose(); abrirTelaDesafios();
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(tela, "Dados inválidos: " + ex.getMessage());
                }
            }
        });

        // Ação Participar
        btnEntrar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if(row < 0) { JOptionPane.showMessageDialog(tela, "Selecione um desafio."); return; }
            
            int id = (int) tabela.getValueAt(row, 0);
            String cpf = JOptionPane.showInputDialog("Digite seu CPF para entrar:");
            if(cpf != null && !cpf.isEmpty()) {
                try {
                    controladorDesafio.participarDesafio(id, cpf);
                    JOptionPane.showMessageDialog(tela, "Inscrição realizada!");
                    tela.dispose(); abrirTelaDesafios();
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage());
                }
            }
        });

        // Ação Ranking
        btnRank.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if(row < 0) { JOptionPane.showMessageDialog(tela, "Selecione um desafio."); return; }
            int id = (int) tabela.getValueAt(row, 0);
            try {
                Desafio d = controladorDesafio.buscarDesafio(id);
                abrirRanking(d);
            } catch(Exception ex) { ex.printStackTrace(); }
        });

        tela.setSize(700, 500);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    private static void abrirRanking(Desafio d) {
        JFrame frame = new JFrame("Ranking: " + d.getNome());
        frame.getContentPane().setBackground(COR_PAINEL_LATERAL);
        
        List<ParticipacaoDesafio> parts = d.getParticipacoes();
        // Ordena por progresso (maior para menor)
        Collections.sort(parts, (p1, p2) -> Double.compare(p2.getProgresso(), p1.getProgresso()));

        String[] cols = {"Posição", "Atleta", "Progresso (km)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        
        int pos = 1;
        for(ParticipacaoDesafio p : parts) {
            model.addRow(new Object[]{pos + "º", p.getUsuario().getNome(), String.format("%.2f km", p.getProgresso()/1000)});
            pos++;
        }

        JTable tb = new JTable(model);
        estilizarTabela(tb);
        frame.add(new JScrollPane(tb));
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ========================================================================
    // TELAS ANTIGAS (MANTIDAS)
    // ========================================================================
    
    public static void abrirTelaCadastroUsuario() {
        JFrame tela = new JFrame("Novo Atleta");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new GridLayout(7, 2, 10, 10));
        ((JComponent)tela.getContentPane()).setBorder(new EmptyBorder(20,20,20,20));

        tela.add(criarLabelSimples("Nome:"));      JTextField txtNome = criarInputEstilizado(""); tela.add(txtNome);
        tela.add(criarLabelSimples("CPF:"));       JTextField txtCpf = criarInputEstilizado(""); tela.add(txtCpf);
        tela.add(criarLabelSimples("Email:"));     JTextField txtEmail = criarInputEstilizado(""); tela.add(txtEmail);
        tela.add(criarLabelSimples("Idade:"));     JTextField txtIdade = criarInputEstilizado(""); tela.add(txtIdade);
        tela.add(criarLabelSimples("Peso (kg):")); JTextField txtPeso = criarInputEstilizado(""); tela.add(txtPeso);
        tela.add(criarLabelSimples("Altura (m):"));JTextField txtAltura = criarInputEstilizado(""); tela.add(txtAltura);

        JButton btnSalvar = criarBotaoEstilizado("SALVAR");
        btnSalvar.setBackground(COR_DESTAQUE);
        btnSalvar.setForeground(Color.BLACK);

        btnSalvar.addActionListener(e -> {
            try {
                Usuario u = new Usuario(
                    txtNome.getText(), Integer.parseInt(txtIdade.getText()),
                    Double.parseDouble(txtPeso.getText().replace(",", ".")),
                    Double.parseDouble(txtAltura.getText().replace(",", ".")),
                    txtEmail.getText(), txtCpf.getText()
                );
                controladorCliente.cadastrarCliente(u);
                JOptionPane.showMessageDialog(tela, "Sucesso!");
                tela.dispose();
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro nos dados: " + ex.getMessage());
            }
        });

        tela.add(new JLabel("")); tela.add(btnSalvar);
        tela.setSize(400, 500);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    public static void abrirTelaCadastroTreino() {
        JFrame tela = new JFrame("Registrar Treino");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(COR_FUNDO);
        centro.setBorder(new EmptyBorder(20,40,20,40));

        JTextField txtCpf = criarInputEstilizado("CPF do Aluno");
        JTextField txtNome = criarInputEstilizado("Nome do Treino (ex: Corrida Matinal)");
        JTextField txtData = criarInputEstilizado("Data (dd/MM/yyyy)");
        JTextField txtDur = criarInputEstilizado("Duração (min)");
        
        centro.add(txtCpf); centro.add(Box.createVerticalStrut(10));
        centro.add(txtNome); centro.add(Box.createVerticalStrut(10));
        centro.add(txtData); centro.add(Box.createVerticalStrut(10));
        centro.add(txtDur); centro.add(Box.createVerticalStrut(20));

        JPanel pnlTipo = new JPanel();
        pnlTipo.setBackground(COR_FUNDO);
        pnlTipo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COR_DESTAQUE), "Detalhes", 0, 0, FONTE_BOTAO, COR_DESTAQUE));
        centro.add(pnlTipo);
        
        tela.add(centro, BorderLayout.CENTER);

        // Botões Tipo
        JPanel pnlSul = new JPanel();
        pnlSul.setBackground(COR_FUNDO);
        JButton btnCorrida = criarBotaoEstilizado("Corrida");
        JButton btnInterv = criarBotaoEstilizado("Intervalado");
        JButton btnSalvar = criarBotaoEstilizado("SALVAR");
        btnSalvar.setBackground(COR_DESTAQUE);
        btnSalvar.setForeground(Color.BLACK);

        pnlSul.add(btnCorrida); pnlSul.add(btnInterv); pnlSul.add(btnSalvar);
        tela.add(pnlSul, BorderLayout.SOUTH);

        final String[] tipo = {null};
        final JTextField[] extras = {new JTextField(), new JTextField(), new JTextField()};

        btnCorrida.addActionListener(e -> {
            tipo[0] = "Corrida";
            pnlTipo.removeAll();
            pnlTipo.add(criarLabelSimples("Distância (m): "));
            extras[0] = criarInputEstilizado("0");
            pnlTipo.add(extras[0]);
            pnlTipo.revalidate(); pnlTipo.repaint();
        });

        btnInterv.addActionListener(e -> {
            tipo[0] = "Intervalado";
            pnlTipo.removeAll();
            pnlTipo.add(criarLabelSimples("Séries: "));
            extras[1] = criarInputEstilizado("0");
            pnlTipo.add(extras[1]);
            pnlTipo.add(criarLabelSimples("Descanso (s): "));
            extras[2] = criarInputEstilizado("0");
            pnlTipo.add(extras[2]);
            pnlTipo.revalidate(); pnlTipo.repaint();
        });

        btnSalvar.addActionListener(e -> {
            try {
                if(tipo[0] == null) throw new Exception("Selecione o tipo.");
                LocalDate dt = LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int dur = Integer.parseInt(txtDur.getText()) * 60;
                
                double dist = 0; int ser = 0, desc = 0;
                if(tipo[0].equals("Corrida")) dist = Double.parseDouble(extras[0].getText());
                else {
                    ser = Integer.parseInt(extras[1].getText());
                    desc = Integer.parseInt(extras[2].getText());
                }

                controladorTreino.cadastrarTreino(txtCpf.getText(), tipo[0], dt, dur, txtNome.getText(), dist, ser, desc);
                JOptionPane.showMessageDialog(tela, "Treino registrado!");
                tela.dispose();
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage());
            }
        });

        tela.setSize(500, 600);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    public static void abrirTelaNotificacoes() {
        Usuario u = identificarUsuario();
        if(u == null) return;

        JFrame tela = new JFrame("Notificações");
        tela.getContentPane().setBackground(COR_FUNDO);
        DefaultListModel<String> model = new DefaultListModel<>();
        
        for(Notificacao n : u.getNotificacoes()) {
            model.addElement((n.isLida() ? "[Lida] " : "[NOVA] ") + n.toString());
        }
        if(model.isEmpty()) model.addElement("Sem notificações.");

        JList<String> list = new JList<>(model);
        list.setBackground(new Color(50,50,50));
        list.setForeground(Color.WHITE);
        
        tela.add(new JScrollPane(list));
        JButton btnLimpar = criarBotaoEstilizado("Marcar lidas / Limpar");
        btnLimpar.addActionListener(e -> {
            u.getNotificacoes().removeIf(Notificacao::isLida);
            u.getNotificacoes().forEach(n -> n.setLida(true));
            JOptionPane.showMessageDialog(tela, "Limpo!");
            tela.dispose();
        });
        
        tela.add(btnLimpar, BorderLayout.SOUTH);
        tela.setSize(400, 300);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // --- MÉTODOS AUXILIARES E ESTILOS ---

    private static Usuario identificarUsuario() {
        Usuario u = controladorCliente.buscarCliente(CPF_LOGADO);
        if(u == null) {
            String cpf = JOptionPane.showInputDialog("Usuário padrão não encontrado. Digite CPF:");
            if(cpf != null) u = controladorCliente.buscarCliente(cpf);
        }
        if(u == null) JOptionPane.showMessageDialog(null, "Usuário não encontrado.");
        return u;
    }

    private static void estilizarTabela(JTable tabela) {
        tabela.setBackground(new Color(45, 45, 45));
        tabela.setForeground(Color.WHITE);
        tabela.setGridColor(new Color(60, 60, 60));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setRowHeight(30);
        tabela.setSelectionBackground(COR_DESTAQUE);
        tabela.setSelectionForeground(Color.BLACK);
        
        javax.swing.table.JTableHeader header = tabela.getTableHeader();
        header.setBackground(COR_PAINEL_LATERAL);
        header.setForeground(COR_DESTAQUE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(COR_DESTAQUE));
    }

    private static JButton criarBotaoEstilizado(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setForeground(COR_TEXTO);
        btn.setBackground(COR_BOTAO_FUNDO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if(!btn.getBackground().equals(COR_DESTAQUE)) btn.setBackground(new Color(70, 70, 70)); }
            public void mouseExited(MouseEvent e) { if(!btn.getBackground().equals(COR_DESTAQUE)) btn.setBackground(COR_BOTAO_FUNDO); }
        });
        return btn;
    }

    private static JTextField criarInputEstilizado(String ph) {
        JTextField campo = new JTextField();
        campo.setBorder(BorderFactory.createTitledBorder(null, ph, 0, 0, new Font("Segoe UI", Font.PLAIN, 12), Color.GRAY));
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(COR_DESTAQUE);
        return campo;
    }
    
    private static GridBagConstraints configuracaoGrid(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x; gbc.gridy = y;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 5, 10, 10);
        return gbc;
    }
    
    private static JLabel criarLabelSimples(String txt) {
        JLabel lbl = new JLabel(txt);
        lbl.setForeground(Color.WHITE);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }
}