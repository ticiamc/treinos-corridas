package br.com.gui;

import br.com.dados.RepositorioClientes;
import br.com.dados.RepositorioDesafio;
import br.com.dados.IRepositorioDesafio;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorDesafio;
import br.com.negocio.ControladorMeta;
import br.com.negocio.ControladorPlanoTreino;
import br.com.negocio.ControladorTreino;
import br.com.negocio.treinos.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class TelaComputador {

    // --- CONTROLADORES E DADOS ---
    public static ControladorCliente controladorCliente;
    public static ControladorTreino controladorTreino;
    public static ControladorMeta controladorMeta;
    public static ControladorDesafio controladorDesafio;
    public static ControladorPlanoTreino controladorPlanoTreino;

    // CPF fixo para facilitar testes
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
        controladorPlanoTreino = new ControladorPlanoTreino(repoClientes);

        // Usuário de teste inicial
        if (controladorCliente.buscarCliente(CPF_LOGADO) == null) {
            Usuario userTeste = new Usuario("Usuário Teste", 25, 70, 1.75, "teste@email.com", CPF_LOGADO);
            controladorCliente.cadastrarCliente(userTeste);
        }

        // [CORREÇÃO] Chama o método com o nome certo agora
        SwingUtilities.invokeLater(() -> abrirTelaAdmin());
    }

    // [CORREÇÃO] Método renomeado de 'criarUI' para 'abrirTelaAdmin' para casar com TelaLogin
    public static void abrirTelaAdmin() {
        JFrame janela = new JFrame("Iron Track - Performance Dashboard (Admin)");
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

        JLabel lblSubtitulo = new JLabel("<html><center><br>Painel Administrativo</center></html>", SwingConstants.CENTER);
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
        
        JButton btnRelatorios = criarBotaoEstilizado("Relatórios");
        btnRelatorios.addActionListener(e -> abrirTelaRelatorios());
        janela.add(btnRelatorios, configuracaoGrid(2, 1));
        
        JButton btnPlanos = criarBotaoEstilizado("Planos");
        btnPlanos.addActionListener(e -> abrirTelaPlanos());
        janela.add(btnPlanos, configuracaoGrid(2, 2));

        // Botão Destaque: TREINOS
        JButton btnTreinos = criarBotaoEstilizado("REGISTRAR TREINO");
        btnTreinos.setForeground(Color.BLACK);
        btnTreinos.setBackground(COR_DESTAQUE);
        btnTreinos.addActionListener(e -> abrirTelaCadastroTreino());
        
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
    // TELA 1: PLANOS DE TREINO (ADMIN e USUÁRIO)
    // ========================================================================
    
    // Versão Admin: Pede CPF
    public static void abrirTelaPlanos() {
        Usuario u = identificarUsuario();
        if (u != null) abrirTelaPlanos(u);
    }

    // Versão Direta: Recebe Usuário
    public static void abrirTelaPlanos(Usuario u) {
        JFrame tela = new JFrame("Planos de Treino - " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        // Tabela de Planos
        String[] colunas = {"ID", "Nome do Plano", "Início", "Fim", "Qtd. Treinos"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        try {
            List<PlanoTreino> planos = controladorPlanoTreino.listarPlanos(u.getCpf());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (PlanoTreino p : planos) {
                model.addRow(new Object[]{
                    p.getIdPlano(), p.getNome(),
                    p.getDataInicio().format(fmt), p.getDataFim().format(fmt),
                    p.getTreinosDoPlano().size()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar planos: " + e.getMessage());
        }

        JTable tabela = new JTable(model);
        estilizarTabela(tabela);
        tela.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Painel de Botões
        JPanel pnlSul = new JPanel();
        pnlSul.setBackground(COR_FUNDO);
        
        JButton btnNovo = criarBotaoEstilizado("Criar Plano");
        JButton btnAddTreino = criarBotaoEstilizado("Adicionar Treino ao Plano");
        btnAddTreino.setBackground(COR_DESTAQUE);
        btnAddTreino.setForeground(Color.BLACK);

        pnlSul.add(btnNovo);
        pnlSul.add(btnAddTreino);
        tela.add(pnlSul, BorderLayout.SOUTH);

        // Ação: Criar Plano
        btnNovo.addActionListener(e -> {
            JTextField txtNome = new JTextField();
            JTextField txtIni = new JTextField();
            JTextField txtFim = new JTextField();
            Object[] msg = {"Nome do Plano:", txtNome, "Início (dd/MM/yyyy):", txtIni, "Fim (dd/MM/yyyy):", txtFim};

            if (JOptionPane.showConfirmDialog(null, msg, "Novo Plano", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    LocalDate ini = LocalDate.parse(txtIni.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate fim = LocalDate.parse(txtFim.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    controladorPlanoTreino.cadastrarPlano(u.getCpf(), txtNome.getText(), ini, fim);
                    tela.dispose(); abrirTelaPlanos(u); // Refresh passando o usuário
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage());
                }
            }
        });

        // Ação: Adicionar Treino
        btnAddTreino.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(tela, "Selecione um plano na tabela."); return; }
            int idPlano = (int) tabela.getValueAt(row, 0);

            String idTreinoStr = JOptionPane.showInputDialog("Digite o ID do Treino para adicionar a este plano\n(Consulte o ID no Histórico de Treinos):");
            if (idTreinoStr != null && !idTreinoStr.isEmpty()) {
                try {
                    controladorPlanoTreino.adicionarTreinoPlano(u.getCpf(), idPlano, Integer.parseInt(idTreinoStr));
                    JOptionPane.showMessageDialog(tela, "Treino adicionado com sucesso!");
                    tela.dispose(); abrirTelaPlanos(u);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage());
                }
            }
        });

        tela.setSize(700, 500);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // ========================================================================
    // TELA 2: RELATÓRIOS E EXPORTAÇÃO
    // ========================================================================
    
    public static void abrirTelaRelatorios() {
        Usuario u = identificarUsuario();
        if (u != null) abrirTelaRelatorios(u);
    }

    public static void abrirTelaRelatorios(Usuario u) {
        JFrame tela = new JFrame("Relatórios - " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        JTextArea areaTexto = new JTextArea();
        areaTexto.setBackground(new Color(40, 40, 40));
        areaTexto.setForeground(Color.WHITE);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaTexto.setEditable(false);
        areaTexto.setMargin(new Insets(10, 10, 10, 10));

        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE PERFORMANCE ===\n");
        sb.append("Atleta: ").append(u.getNome()).append("\n");
        sb.append("CPF: ").append(u.getCpf()).append("\n\n");
        
        double kmTotal = 0;
        int totalTreinos = u.getTreinos().size();
        
        sb.append("Total de Treinos: ").append(totalTreinos).append("\n");
        
        for(Treino t : u.getTreinos()) {
            if(t instanceof Corrida) {
                kmTotal += ((Corrida)t).getDistanciaEmMetros();
            }
            sb.append(" - ").append(t.getDataExecucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(": ").append(t.getNomeTreino()).append("\n");
        }
        sb.append("\nDistância Total Percorrida: ").append(String.format("%.2f", kmTotal/1000)).append(" km\n");
        
        areaTexto.setText(sb.toString());
        tela.add(new JScrollPane(areaTexto), BorderLayout.CENTER);

        JButton btnExportar = criarBotaoEstilizado("Exportar para CSV");
        btnExportar.setBackground(COR_DESTAQUE);
        btnExportar.setForeground(Color.BLACK);
        
        btnExportar.addActionListener(e -> {
            try {
                String nomeArquivo = "Relatorio_" + u.getNome().replaceAll(" ", "_") + ".csv";
                PrintWriter pw = new PrintWriter(new FileWriter(nomeArquivo));
                pw.println("Data;Treino;Duracao_Min;Detalhes"); 
                
                for(Treino t : u.getTreinos()) {
                    String detalhes = (t instanceof Corrida) ? ((Corrida)t).getDistanciaEmMetros() + "m" : "Intervalado";
                    pw.println(t.getDataExecucao().toLocalDate() + ";" + t.getNomeTreino() + ";" + (t.getDuracaoSegundos()/60) + ";" + detalhes);
                }
                pw.close();
                JOptionPane.showMessageDialog(tela, "Arquivo salvo na pasta do projeto:\n" + nomeArquivo);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro ao exportar: " + ex.getMessage());
            }
        });

        tela.add(btnExportar, BorderLayout.SOUTH);
        tela.setSize(500, 600);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // ========================================================================
    // TELA 3: METAS
    // ========================================================================
    
    public static void abrirTelaMetas() {
        Usuario u = identificarUsuario();
        if (u != null) abrirTelaMetas(u);
    }

    public static void abrirTelaMetas(Usuario u) {
        JFrame tela = new JFrame("Metas de " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

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
        } catch (Exception e) {}

        JTable tabela = new JTable(model);
        estilizarTabela(tabela);
        tela.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnNova = criarBotaoEstilizado("Nova Meta");
        btnNova.setBackground(COR_DESTAQUE);
        btnNova.setForeground(Color.BLACK);

        btnNova.addActionListener(e -> {
            JTextField txtDesc = new JTextField();
            JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"DISTANCIA (m)", "TEMPO (min)", "CALORIAS (kcal)"});
            JTextField txtValor = new JTextField();
            JTextField txtData = new JTextField("dd/MM/yyyy");
            Object[] msg = {"Descrição:", txtDesc, "Tipo:", cmbTipo, "Valor Alvo:", txtValor, "Prazo:", txtData};

            if (JOptionPane.showConfirmDialog(null, msg, "Definir Meta", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    TipoMeta tm = TipoMeta.DISTANCIA;
                    if(cmbTipo.getSelectedIndex() == 1) tm = TipoMeta.TEMPO;
                    if(cmbTipo.getSelectedIndex() == 2) tm = TipoMeta.CALORIAS;
                    controladorMeta.cadastrarMeta(u.getCpf(), txtDesc.getText(), tm, Double.parseDouble(txtValor.getText()), LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    tela.dispose(); abrirTelaMetas(u);
                } catch(Exception ex) { JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage()); }
            }
        });

        tela.add(btnNova, BorderLayout.SOUTH);
        tela.setSize(600, 450);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // ========================================================================
    // TELA 4: DESAFIOS (GLOBAL e PARTICIPAÇÃO)
    // ========================================================================
    
    public static void abrirTelaDesafios() {
        abrirTelaDesafios(null); // Versão sem usuário pré-definido (Admin ou visitante)
    }

    public static void abrirTelaDesafios(Usuario usuarioLogado) {
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
            model.addRow(new Object[]{d.getIdDesafio(), d.getNome(), d.getDataInicio().format(fmt), d.getDataFim().format(fmt), d.getParticipacoes().size()});
        }

        JTable tabela = new JTable(model);
        estilizarTabela(tabela);
        tela.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel pnlBotoes = new JPanel();
        pnlBotoes.setBackground(COR_FUNDO);
        
        // Se for admin (usuarioLogado == null), mostra botão criar. Se for usuário, não mostra.
        if (usuarioLogado == null) {
            JButton btnCriar = criarBotaoEstilizado("Criar Desafio (Admin)");
            btnCriar.addActionListener(e -> {
                JTextField txtNome = new JTextField();
                JTextField txtDesc = new JTextField();
                JTextField txtIni = new JTextField();
                JTextField txtFim = new JTextField();
                Object[] msg = {"Nome:", txtNome, "Descrição:", txtDesc, "Início:", txtIni, "Fim:", txtFim};
                if (JOptionPane.showConfirmDialog(null, msg, "Novo Desafio", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        controladorDesafio.cadastrarDesafio(txtNome.getText(), txtDesc.getText(), LocalDate.parse(txtIni.getText(), fmt), LocalDate.parse(txtFim.getText(), fmt));
                        tela.dispose(); abrirTelaDesafios(null);
                    } catch(Exception ex) { JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage()); }
                }
            });
            pnlBotoes.add(btnCriar);
        }

        JButton btnEntrar = criarBotaoEstilizado("Participar");
        JButton btnRank = criarBotaoEstilizado("Ranking");
        btnRank.setBackground(COR_DESTAQUE);
        btnRank.setForeground(Color.BLACK);

        pnlBotoes.add(btnEntrar); 
        pnlBotoes.add(btnRank);
        tela.add(pnlBotoes, BorderLayout.SOUTH);

        btnEntrar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if(row < 0) return;
            
            String cpfAlvo;
            if (usuarioLogado != null) {
                cpfAlvo = usuarioLogado.getCpf(); // Usa o usuário logado automaticamente
            } else {
                cpfAlvo = JOptionPane.showInputDialog("CPF para entrar:"); // Admin digita
            }

            if(cpfAlvo != null) try {
                controladorDesafio.participarDesafio((int)tabela.getValueAt(row, 0), cpfAlvo);
                tela.dispose(); abrirTelaDesafios(usuarioLogado);
                if (usuarioLogado != null) JOptionPane.showMessageDialog(null, "Você entrou no desafio!");
            } catch(Exception ex) { JOptionPane.showMessageDialog(tela, ex.getMessage()); }
        });

        btnRank.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if(row >= 0) try { abrirRanking(controladorDesafio.buscarDesafio((int)tabela.getValueAt(row, 0))); } catch(Exception ex) {}
        });

        tela.setSize(700, 500);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    private static void abrirRanking(Desafio d) {
        JFrame frame = new JFrame("Ranking: " + d.getNome());
        frame.getContentPane().setBackground(COR_PAINEL_LATERAL);
        List<ParticipacaoDesafio> parts = d.getParticipacoes();
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
    // CADASTROS E OUTROS (TREINO e USUÁRIO)
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
                Usuario u = new Usuario(txtNome.getText(), Integer.parseInt(txtIdade.getText()), Double.parseDouble(txtPeso.getText().replace(",", ".")), Double.parseDouble(txtAltura.getText().replace(",", ".")), txtEmail.getText(), txtCpf.getText());
                controladorCliente.cadastrarCliente(u);
                JOptionPane.showMessageDialog(tela, "Sucesso!");
                tela.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage()); }
        });
        tela.add(new JLabel("")); tela.add(btnSalvar);
        tela.setSize(400, 500); tela.setLocationRelativeTo(null); tela.setVisible(true);
    }

    // Versão Admin (Pede CPF)
    public static void abrirTelaCadastroTreino() {
        abrirTelaCadastroTreino(null);
    }

    // Versão Usuário (Preenche CPF)
    public static void abrirTelaCadastroTreino(Usuario usuarioLogado) {
        JFrame tela = new JFrame("Registrar Treino");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(COR_FUNDO);
        centro.setBorder(new EmptyBorder(20,40,20,40));

        JTextField txtCpf = criarInputEstilizado("CPF do Aluno");
        JTextField txtNome = criarInputEstilizado("Nome do Treino");
        JTextField txtData = criarInputEstilizado("Data (dd/MM/yyyy)");
        JTextField txtDur = criarInputEstilizado("Duração (min)");
        
        // Se usuário logado, preenche e trava o CPF
        if (usuarioLogado != null) {
            txtCpf.setText(usuarioLogado.getCpf());
            txtCpf.setEditable(false);
            txtCpf.setBackground(new Color(40,40,40));
        }

        centro.add(txtCpf); centro.add(Box.createVerticalStrut(10));
        centro.add(txtNome); centro.add(Box.createVerticalStrut(10));
        centro.add(txtData); centro.add(Box.createVerticalStrut(10));
        centro.add(txtDur); centro.add(Box.createVerticalStrut(20));

        JPanel pnlTipo = new JPanel();
        pnlTipo.setBackground(COR_FUNDO);
        pnlTipo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COR_DESTAQUE), "Detalhes", 0, 0, FONTE_BOTAO, COR_DESTAQUE));
        centro.add(pnlTipo);
        tela.add(centro, BorderLayout.CENTER);

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
            tipo[0] = "Corrida"; pnlTipo.removeAll();
            pnlTipo.add(criarLabelSimples("Distância (m): ")); extras[0] = criarInputEstilizado("0"); pnlTipo.add(extras[0]);
            pnlTipo.revalidate(); pnlTipo.repaint();
        });
        btnInterv.addActionListener(e -> {
            tipo[0] = "Intervalado"; pnlTipo.removeAll();
            pnlTipo.add(criarLabelSimples("Séries: ")); extras[1] = criarInputEstilizado("0"); pnlTipo.add(extras[1]);
            pnlTipo.add(criarLabelSimples("Descanso (s): ")); extras[2] = criarInputEstilizado("0"); pnlTipo.add(extras[2]);
            pnlTipo.revalidate(); pnlTipo.repaint();
        });

        btnSalvar.addActionListener(e -> {
            try {
                if(tipo[0] == null) throw new Exception("Selecione o tipo.");
                double dist = 0; int ser = 0, desc = 0;
                if(tipo[0].equals("Corrida")) dist = Double.parseDouble(extras[0].getText());
                else { ser = Integer.parseInt(extras[1].getText()); desc = Integer.parseInt(extras[2].getText()); }
                
                controladorTreino.cadastrarTreino(txtCpf.getText(), tipo[0], LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), Integer.parseInt(txtDur.getText()) * 60, txtNome.getText(), dist, ser, desc);
                JOptionPane.showMessageDialog(tela, "Treino registrado!");
                tela.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage()); }
        });
        tela.setSize(500, 600); tela.setLocationRelativeTo(null); tela.setVisible(true);
    }

    public static void abrirTelaNotificacoes() {
        Usuario u = identificarUsuario();
        if(u != null) abrirTelaNotificacoes(u);
    }

    public static void abrirTelaNotificacoes(Usuario u) {
        JFrame tela = new JFrame("Notificações - " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        DefaultListModel<String> model = new DefaultListModel<>();
        for(Notificacao n : u.getNotificacoes()) model.addElement((n.isLida() ? "[Lida] " : "[NOVA] ") + n.toString());
        if(model.isEmpty()) model.addElement("Sem notificações.");
        JList<String> list = new JList<>(model);
        list.setBackground(new Color(50,50,50)); list.setForeground(Color.WHITE);
        tela.add(new JScrollPane(list));
        JButton btnLimpar = criarBotaoEstilizado("Marcar lidas / Limpar");
        btnLimpar.addActionListener(e -> {
            u.getNotificacoes().removeIf(Notificacao::isLida);
            u.getNotificacoes().forEach(n -> n.setLida(true));
            JOptionPane.showMessageDialog(tela, "Limpo!"); tela.dispose();
        });
        tela.add(btnLimpar, BorderLayout.SOUTH);
        tela.setSize(400, 300); tela.setLocationRelativeTo(null); tela.setVisible(true);
    }
    
    // --- UTILITÁRIOS ---
    
    // Usado pelo Admin para encontrar um usuário
    public static Usuario identificarUsuario() {
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
        tabela.getTableHeader().setBackground(COR_PAINEL_LATERAL);
        tabela.getTableHeader().setForeground(COR_DESTAQUE);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
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