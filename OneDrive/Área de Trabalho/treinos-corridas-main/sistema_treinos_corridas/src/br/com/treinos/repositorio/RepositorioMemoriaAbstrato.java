package br.com.treinos.repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementação abstrata de um repositório em memória.
 * Cuida da lógica de gerar IDs e armazenar em um Map,
 * para que as classes filhas foquem em buscas específicas.
 */
public abstract class RepositorioMemoriaAbstrato<T> implements IRepository<T> {

    private final Map<Integer, T> database = new HashMap<>();
    // AtomicInteger garante que o ID seja único mesmo em ambientes concorrentes
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    // Método abstrato para forçar as classes filhas a implementar
    // a forma de obter/definir o ID da entidade específica.
    protected abstract int getId(T entidade);
    protected abstract void setId(T entidade, int id);

    @Override
    public T salvar(T entidade) {
        int id = idGenerator.incrementAndGet();
        setId(entidade, id);
        database.put(id, entidade);
        System.out.println("[REPOSITÓRIO] Salvo " + entidade.getClass().getSimpleName() + " com ID: " + id);
        return entidade;
    }

    @Override
    public T buscarPorId(int id) {
        System.out.println("[REPOSITÓRIO] Buscando " + getClass().getSimpleName().replace("RepositoryMemoria", "") + " com ID: " + id);
        return database.get(id);
    }

    @Override
    public List<T> listarTodos() {
        return new ArrayList<>(database.values());
    }

    @Override
    public T atualizar(T entidade) {
        int id = getId(entidade);
        if (!database.containsKey(id)) {
            throw new RuntimeException(entidade.getClass().getSimpleName() + " com ID " + id + " não encontrado para atualizar.");
        }
        database.put(id, entidade);
        System.out.println("[REPOSITÓRIO] Atualizado " + entidade.getClass().getSimpleName() + " com ID: " + id);
        return entidade;
    }

    @Override
    public void excluir(int id) {
        if (database.remove(id) != null) {
            System.out.println("[REPOSITÓRIO] Excluído " + getClass().getSimpleName().replace("RepositoryMemoria", "") + " com ID: " + id);
        } else {
            System.out.println("[REPOSITÓRIO] Falha ao excluir: ID " + id + " não encontrado.");
        }
    }
}