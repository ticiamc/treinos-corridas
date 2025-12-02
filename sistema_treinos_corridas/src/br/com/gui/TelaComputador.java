package br.com.gui;

import br.com.dados.RepositorioClientes;
import br.com.dados.RepositorioDesafio;
import br.com.dados.RepositorioPlanoTreino;
import br.com.negocio.*;
import br.com.negocio.treinos.*;
import br.com.excecoes.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaComputador {

    public static ControladorCliente controladorCliente;
    public static ControladorTreino controladorTreino;
    public static ControladorMeta controladorMeta;
    public static ControladorDesafio controladorDesafio;
    public static ControladorPlanoTreino controladorPlanoTreino;
    
    public static final String CPF_LOGADO = "000.000.000-00"; 
    public static final Color COR_FUNDO = new Color(30, 30, 30);
    public static final Color COR_DESTAQUE = new Color(74, 255, 86);
    public static final Color COR_PAINEL_LATERAL = new Color(20, 20, 20);
    public static final Color COR_BOTAO_FUNDO = new Color(50, 50, 50);
    public static final Color COR_TEXTO = new Color(240, 240, 240);
    public static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 16);

    static {
        if (controladorCliente == null) {
            RepositorioClientes repoCli = new RepositorioClientes();
            RepositorioDesafio repoDesafio = new RepositorioDesafio();
            RepositorioPlanoTreino repoPlanos = new RepositorioPlanoTreino();
            
            controladorCliente = new ControladorCliente(repoCli);
            controladorTreino = new ControladorTreino(repoCli);
            controladorMeta = new ControladorMeta(repoCli);
            controladorDesafio = new ControladorDesafio(repoDesafio, repoCli);
            controladorPlanoTreino = new ControladorPlanoTreino(repoCli, repoPlanos);
            
            if(controladorCliente.buscarCliente(CPF_LOGADO) == null) {
                Usuario userTeste = new Usuario("Usuário Teste", 25, 70, 1.75, "teste@email.com", CPF_LOGADO);
                controladorCliente.cadastrarCliente(userTeste);
            }
        }
    }

    public TelaComputador() {}

    public JPanel criarPainelAdmin() {
        JPanel painelGeral = new JPanel(new BorderLayout());
        painelGeral.setBackground(COR_FUNDO);
        
        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBackground(COR_PAINEL_LATERAL);
        lateral.setBorder(new EmptyBorder(20, 20, 20, 20));
        lateral.setPreferredSize(new Dimension(200, 0));
        
        JLabel lbl = new JLabel("ADMIN");
        lbl.setForeground(COR_DESTAQUE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lateral.add(lbl);
        lateral.add(Box.createVerticalStrut(30));
        
        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(Color.RED);
        btnSair.setForeground(Color.WHITE);
        btnSair.addActionListener(e -> {
            SessaoUsuario.getInstance().logout();
            GerenciadorTelas.getInstance().carregarTela(new TelaLogin().criarPainelLogin());
        });
        lateral.add(Box.createVerticalGlue());
        lateral.add(btnSair);
        painelGeral.add(lateral, BorderLayout.WEST);
        
        JPanel grid = new JPanel(new GridLayout(3, 2, 10, 10));
        grid.setBackground(COR_FUNDO);
        grid.setBorder(new EmptyBorder(20,20,20,20));
        
        JButton btnCadUser = criarBotaoEstilizado("Cadastrar Usuário");
        btnCadUser.addActionListener(e -> abrirTelaCadastroUsuario());
        
        JButton btnGerenciarUsers = criarBotaoEstilizado("Gerenciar Usuários");
        btnGerenciarUsers.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaGerenciarUsuarios().criarPainel()));

        JButton btnGerenciarDesafios = criarBotaoEstilizado("Gerenciar Desafios");
        btnGerenciarDesafios.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaGerenciarDesafios().criarPainel()));
        
        grid.add(btnCadUser);
        grid.add(btnGerenciarUsers);
        grid.add(btnGerenciarDesafios);
        painelGeral.add(grid, BorderLayout.CENTER);
        return painelGeral;
    }

    // --- CORREÇÃO 1: Tela de Cadastro de Usuário (Admin) ---
    public static void abrirTelaCadastroUsuario() {
        JFrame tela = new JFrame("Novo Atleta");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new GridLayout(7, 2, 10, 10));
        ((JPanel)tela.getContentPane()).setBorder(new EmptyBorder(20,20,20,20));

        JTextField nome = criarInputEstilizado("Nome");
        JTextField cpf = criarInputEstilizado("CPF (000.000.000-00)");
        JTextField email = criarInputEstilizado("Email");
        JTextField idade = criarInputEstilizado("Idade");
        JTextField peso = criarInputEstilizado("Peso (kg)");
        JTextField alt = criarInputEstilizado("Altura (m)");
        
        tela.add(criarLabel("Nome:")); tela.add(nome);
        tela.add(criarLabel("CPF:")); tela.add(cpf);
        tela.add(criarLabel("Email:")); tela.add(email);
        tela.add(criarLabel("Idade:")); tela.add(idade);
        tela.add(criarLabel("Peso:")); tela.add(peso);
        tela.add(criarLabel("Altura:")); tela.add(alt);
        
        JButton btn = criarBotaoEstilizado("Salvar");
        btn.setBackground(COR_DESTAQUE);
        btn.setForeground(Color.BLACK);
        
        btn.addActionListener(e -> {
            try {
                controladorCliente.cadastrarCliente(new Usuario(
                    nome.getText(), Integer.parseInt(idade.getText()), 
                    Double.parseDouble(peso.getText()), Double.parseDouble(alt.getText()), email.getText(), cpf.getText()));
                JOptionPane.showMessageDialog(tela, "Usuário cadastrado com sucesso!");
                tela.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage()); }
        });
        tela.add(new JLabel("")); // Espaçador
        tela.add(btn);
        tela.setSize(450,500);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }
    
    // --- CORREÇÃO 2: Tela de Notificações ---
    public static void abrirTelaNotificacoes() {
        Usuario u = SessaoUsuario.getInstance().getUsuarioLogado();
        if (u == null) return;
        
        JFrame tela = new JFrame("Notificações");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());
        
        DefaultListModel<String> model = new DefaultListModel<>();
        for(Notificacao n : u.getNotificacoes()) {
            model.addElement((n.isLida() ? "[Lida] " : "[NOVA] ") + n.toString());
        }
        
        JList<String> lista = new JList<>(model);
        lista.setBackground(new Color(50, 50, 50));
        lista.setForeground(COR_TEXTO);
        lista.setFont(new Font("Consolas", Font.PLAIN, 12));
        lista.setSelectionBackground(COR_DESTAQUE);
        lista.setSelectionForeground(Color.BLACK);
        
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60,60,60)));
        tela.add(scroll, BorderLayout.CENTER);
        
        JButton btnLimpar = criarBotaoEstilizado("Limpar Tudo");
        btnLimpar.setBackground(new Color(180, 50, 50)); // Vermelho
        btnLimpar.addActionListener(e -> { 
            u.getNotificacoes().clear(); 
            tela.dispose(); 
        });
        
        JPanel pnlSul = new JPanel();
        pnlSul.setBackground(COR_FUNDO);
        pnlSul.add(btnLimpar);
        tela.add(pnlSul, BorderLayout.SOUTH);
        
        tela.setSize(500,400);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    public static void abrirTelaCadastroTreinoUsuarioLogado(Runnable onSucesso) {
        Usuario logado = SessaoUsuario.getInstance().getUsuarioLogado();
        if (logado != null) abrirJanelaTreinoBase(logado, onSucesso);
        else JOptionPane.showMessageDialog(null, "Erro: Nenhum usuário logado.");
    }

    public static void abrirTelaCadastroTreinoUsuarioLogado() {
        abrirTelaCadastroTreinoUsuarioLogado(null);
    }

    private static void abrirJanelaTreinoBase(Usuario usuarioPreDefinido, Runnable onSucesso) {
        JFrame tela = new JFrame("Registrar Treino");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBackground(COR_FUNDO);
        painelCentral.setBorder(new EmptyBorder(20, 50, 20, 50));

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
        painelCentral.add(painelDinamico);
        tela.add(painelCentral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        painelBotoes.setBackground(COR_FUNDO);
        JButton btnCorrida = criarBotaoEstilizado("Corrida");
        JButton btnIntervalado = criarBotaoEstilizado("Intervalado");
        JButton btnFinalizar = criarBotaoEstilizado("SALVAR");
        btnFinalizar.setBackground(COR_DESTAQUE);
        btnFinalizar.setForeground(Color.BLACK);
        
        JButton btnCancelar = criarBotaoEstilizado("Cancelar");
        btnCancelar.setBackground(new Color(180, 50, 50));
        btnCancelar.addActionListener(e -> tela.dispose());

        painelBotoes.add(btnCorrida);
        painelBotoes.add(btnIntervalado);
        painelBotoes.add(btnFinalizar);
        painelBotoes.add(btnCancelar);
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

        btnFinalizar.addActionListener(e -> {
            try {
                if (tipoTreino[0] == null) throw new ValorInvalidoException("Selecione o tipo!");
                LocalDate data = LocalDate.parse(campoData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int duracao = Integer.parseInt(campoDuracao.getText()) * 60;
                
                if (tipoTreino[0].equals("corrida")) {
                    double dist = Double.parseDouble(campoDistancia[0].getText().replace(",", "."));
                    controladorTreino.cadastrarTreino(usuarioPreDefinido.getCpf(), "Corrida", data, duracao, campoNome.getText(), dist, 0, 0);
                } else {
                    int ser = Integer.parseInt(campoSeries[0].getText());
                    int desc = Integer.parseInt(campoDescanso[0].getText());
                    controladorTreino.cadastrarTreino(usuarioPreDefinido.getCpf(), "Intervalado", data, duracao, campoNome.getText(), 0, ser, desc);
                }
                JOptionPane.showMessageDialog(tela, "Registrado!");
                tela.dispose();
                
                if (onSucesso != null) {
                    onSucesso.run();
                }
                
            } catch (Exception ex) { JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage()); }
        });
        tela.setSize(500, 700);
        tela.setVisible(true);
    }
    
    public static void abrirTelaHistorico(Usuario u) {
        GerenciadorTelas.getInstance().carregarTela(new TelaRelatorios(u).criarPainel());
    }

    public static void TelaMetas() { GerenciadorTelas.getInstance().carregarTela(new TelaMetas().criarPainel()); }
    public static void TelaDesafios() { GerenciadorTelas.getInstance().carregarTela(new TelaDesafios().criarPainel()); }
    
    // --- Métodos Auxiliares de Estilo ---
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
        campo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100,100,100)), 
            titulo, 0, 0, new Font("Segoe UI", Font.PLAIN, 12), Color.WHITE));
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(COR_DESTAQUE);
        return campo;
    }
    
    private static JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }
}