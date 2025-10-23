package br.com.treinos.repositorio;

import br.com.treinos.Usuario;

/**
 * Contrato específico para o repositório de Usuário.
 * Herda o CRUD básico e pode adicionar métodos específicos (ex: buscarPorEmail).
 */
public interface IUsuarioRepository extends IRepository<Usuario> {
    Usuario buscarPorEmail(String email);
}