package br.com.gui;

import br.com.dados.RepositorioClientes;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorTreino;
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
import java.time.format.DateTimeParseException; // Importante para tratar erro de data
import java.util.List;

public class TelaComputador {

    // --- CONTROLADORES E DADOS ---
    public static ControladorCliente controladorCliente;
    public static ControladorTreino controladorTreino;

    // CPF fixo caso queira usar como fallback (mas o campo visual terá prioridade)
    public static final String CPF_LOGADO = "000.000.000-00";

    // --- PALETA DE CORES (MODERNA / FITNESS) ---
    private static final Color COR_FUNDO = new Color(30, 30, 30); // Cinza Quase Preto
    private static final Color COR_PAINEL_LATERAL = new Color(20, 20, 20);// Preto Suave
    private static final Color COR_DESTAQUE = new Color(74, 255, 86); // Verde Neon
    private static final Color COR_BOTAO_FUNDO = new Color(50, 50, 50); // Cinza Médio
    private static final Color COR_TEXTO = new Color(240, 240, 240); // Branco

    // Fonte Personalizada
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 16);

    public static void main(String[] args) {
        RepositorioClientes repo = new RepositorioClientes();
        controladorCliente = new ControladorCliente(repo);
        controladorTreino = new ControladorTreino(repo);

        // Usuário de teste inicial para não começar vazio
        Usuario userTeste = new Usuario("Usuário Teste", 25, 70, 1.75, "teste@email.com", CPF_LOGADO);
        controladorCliente.cadastrarCliente(userTeste);

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

        JLabel lblSubtitulo = new JLabel("<html><center><br>Performance &<br>Evolução</center></html>",
                SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);

        painelLateral.add(lblTitulo, BorderLayout.CENTER);
        painelLateral.add(lblSubtitulo, BorderLayout.SOUTH);

        janela.add(painelLateral, gbc);

        // --- BOTÕES (Direita) ---
        gbc.gridheight = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(10, 5, 10, 5);

        // Coluna 1
        gbc.gridx = 1;

        // Botão Cadastrar
        JButton btnCadastrar = criarBotaoEstilizado("Cadastrar");
        btnCadastrar.addActionListener(e -> abrirTelaCadastroUsuario());
        janela.add(btnCadastrar, configuracaoGrid(1, 0));

        // Botão Notificações
        JButton btnNotificacoes = criarBotaoEstilizado("Notificações");
        btnNotificacoes.addActionListener(e -> abrirTelaNotificacoes());
        janela.add(btnNotificacoes, configuracaoGrid(1, 1));

        janela.add(criarBotaoEstilizado("Desafios"), configuracaoGrid(1, 2)); // Placeholder
        janela.add(criarBotaoEstilizado("Metas"), configuracaoGrid(1, 3)); // Placeholder

        // Coluna 2
        gbc.gridx = 2;
        janela.add(criarBotaoEstilizado("Perfil"), configuracaoGrid(2, 0)); // Placeholder
        janela.add(criarBotaoEstilizado("Relatórios"), configuracaoGrid(2, 1)); // Placeholder
        janela.add(criarBotaoEstilizado("Planos"), configuracaoGrid(2, 2)); // Placeholder

        // Botão Especial TREINOS
        JButton btnTreinos = criarBotaoEstilizado("TREINOS");
        btnTreinos.setForeground(Color.BLACK);
        btnTreinos.setBackground(COR_DESTAQUE);
        btnTreinos.addActionListener(e -> abrirTelaCadastroTreino());

        btnTreinos.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnTreinos.setBackground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
                btnTreinos.setBackground(COR_DESTAQUE);
            }
        });

        janela.add(btnTreinos, configuracaoGrid(2, 3));

        janela.setSize(1000, 650);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    // ========================================================================
    // TELA 1: CADASTRO DE USUÁRIO
    // ========================================================================
    public static void abrirTelaCadastroUsuario() {
        JFrame tela = new JFrame("Cadastrar Novo Atleta");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new GridLayout(7, 2, 10, 10)); // Grid para os campos
        ((JComponent) tela.getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // Labels e Campos
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

        // Ação do Botão Salvar
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

        tela.add(new JLabel("")); // Espaço vazio para alinhar o botão
        tela.add(btnSalvar);

        tela.setSize(450, 500);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // ========================================================================
    // TELA 2: NOTIFICAÇÕES
    // ========================================================================
    public static void abrirTelaNotificacoes() {
        Usuario u = controladorCliente.buscarCliente(CPF_LOGADO);
        if (u == null) {
            String cpfDigitado = JOptionPane.showInputDialog(null, "Usuário padrão não encontrado. Digite seu CPF:");
            if (cpfDigitado != null && !cpfDigitado.isEmpty()) {
                u = controladorCliente.buscarCliente(cpfDigitado);
            }
        }

        if (u == null) {
            JOptionPane.showMessageDialog(null, "Usuário não encontrado.");
            return;
        }

        JFrame tela = new JFrame("Notificações de " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());
        ((JComponent) tela.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Lista de Notificações
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
    // TELA 3: CADASTRO DE TREINO (COM CAMPO CPF ATUALIZADO)
    // ========================================================================
    public static void abrirTelaCadastroTreino() {
        JFrame tela = new JFrame("Gerenciar Treinos");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        // --- TOPO ---
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        topo.setBackground(COR_FUNDO);

        JButton btnVerTreinos = criarBotaoEstilizado("Ver Histórico de Aluno");
        btnVerTreinos.addActionListener(e -> {
            // 1. Pega o CPF (tenta o do campo de texto primeiro, depois o padrão)
            // Se o campo CPF estiver vazio na hora de clicar em Histórico, pede num popup
            String cpfConsulta = null;

            // Tenta pegar do campo de texto da tela se já estiver preenchido
            // (Presumindo que você manteve a variável 'campoCpf' acessível ou declarou ela
            // antes)
            // Se 'campoCpf' não for final ou acessível aqui, use o JOptionPane direto:

            cpfConsulta = JOptionPane.showInputDialog(tela, "Digite o CPF do aluno para ver o histórico:");

            if (cpfConsulta != null && !cpfConsulta.isEmpty()) {
                Usuario u = controladorCliente.buscarCliente(cpfConsulta);
                if (u != null) {
                    // [MUDANÇA] Chama a nova tela moderna
                    abrirTelaHistorico(u);
                } else {
                    JOptionPane.showMessageDialog(tela, "Aluno não encontrado.");
                }
            }
        });
        topo.add(btnVerTreinos);
        tela.add(topo, BorderLayout.NORTH);

        // --- CENTRO (Formulário) ---
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBackground(COR_FUNDO);
        painelCentral.setBorder(new EmptyBorder(20, 50, 20, 50));

        // [MUDANÇA AQUI] Adicionado campo visual para CPF
        JTextField campoCpf = criarInputEstilizado("CPF do Aluno (ex: 000.000.000-00)");
        JTextField campoNome = criarInputEstilizado("Nome do Treino (ex: Corrida na Praia)");
        JTextField campoData = criarInputEstilizado("Data (dd/MM/yyyy)");
        JTextField campoDuracao = criarInputEstilizado("Duração (minutos)");

        painelCentral.add(campoCpf); // <-- Adicionado ao painel
        painelCentral.add(Box.createVerticalStrut(15));
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

        // --- RODAPÉ (Botões) ---
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
            JLabel lbl1 = new JLabel("Séries:");
            lbl1.setForeground(Color.WHITE);
            JTextField c1 = criarInputEstilizado("0");
            JLabel lbl2 = new JLabel("Descanso (seg):");
            lbl2.setForeground(Color.WHITE);
            JTextField c2 = criarInputEstilizado("0");

            painelDinamico.add(lbl1);
            painelDinamico.add(c1);
            painelDinamico.add(Box.createVerticalStrut(10));
            painelDinamico.add(lbl2);
            painelDinamico.add(c2);

            campoSeries[0] = c1;
            campoDescanso[0] = c2;
            painelDinamico.revalidate();
            painelDinamico.repaint();
        });

        // --- LÓGICA DE SALVAR ATUALIZADA ---
        btnFinalizar.addActionListener(e -> {
            try {
                // 1. Pega o CPF digitado no campo visual
                String cpfAlvo = campoCpf.getText();

                // 2. Validações básicas
                if (cpfAlvo.isEmpty() || campoNome.getText().isEmpty() || tipoTreino[0] == null) {
                    JOptionPane.showMessageDialog(tela,
                            "Por favor, preencha o CPF, Nome e selecione o Tipo de Treino!");
                    return;
                }

                // 3. Verifica se o usuário existe antes de tentar salvar
                Usuario usuarioEncontrado = controladorCliente.buscarCliente(cpfAlvo);
                if (usuarioEncontrado == null) {
                    JOptionPane.showMessageDialog(tela, "Erro: Usuário com CPF '" + cpfAlvo + "' não encontrado.");
                    return;
                }

                // 4. Captura os outros dados
                String nome = campoNome.getText();
                LocalDate data = LocalDate.parse(campoData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int duracao = Integer.parseInt(campoDuracao.getText()) * 60;

                double dist = 0;
                int ser = 0;
                int desc = 0;
                String tipo = "";

                if (tipoTreino[0].equals("corrida")) {
                    tipo = "Corrida";
                    dist = Double.parseDouble(campoDistancia[0].getText());
                } else {
                    tipo = "Intervalado";
                    ser = Integer.parseInt(campoSeries[0].getText());
                    desc = Integer.parseInt(campoDescanso[0].getText());
                }

                // 5. Envia para o controlador usando o CPF digitado
                controladorTreino.cadastrarTreino(cpfAlvo, tipo, data, duracao, nome, dist, ser, desc);

                JOptionPane.showMessageDialog(tela,
                        "Treino cadastrado com sucesso para " + usuarioEncontrado.getNome() + "!");
                tela.dispose();

            } catch (DateTimeParseException dt) {
                JOptionPane.showMessageDialog(tela, "Erro na data. Use o formato dd/MM/yyyy.");
            } catch (NumberFormatException nf) {
                JOptionPane.showMessageDialog(tela, "Erro nos números. Verifique duração, distância ou séries.");
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
        if (u == null)
            return;

        JFrame tela = new JFrame("Histórico de Performance - " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        // --- Título do Topo ---
        JLabel lblTitulo = new JLabel("Histórico de Atividades", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COR_DESTAQUE);
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        tela.add(lblTitulo, BorderLayout.NORTH);

        // --- Configuração das Colunas ---
        String[] colunas = { "Data", "Treino", "Tipo", "Duração", "Detalhes", "Kcal (Est.)" };

        // Modelo da Tabela (impede edição das células)
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Preenchendo os dados
        DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Treino t : u.getTreinos()) {
            String data = t.getDataExecucao().format(fmtData);
            String nome = t.getNomeTreino(); //
            String tipo = (t instanceof br.com.negocio.treinos.Corrida) ? "Corrida" : "Intervalado";
            String duracao = (t.getDuracaoSegundos() / 60) + " min";

            // Detalhes dependem do tipo
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

        // --- Estilização da JTable ---
        JTable tabela = new JTable(model);
        tabela.setBackground(new Color(45, 45, 45));
        tabela.setForeground(Color.WHITE);
        tabela.setGridColor(new Color(60, 60, 60));
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setRowHeight(30);
        tabela.setFillsViewportHeight(true);
        tabela.setSelectionBackground(COR_DESTAQUE);
        tabela.setSelectionForeground(Color.BLACK);

        // Estilo do Cabeçalho
        javax.swing.table.JTableHeader header = tabela.getTableHeader();
        header.setBackground(COR_PAINEL_LATERAL);
        header.setForeground(COR_DESTAQUE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(COR_DESTAQUE));

        // ScrollPane com borda invisível
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(COR_FUNDO);
        scroll.setBorder(new EmptyBorder(10, 20, 20, 20)); // Margens laterais

        tela.add(scroll, BorderLayout.CENTER);

        // Botão Fechar
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
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(COR_DESTAQUE)) {
                    btn.setBackground(new Color(70, 70, 70));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(COR_DESTAQUE)) {
                    btn.setBackground(COR_BOTAO_FUNDO);
                }
            }
        });
        return btn;
    }

    private static JTextField criarInputEstilizado(String titulo) {
        JTextField campo = new JTextField();
        campo.setBorder(BorderFactory.createTitledBorder(null, titulo, 0, 0, new Font("Segoe UI", Font.PLAIN, 12),
                Color.WHITE));
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