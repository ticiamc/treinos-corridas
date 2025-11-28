package br.com.negocio;

import br.com.dados.IRepositorioCliente;
import br.com.negocio.treinos.Meta;
import br.com.negocio.treinos.TipoMeta;
import br.com.negocio.treinos.Usuario;
import java.time.LocalDate;
import java.util.List;

public class ControladorMeta {
    private IRepositorioCliente repositorioCliente;

    public ControladorMeta(IRepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    public void cadastrarMeta(String cpfUsuario, String descricao, TipoMeta tipoMeta, double valorAlvo, LocalDate dataFim) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) throw new Exception("Cliente não encontrado.");
        
        Meta meta = new Meta(descricao, valorAlvo, tipoMeta, LocalDate.now(), dataFim);
        usuario.adicionarMeta(meta);
        repositorioCliente.atualizarElemento(usuario);
    }

    public List<Meta> listarMetas(String cpfUsuario) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) throw new Exception("Cliente não encontrado.");
        return usuario.getMetas();
    }

    public void removerMeta(String cpfUsuario, int idMeta) throws Exception {
        Usuario usuario = repositorioCliente.buscarElementoPorCpf(cpfUsuario);
        if (usuario == null) throw new Exception("Cliente não encontrado.");

        Meta meta = usuario.buscarMetaPorId(idMeta);
        if (meta == null) throw new Exception("Meta não encontrada.");

        usuario.removerMeta(meta);
        repositorioCliente.atualizarElemento(usuario);
    }
}