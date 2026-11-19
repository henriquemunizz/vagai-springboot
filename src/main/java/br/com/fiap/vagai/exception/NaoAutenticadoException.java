package br.com.fiap.vagai.exception;

public class NaoAutenticadoException extends RuntimeException {
    public NaoAutenticadoException() {
        super("Autenticação necessária ou credenciais inválidas.");
    }
}
