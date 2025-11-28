package br.com.excecoes;

public class UsuarioNaoEncontradoException extends Exception {
    public UsuarioNaoEncontradoException(String cpf) {
        super("Usuário com CPF '" + cpf + "' não foi encontrado.");
    }
}