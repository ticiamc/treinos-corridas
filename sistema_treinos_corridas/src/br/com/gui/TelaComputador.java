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
    private static final Color COR_FUNDO = new Color(30, 30, 30);
    private static final Color COR_DESTAQUE = new Color(74, 255, 86);
    private static final Color COR_PAINEL_LATERAL = new Color(20, 20, 20);
    private static final Color COR_BOTAO_FUNDO = new Color(50, 50, 50);
    private static final Color COR_TEXTO = new Color(240, 240, 240);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 16);

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
        btnSair.addActionListener(e -> {
            SessaoUsuario.getInstance().logout();
            GerenciadorTelas.getInstance().carregarTela(new TelaLogin().criarPainelLogin());
        });
        lateral.add(Box.createVerticalGlue());
        lateral.add(btnSair);
        painelGeral.add(lateral, BorderLayout.WEST);
        
        JPanel grid = new JPanel(new GridLayout(3, 3, 10, 10));
        grid.setBackground(COR_FUNDO);
        grid.setBorder(new EmptyBorder(20,20,20,20));
        
        JButton btnCadUser = criarBotaoEstilizado("Cadastrar Usuário");
        btnCadUser.addActionListener(e -> abrirTelaCadastroUsuario());
        JButton btnCriarDesafio = criarBotaoEstilizado("Criar Desafio");
        btnCriarDesafio.addActionListener(e -> TelaDesafios.abrirDialogoCriarDesafio());
        
        grid.add(btnCadUser);
        grid.add(btnCriarDesafio);
        painelGeral.add(grid, BorderLayout.CENTER);
        return painelGeral;
    }

    public static void abrirTelaCadastroUsuario() {
        JFrame tela = new JFrame("Novo Atleta");
        tela.setLayout(new GridLayout(7, 2));
        JTextField nome = new JTextField();
        JTextField cpf = new JTextField();
        JTextField email = new JTextField();
        JTextField idade = new JTextField();
        JTextField peso = new JTextField();
        JTextField alt = new JTextField();
        
        tela.add(new JLabel("Nome:")); tela.add(nome);
        tela.add(new JLabel("CPF:")); tela.add(cpf);
        tela.add(new JLabel("Email:")); tela.add(email);
        tela.add(new JLabel("Idade:")); tela.add(idade);
        tela.add(new JLabel("Peso:")); tela.add(peso);
        tela.add(new JLabel("Altura:")); tela.add(alt);
        
        JButton btn = new JButton("Salvar");
        btn.addActionListener(e -> {
            try {
                controladorCliente.cadastrarCliente(new Usuario(
                    nome.getText(), Integer.parseInt(idade.getText()), 
                    Double.parseDouble(peso.getText()), Double.parseDouble(alt.getText()), email.getText(), cpf.getText()));
                JOptionPane.showMessageDialog(tela, "Sucesso!");
                tela.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage()); }
        });
        tela.add(btn);
        tela.setSize(400,400);
        tela.setVisible(true);
    }
    
    public static void abrirTelaCadastroTreinoUsuarioLogado() {
        Usuario logado = SessaoUsuario.getInstance().getUsuarioLogado();
        if (logado != null) abrirJanelaTreinoBase(logado);
        else JOptionPane.showMessageDialog(null, "Erro: Nenhum usuário logado.");
    }

    private static void abrirJanelaTreinoBase(Usuario usuarioPreDefinido) {
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
            } catch (Exception ex) { JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage()); }
        });
        tela.setSize(500, 700);
        tela.setVisible(true);
    }
    
    public static void abrirTelaNotificacoes() {
        Usuario u = SessaoUsuario.getInstance().getUsuarioLogado();
        if (u == null) return;
        JFrame tela = new JFrame("Notificações");
        tela.getContentPane().setBackground(COR_FUNDO);
        DefaultListModel<String> model = new DefaultListModel<>();
        for(Notificacao n : u.getNotificacoes()) model.addElement((n.isLida() ? "[Lida] " : "[NOVA] ") + n.toString());
        
        tela.add(new JScrollPane(new JList<>(model)));
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> { u.getNotificacoes().clear(); tela.dispose(); });
        tela.add(btnLimpar, BorderLayout.SOUTH);
        tela.setSize(400,300);
        tela.setVisible(true);
    }

    public static void TelaMetas() { GerenciadorTelas.getInstance().carregarTela(new TelaMetas().criarPainel()); }
    public static void TelaDesafios() { GerenciadorTelas.getInstance().carregarTela(new TelaDesafios().criarPainel()); }
    
    private static JButton criarBotaoEstilizado(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setForeground(COR_TEXTO);
        btn.setBackground(COR_BOTAO_FUNDO);
        return btn;
    }
    private static JTextField criarInputEstilizado(String titulo) {
        JTextField campo = new JTextField();
        campo.setBorder(BorderFactory.createTitledBorder(null, titulo, 0, 0, new Font("Segoe UI", Font.PLAIN, 12), Color.WHITE));
        campo.setBackground(new Color(60, 60, 60));
        campo.setForeground(Color.WHITE);
        return campo;
    }
}