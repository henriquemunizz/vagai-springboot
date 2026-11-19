package br.com.fiap.vagai.exception;

import br.com.fiap.vagai.exception.*;
import java.sql.SQLException;
import br.com.fiap.vagai.dto.ErroResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {
    public static ResponseEntity<ErroResponseDto> resposta(int status, String mensagem) {
        return ResponseEntity.status(status).contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(new ErroResponseDto(status, mensagem));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDto> tratar(Exception erro) {
        if (erro instanceof NaoAutenticadoException) return resposta(401, erro.getMessage());
        if (erro instanceof SemPermissaoException) return resposta(403, erro.getMessage());
        if (erro instanceof EntidadeNaoEncontradaException) return resposta(404, erro.getMessage());
        if (erro instanceof ConflitoException) return resposta(409, erro.getMessage());
        if (erro instanceof EntradaInvalidaException) return resposta(400, erro.getMessage());
        if (erro instanceof MethodArgumentNotValidException validacao) {
            var campo = validacao.getBindingResult().getFieldError();
            return resposta(400, campo == null ? "Dados inválidos." : "Campo inválido: " + campo.getField());
        }
        if (erro instanceof MethodArgumentTypeMismatchException ||
            erro instanceof org.springframework.http.converter.HttpMessageNotReadableException)
            return resposta(400, "Verifique os tipos e campos informados.");
        if (erro instanceof ErrorResponse http) {
            int status = http.getStatusCode().value();
            return resposta(status, switch (status) {
                case 400 -> "Parâmetros inválidos ou ausentes.";
                case 404 -> "Recurso não encontrado.";
                case 405 -> "Método HTTP não permitido.";
                default -> "Requisição não atendida.";
            });
        }
        if (erro instanceof SQLException sql) {
            if (sql.getErrorCode() == 1 || sql.getErrorCode() == 2292 || sql.getErrorCode() == 2291 ||
                (sql.getSQLState() != null && sql.getSQLState().startsWith("23")))
                return resposta(409, "Registro duplicado ou com vínculo incompatível.");
        }
        org.slf4j.LoggerFactory.getLogger(ApiExceptionHandler.class).error("Falha ao atender requisição", erro);
        return resposta(500, "Falha interna ao atender a requisição.");
    }
}
