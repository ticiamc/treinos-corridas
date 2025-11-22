package br.com.gui;

import br.com.dados.RepositorioClientes;
import br.com.negocio.ControladorCliente;
import br.com.negocio.ControladorTreino;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.*;

public class TelaComputador {

    // Controladores estáticos para serem acessados por todas as telas
    public static ControladorCliente controladorCliente;
    public static ControladorTreino controladorTreino;
    
    // CPF fixo para simular o usuário logado (já que não temos tela de login)
    public static final String CPF_LOGADO = "000.000.000-00"; 

    public static void main(String[] args) {
        
        // 1. Inicializa os Repositórios e Controladores
        RepositorioClientes repo = new RepositorioClientes();
        controladorCliente = new ControladorCliente(repo);
        controladorTreino = new ControladorTreino(repo);

        // 2. Cria um usuário de teste para não dar erro de "usuário não encontrado"
        Usuario userTeste = new Usuario("Usuário Teste", 25, 70, 1.75, "teste@email.com", CPF_LOGADO);
        controladorCliente.cadastrarCliente(userTeste);
        System.out.println("Sistema iniciado com usuário de teste: " + userTeste.getNome());

        SwingUtilities.invokeLater(() -> criarUI());
    }

    public static void criarUI(){
        JFrame janela = new JFrame("Minha janela");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        janela.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.weightx = 1;
        gbc.weighty = 1;
        

        // Painel de Texto com a marca
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        
        JLabel painel_marca = new JLabel("TOTALPAS POBRE", SwingConstants.CENTER);
        painel_marca.setFont(new Font("Arial", Font.BOLD, 30));
        painel_marca.setOpaque(true);
        painel_marca.setBackground(Color.BLACK);
        painel_marca.setBorder(BorderFactory.createLineBorder(new Color(74, 255, 86), 10));
        painel_marca.setForeground(new Color(74, 255, 86));
        janela.add(painel_marca, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 1;

        // Botão Cadastro de Usuário
        gbc.gridx = 1; 
        gbc.gridy = 0;
        JButton botao1 = new JButton("Cadastrar");
        botao1.setFont(new Font("Arial", Font.BOLD, 20)); 
        janela.add(botao1, gbc);

        // Botão Acessar Perfil do usuário
        gbc.gridx = 2; 
        gbc.gridy = 0;
        JButton botao2 = new JButton("Acessar Perfil");
        botao2.setFont(new Font("Arial", Font.BOLD, 20));  
        janela.add(botao2, gbc);

        // Botão de Notificações
        gbc.gridx = 1; 
        gbc.gridy = 1;
        JButton botao3 = new JButton("Notificações");
        botao3.setFont(new Font("Arial", Font.BOLD, 20));  
        janela.add(botao3, gbc);

        // Botão de Relatórios
        gbc.gridx = 2; 
        gbc.gridy = 1;
        JButton botao4 = new JButton("Relatórios");
        botao4.setFont(new Font("Arial", Font.BOLD, 20));  
        janela.add(botao4, gbc);

        // Botão de Desafios
        gbc.gridx = 1; 
        gbc.gridy = 2;
        JButton botao5 = new JButton("Desafios");
        botao5.setFont(new Font("Arial", Font.BOLD, 20));  
        janela.add(botao5, gbc);
        
        // Botão de Planos de Treinos
        gbc.gridx = 2; 
        gbc.gridy = 2;
        JButton botao6 = new JButton("Planos de Treino"); 
        botao6.setFont(new Font("Arial", Font.BOLD, 20)); 
        janela.add(botao6, gbc);

        // Botão de Metas
        gbc.gridx = 1; 
        gbc.gridy = 3;
        JButton botao7 = new JButton("Metas"); 
        botao7.setFont(new Font("Arial", Font.BOLD, 20)); 
        janela.add(botao7, gbc);

        // Botão de Treinos
        gbc.gridx = 2; 
        gbc.gridy = 3;
        JButton botao8 = new JButton("Treinos"); 
        botao8.setFont(new Font("Arial", Font.BOLD, 20)); 
        botao8.addActionListener(e -> TelaComputador.abrirTelaCadastroTreino());
        janela.add(botao8, gbc);



        janela.setSize(1000, 700);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    public static void abrirTelaCadastroTreino() {

        // -------------------------------
        // JANELA
        // -------------------------------
        JFrame tela = new JFrame("Cadastrar Treino");
        tela.setLayout(new BorderLayout());

        // -------------------------------
        // TOPO COM SOMENTE "Ver Treinos"
        // -------------------------------
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        JButton btnVerTreinos = new JButton("Ver Treinos");
        
        btnVerTreinos.addActionListener(e -> {
            Usuario u = controladorCliente.buscarCliente(CPF_LOGADO);
            if (u != null) {
                List<Treino> treinos = u.getTreinos();
                StringBuilder sb = new StringBuilder();
                sb.append("TREINOS DE ").append(u.getNome().toUpperCase()).append(":\n\n");
                
                if (treinos.isEmpty()) {
                    sb.append("Nenhum treino cadastrado ainda.");
                } else {
                    for (Treino t : treinos) {
                        sb.append(t.toString()).append("\n-------------------\n");
                    }
                }
                // Mostra num pop-up simples
                JTextArea textArea = new JTextArea(sb.toString());
                JScrollPane scrollPane = new JScrollPane(textArea);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showMessageDialog(tela, scrollPane, "Histórico", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        topo.add(btnVerTreinos);
        tela.add(topo, BorderLayout.NORTH);

        // -------------------------------
        // PAINEL CENTRAL COM CAMPOS FIXOS
        // -------------------------------
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JTextField campoNome = new JTextField();
        campoNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campoNome.setBorder(BorderFactory.createTitledBorder("Nome do Treino"));

        JTextField campoData = new JTextField();
        campoData.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campoData.setBorder(BorderFactory.createTitledBorder("Data (dd/MM/yyyy)"));

        JTextField campoDuracao = new JTextField();
        campoDuracao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campoDuracao.setBorder(BorderFactory.createTitledBorder("Duração (segundos)"));

        painelCentral.add(campoNome);
        painelCentral.add(Box.createVerticalStrut(15));
        painelCentral.add(campoData);
        painelCentral.add(Box.createVerticalStrut(15));
        painelCentral.add(campoDuracao);
        painelCentral.add(Box.createVerticalStrut(25));

        // -------------------------------
        // PAINEL DINÂMICO
        // -------------------------------
        JPanel painelDinamico = new JPanel();
        painelDinamico.setLayout(new BoxLayout(painelDinamico, BoxLayout.Y_AXIS));
        painelDinamico.setBorder(BorderFactory.createTitledBorder("Detalhes do treino"));

        painelCentral.add(painelDinamico);

        tela.add(painelCentral, BorderLayout.CENTER);

        // -------------------------------
        // BOTÕES (Corrida + Ver Treinos + Intervalado + Finalizar)
        // -------------------------------
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnCorrida = new JButton("Corrida");
        JButton btnIntervalado = new JButton("Intervalado");
        JButton btnFinalizar = new JButton("Finalizar Cadastro");

        painelBotoes.add(btnCorrida);
        painelBotoes.add(btnVerTreinos);
        painelBotoes.add(btnIntervalado);
        painelBotoes.add(btnFinalizar);

        tela.add(painelBotoes, BorderLayout.SOUTH);

        // Variáveis para capturar os campos criados dinamicamente
        final JTextField[] campoDistancia = {null};
        final JTextField[] campoSeries = {null};
        final JTextField[] campoDescanso = {null};
        final String[] tipoTreino = {null};

        // -------------------------------
        // BOTÃO "CORRIDA"
        // -------------------------------
        btnCorrida.addActionListener(e -> {
            tipoTreino[0] = "corrida";
            painelDinamico.removeAll();

            JLabel lbl = new JLabel("Distância percorrida (metros):");
            JTextField campo = new JTextField();
            campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

            painelDinamico.add(lbl);
            painelDinamico.add(Box.createVerticalStrut(10));
            painelDinamico.add(campo);

            campoDistancia[0] = campo;
            campoSeries[0] = null;
            campoDescanso[0] = null;

            painelDinamico.revalidate();
            painelDinamico.repaint();
        });

        // -------------------------------
        // BOTÃO "INTERVALADO"
        // -------------------------------
        btnIntervalado.addActionListener(e -> {
            tipoTreino[0] = "intervalado";
            painelDinamico.removeAll();

            JLabel lblS = new JLabel("Número de séries:");
            JTextField campoS = new JTextField();
            campoS.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

            JLabel lblD = new JLabel("Descanso entre séries (segundos):");
            JTextField campoD = new JTextField();
            campoD.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

            painelDinamico.add(lblS);
            painelDinamico.add(Box.createVerticalStrut(10));
            painelDinamico.add(campoS);

            painelDinamico.add(Box.createVerticalStrut(15));

            painelDinamico.add(lblD);
            painelDinamico.add(Box.createVerticalStrut(10));
            painelDinamico.add(campoD);

            campoSeries[0] = campoS;
            campoDescanso[0] = campoD;
            campoDistancia[0] = null;

            painelDinamico.revalidate();
            painelDinamico.repaint();
        });

        // -------------------------------
        // BOTÃO "FINALIZAR"
        // -------------------------------
        btnFinalizar.addActionListener(e -> {

            // VALIDA CAMPOS FIXOS
            if (campoNome.getText().trim().isEmpty() ||
                campoData.getText().trim().isEmpty() ||
                campoDuracao.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(tela,
                        "Preencha todos os campos principais!",
                        "Campos vazios",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Valida data
            if (!campoData.getText().matches("\\d{2}/\\d{2}/\\d{4}")) {
                JOptionPane.showMessageDialog(tela,
                        "A data deve estar no formato dd/MM/yyyy.",
                        "Data inválida",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Valida duração
            try {
                if (Integer.parseInt(campoDuracao.getText()) <= 0) {
                    JOptionPane.showMessageDialog(tela,
                            "A duração deve ser maior que 0.",
                            "Valor inválido",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(tela,
                        "A duração deve ser um número inteiro.",
                        "Formato inválido",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Valida tipo de treino
            if (tipoTreino[0] == null) {
                JOptionPane.showMessageDialog(tela,
                        "Selecione o tipo de treino (Corrida ou Intervalado).",
                        "Treino não selecionado",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // -------------------------------
            // VALIDAÇÕES ESPECÍFICAS
            // -------------------------------
            if (tipoTreino[0].equals("corrida")) {

                if (campoDistancia[0] == null || campoDistancia[0].getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(tela,
                            "Digite a distância percorrida.",
                            "Campo obrigatório",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    if (Double.parseDouble(campoDistancia[0].getText()) <= 0) {
                        JOptionPane.showMessageDialog(tela,
                                "A distância deve ser maior que 0.",
                                "Valor inválido",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(tela,
                            "A distância deve ser um número válido.",
                            "Formato inválido",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } else if (tipoTreino[0].equals("intervalado")) {

                // Séries
                if (campoSeries[0].getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(tela,
                            "Preencha o número de séries.",
                            "Campo obrigatório",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    if (Integer.parseInt(campoSeries[0].getText()) <= 0) {
                        JOptionPane.showMessageDialog(tela,
                                "As séries devem ser maiores que 0.",
                                "Valor inválido",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(tela,
                            "O número de séries deve ser um inteiro válido.",
                            "Formato inválido",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Descanso
                if (campoDescanso[0].getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(tela,
                            "Preencha o descanso entre séries.",
                            "Campo obrigatório",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    if (Integer.parseInt(campoDescanso[0].getText()) <= 0) {
                        JOptionPane.showMessageDialog(tela,
                                "O descanso deve ser maior que 0.",
                                "Valor inválido",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(tela,
                            "O descanso deve ser um inteiro válido.",
                            "Formato inválido",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try {
                // 1. Preparar os dados
                String nomeTreino = campoNome.getText();
                LocalDate dataTreino = LocalDate.parse(campoData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int duracaoSegundos = Integer.parseInt(campoDuracao.getText()) * 60; // Converte minutos p/ segundos
                
                double distancia = 0;
                int series = 0;
                int descanso = 0;
                String tipoStr = "";

                // 2. Pegar dados específicos (Corrida ou Intervalado)
                if (tipoTreino[0].equals("corrida")) {
                    tipoStr = "Corrida";
                    distancia = Double.parseDouble(campoDistancia[0].getText());
                } else {
                    tipoStr = "Intervalado";
                    series = Integer.parseInt(campoSeries[0].getText());
                    descanso = Integer.parseInt(campoDescanso[0].getText());
                }

                // 3. Chamar o Controlador para salvar
                controladorTreino.cadastrarTreino(
                    CPF_LOGADO,    // CPF do usuário fixo
                    tipoStr,       // "Corrida" ou "Intervalado"
                    dataTreino, 
                    duracaoSegundos, 
                    nomeTreino, 
                    distancia, 
                    series, 
                    descanso
                );
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, "Erro ao salvar no sistema: " + ex.getMessage());
                return; // Para aqui se der erro
            }

            // -------------------------------
            // SUCESSO
            // -------------------------------
            JOptionPane.showMessageDialog(tela,
                    "Treino cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            tela.dispose();
        });

        // -------------------------------
        // CONFIGURAÇÕES FINAIS
        // -------------------------------
        tela.setSize(800, 600);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }
}
