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
@RequestMapping("/estacionamentos")
@Transactional(rollbackFor = SQLException.class)
public class EstacionamentoController {
    private final EstacionamentoDao dao;
    private final Autenticacao autenticacao;
    private final ModelMapper mapper;

    public EstacionamentoController(EstacionamentoDao dao, Autenticacao autenticacao, ModelMapper mapper) {
        this.dao = dao;
        this.autenticacao = autenticacao;
        this.mapper = mapper;
    }

    @GetMapping
    public List<EstacionamentoResponseDto> listar(@RequestParam(required = false) String rua,
                                            @RequestParam(required = false) String bairro) throws SQLException {
        return dao.listar(rua == null ? null : rua.strip(), bairro == null ? null : bairro.strip()).stream().map(this::saida).toList();
    }

    @GetMapping("/{id}")
    public EstacionamentoResponseDto buscar(@PathVariable long id) throws SQLException {
        Estacionamento registro = dao.buscar(id);
        return saida(registro);
    }

    @PostMapping
    public ResponseEntity<EstacionamentoResponseDto> cadastrar(@Valid @RequestBody EstacionamentoRequestDto dto,
                                          HttpServletRequest request) throws SQLException {
        Usuario usuario = autenticacao.atual(request);
        Estacionamento registro = mapper.map(dto, Estacionamento.class);
        registro.setCriador(usuario);
        dao.cadastrar(registro);
        return ResponseEntity.created(URI.create("/estacionamentos/" + registro.getId())).body(saida(registro));
    }

    @PutMapping("/{id}")
    public EstacionamentoResponseDto atualizar(@PathVariable long id, @Valid @RequestBody EstacionamentoRequestDto dto,
                              HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Estacionamento registro = dao.bloquear(id);
        exigirLocalEditavel(request, registro);
        mapper.map(dto, registro);
        dao.atualizar(registro);
        return saida(dao.buscar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable long id, HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Estacionamento registro = dao.bloquear(id);
        exigirLocalEditavel(request, registro);
        dao.remover(id);
        return ResponseEntity.noContent().build();
    }

    private void exigirLocalEditavel(HttpServletRequest request, Estacionamento local) throws SQLException {
        autenticacao.exigirDono(request, local.getIdUsuarioCriador());
    }

    private EstacionamentoResponseDto saida(Estacionamento registro) {
        return mapper.map(registro, EstacionamentoResponseDto.class);
    }
}
