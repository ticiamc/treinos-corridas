package br.com.gui.TelasPlanosTreino;

import br.com.gui.GerenciadorTelas;
import br.com.gui.TelaComputador;
import br.com.gui.TelaPrincipalUsuario; // Import necessário
import br.com.negocio.ControladorCliente;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TelaCriarPlanoTreino {

    private final ControladorCliente controlador;

    public TelaCriarPlanoTreino(ControladorCliente controlador) {
        this.controlador = controlador;
    }

    public JPanel criarPainel() {
        Color corFundo = new Color(30, 30, 30);
        Color corCard = new Color(45, 45, 45);
        Color corDestaque = new Color(74, 255, 86);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(corFundo);
        painel.setBorder(new EmptyBorder(20,20,20,20));

        JLabel titulo = new JLabel("Criar Plano de Treino", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(corDestaque);
        painel.add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5,2,10,10));
        form.setBackground(corCard);
        form.setBorder(new EmptyBorder(20,20,20,20));

        JLabel lblNome = new JLabel("Nome do Plano:"); lblNome.setForeground(Color.WHITE);
        JTextField txtNome = new JTextField();
        
        JLabel lblInicio = new JLabel("Início (AAAA-MM-DD):"); lblInicio.setForeground(Color.WHITE);
        JTextField txtInicio = new JTextField(LocalDate.now().toString());
        
        JLabel lblFim = new JLabel("Fim (AAAA-MM-DD):"); lblFim.setForeground(Color.WHITE);
        JTextField txtFim = new JTextField(LocalDate.now().plusWeeks(4).toString());

        form.add(lblNome); form.add(txtNome);
        form.add(lblInicio); form.add(txtInicio);
        form.add(lblFim); form.add(txtFim);

        painel.add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBackground(corFundo);

        JButton btnSalvar = new JButton("Criar e Salvar");
        btnSalvar.setBackground(corDestaque);
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> {
            try {
                Usuario usuario = SessaoUsuario.getInstance().getUsuarioLogado();
                if (usuario == null) {
                    JOptionPane.showMessageDialog(painel, "Login necessário.");
                    return;
                }

                // Tenta cadastrar (Controller vai validar nome vazio e datas)
                TelaComputador.controladorPlanoTreino.cadastrarPlano(
                    usuario.getCpf(), 
                    txtNome.getText(), 
                    LocalDate.parse(txtInicio.getText()), 
                    LocalDate.parse(txtFim.getText())
                );

                JOptionPane.showMessageDialog(painel, "Plano criado com sucesso!");
                // Redireciona para a lista para ver o plano criado
                GerenciadorTelas.getInstance().carregarTela(new TelaListarPlanosTreino(controlador).criarPainel());

            } catch (DateTimeParseException dt) {
                JOptionPane.showMessageDialog(painel, "Data inválida. Use o formato AAAA-MM-DD.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage());
            }
        });

        // --- BOTÃO VOLTAR AO MENU PRINCIPAL ---
        JButton btnVoltar = new JButton("Voltar ao Menu Principal");
        btnVoltar.setBackground(new Color(60,60,60));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario()));

        botoes.add(btnVoltar);
        botoes.add(btnSalvar);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }
}