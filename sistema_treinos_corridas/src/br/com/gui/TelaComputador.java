package br.com.gui;

import br.com.dados.RepositorioClientes;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorTreino;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Notificacao;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaComputador {

    // --- CONTROLADORES E DADOS ---
    public static ControladorCliente controladorCliente;
    public static ControladorTreino controladorTreino;
    public static final String CPF_LOGADO = "000.000.000-00"; 

    // --- PALETA DE CORES (MODERNA) ---
    private static final Color COR_FUNDO = new Color(30, 30, 30);
    private static final Color COR_PAINEL_LATERAL = new Color(20, 20, 20);
    private static final Color COR_DESTAQUE = new Color(74, 255, 86); // Verde Neon
    private static final Color COR_BOTAO_FUNDO = new Color(50, 50, 50);
    private static final Color COR_TEXTO = new Color(240, 240, 240);
    
    // Cores específicas da Tabela
    private static final Color COR_TABELA_FUNDO = new Color(45, 45, 45);
    private static final Color COR_TABELA_LINHA = new Color(60, 60, 60);

    // Fontes
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 16);

    // --- Inicialização Estática ---
    static {
        if (controladorCliente == null) {
            RepositorioClientes repo = new RepositorioClientes();
            controladorCliente = new ControladorCliente(repo);
            controladorTreino = new ControladorTreino(repo);
            
            // Dados de teste
            if(controladorCliente.buscarCliente(CPF_LOGADO) == null) {
                Usuario userTeste = new Usuario("Usuário Teste", 25, 70, 1.75, "teste@email.com", CPF_LOGADO);
                controladorCliente.cadastrarCliente(userTeste);
            }
        }
    }

    public TelaComputador() {}

    // --- [PRINCIPAL] Método que retorna o PAINEL do Admin ---
    public JPanel criarPainelAdmin() {
        JPanel painelGeral = new JPanel(new GridBagLayout());
        painelGeral.setBackground(COR_FUNDO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        // PAINEL LATERAL
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 4; 
        gbc.weightx = 0.3; gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel painelLateral = new JPanel(new BorderLayout());
        painelLateral.setBackground(COR_PAINEL_LATERAL);
        painelLateral.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_DESTAQUE, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitulo = new JLabel("<html><center>IRON<br>TRACK</center></html>", SwingConstants.CENTER);
        lblTitulo.setFont(FONTE_TITULO);
        lblTitulo.setForeground(COR_DESTAQUE);
        
        JLabel lblSubtitulo = new JLabel("<html><center><br>Gestão &<br>Administração</center></html>", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);

        painelLateral.add(lblTitulo, BorderLayout.CENTER);
        painelLateral.add(lblSubtitulo, BorderLayout.NORTH);

        JButton btnLogout = new JButton("SAIR (LOGOUT)");
        btnLogout.setBackground(new Color(200, 50, 50));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            SessaoUsuario.getInstance().logout();
            GerenciadorTelas.getInstance().carregarTela(new TelaLogin().criarPainelLogin());
        });
        painelLateral.add(btnLogout, BorderLayout.SOUTH);
        
        painelGeral.add(painelLateral, gbc);

        // BOTÕES DE FUNCIONALIDADES
        gbc.gridheight = 1; gbc.weightx = 0.35;
        gbc.insets = new Insets(10, 5, 10, 5);

        gbc.gridx = 1; 
        JButton btnCadastrar = criarBotaoEstilizado("Cadastrar Atleta");
        btnCadastrar.addActionListener(e -> abrirTelaCadastroUsuario());
        painelGeral.add(btnCadastrar, configuracaoGrid(1, 0));

        JButton btnNotificacoes = criarBotaoEstilizado("Ver Notificações");
        btnNotificacoes.addActionListener(e -> abrirTelaNotificacoes());
        painelGeral.add(btnNotificacoes, configuracaoGrid(1, 1));
        
        painelGeral.add(criarBotaoEstilizado("Gerenciar Desafios"), configuracaoGrid(1, 2));
        painelGeral.add(criarBotaoEstilizado("Gerenciar Metas"), configuracaoGrid(1, 3));

        gbc.gridx = 2;
        painelGeral.add(criarBotaoEstilizado("Relatórios Gerais"), configuracaoGrid(2, 0));
        painelGeral.add(criarBotaoEstilizado("Planos de Treino"), configuracaoGrid(2, 1));
        
        JButton btnTreinos = criarBotaoEstilizado("REGISTRAR TREINO");
        btnTreinos.setForeground(Color.BLACK);
        btnTreinos.setBackground(COR_DESTAQUE); 
        btnTreinos.addActionListener(e -> abrirTelaCadastroTreino());
        
        btnTreinos.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnTreinos.setBackground(Color.WHITE); }
            public void mouseExited(MouseEvent e) { btnTreinos.setBackground(COR_DESTAQUE); }
        });

        painelGeral.add(btnTreinos, configuracaoGrid(2, 2));

        return painelGeral;
    }

    // --- MÉTODOS DE JANELAS SECUNDÁRIAS ---

    public static void abrirTelaCadastroUsuario() {
        JFrame tela = new JFrame("Cadastrar Novo Atleta");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new GridLayout(7, 2, 10, 10));
        ((JComponent) tela.getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        tela.add(criarLabelSimples("Nome:"));
        JTextField txtNome = criarInputEstilizado("Nome Completo");
        tela.add(txtNome);

        tela.add(criarLabelSimples("CPF:"));
        JTextField txtCpf = criarInputEstilizado("000.000.000-00");
        tela.add(txtCpf);

        tela.add(criarLabelSimples("Email:"));
        JTextField txtEmail = criarInputEstilizado("exemplo@email.com");
        tela.add(txtEmail);

        tela.add(criarLabelSimples("Idade:"));
        JTextField txtIdade = criarInputEstilizado("00");
        tela.add(txtIdade);

        tela.add(criarLabelSimples("Peso (kg):"));
        JTextField txtPeso = criarInputEstilizado("00.0");
        tela.add(txtPeso);

        tela.add(criarLabelSimples("Altura (m):"));
        JTextField txtAltura = criarInputEstilizado("0.00");
        tela.add(txtAltura);

        JButton btnSalvar = criarBotaoEstilizado("SALVAR CADASTRO");
        btnSalvar.setBackground(COR_DESTAQUE);
        btnSalvar.setForeground(Color.BLACK);

        btnSalvar.addActionListener(e -> {
            try {
                String nome = txtNome.getText();
                String cpf = txtCpf.getText();
                String email = txtEmail.getText();
                int idade = Integer.parseInt(txtIdade.getText());
                double peso = Double.parseDouble(txtPeso.getText().replace(",", "."));
                double altura = Double.parseDouble(txtAltura.getText().replace(",", "."));

                Usuario novoUsuario = new Usuario(nome, idade, peso, altura, email, cpf);
                controladorCliente.cadastrarCliente(novoUsuario);

                JOptionPane.showMessageDialog(tela, "Atleta " + nome + " cadastrado com sucesso!");
                tela.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro ao cadastrar: " + ex.getMessage());
            }
        });

        tela.add(new JLabel(""));
        tela.add(btnSalvar);
        tela.setSize(450, 500);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    public static void abrirTelaCadastroTreino() {
        abrirJanelaTreinoBase(null);
    }

    public static void abrirTelaCadastroTreinoUsuarioLogado() {
        Usuario logado = SessaoUsuario.getInstance().getUsuarioLogado();
        if (logado != null) {
            abrirJanelaTreinoBase(logado);
        } else {
            JOptionPane.showMessageDialog(null, "Erro: Nenhum usuário logado.");
        }
    }

    private static void abrirJanelaTreinoBase(Usuario usuarioPreDefinido) {
        JFrame tela = new JFrame("Registrar Treino");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        topo.setBackground(COR_FUNDO);
        
        JButton btnVerTreinos = criarBotaoEstilizado("Ver Histórico");
        btnVerTreinos.addActionListener(e -> {
            if (usuarioPreDefinido != null) {
                abrirTelaHistorico(usuarioPreDefinido);
            } else {
                String cpfConsulta = JOptionPane.showInputDialog(tela, "Digite o CPF para ver histórico:");
                if (cpfConsulta != null) {
                    Usuario u = controladorCliente.buscarCliente(cpfConsulta);
                    if (u != null) abrirTelaHistorico(u);
                    else JOptionPane.showMessageDialog(tela, "Não encontrado.");
                }
            }
        });
        topo.add(btnVerTreinos);
        tela.add(topo, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBackground(COR_FUNDO);
        painelCentral.setBorder(new EmptyBorder(20, 50, 20, 50));

        JTextField campoCpf = null;
        if (usuarioPreDefinido == null) {
            campoCpf = criarInputEstilizado("CPF do Aluno");
            painelCentral.add(campoCpf);
            painelCentral.add(Box.createVerticalStrut(15));
        }

        JTextField campoNome = criarInputEstilizado("Nome do Treino");
        JTextField campoData = criarInputEstilizado("Data (dd/MM/yyyy)");
        JTextField campoDuracao = criarInputEstilizado("Duração (min)");

        painelCentral.add(campoNome);
        painelCentral.add(Box.createVerticalStrut(15));
        painelCentral.add(campoData);
        painelCentral.add(Box.createVerticalStrut(15));
        painelCentral.add(campoDuracao);
        painelCentral.add(Box.createVerticalStrut(20));

        JPanel painelDinamico = new JPanel();
        painelDinamico.setLayout(new BoxLayout(painelDinamico, BoxLayout.Y_AXIS));
        painelDinamico.setBackground(COR_FUNDO);
        painelDinamico.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_DESTAQUE), " Detalhes ", 
            0, 0, FONTE_BOTAO, COR_DESTAQUE));
        
        painelCentral.add(painelDinamico);
        tela.add(painelCentral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        painelBotoes.setBackground(COR_FUNDO);

        JButton btnCorrida = criarBotaoEstilizado("Corrida");
        JButton btnIntervalado = criarBotaoEstilizado("Intervalado");
        JButton btnFinalizar = criarBotaoEstilizado("SALVAR");
        btnFinalizar.setBackground(COR_DESTAQUE);
        btnFinalizar.setForeground(Color.BLACK);

        painelBotoes.add(btnCorrida);
        painelBotoes.add(btnIntervalado);
        painelBotoes.add(btnFinalizar);
        tela.add(painelBotoes, BorderLayout.SOUTH);

        final JTextField[] campoDistancia = {null};
        final JTextField[] campoSeries = {null};
        final JTextField[] campoDescanso = {null};
        final String[] tipoTreino = {null};

        btnCorrida.addActionListener(e -> {
            tipoTreino[0] = "corrida";
            painelDinamico.removeAll();
            JLabel lbl = new JLabel("Distância (m):"); lbl.setForeground(Color.WHITE);
            JTextField campo = criarInputEstilizado("0");
            painelDinamico.add(lbl); painelDinamico.add(campo);
            campoDistancia[0] = campo;
            painelDinamico.revalidate(); painelDinamico.repaint();
        });

        btnIntervalado.addActionListener(e -> {
            tipoTreino[0] = "intervalado";
            painelDinamico.removeAll();
            JLabel lbl1 = new JLabel("Séries:"); lbl1.setForeground(Color.WHITE);
            JTextField c1 = criarInputEstilizado("0");
            JLabel lbl2 = new JLabel("Descanso (s):"); lbl2.setForeground(Color.WHITE);
            JTextField c2 = criarInputEstilizado("0");
            painelDinamico.add(lbl1); painelDinamico.add(c1);
            painelDinamico.add(Box.createVerticalStrut(10));
            painelDinamico.add(lbl2); painelDinamico.add(c2);
            campoSeries[0] = c1; campoDescanso[0] = c2;
            painelDinamico.revalidate(); painelDinamico.repaint();
        });

        JTextField finalCampoCpf = campoCpf;
        btnFinalizar.addActionListener(e -> {
            try {
                String cpfAlvo;
                if (usuarioPreDefinido != null) {
                    cpfAlvo = usuarioPreDefinido.getCpf();
                } else {
                    if(finalCampoCpf.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(tela, "Digite o CPF."); return;
                    }
                    cpfAlvo = finalCampoCpf.getText();
                    if (controladorCliente.buscarCliente(cpfAlvo) == null) {
                        JOptionPane.showMessageDialog(tela, "Cliente não encontrado."); return;
                    }
                }

                String nome = campoNome.getText();
                LocalDate data = LocalDate.parse(campoData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int duracao = Integer.parseInt(campoDuracao.getText()) * 60;
                
                if (tipoTreino[0] == null) { JOptionPane.showMessageDialog(tela, "Selecione o tipo!"); return; }

                if (tipoTreino[0].equals("corrida")) {
                    double dist = Double.parseDouble(campoDistancia[0].getText());
                    controladorTreino.cadastrarTreino(cpfAlvo, "Corrida", data, duracao, nome, dist, 0, 0);
                } else {
                    int ser = Integer.parseInt(campoSeries[0].getText());
                    int desc = Integer.parseInt(campoDescanso[0].getText());
                    controladorTreino.cadastrarTreino(cpfAlvo, "Intervalado", data, duracao, nome, 0, ser, desc);
                }
                JOptionPane.showMessageDialog(tela, "Salvo!");
                tela.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage());
            }
        });

        tela.setSize(500, 650);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    public static void abrirTelaNotificacoes() {
        Usuario u = null;
        if (SessaoUsuario.getInstance().isUsuarioLogado()) {
            u = SessaoUsuario.getInstance().getUsuarioLogado();
        } else {
            String cpf = JOptionPane.showInputDialog(null, "CPF do aluno:");
            if (cpf != null) u = controladorCliente.buscarCliente(cpf);
        }

        if (u == null) { JOptionPane.showMessageDialog(null, "Não encontrado."); return; }

        JFrame tela = new JFrame("Notificações - " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for(Notificacao n : u.getNotificacoes()) listModel.addElement((n.isLida() ? "[Lida] " : "[NOVA] ") + n.toString());
        if(listModel.isEmpty()) listModel.addElement("Sem notificações.");

        JList<String> lista = new JList<>(listModel);
        lista.setBackground(new Color(50, 50, 50));
        lista.setForeground(Color.WHITE);
        tela.add(new JScrollPane(lista), BorderLayout.CENTER);

        JButton btnLimpar = criarBotaoEstilizado("Limpar Lidas");
        Usuario finalU = u;
        btnLimpar.addActionListener(e -> {
            finalU.getNotificacoes().removeIf(Notificacao::isLida);
            for(Notificacao n : finalU.getNotificacoes()) n.setLida(true);
            tela.dispose();
        });
        tela.add(btnLimpar, BorderLayout.SOUTH);
        tela.setSize(400, 400);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // --- [CORREÇÃO] TELA DE HISTÓRICO MODERNA RESTAURADA ---
    public static void abrirTelaHistorico(Usuario u) {
        if (u == null) return;

        JFrame tela = new JFrame("Histórico de Performance - " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        // Título Estilizado
        JLabel lblTitulo = new JLabel("Histórico de Atividades", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COR_DESTAQUE);
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        tela.add(lblTitulo, BorderLayout.NORTH);

        // Modelo da Tabela
        String[] colunas = {"Data", "Treino", "Tipo", "Duração", "Detalhes", "Kcal"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Treino t : u.getTreinos()) {
            String data = t.getDataExecucao().format(fmtData);
            String nome = t.getNomeTreino();
            String tipo = (t instanceof br.com.negocio.treinos.Corrida) ? "Corrida" : "Intervalado";
            String duracao = (t.getDuracaoSegundos() / 60) + " min";
            
            String detalhes = "-";
            if (t instanceof br.com.negocio.treinos.Corrida) {
                double km = ((br.com.negocio.treinos.Corrida) t).getDistanciaEmMetros() / 1000.0;
                detalhes = String.format("%.2f km", km);
            } else if (t instanceof br.com.negocio.treinos.Intervalado) {
                br.com.negocio.treinos.Intervalado i = (br.com.negocio.treinos.Intervalado) t;
                detalhes = i.getSeries() + " séries / " + i.getDescansoEntreSeriesSeg() + "s";
            }

            String calorias = String.format("%.0f", t.calcularCaloriasQueimadas(u));
            model.addRow(new Object[]{data, nome, tipo, duracao, detalhes, calorias});
        }

        // --- ESTILIZAÇÃO AVANÇADA DA TABELA ---
        JTable tabela = new JTable(model);
        tabela.setBackground(COR_TABELA_FUNDO);
        tabela.setForeground(Color.WHITE);
        tabela.setGridColor(COR_TABELA_LINHA);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setRowHeight(30); // Linhas mais altas
        tabela.setFillsViewportHeight(true);
        tabela.setSelectionBackground(COR_DESTAQUE);
        tabela.setSelectionForeground(Color.BLACK);

        // Remove bordas
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));

        // Cabeçalho da Tabela
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(COR_PAINEL_LATERAL);
        header.setForeground(COR_DESTAQUE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COR_DESTAQUE));
        
        // Renderizador para centralizar texto
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i=0; i<tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(COR_FUNDO); // Fundo escuro atrás da tabela
        scroll.setBorder(new EmptyBorder(10, 20, 20, 20)); 
        
        tela.add(scroll, BorderLayout.CENTER);

        // Botão Fechar
        JButton btnFechar = criarBotaoEstilizado("FECHAR");
        btnFechar.addActionListener(e -> tela.dispose());
        JPanel painelBotao = new JPanel();
        painelBotao.setBackground(COR_FUNDO);
        painelBotao.setBorder(new EmptyBorder(0,0,20,0));
        painelBotao.add(btnFechar);
        tela.add(painelBotao, BorderLayout.SOUTH);

        tela.setSize(800, 500);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // --- MÉTODOS AUXILIARES DE ESTILO ---
    private static GridBagConstraints configuracaoGrid(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x; gbc.gridy = y; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.insets = new Insets(10, 5, 10, 10);
        return gbc;
    }

    private static JButton criarBotaoEstilizado(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setForeground(COR_TEXTO);
        btn.setBackground(COR_BOTAO_FUNDO);
        btn.setFocusPainted(false);
        return btn;
    }

    private static JTextField criarInputEstilizado(String titulo) {
        JTextField campo = new JTextField();
        campo.setBorder(BorderFactory.createTitledBorder(null, titulo, 0, 0, new Font("Segoe UI", Font.PLAIN, 12), Color.WHITE));
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(COR_DESTAQUE);
        return campo;
    }

    private static JLabel criarLabelSimples(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    public static void TelaMetas(){ JOptionPane.showMessageDialog(null, "Em breve."); }
    public static void TelaDesafios(){ JOptionPane.showMessageDialog(null, "Em breve."); }
}