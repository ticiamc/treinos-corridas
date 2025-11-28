package br.com.excecoes;

public class CampoVazioException extends Exception {
    public CampoVazioException(String nomeCampo) {
        super("O campo '" + nomeCampo + "' é obrigatório e não pode estar vazio.");
    }
}