package br.com.fiap.vagai.controller;

import br.com.fiap.vagai.config.Autenticacao;
import br.com.fiap.vagai.dao.*;
import br.com.fiap.vagai.dto.*;
import br.com.fiap.vagai.exception.*;
import br.com.fiap.vagai.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.sql.SQLException;
import java.time.*;
import java.util.List;

@RestController
@Transactional(rollbackFor = SQLException.class)
public class ReporteController {
    private final ReporteDao dao;
    private final EstacionamentoDao locais;
    private final Autenticacao autenticacao;
    private final ModelMapper mapper;

    public ReporteController(ReporteDao dao, EstacionamentoDao locais, Autenticacao autenticacao, ModelMapper mapper) {
        this.dao = dao;
        this.locais = locais;
        this.autenticacao = autenticacao;
        this.mapper = mapper;
    }

    @GetMapping("/reportes")
    public List<ReporteResponseDto> listar() throws SQLException {
        return dao.listar().stream().map(this::saida).toList();
    }

    @GetMapping("/reportes/{id}")
    public ReporteResponseDto buscar(@PathVariable long id) throws SQLException {
        Reporte registro = dao.buscar(id);
        return saida(registro);
    }

    @PostMapping("/reportes")
    public ResponseEntity<ReporteResponseDto> cadastrar(@Valid @RequestBody ReporteRequestDto dto,
                                          HttpServletRequest request) throws SQLException {
        Usuario usuario = autenticacao.atual(request);
        Reporte registro = mapper.map(dto, Reporte.class);
        registro.setUsuario(usuario);
        registro.setData(LocalDateTime.now(ZoneOffset.UTC).withNano(0));
        Estacionamento local = locais.bloquear(dto.getIdEstacionamento());
        validarLocal(local);
        registro.setEstacionamento(local);
        dao.cadastrar(registro);
        return ResponseEntity.created(URI.create("/reportes/" + registro.getId())).body(saida(registro));
    }

    @PutMapping("/reportes/{id}")
    public ReporteResponseDto atualizar(@PathVariable long id, @Valid @RequestBody ReporteRequestDto dto,
                              HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Reporte registro = dao.buscar(id);
        autenticacao.exigirDono(request, registro.getIdUsuario());
        if (!registro.getIdEstacionamento().equals(dto.getIdEstacionamento()))
            throw new ConflitoException("O vínculo com o estacionamento é permanente.");
        validarLocal(locais.bloquear(registro.getIdEstacionamento()));
        mapper.map(dto, registro);
        dao.atualizar(registro);
        return saida(dao.buscar(id));
    }

    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<Void> remover(@PathVariable long id, HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Reporte registro = dao.buscar(id);
        autenticacao.exigirDono(request, registro.getIdUsuario());
        dao.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estacionamentos/{id}/reportes")
    public List<ReporteResponseDto> doLocal(@PathVariable long id) throws SQLException {
        locais.buscar(id);
        return dao.doLocal(id).stream().map(this::saida).toList();
    }

    @PostMapping("/estacionamentos/{id}/reportes")
    public ResponseEntity<ReporteResponseDto> cadastrarNoLocal(@PathVariable long id,
            @Valid @RequestBody ReporteLocalRequestDto dados, HttpServletRequest request) throws SQLException {
        ReporteRequestDto dto = mapper.map(dados, ReporteRequestDto.class);
        dto.setIdEstacionamento(id);
        return cadastrar(dto, request);
    }

    private void validarLocal(Estacionamento local) {
        if (local.getAtivo() == 0) throw new ConflitoException("Local inativo.");
        if ("PRIVADO".equals(local.getTipo())) throw new ConflitoException("Local privado não recebe reportes de vaga.");
    }

    private ReporteResponseDto saida(Reporte registro) {
        ReporteResponseDto resposta = mapper.map(registro, ReporteResponseDto.class);
        resposta.setAtual(registro.atualEm(LocalDateTime.now(ZoneOffset.UTC)));
        return resposta;
    }
}
