package br.com.gui;

import br.com.gui.TelasPlanosTreino.TelaPlanosPrincipal;
import br.com.negocio.Fachada;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Notificacao;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;
import br.com.excecoes.CampoVazioException;
import br.com.excecoes.UsuarioNaoEncontradoException;
import br.com.excecoes.ValorInvalidoException;

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

    // Cores e Fontes padronizadas
    private static final Color COR_FUNDO = new Color(30, 30, 30);
    private static final Color COR_DESTAQUE = new Color(74, 255, 86);
    private static final Color COR_PAINEL_LATERAL = new Color(20, 20, 20);
    private static final Color COR_BOTAO_FUNDO = new Color(50, 50, 50);
    private static final Color COR_TEXTO = new Color(240, 240, 240);
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 14);

    public TelaComputador() {
        // Construtor vazio
    }

    public JPanel criarPainelAdmin() {
        JPanel painelGeral = new JPanel(new GridBagLayout());
        painelGeral.setBackground(COR_FUNDO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        // --- PAINEL LATERAL ---
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

        // --- GRID DE BOTÕES ---
        gbc.gridheight = 1; gbc.weightx = 0.35;
        gbc.insets = new Insets(10, 5, 10, 5);

        // Coluna 1
        gbc.gridx = 1; 
        JButton btnCadastrar = criarBotaoEstilizado("Cadastrar Atleta");
        btnCadastrar.addActionListener(e -> abrirTelaCadastroUsuario());
        painelGeral.add(btnCadastrar, configuracaoGrid(1, 0));

        JButton btnNotificacoes = criarBotaoEstilizado("Ver Notificações");
        btnNotificacoes.addActionListener(e -> abrirTelaNotificacoes());
        painelGeral.add(btnNotificacoes, configuracaoGrid(1, 1));
        
        JButton btnDesafios = criarBotaoEstilizado("Gerenciar Desafios");
        btnDesafios.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaGerenciarDesafios().criarPainel()));
        painelGeral.add(btnDesafios, configuracaoGrid(1, 2));

        JButton btnMetas = criarBotaoEstilizado("Gerenciar Metas");
        btnMetas.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaMetas().criarPainel()));
        painelGeral.add(btnMetas, configuracaoGrid(1, 3));

        // Coluna 2
        gbc.gridx = 2;
        JButton btnRelatorios = criarBotaoEstilizado("Relatórios Gerais");
        btnRelatorios.addActionListener(e -> {
             String cpf = JOptionPane.showInputDialog("CPF do atleta para relatório:");
             if(cpf != null) {
                 Usuario u = Fachada.getInstance().getControladorCliente().buscarCliente(cpf);
                 if(u!=null) GerenciadorTelas.getInstance().carregarTela(new TelaRelatorios(u).criarPainel());
                 else JOptionPane.showMessageDialog(painelGeral, "Não encontrado.");
             }
        });
        painelGeral.add(btnRelatorios, configuracaoGrid(2, 0));
        
        JButton btnPlanos = criarBotaoEstilizado("Planos de Treino");
        btnPlanos.addActionListener(e -> {
            TelaPlanosPrincipal telaPlanos = new TelaPlanosPrincipal(Fachada.getInstance().getControladorCliente());
            GerenciadorTelas.getInstance().carregarTela(telaPlanos.criarPainel());
        });
        painelGeral.add(btnPlanos, configuracaoGrid(2, 1));

        JButton btnTreinos = criarBotaoEstilizado("REGISTRAR TREINO");
        btnTreinos.setForeground(Color.BLACK);
        btnTreinos.setBackground(COR_DESTAQUE); 
        btnTreinos.addActionListener(e -> abrirTelaCadastroTreino());
        btnTreinos.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnTreinos.setBackground(Color.WHITE); }
            public void mouseExited(MouseEvent e) { btnTreinos.setBackground(COR_DESTAQUE); }
        });
        painelGeral.add(btnTreinos, configuracaoGrid(2, 2));
        
        JButton btnGerenciarUsers = criarBotaoEstilizado("Listar Usuários");
        btnGerenciarUsers.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaGerenciarUsuarios().criarPainel()));
        painelGeral.add(btnGerenciarUsers, configuracaoGrid(2, 3));

        return painelGeral;
    }

    // --- MÉTODOS AUXILIARES E POPUPS ---

    public static void abrirTelaCadastroUsuario() {
        JDialog d = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Novo Atleta", true);
        d.setLayout(new GridLayout(7, 2, 10, 10));
        JTextField nome = new JTextField();
        JTextField cpf = new JTextField();
        JTextField email = new JTextField();
        JTextField idade = new JTextField();
        JTextField peso = new JTextField();
        JTextField alt = new JTextField();
        
        d.add(new JLabel(" Nome:")); d.add(nome);
        d.add(new JLabel(" CPF:")); d.add(cpf);
        d.add(new JLabel(" Email:")); d.add(email);
        d.add(new JLabel(" Idade:")); d.add(idade);
        d.add(new JLabel(" Peso:")); d.add(peso);
        d.add(new JLabel(" Altura:")); d.add(alt);
        
        JButton btn = new JButton("Salvar");
        btn.addActionListener(e -> {
            try {
                Fachada.getInstance().getControladorCliente().cadastrarCliente(new Usuario(
                    nome.getText(), Integer.parseInt(idade.getText()), 
                    Double.parseDouble(peso.getText()), Double.parseDouble(alt.getText()), email.getText(), cpf.getText()));
                JOptionPane.showMessageDialog(d, "Sucesso!");
                d.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Erro ao cadastrar: " + ex.getMessage());
            }
        });

        d.add(new JLabel("")); d.add(btn);
        d.setSize(450, 400);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

    public static void abrirTelaCadastroTreino() {
        abrirJanelaTreinoBase(null);
    }

    public static void abrirTelaCadastroTreinoUsuarioLogado() {
        Usuario logado = SessaoUsuario.getInstance().getUsuarioLogado();
        if (logado != null) abrirJanelaTreinoBase(logado);
        else JOptionPane.showMessageDialog(null, "Erro: Nenhum usuário logado.");
    }

    private static void abrirJanelaTreinoBase(Usuario usuarioPreDefinido) {
        JDialog tela = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Registrar Treino", true);
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

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
        campoData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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
            BorderFactory.createLineBorder(COR_DESTAQUE), " Detalhes Específicos ", 
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
            JLabel lbl = new JLabel("Distância (metros):"); lbl.setForeground(Color.WHITE);
            JTextField campo = criarInputEstilizado("0");
            painelDinamico.add(lbl); painelDinamico.add(campo);
            campoDistancia[0] = campo;
            painelDinamico.revalidate(); painelDinamico.repaint();
            tela.pack();
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
            tela.pack();
        });

        JTextField finalCampoCpf = campoCpf;
        
        btnFinalizar.addActionListener(e -> {
            try {
                String cpfAlvo = (usuarioPreDefinido != null) ? usuarioPreDefinido.getCpf() : finalCampoCpf.getText();
                if (tipoTreino[0] == null) throw new ValorInvalidoException("Selecione o tipo de treino!");
                
                int dur = Integer.parseInt(campoDuracao.getText()) * 60;
                double dist = 0; int ser = 0, desc = 0;
                
                if (tipoTreino[0].equals("corrida")) dist = Double.parseDouble(campoDistancia[0].getText().replace(",","."));
                else {
                    ser = Integer.parseInt(campoSeries[0].getText());
                    desc = Integer.parseInt(campoDescanso[0].getText());
                }

                Fachada.getInstance().getControladorTreino().cadastrarTreino(cpfAlvo, 
                    tipoTreino[0].equals("corrida") ? "Corrida" : "Intervalado", 
                    LocalDate.parse(campoData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
                    dur, campoNome.getText(), dist, ser, desc);

                JOptionPane.showMessageDialog(tela, "Treino registrado!");
                tela.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage());
            }
        });
        tela.setSize(500, 600);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    public static void abrirTelaNotificacoes() {
        // Implementação simplificada usando Dialogo
        Usuario u = SessaoUsuario.getInstance().isUsuarioLogado() ? 
            SessaoUsuario.getInstance().getUsuarioLogado() : 
            Fachada.getInstance().getControladorCliente().buscarCliente(JOptionPane.showInputDialog("CPF:"));

        if (u == null) return;

        JDialog d = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Notificações", true);
        DefaultListModel<String> model = new DefaultListModel<>();
        for(Notificacao n : u.getNotificacoes()) model.addElement(n.toString());
        d.add(new JScrollPane(new JList<>(model)));
        
        JButton btnOk = new JButton("Marcar como Lidas");
        btnOk.addActionListener(e -> {
            Fachada.getInstance().getControladorCliente().verNotificacoes(u);
            d.dispose();
        });
        d.add(btnOk, BorderLayout.SOUTH);
        d.setSize(400,300);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

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
}