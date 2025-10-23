package br.com.treinos.repositorio;

import br.com.treinos.Desafio;

/**
 * Contrato específico para o repositório de Desafio.
 */
public interface IDesafioRepository extends IRepository<Desafio> {
    // Poderia ter métodos como: List<Desafio> buscarDesafiosAtivos(LocalDate hoje);
}