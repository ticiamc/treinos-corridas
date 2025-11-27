package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.Notificacao;
import br.com.negocio.treinos.PlanoTreino;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ControladorPlanoTreino {

    private IRepositorioCliente repositorioCliente;

    public ControladorPlanoTreino(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    public void cadastrarPlano(String cpfUsuario, String nome, LocalDate dataInicio, LocalDate dataFim) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }

        PlanoTreino plano = new PlanoTreino(nome, dataInicio, dataFim, usuario);
        usuario.adicionarPlanoTreino(plano);
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Plano de treino cadastrado com sucesso! ID: " + plano.getIdPlano());
    }

    public List<PlanoTreino> listarPlanos(String cpfUsuario) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }
        return usuario.getPlanos();
    }

    public void removerPlano(String cpfUsuario, int idPlano) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }

        PlanoTreino plano = usuario.buscarPlanoPorId(idPlano);
        if (plano == null) {
            throw new Exception("Plano de treino não encontrado.");
        }

        usuario.removerPlanoTreino(plano);
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Plano de treino removido com sucesso!");
    }

    public void adicionarTreinoPlano(String cpfUsuario, int idPlano, int idTreino) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }

        PlanoTreino plano = usuario.buscarPlanoPorId(idPlano);
        if (plano == null) {
            throw new Exception("Plano de treino não encontrado.");
        }

        Treino treino = usuario.buscarTreinoPorId(idTreino);
        if (treino == null) {
            throw new Exception("Treino não encontrado na lista principal do usuário.");
        }

        plano.adicionarTreino(treino);
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Treino adicionado ao plano com sucesso!");
    }

    public void removerTreinoPlano(String cpfUsuario, int idPlano, int idTreino) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }

        PlanoTreino plano = usuario.buscarPlanoPorId(idPlano);
        if (plano == null) {
            throw new Exception("Plano de treino não encontrado.");
        }

        Treino treinoParaRemover = null;
        for (Treino t : plano.getTreinosDoPlano()) {
            if (t.getIdTreino() == idTreino) {
                treinoParaRemover = t;
                break;
            }
        }

        if (treinoParaRemover == null) {
            throw new Exception("Treino não encontrado *dentro* deste plano.");
        }

        plano.removerTreino(treinoParaRemover);
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Treino removido do plano com sucesso!");
    }

    /**
     * [REQ22] Verifica se o usuário tem treinos agendados para HOJE em seus planos
     * e gera uma notificação de lembrete se encontrar.
     */
    public void verificarLembretesDoDia(Usuario usuario) {
        if (usuario == null || usuario.getPlanos().isEmpty()) return;

        LocalDate hoje = LocalDate.now();
        boolean temTreinoHoje = false;

        for (PlanoTreino plano : usuario.getPlanos()) {
            // Verifica se o plano está ativo (dentro da data)
            if (!hoje.isBefore(plano.getDataInicio()) && !hoje.isAfter(plano.getDataFim())) {
                for (Treino t : plano.getTreinosDoPlano()) {
                    // Verifica se a data do treino é hoje (ignorando hora)
                    if (t.getDataExecucao().toLocalDate().isEqual(hoje)) {
                        temTreinoHoje = true;
                        break;
                    }
                }
            }
        }

        if (temTreinoHoje) {
            // Verifica se já não mandou esse lembrete hoje para não flodar notificações repetidas
            boolean jaAvisou = usuario.getNotificacoes().stream()
                .anyMatch(n -> n.getMensagem().contains("Lembrete: Você tem treinos planejados para hoje!") 
                               && n.getData().toLocalDate().isEqual(hoje));
            
            if (!jaAvisou) {
                usuario.adicionarNotificacao(new Notificacao(
                    UUID.randomUUID(), 
                    "Lembrete: Você tem treinos planejados para hoje! Consulte seus Planos.", 
                    java.time.LocalDateTime.now()
                ));
            }
        }
    }
}