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
public class RegraController {
    private final RegraDao dao;
    private final EstacionamentoDao locais;
    private final Autenticacao autenticacao;
    private final ModelMapper mapper;

    public RegraController(RegraDao dao, EstacionamentoDao locais, Autenticacao autenticacao, ModelMapper mapper) {
        this.dao = dao;
        this.locais = locais;
        this.autenticacao = autenticacao;
        this.mapper = mapper;
    }

    @GetMapping("/regras")
    public List<RegraResponseDto> listar() throws SQLException {
        return dao.listar().stream().map(this::saida).toList();
    }

    @GetMapping("/regras/{id}")
    public RegraResponseDto buscar(@PathVariable long id) throws SQLException {
        Regra registro = dao.buscar(id);
        return saida(registro);
    }

    @PostMapping("/regras")
    public ResponseEntity<RegraResponseDto> cadastrar(@Valid @RequestBody RegraRequestDto dto,
                                          HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Regra registro = mapper.map(dto, Regra.class);
        exigirLocalEditavel(request, locais.bloquear(dto.getIdEstacionamento()));
        dao.cadastrar(registro);
        return ResponseEntity.created(URI.create("/regras/" + registro.getId())).body(saida(registro));
    }

    @PutMapping("/regras/{id}")
    public RegraResponseDto atualizar(@PathVariable long id, @Valid @RequestBody RegraRequestDto dto,
                              HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Regra registro = dao.buscar(id);
        exigirLocalEditavel(request, locais.bloquear(registro.getIdEstacionamento()));
        if (!registro.getIdEstacionamento().equals(dto.getIdEstacionamento()))
            throw new ConflitoException("O vínculo com o estacionamento é permanente.");
        mapper.map(dto, registro);
        dao.atualizar(registro);
        return saida(dao.buscar(id));
    }

    @DeleteMapping("/regras/{id}")
    public ResponseEntity<Void> remover(@PathVariable long id, HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Regra registro = dao.buscar(id);
        exigirLocalEditavel(request, locais.bloquear(registro.getIdEstacionamento()));
        dao.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estacionamentos/{id}/regras")
    public List<RegraResponseDto> doLocal(@PathVariable long id,
            @RequestParam(required = false) Integer diaSemana,
            @RequestParam(required = false) Integer minuto) throws SQLException {
        locais.buscar(id);
        if ((diaSemana == null) != (minuto == null))
            throw new EntradaInvalidaException("Informe diaSemana e minuto juntos.");
        if (diaSemana != null && (diaSemana < 1 || diaSemana > 7 || minuto < 0 || minuto > 1439))
            throw new EntradaInvalidaException("Dia deve estar entre 1 e 7 e minuto entre 0 e 1439.");
        return dao.doLocal(id).stream()
                .filter(regra -> diaSemana == null || regra.aplicaEm(diaSemana, minuto))
                .map(this::saida).toList();
    }

    private void exigirLocalEditavel(HttpServletRequest request, Estacionamento local) throws SQLException {
        autenticacao.exigirDono(request, local.getIdUsuarioCriador());
    }

    private RegraResponseDto saida(Regra registro) {
        return mapper.map(registro, RegraResponseDto.class);
    }
}
