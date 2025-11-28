package br.com.gui.TelasPlanosTreino;

import br.com.negocio.ControladorCliente;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Usuario;
import br.com.negocio.treinos.PlanoTreino;

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

        form.add(criarLabel("Nome do Plano:"));
        JTextField txtNome = new JTextField();
        form.add(txtNome);

        form.add(criarLabel("Data Início (AAAA-MM-DD):"));
        JTextField txtInicio = new JTextField(LocalDate.now().toString());
        form.add(txtInicio);

        form.add(criarLabel("Data Fim (AAAA-MM-DD):"));
        JTextField txtFim = new JTextField(LocalDate.now().plusWeeks(4).toString());
        form.add(txtFim);

        // Usuário alvo: tenta pegar logado, se null pede CPF
        form.add(criarLabel("CPF do Usuário (opcional):"));
        JTextField txtCpf = new JTextField();
        form.add(txtCpf);

        painel.add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setBackground(corFundo);

        JButton btnSalvar = new JButton("Criar e Salvar");
        btnSalvar.setBackground(corDestaque);
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) { JOptionPane.showMessageDialog(painel, "Digite um nome para o plano."); return; }

            LocalDate inicio, fim;
            try {
                inicio = LocalDate.parse(txtInicio.getText().trim());
                fim = LocalDate.parse(txtFim.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(painel, "Formato de data inválido. Use AAAA-MM-DD.");
                return;
            }
            if (fim.isBefore(inicio)) {
                JOptionPane.showMessageDialog(painel, "Data fim não pode ser anterior à data início.");
                return;
            }

            Usuario usuario = SessaoUsuario.getInstance().getUsuarioLogado();
            String cpfDigitado = txtCpf.getText().trim();
            if (!cpfDigitado.isEmpty()) {
                usuario = controlador.buscarCliente(cpfDigitado);
            }
            if (usuario == null) {
                JOptionPane.showMessageDialog(painel, "Usuário não encontrado. Faça login ou informe CPF válido.");
                return;
            }

            PlanoTreino plano = new PlanoTreino(nome, inicio, fim, usuario);
            usuario.adicionarPlano(plano);
            controlador.atualizarCliente(usuario);

            JOptionPane.showMessageDialog(painel, "Plano criado com sucesso!");
            // volta para lista
            GerenciadorTelas.getInstance().carregarTela(new TelaListarPlanosTreino(controlador).criarPainel());
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(60,60,60));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPlanosPrincipal(controlador).criarPainel()));

        botoes.add(btnVoltar);
        botoes.add(btnSalvar);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }

    private JLabel criarLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }
}