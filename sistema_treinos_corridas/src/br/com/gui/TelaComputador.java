package br.com.gui;

import br.com.dados.RepositorioClientes;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorTreino;
import br.com.negocio.SessaoUsuario; // [NOVO] Importante para pegar o usuário logado
import br.com.negocio.treinos.Notificacao;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaComputador {

    // --- CONTROLADORES E DADOS ---
    // Mantemos estáticos para serem acessados pela TelaLogin e TelaPrincipalUsuario
    public static ControladorCliente controladorCliente;
    public static ControladorTreino controladorTreino;

    public static final String CPF_LOGADO = "000.000.000-00"; // Fallback para testes

    // --- PALETA DE CORES (MODERNA / FITNESS) ---
    private static final Color COR_FUNDO = new Color(30, 30, 30);
    private static final Color COR_PAINEL_LATERAL = new Color(20, 20, 20);
    private static final Color COR_DESTAQUE = new Color(74, 255, 86);
    private static final Color COR_BOTAO_FUNDO = new Color(50, 50, 50);
    private static final Color COR_TEXTO = new Color(240, 240, 240);

    // Fonte Personalizada
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 16);

    public static void main(String[] args) {
        // Inicialização dos controladores (Se rodar direto por aqui)
        RepositorioClientes repo = new RepositorioClientes();
        controladorCliente = new ControladorCliente(repo);
        controladorTreino = new ControladorTreino(repo);

        // Usuário de teste
        Usuario userTeste = new Usuario("Usuário Teste", 25, 70, 1.75, "teste@email.com", CPF_LOGADO);
        controladorCliente.cadastrarCliente(userTeste);

        // Se rodar este arquivo direto, abre a tela de Admin
        SwingUtilities.invokeLater(() -> abrirTelaAdmin());
    }

    // [MODIFICADO] Renomeado de criarUI para abrirTelaAdmin
    public static void abrirTelaAdmin() {
        JFrame janela = new JFrame("Iron Track - Painel Administrativo"); // Título atualizado
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.getContentPane().setBackground(COR_FUNDO);
        janela.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // --- PAINEL LATERAL (Esquerda) ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.weightx = 0.3;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel painelLateral = new JPanel();
        painelLateral.setLayout(new BorderLayout());
        painelLateral.setBackground(COR_PAINEL_LATERAL);
        painelLateral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_DESTAQUE, 2),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitulo = new JLabel("<html><center>IRON<br>TRACK</center></html>", SwingConstants.CENTER);
        lblTitulo.setFont(FONTE_TITULO);
        lblTitulo.setForeground(COR_DESTAQUE);

        JLabel lblSubtitulo = new JLabel("<html><center><br>Gestão &<br>Administração</center></html>", // Subtítulo atualizado
                SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);

        painelLateral.add(lblTitulo, BorderLayout.CENTER);
        
        // Botão de Logout no painel lateral
        JButton btnLogout = criarBotaoEstilizado("SAIR / LOGOUT");
        btnLogout.setBackground(new Color(200, 50, 50));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> {
            janela.dispose();
            // Aqui você chamaria TelaLogin.abrirTelaLogin(); se a classe existir
            System.out.println("Logout realizado. Reinicie para logar novamente.");
        });
        painelLateral.add(btnLogout, BorderLayout.SOUTH);

        janela.add(painelLateral, gbc);

        // --- BOTÕES (Direita) ---
        gbc.gridheight = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(10, 5, 10, 5);

        // Coluna 1
        gbc.gridx = 1;

        // Botão Cadastrar (Funcionalidade de Admin)
        JButton btnCadastrar = criarBotaoEstilizado("Cadastrar Atleta");
        btnCadastrar.addActionListener(e -> abrirTelaCadastroUsuario());
        janela.add(btnCadastrar, configuracaoGrid(1, 0));

        // Botão Notificações (Admin vê notificações de alunos específicos)
        JButton btnNotificacoes = criarBotaoEstilizado("Ver Notificações");
        btnNotificacoes.addActionListener(e -> abrirTelaNotificacoes());
        janela.add(btnNotificacoes, configuracaoGrid(1, 1));

        janela.add(criarBotaoEstilizado("Gerenciar Desafios"), configuracaoGrid(1, 2));
        janela.add(criarBotaoEstilizado("Gerenciar Metas"), configuracaoGrid(1, 3));

        // Coluna 2
        gbc.gridx = 2;
        janela.add(criarBotaoEstilizado("Relatórios Gerais"), configuracaoGrid(2, 0));
        janela.add(criarBotaoEstilizado("Planos de Treino"), configuracaoGrid(2, 1));
        
        // Botão Especial TREINOS (Admin registra treino PARA um aluno)
        JButton btnTreinos = criarBotaoEstilizado("REGISTRAR TREINO");
        btnTreinos.setForeground(Color.BLACK);
        btnTreinos.setBackground(COR_DESTAQUE);
        // Admin usa a tela que pede CPF
        btnTreinos.addActionListener(e -> abrirTelaCadastroTreino());

        btnTreinos.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnTreinos.setBackground(Color.WHITE); }
            public void mouseExited(MouseEvent e) { btnTreinos.setBackground(COR_DESTAQUE); }
        });

        janela.add(btnTreinos, configuracaoGrid(2, 2));

        janela.setSize(1000, 650);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    // ========================================================================
    // TELA 1: CADASTRO DE USUÁRIO (Acessível pelo Admin)
    // ========================================================================
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
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(tela, "Erro: Verifique se Idade, Peso e Altura são números válidos.");
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

    // ========================================================================
    // TELA 2: NOTIFICAÇÕES (Inteligente: Detecta Admin vs Usuário)
    // ========================================================================
    public static void abrirTelaNotificacoes() {
        Usuario u = null;

        // 1. Tenta pegar da Sessão (Caso seja o Aluno logado)
        if (SessaoUsuario.getInstance().isUsuarioLogado()) {
            u = SessaoUsuario.getInstance().getUsuarioLogado();
        } 
        // 2. Se for Admin (sem usuário na sessão), pede o CPF
        else {
            String cpfDigitado = JOptionPane.showInputDialog(null, "Digite o CPF do aluno para ver as notificações:");
            if (cpfDigitado != null && !cpfDigitado.isEmpty()) {
                u = controladorCliente.buscarCliente(cpfDigitado);
            }
        }

        if (u == null) {
            JOptionPane.showMessageDialog(null, "Usuário não encontrado ou CPF inválido.");
            return;
        }

        JFrame tela = new JFrame("Notificações de " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());
        ((JComponent) tela.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<Notificacao> notificacoes = u.getNotificacoes();

        if (notificacoes.isEmpty()) {
            listModel.addElement("Você não tem novas notificações.");
        } else {
            for (Notificacao n : notificacoes) {
                String prefixo = n.isLida() ? "[Lida] " : "[NOVA!] ";
                listModel.addElement(prefixo + n.toString());
            }
        }

        JList<String> listaVisual = new JList<>(listModel);
        listaVisual.setBackground(new Color(50, 50, 50));
        listaVisual.setForeground(Color.WHITE);
        listaVisual.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(listaVisual);
        scroll.setBorder(BorderFactory.createLineBorder(COR_PAINEL_LATERAL));
        tela.add(scroll, BorderLayout.CENTER);

        JButton btnLimpar = criarBotaoEstilizado("Marcar todas como Lidas / Limpar");
        Usuario usuarioFinal = u;

        btnLimpar.addActionListener(e -> {
            for (Notificacao n : usuarioFinal.getNotificacoes()) {
                n.setLida(true);
            }
            usuarioFinal.getNotificacoes().removeIf(Notificacao::isLida);
            JOptionPane.showMessageDialog(tela, "Notificações limpas!");
            tela.dispose();
        });

        tela.add(btnLimpar, BorderLayout.SOUTH);
        tela.setSize(500, 400);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // ========================================================================
    // TELA 3.1: CADASTRO DE TREINO - VERSÃO ADMIN (COM CPF)
    // ========================================================================
    public static void abrirTelaCadastroTreino() {
        abrirJanelaTreinoBase(null); // Null indica que deve pedir CPF
    }

    // ========================================================================
    // TELA 3.2: CADASTRO DE TREINO - VERSÃO USUÁRIO (SEM CPF) [NOVO]
    // ========================================================================
    public static void abrirTelaCadastroTreinoUsuarioLogado() {
        Usuario logado = SessaoUsuario.getInstance().getUsuarioLogado();
        if (logado != null) {
            abrirJanelaTreinoBase(logado); // Passa o usuário logado
        } else {
            JOptionPane.showMessageDialog(null, "Erro: Nenhum usuário logado.");
        }
    }

    // Método privado compartilhado para montar a tela de treino
    private static void abrirJanelaTreinoBase(Usuario usuarioPreDefinido) {
        JFrame tela = new JFrame("Registrar Treino");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        // --- TOPO ---
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        topo.setBackground(COR_FUNDO);

        JButton btnVerTreinos = criarBotaoEstilizado("Ver Histórico");
        btnVerTreinos.addActionListener(e -> {
            // Se já tem usuário (Tela do Aluno), abre direto. Se não (Admin), pede CPF.
            if (usuarioPreDefinido != null) {
                abrirTelaHistorico(usuarioPreDefinido);
            } else {
                String cpfConsulta = JOptionPane.showInputDialog(tela, "Digite o CPF do aluno para ver o histórico:");
                if (cpfConsulta != null && !cpfConsulta.isEmpty()) {
                    Usuario u = controladorCliente.buscarCliente(cpfConsulta);
                    if (u != null) abrirTelaHistorico(u);
                    else JOptionPane.showMessageDialog(tela, "Aluno não encontrado.");
                }
            }
        });
        topo.add(btnVerTreinos);
        tela.add(topo, BorderLayout.NORTH);

        // --- CENTRO ---
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBackground(COR_FUNDO);
        painelCentral.setBorder(new EmptyBorder(20, 50, 20, 50));

        // Só mostra campo de CPF se NÃO tiver usuário pré-definido (Modo Admin)
        JTextField campoCpf = null;
        if (usuarioPreDefinido == null) {
            campoCpf = criarInputEstilizado("CPF do Aluno (ex: 000.000.000-00)");
            painelCentral.add(campoCpf);
            painelCentral.add(Box.createVerticalStrut(15));
        }

        JTextField campoNome = criarInputEstilizado("Nome do Treino (ex: Corrida na Praia)");
        JTextField campoData = criarInputEstilizado("Data (dd/MM/yyyy)");
        JTextField campoDuracao = criarInputEstilizado("Duração (minutos)");

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
                BorderFactory.createLineBorder(COR_DESTAQUE), " Detalhes do Tipo ",
                0, 0, FONTE_BOTAO, COR_DESTAQUE));

        painelCentral.add(painelDinamico);
        tela.add(painelCentral, BorderLayout.CENTER);

        // --- RODAPÉ ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        painelBotoes.setBackground(COR_FUNDO);

        JButton btnCorrida = criarBotaoEstilizado("Corrida");
        JButton btnIntervalado = criarBotaoEstilizado("Intervalado");
        JButton btnFinalizar = criarBotaoEstilizado("SALVAR TREINO");
        btnFinalizar.setBackground(COR_DESTAQUE);
        btnFinalizar.setForeground(Color.BLACK);

        painelBotoes.add(btnCorrida);
        painelBotoes.add(btnIntervalado);
        painelBotoes.add(btnFinalizar);
        tela.add(painelBotoes, BorderLayout.SOUTH);

        final JTextField[] campoDistancia = { null };
        final JTextField[] campoSeries = { null };
        final JTextField[] campoDescanso = { null };
        final String[] tipoTreino = { null };

        // Ações dos botões de tipo
        btnCorrida.addActionListener(e -> {
            tipoTreino[0] = "corrida";
            painelDinamico.removeAll();
            JLabel lbl = new JLabel("Distância (metros):");
            lbl.setForeground(Color.WHITE);
            JTextField campo = criarInputEstilizado("0");
            painelDinamico.add(lbl);
            painelDinamico.add(campo);
            campoDistancia[0] = campo;
            painelDinamico.revalidate();
            painelDinamico.repaint();
        });

        btnIntervalado.addActionListener(e -> {
            tipoTreino[0] = "intervalado";
            painelDinamico.removeAll();
            JLabel lbl1 = new JLabel("Séries:"); lbl1.setForeground(Color.WHITE);
            JTextField c1 = criarInputEstilizado("0");
            JLabel lbl2 = new JLabel("Descanso (seg):"); lbl2.setForeground(Color.WHITE);
            JTextField c2 = criarInputEstilizado("0");
            painelDinamico.add(lbl1); painelDinamico.add(c1);
            painelDinamico.add(Box.createVerticalStrut(10));
            painelDinamico.add(lbl2); painelDinamico.add(c2);
            campoSeries[0] = c1;
            campoDescanso[0] = c2;
            painelDinamico.revalidate();
            painelDinamico.repaint();
        });

        // --- LÓGICA DE SALVAR FINAL ---
        JTextField finalCampoCpf = campoCpf; // Para acesso no lambda
        btnFinalizar.addActionListener(e -> {
            try {
                String cpfAlvo;
                Usuario usuarioAlvo;

                // 1. Determina quem é o usuário alvo
                if (usuarioPreDefinido != null) {
                    // Modo Usuário Logado
                    usuarioAlvo = usuarioPreDefinido;
                    cpfAlvo = usuarioAlvo.getCpf();
                } else {
                    // Modo Admin (Lê do campo)
                    if (finalCampoCpf.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(tela, "Preencha o CPF do aluno!"); return;
                    }
                    cpfAlvo = finalCampoCpf.getText();
                    usuarioAlvo = controladorCliente.buscarCliente(cpfAlvo);
                    if (usuarioAlvo == null) {
                        JOptionPane.showMessageDialog(tela, "Usuário com CPF '" + cpfAlvo + "' não encontrado."); return;
                    }
                }

                if (campoNome.getText().isEmpty() || tipoTreino[0] == null) {
                    JOptionPane.showMessageDialog(tela, "Preencha todos os dados!"); return;
                }

                String nome = campoNome.getText();
                LocalDate data = LocalDate.parse(campoData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int duracao = Integer.parseInt(campoDuracao.getText()) * 60;
                double dist = 0; int ser = 0; int desc = 0;
                String tipo = "";

                if (tipoTreino[0].equals("corrida")) {
                    tipo = "Corrida";
                    dist = Double.parseDouble(campoDistancia[0].getText());
                } else {
                    tipo = "Intervalado";
                    ser = Integer.parseInt(campoSeries[0].getText());
                    desc = Integer.parseInt(campoDescanso[0].getText());
                }

                controladorTreino.cadastrarTreino(cpfAlvo, tipo, data, duracao, nome, dist, ser, desc);
                JOptionPane.showMessageDialog(tela, "Treino registrado com sucesso para " + usuarioAlvo.getNome() + "!");
                tela.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage());
            }
        });

        tela.setSize(500, 700);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // ========================================================================
    // TELA 4: HISTÓRICO MODERNO (TABELA)
    // ========================================================================
    public static void abrirTelaHistorico(Usuario u) {
        if (u == null) return;

        JFrame tela = new JFrame("Histórico de Performance - " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Histórico de Atividades", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COR_DESTAQUE);
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        tela.add(lblTitulo, BorderLayout.NORTH);

        String[] colunas = { "Data", "Treino", "Tipo", "Duração", "Detalhes", "Kcal (Est.)" };

        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
                detalhes = i.getSeries() + " séries / " + i.getDescansoEntreSeriesSeg() + "s desc.";
            }
            String calorias = String.format("%.0f", t.calcularCaloriasQueimadas(u));
            model.addRow(new Object[] { data, nome, tipo, duracao, detalhes, calorias });
        }

        JTable tabela = new JTable(model);
        tabela.setBackground(new Color(45, 45, 45));
        tabela.setForeground(Color.WHITE);
        tabela.setGridColor(new Color(60, 60, 60));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setRowHeight(30);
        tabela.setFillsViewportHeight(true);
        tabela.setSelectionBackground(COR_DESTAQUE);
        tabela.setSelectionForeground(Color.BLACK);

        javax.swing.table.JTableHeader header = tabela.getTableHeader();
        header.setBackground(COR_PAINEL_LATERAL);
        header.setForeground(COR_DESTAQUE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(COR_DESTAQUE));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(COR_FUNDO);
        scroll.setBorder(new EmptyBorder(10, 20, 20, 20));

        tela.add(scroll, BorderLayout.CENTER);

        JButton btnFechar = criarBotaoEstilizado("Fechar");
        btnFechar.addActionListener(e -> tela.dispose());
        JPanel painelBotao = new JPanel();
        painelBotao.setBackground(COR_FUNDO);
        painelBotao.add(btnFechar);
        tela.add(painelBotao, BorderLayout.SOUTH);

        tela.setSize(800, 500);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // --- MÉTODOS AUXILIARES DE ESTILO ---

    private static GridBagConstraints configuracaoGrid(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 5, 10, 10);
        return gbc;
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
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(COR_DESTAQUE)) btn.setBackground(new Color(70, 70, 70));
            }
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(COR_DESTAQUE)) btn.setBackground(COR_BOTAO_FUNDO);
            }
        });
        return btn;
    }

    private static JTextField criarInputEstilizado(String titulo) {
        JTextField campo = new JTextField();
        campo.setBorder(BorderFactory.createTitledBorder(null, titulo, 0, 0, new Font("Segoe UI", Font.PLAIN, 12), Color.WHITE));
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(COR_DESTAQUE);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return campo;
    }

    private static JLabel criarLabelSimples(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }

    public static void TelaMetas() {
        JOptionPane.showMessageDialog(null, "Funcionalidade de Metas em desenvolvimento.");
    }

    public static void TelaDesafios() {
        JOptionPane.showMessageDialog(null, "Funcionalidade de Desafios em desenvolvimento.");
    }
}