package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.dados.IRepositorioPlanoTreino;
import br.com.excecoes.CampoVazioException;
import br.com.negocio.treinos.PlanoTreino;
import br.com.negocio.treinos.Treino;
import br.com.negocio.treinos.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ControladorPlanoTreino {

    private IRepositorioCliente repositorioCliente;
    private IRepositorioPlanoTreino repositorioPlanoTreino;

    public ControladorPlanoTreino(IRepositorioCliente repositorioCliente, IRepositorioPlanoTreino repositorioPlanoTreino) {
        this.repositorioCliente = repositorioCliente;
        this.repositorioPlanoTreino = repositorioPlanoTreino;
    }

    public void cadastrarPlano(String cpfUsuario, String nome, LocalDate dataInicio, LocalDate dataFim) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new CampoVazioException("Nome do Plano");
        }

        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) throw new Exception("Cliente não encontrado.");
        if (dataInicio.isAfter(dataFim)) throw new Exception("Data de início não pode ser posterior à data final.");

        PlanoTreino plano = new PlanoTreino(nome, dataInicio, dataFim, usuario);
        
        usuario.adicionarPlanoTreino(plano);
        repositorioPlanoTreino.cadastrar(plano);
        
        repositorioCliente.atualizarElemento(usuario);
    }

    public List<PlanoTreino> listarPlanos(String cpfUsuario) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) throw new Exception("Cliente não encontrado.");
        return usuario.getPlanos();
    }

    public void removerPlano(String cpfUsuario, int idPlano) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) throw new Exception("Cliente não encontrado.");

        PlanoTreino plano = usuario.buscarPlanoPorId(idPlano);
        if (plano == null) throw new Exception("Plano de treino não encontrado.");

        usuario.removerPlanoTreino(plano);
        repositorioPlanoTreino.remover(idPlano);
        
        repositorioCliente.atualizarElemento(usuario);
    }

    public void adicionarTreinoPlano(String cpfUsuario, int idPlano, int idTreino) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) throw new Exception("Cliente não encontrado.");

        PlanoTreino plano = usuario.buscarPlanoPorId(idPlano);
        if (plano == null) throw new Exception("Plano de treino não encontrado.");

        Treino treino = usuario.buscarTreinoPorId(idTreino);
        if (treino == null) throw new Exception("Treino não encontrado na lista do usuário.");

        // --- VALIDAÇÃO DE DATA (NOVO) ---
        LocalDate dataTreino = treino.getDataExecucao().toLocalDate();
        if (dataTreino.isBefore(plano.getDataInicio()) || dataTreino.isAfter(plano.getDataFim())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            throw new Exception("A data do treino (" + dataTreino.format(fmt) + ") está fora da vigência do plano (" + 
                plano.getDataInicio().format(fmt) + " a " + plano.getDataFim().format(fmt) + ").");
        }
        // --------------------------------

        plano.adicionarTreino(treino);
        repositorioCliente.atualizarElemento(usuario);
    }
    
    public List<PlanoTreino> listarTodosOsPlanosDoSistema() {
        return repositorioPlanoTreino.listarTodos();
    }

    public void verificarLembretesDoDia(Usuario usuario) {
        if (usuario == null) return;
        
        LocalDate hoje = LocalDate.now();
        boolean encontrouTreinoHoje = false;
        
        for (PlanoTreino plano : usuario.getPlanos()) {
            if (!hoje.isBefore(plano.getDataInicio()) && !hoje.isAfter(plano.getDataFim())) {
                for (Treino t : plano.getTreinosDoPlano()) {
                    if (t.getDataExecucao().toLocalDate().equals(hoje)) {
                        System.out.println("Lembrete: Você tem o treino '" + t.getNomeTreino() + 
                                         "' agendado para hoje no plano '" + plano.getNome() + "'!");
                        encontrouTreinoHoje = true;
                    }
                }
            }
        }
        
        if (!encontrouTreinoHoje) {
            System.out.println("Nenhum treino específico agendado para hoje nos seus planos.");
        }
    }
}