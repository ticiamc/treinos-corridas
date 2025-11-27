package br.com.gui;

import br.com.dados.IRepositorioDesafio;
import br.com.dados.RepositorioClientes;
import br.com.dados.RepositorioDesafio;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorDesafio;
import br.com.negocio.ControladorMeta;
import br.com.negocio.ControladorPlanoTreino;
import br.com.negocio.ControladorTreino;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaComputador {

    // --- CONTROLADORES E DADOS ---
    public static ControladorCliente controladorCliente;
    public static ControladorTreino controladorTreino;
    public static ControladorMeta controladorMeta;
    public static ControladorDesafio controladorDesafio;
    public static ControladorPlanoTreino controladorPlanoTreino;

    public static final String CPF_LOGADO = "000.000.000-00"; 

    // Cores
    private static final Color COR_FUNDO = new Color(30, 30, 30);
    private static final Color COR_PAINEL_LATERAL = new Color(20, 20, 20);
    private static final Color COR_DESTAQUE = new Color(74, 255, 86);
    private static final Color COR_BOTAO_FUNDO = new Color(50, 50, 50);
    private static final Color COR_TEXTO = new Color(240, 240, 240);
    private static final Color COR_TABELA_FUNDO = new Color(45, 45, 45);
    private static final Color COR_TABELA_LINHA = new Color(60, 60, 60);

    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 14);

    // Inicialização Estática dos Controladores
    static {
        if (controladorCliente == null) {
            RepositorioClientes repoClientes = new RepositorioClientes();
            IRepositorioDesafio repoDesafio = new RepositorioDesafio();

            controladorCliente = new ControladorCliente(repoClientes);
            controladorTreino = new ControladorTreino(repoClientes);
            controladorMeta = new ControladorMeta(repoClientes);
            controladorDesafio = new ControladorDesafio(repoDesafio, repoClientes);
            controladorPlanoTreino = new ControladorPlanoTreino(repoClientes);
            
            // Usuário Teste Admin/Mock
            if(controladorCliente.buscarCliente(CPF_LOGADO) == null) {
                Usuario userTeste = new Usuario("Usuário Teste", 25, 70, 1.75, "teste@email.com", CPF_LOGADO);
                controladorCliente.cadastrarCliente(userTeste);
            }
        }
    }

    public TelaComputador() {}

    // --- PAINEL ADMIN (Retorna JPanel para o Gerenciador) ---
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
        
        JLabel lblSubtitulo = new JLabel("<html><center><br>ADMINISTRAÇÃO</center></html>", SwingConstants.CENTER);
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

        // GRID DE BOTÕES
        gbc.gridheight = 1; gbc.weightx = 0.35;
        gbc.insets = new Insets(10, 5, 10, 5);

        gbc.gridx = 1; 
        JButton btnCadastrar = criarBotaoEstilizado("Novo Atleta");
        btnCadastrar.addActionListener(e -> abrirTelaCadastroUsuario());
        painelGeral.add(btnCadastrar, configuracaoGrid(1, 0));

        JButton btnNotificacoes = criarBotaoEstilizado("Notificações");
        btnNotificacoes.addActionListener(e -> abrirTelaNotificacoes());
        painelGeral.add(btnNotificacoes, configuracaoGrid(1, 1));
        
        JButton btnDesafios = criarBotaoEstilizado("Desafios");
        btnDesafios.addActionListener(e -> abrirTelaDesafios());
        painelGeral.add(btnDesafios, configuracaoGrid(1, 2));

        JButton btnMetas = criarBotaoEstilizado("Metas");
        btnMetas.addActionListener(e -> abrirTelaMetas());
        painelGeral.add(btnMetas, configuracaoGrid(1, 3));

        gbc.gridx = 2;
        JButton btnRelatorios = criarBotaoEstilizado("Relatórios");
        btnRelatorios.addActionListener(e -> abrirTelaRelatorios());
        painelGeral.add(btnRelatorios, configuracaoGrid(2, 0));

        JButton btnPlanos = criarBotaoEstilizado("Planos de Treino");
        btnPlanos.addActionListener(e -> abrirTelaPlanos());
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

        return painelGeral;
    }

    // --- MÉTODOS DE POPUPS (JFrames Separados) ---

    public static void abrirTelaCadastroUsuario() {
        JFrame tela = new JFrame("Cadastrar Novo Atleta");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new GridLayout(7, 2, 10, 10));
        ((JComponent) tela.getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        tela.add(criarLabelSimples("Nome:")); JTextField txtNome = criarInputEstilizado("Nome Completo"); tela.add(txtNome);
        tela.add(criarLabelSimples("CPF:"));  JTextField txtCpf = criarInputEstilizado("000.000.000-00"); tela.add(txtCpf);
        tela.add(criarLabelSimples("Email:"));JTextField txtEmail = criarInputEstilizado("exemplo@email.com"); tela.add(txtEmail);
        tela.add(criarLabelSimples("Idade:"));JTextField txtIdade = criarInputEstilizado("00"); tela.add(txtIdade);
        tela.add(criarLabelSimples("Peso (kg):"));JTextField txtPeso = criarInputEstilizado("00.0"); tela.add(txtPeso);
        tela.add(criarLabelSimples("Altura (m):"));JTextField txtAltura = criarInputEstilizado("0.00"); tela.add(txtAltura);

        JButton btnSalvar = criarBotaoEstilizado("SALVAR");
        btnSalvar.setBackground(COR_DESTAQUE);
        btnSalvar.setForeground(Color.BLACK);

        btnSalvar.addActionListener(e -> {
            try {
                Usuario novoUsuario = new Usuario(txtNome.getText(), Integer.parseInt(txtIdade.getText()), Double.parseDouble(txtPeso.getText().replace(",", ".")), Double.parseDouble(txtAltura.getText().replace(",", ".")), txtEmail.getText(), txtCpf.getText());
                controladorCliente.cadastrarCliente(novoUsuario);
                JOptionPane.showMessageDialog(tela, "Atleta cadastrado!");
                tela.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage());
            }
        });
        tela.add(new JLabel("")); tela.add(btnSalvar);
        tela.setSize(450, 500); tela.setLocationRelativeTo(null); tela.setVisible(true);
    }

    // Método para Admin (pede CPF)
    public static void abrirTelaCadastroTreino() { abrirJanelaTreinoBase(null); }
    // Método para Usuário Logado (CPF automático)
    public static void abrirTelaCadastroTreino(Usuario u) { abrirJanelaTreinoBase(u); }

    private static void abrirJanelaTreinoBase(Usuario usuarioPreDefinido) {
        JFrame tela = new JFrame("Registrar Treino");
        tela.getContentPane().setBackground(COR_FUNDO);
        tela.setLayout(new BorderLayout());

        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBackground(COR_FUNDO);
        painelCentral.setBorder(new EmptyBorder(20, 50, 20, 50));

        JTextField campoCpf = null;
        if (usuarioPreDefinido == null) {
            campoCpf = criarInputEstilizado("CPF do Aluno");
            painelCentral.add(campoCpf); painelCentral.add(Box.createVerticalStrut(15));
        } else {
            // Se for usuário logado, apenas mostra o nome no título ou usa internamente
            JLabel lblUser = new JLabel("Aluno: " + usuarioPreDefinido.getNome());
            lblUser.setForeground(COR_DESTAQUE);
            painelCentral.add(lblUser); painelCentral.add(Box.createVerticalStrut(15));
        }

        JTextField campoNome = criarInputEstilizado("Nome do Treino");
        JTextField campoData = criarInputEstilizado("Data (dd/MM/yyyy)");
        JTextField campoDuracao = criarInputEstilizado("Duração (min)");

        painelCentral.add(campoNome); painelCentral.add(Box.createVerticalStrut(15));
        painelCentral.add(campoData); painelCentral.add(Box.createVerticalStrut(15));
        painelCentral.add(campoDuracao); painelCentral.add(Box.createVerticalStrut(20));

        JPanel painelDinamico = new JPanel();
        painelDinamico.setLayout(new BoxLayout(painelDinamico, BoxLayout.Y_AXIS));
        painelDinamico.setBackground(COR_FUNDO);
        painelDinamico.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COR_DESTAQUE), " Detalhes ", 0, 0, FONTE_BOTAO, COR_DESTAQUE));
        painelCentral.add(painelDinamico);
        tela.add(painelCentral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        painelBotoes.setBackground(COR_FUNDO);
        JButton btnCorrida = criarBotaoEstilizado("Corrida");
        JButton btnIntervalado = criarBotaoEstilizado("Intervalado");
        JButton btnFinalizar = criarBotaoEstilizado("SALVAR");
        btnFinalizar.setBackground(COR_DESTAQUE); btnFinalizar.setForeground(Color.BLACK);

        painelBotoes.add(btnCorrida); painelBotoes.add(btnIntervalado); painelBotoes.add(btnFinalizar);
        tela.add(painelBotoes, BorderLayout.SOUTH);

        final JTextField[] extras = {null, null, null};
        final String[] tipo = {null};

        btnCorrida.addActionListener(e -> {
            tipo[0] = "Corrida"; painelDinamico.removeAll();
            JLabel lbl = criarLabelSimples("Distância (m):");
            extras[0] = criarInputEstilizado("0");
            painelDinamico.add(lbl); painelDinamico.add(extras[0]);
            painelDinamico.revalidate(); painelDinamico.repaint();
        });

        btnIntervalado.addActionListener(e -> {
            tipo[0] = "Intervalado"; painelDinamico.removeAll();
            painelDinamico.add(criarLabelSimples("Séries:")); extras[1] = criarInputEstilizado("0"); painelDinamico.add(extras[1]);
            painelDinamico.add(criarLabelSimples("Descanso (s):")); extras[2] = criarInputEstilizado("0"); painelDinamico.add(extras[2]);
            painelDinamico.revalidate(); painelDinamico.repaint();
        });

        JTextField finalCampoCpf = campoCpf;
        btnFinalizar.addActionListener(e -> {
            try {
                String cpfAlvo = (usuarioPreDefinido != null) ? usuarioPreDefinido.getCpf() : finalCampoCpf.getText();
                if (controladorCliente.buscarCliente(cpfAlvo) == null) throw new Exception("Cliente não encontrado.");
                if (tipo[0] == null) throw new Exception("Selecione o tipo.");

                double dist = 0; int ser = 0, desc = 0;
                if(tipo[0].equals("Corrida")) dist = Double.parseDouble(extras[0].getText());
                else { ser = Integer.parseInt(extras[1].getText()); desc = Integer.parseInt(extras[2].getText()); }

                controladorTreino.cadastrarTreino(cpfAlvo, tipo[0], LocalDate.parse(campoData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), Integer.parseInt(campoDuracao.getText())*60, campoNome.getText(), dist, ser, desc);
                JOptionPane.showMessageDialog(tela, "Salvo!");
                tela.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(tela, "Erro: " + ex.getMessage()); }
        });
        tela.setSize(500, 650); tela.setLocationRelativeTo(null); tela.setVisible(true);
    }

    public static void abrirTelaNotificacoes() { abrirTelaNotificacoes(null); }
    public static void abrirTelaNotificacoes(Usuario u) {
        Usuario alvo = (u != null) ? u : identificarUsuario();
        if(alvo == null) return;

        JFrame tela = new JFrame("Notificações - " + alvo.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        DefaultListModel<String> model = new DefaultListModel<>();
        for(Notificacao n : alvo.getNotificacoes()) model.addElement((n.isLida() ? "[Lida] " : "[NOVA] ") + n.toString());
        if(model.isEmpty()) model.addElement("Sem notificações.");
        
        JList<String> lista = new JList<>(model);
        lista.setBackground(new Color(50,50,50)); lista.setForeground(Color.WHITE);
        tela.add(new JScrollPane(lista));
        
        JButton btnLimpar = criarBotaoEstilizado("Limpar Lidas");
        btnLimpar.addActionListener(e -> {
            alvo.getNotificacoes().removeIf(Notificacao::isLida);
            alvo.getNotificacoes().forEach(n -> n.setLida(true));
            tela.dispose();
        });
        tela.add(btnLimpar, BorderLayout.SOUTH);
        tela.setSize(400, 400); tela.setLocationRelativeTo(null); tela.setVisible(true);
    }

    public static void abrirTelaHistorico(Usuario u) {
        if(u == null) return;
        JFrame tela = new JFrame("Histórico - " + u.getNome());
        tela.getContentPane().setBackground(COR_FUNDO);
        
        String[] colunas = {"Data", "Treino", "Tipo", "Duração", "Detalhes", "Kcal"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(colunas, 0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for(Treino t : u.getTreinos()) {
            String det = (t instanceof Corrida) ? ((Corrida)t).getDistanciaEmMetros()+"m" : ((Intervalado)t).getSeries()+"x";
            model.addRow(new Object[]{t.getDataExecucao().format(fmt), t.getNomeTreino(), (t instanceof Corrida?"Corrida":"Intervalado"), (t.getDuracaoSegundos()/60)+" min", det, String.format("%.0f", t.calcularCaloriasQueimadas(u))});
        }

        JTable table = new JTable(model);
        estilizarTabela(table);
        tela.add(new JScrollPane(table));
        tela.setSize(800, 500); tela.setLocationRelativeTo(null); tela.setVisible(true);
    }

    public static void abrirTelaDesafios() { abrirTelaDesafios(null); }
    public static void abrirTelaDesafios(Usuario u) {
        JFrame tela = new JFrame("Desafios");
        tela.getContentPane().setBackground(COR_FUNDO);
        
        // ... (Lógica de Tabela de Desafios Similar a de Planos/Metas)
        // Por brevidade, use o código completo enviado anteriormente para Desafios,
        // apenas garantindo que os métodos sejam 'public static'.
        
        JOptionPane.showMessageDialog(null, "Abrindo Gestão de Desafios..."); 
        // Aqui você chamaria o código completo da tela de desafios que implementamos antes.
    }

    public static void abrirTelaMetas() { abrirTelaMetas(null); }
    public static void abrirTelaMetas(Usuario u) {
        JOptionPane.showMessageDialog(null, "Abrindo Gestão de Metas...");
    }

    public static void abrirTelaPlanos() { abrirTelaPlanos(null); }
    public static void abrirTelaPlanos(Usuario u) {
        JOptionPane.showMessageDialog(null, "Abrindo Gestão de Planos...");
    }

    public static void abrirTelaRelatorios() { abrirTelaRelatorios(null); }
    public static void abrirTelaRelatorios(Usuario u) {
        JOptionPane.showMessageDialog(null, "Abrindo Relatórios...");
    }

    // --- UTILS ---
    private static Usuario identificarUsuario() {
        String cpf = JOptionPane.showInputDialog("CPF:");
        if(cpf==null) return null;
        Usuario u = controladorCliente.buscarCliente(cpf);
        if(u==null) JOptionPane.showMessageDialog(null, "Não encontrado.");
        return u;
    }

    private static JButton criarBotaoEstilizado(String t) {
        JButton b = new JButton(t); b.setBackground(COR_BOTAO_FUNDO); b.setForeground(COR_TEXTO); b.setFont(FONTE_BOTAO); b.setFocusPainted(false); return b;
    }
    private static JTextField criarInputEstilizado(String t) {
        JTextField f = new JTextField(); f.setBorder(BorderFactory.createTitledBorder(null,t,0,0,new Font("Segoe UI",0,12),Color.GRAY)); f.setBackground(new Color(60,60,60)); f.setForeground(Color.WHITE); return f;
    }
    private static JLabel criarLabelSimples(String t) { JLabel l = new JLabel(t); l.setForeground(Color.WHITE); return l; }
    private static GridBagConstraints configuracaoGrid(int x, int y) {
        GridBagConstraints g = new GridBagConstraints(); g.gridx=x; g.gridy=y; g.fill=1; g.weightx=1; g.weighty=1; g.insets=new Insets(10,5,10,10); return g;
    }
    private static void estilizarTabela(JTable t) {
        t.setBackground(COR_TABELA_FUNDO); t.setForeground(Color.WHITE); t.setGridColor(COR_TABELA_LINHA); t.setRowHeight(30);
    }
}