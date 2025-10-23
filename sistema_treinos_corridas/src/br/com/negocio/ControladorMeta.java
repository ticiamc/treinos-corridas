package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.Meta;
import br.com.negocio.treinos.TipoMeta;
import br.com.negocio.treinos.Usuario;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para Metas.
 * (Arquivo criado pois não estava presente no projeto original)
 */
public class ControladorMeta {

    private IRepositorioCliente repositorioCliente;

    public ControladorMeta(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    public void cadastrarMeta(String cpfUsuario, String descricao, TipoMeta tipoMeta, double valorAlvo, LocalDate dataFim) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }
        
        // A data de início é sempre hoje ao cadastrar
        Meta meta = new Meta(descricao, valorAlvo, tipoMeta, LocalDate.now(), dataFim);
        usuario.adicionarMeta(meta);
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Meta cadastrada com sucesso! ID: " + meta.getIdMeta());
    }

    public List<Meta> listarMetas(String cpfUsuario) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }
        return usuario.getMetas();
    }

    public void atualizarMeta(String cpfUsuario, int idMeta, String novaDescricao, double novoValorAlvo, LocalDate novaDataFim) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }

        Meta meta = usuario.buscarMetaPorId(idMeta);
        if (meta == null) {
            throw new Exception("Meta não encontrada para este usuário.");
        }

        meta.setDescricao(novaDescricao);
        meta.setValorAlvo(novoValorAlvo);
        meta.setDataFim(novaDataFim);
        // A data de início não deve ser alterada, mas a data fim sim.
        
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Meta atualizada com sucesso!");
    }

    public void removerMeta(String cpfUsuario, int idMeta) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }

        Meta meta = usuario.buscarMetaPorId(idMeta);
        if (meta == null) {
            throw new Exception("Meta não encontrada para este usuário.");
        }

        usuario.removerMeta(meta);
        repositorioCliente.atualizarElemento(usuario);
        System.out.println("Meta removida com sucesso!");
    }
}
