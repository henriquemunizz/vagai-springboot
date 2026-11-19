package br.com.fiap.vagai.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import br.com.fiap.vagai.dto.ErroResponseDto;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ErroController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<ErroResponseDto> erro(HttpServletRequest request) {
        Object valor = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = valor instanceof Integer codigo ? codigo : 404;
        return ApiExceptionHandler.resposta(status, status >= 500 ? "Falha interna ao atender a requisição." : "Requisição não atendida.");
    }
}
