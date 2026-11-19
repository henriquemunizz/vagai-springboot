package br.com.fiap.vagai.exception;

public class SemPermissaoException extends RuntimeException {
    public SemPermissaoException() {
        super("Operação restrita ao proprietário.");
    }
}
