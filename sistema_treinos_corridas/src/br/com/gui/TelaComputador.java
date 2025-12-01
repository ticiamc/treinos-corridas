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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TelaComputador {

    // Cores e Fontes
    private static final Color COR_FUNDO = new Color(30, 30, 30);
    private static final Color COR_DESTAQUE = new Color(74, 255, 86);
    private static final Color COR_PAINEL_LATERAL = new Color(20, 20, 20);
    private static final Color COR_BOTAO_FUNDO = new Color(50, 50, 50);
    private static final Color COR_TEXTO = new Color(240, 240, 240);
    private static final Color COR_TABELA_FUNDO = new Color(45, 45, 45);
    private static final Color COR_TABELA_LINHA = new Color(60, 60, 60);
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 16);

    public TelaComputador() {
        // A Fachada já garante a inicialização dos controladores
    }

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

        // BOTÕES
        gbc.gridheight = 1; gbc.weightx = 0.35;
        gbc.insets = new Insets(10, 5, 10, 5);

        // Coluna 1 de Botões
        gbc.gridx = 1; 
        JButton btnCadastrar = criarBotaoEstilizado("Cadastrar Atleta");
        btnCadastrar.addActionListener(e -> abrirTelaCadastroUsuario());
        painelGeral.add(btnCadastrar, configuracaoGrid(1, 0));

        JButton btnNotificacoes = criarBotaoEstilizado("Ver Notificações");
        btnNotificacoes.addActionListener(e -> abrirTelaNotificacoes());
        painelGeral.add(btnNotificacoes, configuracaoGrid(1, 1));
        
        JButton btnDesafios = criarBotaoEstilizado("Gerenciar Desafios");
        btnDesafios.addActionListener(e -> TelaDesafios()); // Chama método auxiliar
        painelGeral.add(btnDesafios, configuracaoGrid(1, 2));

        JButton btnMetas = criarBotaoEstilizado("Gerenciar Metas");
        btnMetas.addActionListener(e -> TelaMetas()); // Chama método auxiliar
        painelGeral.add(btnMetas, configuracaoGrid(1, 3));

        // Coluna 2 de Botões
        gbc.gridx = 2;
        // Botão para Tela de Relatórios e Exportação
        JButton btnRelatorios = criarBotaoEstilizado("Relatórios Gerais");
        btnRelatorios.addActionListener(e -> {
             // Exemplo: Abre relatório do usuário logado ou pede CPF
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
        
        // Botão extra para gerenciar usuários
        JButton btnGerenciarUsers = criarBotaoEstilizado("Listar Usuários");
        btnGerenciarUsers.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaGerenciarUsuarios().criarPainel()));
        painelGeral.add(btnGerenciarUsers, configuracaoGrid(2, 3));

        return painelGeral;
    }

    // --- MÉTODOS DE JANELAS SECUNDÁRIAS ---

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
                // USO DA FACHADA
                Fachada.getInstance().getControladorCliente().cadastrarCliente(new Usuario(
                    nome.getText(), Integer.parseInt(idade.getText()), 
                    Double.parseDouble(peso.getText()), Double.parseDouble(alt.getText()), email.getText(), cpf.getText()));
                JOptionPane.showMessageDialog(tela, "Sucesso!");
                tela.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro ao cadastrar: " + ex.getMessage());
            }
        });

        tela.add(new JLabel("")); tela.add(btn);
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
                if (cpfConsulta != null && !cpfConsulta.trim().isEmpty()) {
                    // USO DA FACHADA
                    Usuario u = Fachada.getInstance().getControladorCliente().buscarCliente(cpfConsulta);
                    if (u != null) abrirTelaHistorico(u);
                    else JOptionPane.showMessageDialog(tela, "Usuário não encontrado.");
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
            JLabel lbl2 = new JLabel("Descanso (segundos):"); lbl2.setForeground(Color.WHITE);
            JTextField c2 = criarInputEstilizado("0");
            painelDinamico.add(lbl1); painelDinamico.add(c1);
            painelDinamico.add(Box.createVerticalStrut(10));
            painelDinamico.add(lbl2); painelDinamico.add(c2);
            campoSeries[0] = c1; campoDescanso[0] = c2;
            painelDinamico.revalidate(); painelDinamico.repaint();
        });

        JTextField finalCampoCpf = campoCpf;
        
        // --- LÓGICA DE SALVAR VIA FACHADA ---
        btnFinalizar.addActionListener(e -> {
            try {
                String cpfAlvo;
                if (usuarioPreDefinido != null) {
                    cpfAlvo = usuarioPreDefinido.getCpf();
                } else {
                    if (finalCampoCpf.getText().trim().isEmpty()) {
                        throw new CampoVazioException("CPF do Aluno");
                    }
                    cpfAlvo = finalCampoCpf.getText();
                }

                if (campoNome.getText().trim().isEmpty()) throw new CampoVazioException("Nome do Treino");
                if (tipoTreino[0] == null) throw new ValorInvalidoException("Selecione o tipo de treino!");

                LocalDate data;
                try {
                    data = LocalDate.parse(campoData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (DateTimeParseException dt) {
                    throw new ValorInvalidoException("Data inválida. Use o formato dd/MM/yyyy.");
                }

                int duracao;
                double dist = 0;
                int ser = 0, desc = 0;

                try {
                    duracao = Integer.parseInt(campoDuracao.getText()) * 60; 
                    if (tipoTreino[0].equals("corrida")) {
                        if (campoDistancia[0] == null) throw new ValorInvalidoException("Defina a distância.");
                        dist = Double.parseDouble(campoDistancia[0].getText().replace(",", "."));
                    } else {
                        if (campoSeries[0] == null) throw new ValorInvalidoException("Defina séries e descanso.");
                        ser = Integer.parseInt(campoSeries[0].getText());
                        desc = Integer.parseInt(campoDescanso[0].getText());
                    }
                } catch (NumberFormatException nf) {
                    throw new ValorInvalidoException("Verifique os campos numéricos.");
                }

                // USO DA FACHADA
                Fachada.getInstance().getControladorTreino().cadastrarTreino(cpfAlvo, 
                    tipoTreino[0].equals("corrida") ? "Corrida" : "Intervalado", 
                    data, duracao, campoNome.getText(), dist, ser, desc);

                JOptionPane.showMessageDialog(tela, "Treino registrado com sucesso!");
                tela.dispose();

            } catch (CampoVazioException | ValorInvalidoException | UsuarioNaoEncontradoException ex) {
                JOptionPane.showMessageDialog(tela, "Atenção: " + ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        tela.setSize(500, 700);
        tela.setVisible(true);
    }

    public static void abrirTelaNotificacoes() {
        Usuario u = null;
        if (SessaoUsuario.getInstance().isUsuarioLogado()) {
            u = SessaoUsuario.getInstance().getUsuarioLogado();
        } else {
            String cpf = JOptionPane.showInputDialog(null, "CPF do aluno:");
            if (cpf != null && !cpf.trim().isEmpty()) {
                u = Fachada.getInstance().getControladorCliente().buscarCliente(cpf);
            }
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
            Fachada.getInstance().getControladorCliente().verNotificacoes(finalU); // Marca como lidas
            tela.dispose();
        });
        tela.add(btnLimpar, BorderLayout.SOUTH);
        tela.setSize(400, 400);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    public static void abrirTelaHistorico(Usuario u) {
        if (u == null) return;
        new TelaHistoricoTreinos(u).criarPainel(); 
        // Nota: Ajustei para chamar a classe de histórico corretamente, 
        // mas idealmente a TelaHistoricoTreinos deve ser exibida num JFrame ou adicionada ao Gerenciador.
        // Abaixo segue a lógica original adaptada para exibir JTable:
        
        JFrame tela = new JFrame("Histórico");
        tela.setContentPane(new TelaHistoricoTreinos(u).criarPainel());
        tela.setSize(800, 500);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // Métodos Auxiliares
    private static GridBagConstraints configuracaoGrid(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x; gbc.gridy = y; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.insets = new Insets(10, 5, 10, 10);
        return gbc;
    }

    public static void TelaMetas() { GerenciadorTelas.getInstance().carregarTela(new TelaMetas().criarPainel()); }
    public static void TelaDesafios() { GerenciadorTelas.getInstance().carregarTela(new TelaDesafios().criarPainel()); }
    
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