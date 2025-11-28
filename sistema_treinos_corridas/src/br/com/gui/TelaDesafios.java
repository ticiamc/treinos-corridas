package br.com.gui;

import br.com.negocio.ControladorDesafio;
import br.com.negocio.SessaoUsuario;
import br.com.negocio.treinos.Desafio;
import br.com.negocio.treinos.Relatorio;
import br.com.negocio.treinos.Usuario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TelaDesafios {

    private ControladorDesafio controladorDesafio;
    private Usuario usuarioLogado;
    
    public TelaDesafios() {
        this.controladorDesafio = TelaComputador.controladorDesafio;
        this.usuarioLogado = SessaoUsuario.getInstance().getUsuarioLogado();
    }

    public JPanel criarPainel() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30,30,30));
        
        // Cabeçalho com Título e Botão Criar
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(30,30,30));
        
        JLabel titulo = new JLabel("Central de Desafios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(74, 255, 86));
        titulo.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        topo.add(titulo, BorderLayout.CENTER);

        // Botão Criar (Disponível para usuários logados)
        if (usuarioLogado != null) {
            JButton btnCriar = new JButton("+ Criar Novo");
            btnCriar.setBackground(new Color(74, 255, 86));
            btnCriar.setForeground(Color.BLACK);
            btnCriar.addActionListener(e -> abrirDialogoCriarDesafio());
            topo.add(btnCriar, BorderLayout.EAST);
        }
        painel.add(topo, BorderLayout.NORTH);
        
        // Abas
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Todos os Desafios", criarPainelLista(false));
        if(usuarioLogado != null) {
            abas.addTab("Meus Desafios (Participando)", criarPainelLista(true));
        }
        
        JPanel rodape = new JPanel();
        rodape.setBackground(new Color(30,30,30));
        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> GerenciadorTelas.getInstance().carregarTela(new TelaPrincipalUsuario().criarPainelUsuario()));
        rodape.add(btnVoltar);
        
        painel.add(abas, BorderLayout.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelLista(boolean apenasMeus) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(40,40,40));
        
        DefaultListModel<Desafio> model = new DefaultListModel<>();
        List<Desafio> lista = controladorDesafio.listarDesafios();
        
        for(Desafio d : lista) {
            boolean participa = d.getParticipacoes().stream()
                .anyMatch(par -> par.getUsuario().getCpf().equals(usuarioLogado.getCpf()));
            
            if (apenasMeus && participa) model.addElement(d);
            else if (!apenasMeus) model.addElement(d);
        }
        
        JList<Desafio> jList = new JList<>(model);
        jList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Desafio d = (Desafio) value;
                String criador = (d.getCriador() == null) ? "Admin" : d.getCriador().getNome();
                String txt = String.format("ID %d: %s | %s - %s | Criador: %s", 
                    d.getIdDesafio(), d.getNome(), d.getDataInicio(), d.getDataFim(), criador);
                return super.getListCellRendererComponent(list, txt, index, isSelected, cellHasFocus);
            }
        });
        
        p.add(new JScrollPane(jList), BorderLayout.CENTER);
        
        // Painel de Botões de Ação
        JPanel botoes = new JPanel();
        
        if(!apenasMeus) {
            JButton btnEntrar = new JButton("Participar");
            btnEntrar.addActionListener(e -> {
                Desafio sel = jList.getSelectedValue();
                if(sel != null) {
                    try {
                        controladorDesafio.participarDesafio(sel.getIdDesafio(), usuarioLogado.getCpf());
                        JOptionPane.showMessageDialog(p, "Você entrou no desafio!");
                    } catch(Exception ex) { JOptionPane.showMessageDialog(p, ex.getMessage()); }
                }
            });
            botoes.add(btnEntrar);
        }
        
        JButton btnRanking = new JButton("Ver Ranking");
        btnRanking.addActionListener(e -> {
            Desafio sel = jList.getSelectedValue();
            if(sel != null) JOptionPane.showMessageDialog(p, new JTextArea(Relatorio.gerarRankingDesafioTexto(sel)));
        });
        botoes.add(btnRanking);

        // Botão EDITAR (Só aparece se o usuário for o dono do desafio)
        JButton btnEditar = new JButton("Editar (Dono)");
        btnEditar.setBackground(new Color(255, 200, 0));
        btnEditar.addActionListener(e -> {
            Desafio sel = jList.getSelectedValue();
            if (sel == null) {
                JOptionPane.showMessageDialog(p, "Selecione um desafio.");
                return;
            }
            // Verifica permissão
            boolean isDono = sel.getCriador() != null && sel.getCriador().getCpf().equals(usuarioLogado.getCpf());
            
            if (isDono) {
                abrirDialogoEditarDesafio(sel);
            } else {
                JOptionPane.showMessageDialog(p, "Apenas o criador deste desafio pode editá-lo.");
            }
        });
        botoes.add(btnEditar);

        // Botão EXCLUIR (Dono)
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(180, 50, 50));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(e -> {
            Desafio sel = jList.getSelectedValue();
            if (sel == null) return;
            boolean isDono = sel.getCriador() != null && sel.getCriador().getCpf().equals(usuarioLogado.getCpf());
            
            if (isDono) {
                int opt = JOptionPane.showConfirmDialog(p, "Tem certeza que deseja excluir?");
                if(opt == JOptionPane.YES_OPTION) {
                    controladorDesafio.removerDesafio(sel.getIdDesafio());
                    model.removeElement(sel);
                }
            } else {
                JOptionPane.showMessageDialog(p, "Sem permissão.");
            }
        });
        botoes.add(btnExcluir);

        p.add(botoes, BorderLayout.SOUTH);
        return p;
    }
    
    // Dialogo de Criação
    public static void abrirDialogoCriarDesafio() {
        JDialog d = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Criar Desafio", true);
        d.setLayout(new GridLayout(5,2));
        d.setSize(400,250);
        d.setLocationRelativeTo(null);
        
        JTextField nome = new JTextField();
        JTextField desc = new JTextField();
        JTextField ini = new JTextField(LocalDate.now().toString());
        JTextField fim = new JTextField(LocalDate.now().plusMonths(1).toString());
        
        d.add(new JLabel(" Nome:")); d.add(nome);
        d.add(new JLabel(" Descrição:")); d.add(desc);
        d.add(new JLabel(" Início (aaaa-mm-dd):")); d.add(ini);
        d.add(new JLabel(" Fim (aaaa-mm-dd):")); d.add(fim);
        
        JButton btn = new JButton("Criar");
        btn.addActionListener(e -> {
            try {
                // Pega usuário logado (pode ser null se for Admin logado como 'admin')
                Usuario criador = SessaoUsuario.getInstance().getUsuarioLogado();
                
                TelaComputador.controladorDesafio.cadastrarDesafio(
                    nome.getText(), desc.getText(), 
                    LocalDate.parse(ini.getText()), LocalDate.parse(fim.getText()),
                    criador 
                );
                JOptionPane.showMessageDialog(d, "Desafio criado!");
                d.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(d, "Erro: " + ex.getMessage()); }
        });
        d.add(btn);
        d.setVisible(true);
    }

    // Dialogo de Edição
    private void abrirDialogoEditarDesafio(Desafio desafio) {
        JDialog d = new JDialog(GerenciadorTelas.getInstance().getJanelaPrincipal(), "Editar Desafio", true);
        d.setLayout(new GridLayout(5,2));
        d.setSize(400,250);
        d.setLocationRelativeTo(null);
        
        JTextField nome = new JTextField(desafio.getNome());
        JTextField desc = new JTextField(desafio.getDescricao());
        JTextField ini = new JTextField(desafio.getDataInicio().toString());
        JTextField fim = new JTextField(desafio.getDataFim().toString());
        
        d.add(new JLabel(" Nome:")); d.add(nome);
        d.add(new JLabel(" Descrição:")); d.add(desc);
        d.add(new JLabel(" Início (aaaa-mm-dd):")); d.add(ini);
        d.add(new JLabel(" Fim (aaaa-mm-dd):")); d.add(fim);
        
        JButton btn = new JButton("Salvar Alterações");
        btn.addActionListener(e -> {
            try {
                controladorDesafio.atualizarDesafio(
                    desafio.getIdDesafio(),
                    nome.getText(), desc.getText(), 
                    LocalDate.parse(ini.getText()), LocalDate.parse(fim.getText())
                );
                JOptionPane.showMessageDialog(d, "Atualizado com sucesso!");
                d.dispose();
                
                // Recarrega a tela atual para refletir as mudanças na lista
                GerenciadorTelas.getInstance().carregarTela(new TelaDesafios().criarPainel());
                
            } catch(Exception ex) { JOptionPane.showMessageDialog(d, "Erro: " + ex.getMessage()); }
        });
        d.add(btn);
        d.setVisible(true);
    }
}