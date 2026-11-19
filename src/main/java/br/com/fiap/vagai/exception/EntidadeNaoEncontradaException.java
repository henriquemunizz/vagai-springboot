package br.com.fiap.vagai.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException() {
        super("Registro não encontrado.");
    }
}
