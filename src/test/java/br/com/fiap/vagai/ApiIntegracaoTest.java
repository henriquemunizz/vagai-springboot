package br.com.fiap.vagai;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest(properties = {
    "vagai.origem=https://vagai-three.vercel.app, http://localhost:5173",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.url=jdbc:h2:mem:vagai;MODE=Oracle;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password="})
class ApiIntegracaoTest {
    @Autowired MockMvc mvc;
    @Autowired DataSource origem;
    final ObjectMapper json = new ObjectMapper();

    class Cliente {
        MockHttpSession sessao;

        JsonNode enviar(String metodo, String rota, Object corpo, int esperado) throws Exception {
            var pedido = request(org.springframework.http.HttpMethod.valueOf(metodo), URI.create(rota))
                    .contentType("application/json");
            if (sessao != null && !sessao.isInvalid()) pedido.session(sessao);
            if (corpo != null) pedido.content(json.writeValueAsString(corpo));
            var resultado = mvc.perform(pedido).andReturn();
            sessao = (MockHttpSession) resultado.getRequest().getSession(false);
            var resposta = resultado.getResponse();
            assertEquals(esperado, resposta.getStatus(), metodo + " " + rota + " " + resposta.getContentAsString());
            if (esperado == 201) assertNotNull(resposta.getHeader("Location"));
            if (esperado == 204) { assertEquals("", resposta.getContentAsString()); return null; }
            JsonNode dados = json.readTree(resposta.getContentAsString(java.nio.charset.StandardCharsets.UTF_8));
            if (esperado >= 400) assertEquals(esperado, dados.get("status").asInt());
            return dados;
        }

        JsonNode get(String rota, int status) throws Exception { return enviar("GET", rota, null, status); }
        JsonNode post(String rota, Object corpo, int status) throws Exception { return enviar("POST", rota, corpo, status); }
        void put(String rota, Object corpo, int status) throws Exception { enviar("PUT", rota, corpo, status); }
        void delete(String rota, int status) throws Exception { enviar("DELETE", rota, null, status); }
    }

    @Test
    void corsParaReact() throws Exception {
        for (String origemTeste : List.of("http://localhost:5173", "https://vagai-three.vercel.app", "https://outra.example")) {
            var resposta = mvc.perform(options("/usuarios/login").header("Origin", origemTeste)
                    .header("Access-Control-Request-Method", "POST")
                    .header("Access-Control-Request-Headers", "content-type")).andReturn().getResponse();
            if (!origemTeste.equals("https://outra.example")) {
                assertEquals(200, resposta.getStatus());
                assertEquals(origemTeste, resposta.getHeader("Access-Control-Allow-Origin"));
                assertEquals("true", resposta.getHeader("Access-Control-Allow-Credentials"));
            } else {
                assertEquals(403, resposta.getStatus());
                assertEquals(403, json.readTree(resposta.getContentAsString()).get("status").asInt());
                assertNull(resposta.getHeader("Access-Control-Allow-Origin"));
            }
        }
    }

    @Test
    void jornadaDeDoisMotoristas() throws Exception {
        var banco = new JdbcTemplate(origem);
        new ResourceDatabasePopulator(new FileSystemResource("sql/01_estrutura.sql"),
                new FileSystemResource("sql/02_dados_teste.sql")).execute(origem);
        for (String tabela : List.of("usuario", "estacionamento", "regra_estacionamento", "reporte", "avaliacao", "usuario_favorito"))
            assertEquals(tabela.equals("usuario_favorito") ? 11 : 12,
                    banco.queryForObject("select count(*) from tb_" + tabela, Integer.class));
        String consultas = Files.readString(Path.of("sql/03_consultas.sql")).replaceAll("(?m)^--.*$", "");
        int total = 0;
        for (String sql : consultas.split(";")) if (!sql.isBlank()) { banco.queryForList(sql); total++; }
        assertEquals(10, total);

        Cliente ana = new Cliente(), bia = new Cliente(), publico = new Cliente();
        Map<String,String> conta = Map.of("nome", "motorista-a", "senha", UUID.randomUUID().toString());
        var outra = Map.of("nome", "motorista-b", "senha", UUID.randomUUID().toString());
        String usuarios = "/usuarios/";
        long idAna = ana.post("/usuarios", conta, 201).get("id").asLong();
        String perfil = usuarios + idAna;
        bia.post("/usuarios", outra, 201);
        ana.post("/usuarios", conta, 409);
        ana.post("/usuarios", Map.of("nome", "x", "senha", "x"), 400);
        publico.get("/usuarios/me", 401);
        ana.post("/usuarios/login", Map.of("nome", "motorista-a", "senha", UUID.randomUUID().toString()), 401);
        ana.post("/usuarios/login", conta, 200);
        bia.post("/usuarios/login", outra, 200);
        assertFalse(ana.get("/usuarios/me", 200).toString().contains("senha"));
        assertEquals(1, ana.get("/usuarios", 200).size());
        ana.get(perfil, 200);
        bia.get(perfil, 403);
        bia.put(perfil, conta, 403);
        bia.delete(perfil, 403);

        var local = new HashMap<String,Object>(Map.of("nome", "Local colaborativo", "tipo", "ROTATIVO",
                "rua", "Rua Teste API", "bairro", "Centro", "numero", "20", "ativo", 1));
        String locais = "/estacionamentos";
        publico.post(locais, local, 401);
        long id = ana.post(locais, local, 201).get("id").asLong();
        String lugar = locais + "/" + id;
        publico.get(locais, 200);
        bia.put(lugar, local, 403);
        bia.delete(lugar, 403);
        local.put("numero", "25");
        ana.put(lugar, local, 200);
        var nota = Map.of("nota", 5, "comentario", "Boa localização");
        var relato = Map.of("tipo", "SEM_VAGA");
        String av = "/avaliacoes/" + ana.post(lugar + "/avaliacoes", nota, 201).get("id").asLong();
        ana.post(lugar + "/avaliacoes", nota, 409);
        String re = "/reportes/" + ana.post(lugar + "/reportes", relato, 201).get("id").asLong();
        var avaliacao = Map.of("nota", 4, "idEstacionamento", id);
        var reporte = Map.of("tipo", "VAGA_ENCONTRADA", "idEstacionamento", id);
        String av2 = "/avaliacoes/" + bia.post("/avaliacoes", avaliacao, 201).get("id").asLong();
        String re2 = "/reportes/" + bia.post("/reportes", reporte, 201).get("id").asLong();
        bia.delete(av2, 204);
        bia.delete(re2, 204);
        var regra = Map.of("descricao", "Proibido à noite", "idEstacionamento", id, "ativo", 1,
                "diaSemana", 1, "inicio", 1200, "fim", 360, "tipo", "PROIBIDO");
        String rg = "/regras/" + ana.post("/regras", regra, 201).get("id").asLong();
        bia.post("/regras", regra, 403);
        var favorito = Map.of("idEstacionamento", id);
        String fav = "/favoritos/" + ana.post("/favoritos", favorito, 201).get("id").asLong();
        ana.post("/favoritos", favorito, 409);
        assertEquals(1, ana.get("/usuarios/me/favoritos", 200).size());
        assertEquals(0, bia.get("/favoritos", 200).size());
        bia.get(fav, 403);
        for (String rota : List.of("/avaliacoes", "/reportes", "/regras")) {
            publico.get(rota, 200);
            assertEquals(1, publico.get(lugar + rota, 200).size());
        }
        for (String rota : List.of(av, re, rg)) publico.get(rota, 200);
        ana.get(fav, 200);
        for (var item : Map.of(av, avaliacao, re, reporte, rg, regra, fav, favorito).entrySet()) {
            bia.put(item.getKey(), item.getValue(), 403);
            bia.delete(item.getKey(), 403);
            ana.put(item.getKey(), item.getValue(), 200);
        }
        ana.delete(lugar, 409);
        ana.post(lugar + "/avaliacoes", Map.of("nota", 6), 400);
        ana.post("/estacionamentos/999999/reportes", relato, 404);
        String busca = locais + "?rua=rua%20teste%20api";
        assertEquals(1, publico.get(busca, 200).size());
        assertEquals("25", publico.get(busca, 200).get(0).get("numero").asString());
        assertEquals(1, publico.get(lugar + "/regras?diaSemana=2&minuto=359", 200).size());
        assertEquals(0, publico.get(lugar + "/regras?diaSemana=2&minuto=360", 200).size());
        publico.get(lugar + "/regras?diaSemana=8&minuto=10", 400);
        publico.get(lugar + "/regras?diaSemana=1", 400);
        var invalida = new HashMap<String,Object>(regra);
        invalida.put("inicio", 1440);
        ana.post("/regras", invalida, 400);
        var outroLocal = new HashMap<>(local);
        outroLocal.put("tipo", "GRATUITO");
        outroLocal.put("numero", "10");
        String gratuito = locais + "/" + ana.post(locais, outroLocal, 201).get("id").asLong();
        outroLocal.put("primeiraHora", 5);
        ana.post(locais, outroLocal, 400);
        outroLocal.put("tipo", "PRIVADO");
        ana.post(locais, outroLocal, 400);
        outroLocal.put("primeiraHora", 18);
        outroLocal.put("demaisHoras", 8);
        outroLocal.put("numero", "30A");
        String privado = locais + "/" + ana.post(locais, outroLocal, 201).get("id").asLong();
        var lista = publico.get(busca, 200);
        assertEquals(3, lista.size());
        assertEquals(Set.of("GRATUITO", "ROTATIVO", "PRIVADO"),
                new HashSet<>(List.of(lista.get(0).get("tipo").asString(),
                        lista.get(1).get("tipo").asString(), lista.get(2).get("tipo").asString())));
        assertEquals(18, publico.get(privado, 200).get("primeiraHora").asInt());
        assertEquals(0, publico.get(busca + "&bairro=Outro", 200).size());
        assertEquals(0, publico.get(locais + "?rua=%25", 200).size());
        ana.delete(gratuito, 204);
        ana.delete(privado, 204);
        banco.update("UPDATE tb_reporte SET dt_reporte = TIMESTAMP '2020-01-01 00:00:00' WHERE id_estacionamento = ?", id);
        assertFalse(publico.get(re, 200).get("atual").asBoolean());
        ana.delete(perfil, 409);
        local.put("ativo", 0);
        ana.put(lugar, local, 200);
        ana.post(lugar + "/reportes", relato, 409);
        assertEquals(0, publico.get(busca, 200).size());
        local.put("ativo", 1);
        local.put("tipo", "PRIVADO");
        local.put("primeiraHora", 18);
        local.put("demaisHoras", 8);
        ana.put(lugar, local, 200);
        ana.post(lugar + "/reportes", relato, 409);
        publico.get("/inexistente", 404);
        publico.enviar("PATCH", lugar, local, 405);

        for (String rota : List.of(fav, rg, re, av)) ana.delete(rota, 204);
        ana.delete(lugar, 204);
        publico.get(lugar, 404);
        ana.put("/usuarios/me", conta, 200);
        ana.post("/usuarios/logout", null, 204);
        ana.post("/usuarios/logout", null, 401);
        ana.get("/usuarios/me", 401);
        ana.post("/usuarios/login", conta, 200);
        var contaLocal = new HashMap<>(local);
        long orfao = ana.post(locais, contaLocal, 201).get("id").asLong();
        ana.delete("/usuarios/me", 204);
        assertTrue(publico.get(locais + "/" + orfao, 200).get("idUsuarioCriador").isNull());
        ana.get("/usuarios/me", 401);
        banco.execute("ALTER TABLE tb_reporte RENAME TO tb_reporte_indisponivel");
        try {
            assertEquals("Falha interna ao atender a requisição.", publico.get("/reportes", 500).get("mensagem").asString());
        } finally {
            banco.execute("ALTER TABLE tb_reporte_indisponivel RENAME TO tb_reporte");
        }
    }

}
