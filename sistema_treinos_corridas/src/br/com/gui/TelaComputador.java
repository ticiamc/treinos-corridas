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
import java.util.List;

public class TelaComputador {

    // --- CONTROLADORES E DADOS ---
    public static ControladorCliente controladorCliente;
    public static ControladorTreino controladorTreino;
    
    // CPF fixo para teste. Em um sistema real, isso viria de uma tela de Login.
    public static final String CPF_LOGADO = "000.000.000-00"; 

    // --- PALETA DE CORES (MODERNA / FITNESS) ---
    private static final Color COR_FUNDO = new Color(30, 30, 30);         // Cinza Quase Preto
    private static final Color COR_PAINEL_LATERAL = new Color(20, 20, 20);// Preto Suave
    private static final Color COR_DESTAQUE = new Color(74, 255, 86);     // Verde Neon
    private static final Color COR_BOTAO_FUNDO = new Color(50, 50, 50);   // Cinza Médio
    private static final Color COR_TEXTO = new Color(240, 240, 240);      // Branco

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

    public static void criarUI(){
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
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitulo = new JLabel("<html><center>IRON<br>TRACK</center></html>", SwingConstants.CENTER);
        lblTitulo.setFont(FONTE_TITULO);
        lblTitulo.setForeground(COR_DESTAQUE);
        
        JLabel lblSubtitulo = new JLabel("<html><center><br>Performance &<br>Evolução</center></html>", SwingConstants.CENTER);
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
        
        // [ATUALIZAÇÃO] Botão Cadastrar agora chama a tela de cadastro
        JButton btnCadastrar = criarBotaoEstilizado("Cadastrar");
        btnCadastrar.addActionListener(e -> abrirTelaCadastroUsuario());
        janela.add(btnCadastrar, configuracaoGrid(1, 0));

        // [ATUALIZAÇÃO] Botão Notificações agora chama a tela de notificações
        JButton btnNotificacoes = criarBotaoEstilizado("Notificações");
        btnNotificacoes.addActionListener(e -> abrirTelaNotificacoes());
        janela.add(btnNotificacoes, configuracaoGrid(1, 1));
        
        janela.add(criarBotaoEstilizado("Desafios"), configuracaoGrid(1, 2)); // Placeholder
        janela.add(criarBotaoEstilizado("Metas"), configuracaoGrid(1, 3));    // Placeholder

        // Coluna 2
        gbc.gridx = 2;
        janela.add(criarBotaoEstilizado("Perfil"), configuracaoGrid(2, 0));     // Placeholder
        janela.add(criarBotaoEstilizado("Relatórios"), configuracaoGrid(2, 1)); // Placeholder
        janela.add(criarBotaoEstilizado("Planos"), configuracaoGrid(2, 2));     // Placeholder
        
        // Botão Especial TREINOS
        JButton btnTreinos = criarBotaoEstilizado("TREINOS");
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
    // TELA 1: CADASTRO DE USUÁRIO (NOVO)
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
    // TELA 2: NOTIFICAÇÕES (NOVO)
    // ========================================================================
    public static void abrirTelaNotificacoes() {
        // Tenta achar o usuário padrão, senão pede CPF
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

        // Botão para limpar/marcar como lidas
        JButton btnLimpar = criarBotaoEstilizado("Marcar todas como Lidas / Limpar");
        Usuario usuarioFinal = u; 
        
        btnLimpar.addActionListener(e -> {
            for(Notificacao n : usuarioFinal.getNotificacoes()){
                n.setLida(true);
            }
            // Remove as lidas da lista (opcional, para limpar a tela)
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
    // TELA 3: CADASTRO DE TREINO (ATUALIZADO)
    // ========================================================================
    public static void abrirTelaCadastroTreino() {
        JFrame tela = new JFrame("Gerenciar Treinos");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        // --- TOPO ---
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        topo.setBackground(COR_FUNDO);
        
        JButton btnVerTreinos = criarBotaoEstilizado("Ver Histórico");
        btnVerTreinos.addActionListener(e -> {
            // Tenta pegar usuário logado ou pede CPF
            Usuario u = controladorCliente.buscarCliente(CPF_LOGADO);
            if(u == null) {
                String cpfInput = JOptionPane.showInputDialog(tela, "Digite o CPF para ver histórico:");
                if(cpfInput != null) u = controladorCliente.buscarCliente(cpfInput);
            }
            
            if (u != null) {
                List<Treino> treinos = u.getTreinos();
                StringBuilder sb = new StringBuilder();
                if (treinos.isEmpty()) sb.append("Nenhum treino registrado.");
                else for (Treino t : treinos) sb.append(t.toString()).append("\n-------------------\n");
                
                JTextArea area = new JTextArea(sb.toString());
                area.setFont(new Font("Consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(tela, new JScrollPane(area), "Histórico de " + u.getNome(), JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(tela, "Usuário não encontrado.");
            }
        });
        topo.add(btnVerTreinos);
        tela.add(topo, BorderLayout.NORTH);

        // --- CENTRO (Formulário) ---
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBackground(COR_FUNDO);
        painelCentral.setBorder(new EmptyBorder(20, 50, 20, 50));

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
            BorderFactory.createLineBorder(COR_DESTAQUE), " Detalhes ", 
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

        final JTextField[] campoDistancia = {null};
        final JTextField[] campoSeries = {null};
        final JTextField[] campoDescanso = {null};
        final String[] tipoTreino = {null};

        btnCorrida.addActionListener(e -> {
            tipoTreino[0] = "corrida";
            painelDinamico.removeAll();
            JLabel lbl = new JLabel("Distância (metros):");
            lbl.setForeground(Color.WHITE);
            JTextField campo = criarInputEstilizado("0");
            painelDinamico.add(lbl);
            painelDinamico.add(campo);
            campoDistancia[0] = campo;
            painelDinamico.revalidate(); painelDinamico.repaint();
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
            
            campoSeries[0] = c1; campoDescanso[0] = c2;
            painelDinamico.revalidate(); painelDinamico.repaint();
        });

        // LÓGICA DE SALVAR
        btnFinalizar.addActionListener(e -> {
            try {
                // VERIFICAÇÃO DE USUÁRIO
                String cpfAlvo = CPF_LOGADO;
                Usuario usuarioCheck = controladorCliente.buscarCliente(CPF_LOGADO);
                
                if (usuarioCheck == null) {
                    cpfAlvo = JOptionPane.showInputDialog(tela, "Usuário padrão não encontrado. Digite o CPF do atleta:");
                    if (cpfAlvo == null || cpfAlvo.trim().isEmpty()) return; // Cancelou
                    
                    if (controladorCliente.buscarCliente(cpfAlvo) == null) {
                        JOptionPane.showMessageDialog(tela, "CPF não cadastrado no sistema.");
                        return;
                    }
                }

                if (campoNome.getText().isEmpty() || tipoTreino[0] == null) {
                    JOptionPane.showMessageDialog(tela, "Preencha todos os dados e selecione o tipo de treino!"); 
                    return;
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
                
                JOptionPane.showMessageDialog(tela, "Treino salvo com sucesso!\nVerifique suas Notificações para ver se bateu alguma meta.");
                tela.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage());
            }
        });

        tela.setSize(500, 650);
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
        campo.setBorder(BorderFactory.createTitledBorder(null, titulo, 0, 0, new Font("Segoe UI", Font.PLAIN, 12), Color.WHITE));
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(COR_DESTAQUE);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return campo;
    }

    // Auxiliar para labels simples usados no cadastro
    private static JLabel criarLabelSimples(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }

    public static void TelaMetas(){
        JOptionPane.showMessageDialog(null, "Funcionalidade de Metas em desenvolvimento.");
    }

    public static void TelaDesafios(){
        JOptionPane.showMessageDialog(null, "Funcionalidade de Desafios em desenvolvimento.");
    }
}