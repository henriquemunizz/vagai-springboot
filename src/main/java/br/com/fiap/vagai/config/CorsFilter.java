package br.com.fiap.vagai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import org.springframework.http.*;
import org.springframework.http.server.ServerHttpResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.net.URI;

@org.springframework.stereotype.Component
@org.springframework.core.annotation.Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends org.springframework.web.filter.CorsFilter {
    public CorsFilter(@Value("${vagai.origem}") String origem) {
        super(configurar(origem));
        setCorsProcessor(new DefaultCorsProcessor() {
            @Override
            protected void rejectRequest(ServerHttpResponse resposta) throws IOException {
                resposta.setStatusCode(HttpStatus.FORBIDDEN);
                resposta.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                resposta.getBody().write("""
                        {"status":403,"mensagem":"Origem ou operação CORS não permitida."}
                        """.getBytes(StandardCharsets.UTF_8));
                resposta.flush();
            }
        });
    }

    private static void validarOrigem(String origem) {
        URI uri = URI.create(origem);
        if (uri.getHost() == null || !java.util.Set.of("https", "http").contains(uri.getScheme())
                || !uri.getRawPath().isEmpty() || uri.getRawQuery() != null || uri.getRawFragment() != null
                || uri.getUserInfo() != null) throw new IllegalArgumentException("FRONTEND_ORIGIN inválida");
    }

    private static UrlBasedCorsConfigurationSource configurar(String origens) {
        CorsConfiguration regras = new CorsConfiguration();
        for (String valor : origens.split(",", -1)) {
            String origem = valor.strip();
            validarOrigem(origem);
            regras.addAllowedOrigin(origem);
        }
        regras.setAllowCredentials(true);
        regras.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        regras.setAllowedHeaders(List.of("Content-Type", "Accept"));
        regras.setExposedHeaders(List.of("Location"));
        UrlBasedCorsConfigurationSource rotas = new UrlBasedCorsConfigurationSource();
        rotas.registerCorsConfiguration("/**", regras);
        return rotas;
    }
}
