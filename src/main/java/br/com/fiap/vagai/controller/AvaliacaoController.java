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
public class AvaliacaoController {
    private final AvaliacaoDao dao;
    private final EstacionamentoDao locais;
    private final Autenticacao autenticacao;
    private final ModelMapper mapper;

    public AvaliacaoController(AvaliacaoDao dao, EstacionamentoDao locais, Autenticacao autenticacao, ModelMapper mapper) {
        this.dao = dao;
        this.locais = locais;
        this.autenticacao = autenticacao;
        this.mapper = mapper;
    }

    @GetMapping("/avaliacoes")
    public List<AvaliacaoResponseDto> listar() throws SQLException {
        return dao.listar().stream().map(this::saida).toList();
    }

    @GetMapping("/avaliacoes/{id}")
    public AvaliacaoResponseDto buscar(@PathVariable long id) throws SQLException {
        Avaliacao registro = dao.buscar(id);
        return saida(registro);
    }

    @PostMapping("/avaliacoes")
    public ResponseEntity<AvaliacaoResponseDto> cadastrar(@Valid @RequestBody AvaliacaoRequestDto dto,
                                          HttpServletRequest request) throws SQLException {
        Usuario usuario = autenticacao.atual(request);
        Avaliacao registro = mapper.map(dto, Avaliacao.class);
        registro.setUsuario(usuario);
        registro.setData(LocalDateTime.now(ZoneOffset.UTC).withNano(0));
        registro.setEstacionamento(locais.buscar(dto.getIdEstacionamento()));
        dao.cadastrar(registro);
        return ResponseEntity.created(URI.create("/avaliacoes/" + registro.getId())).body(saida(registro));
    }

    @PutMapping("/avaliacoes/{id}")
    public AvaliacaoResponseDto atualizar(@PathVariable long id, @Valid @RequestBody AvaliacaoRequestDto dto,
                              HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Avaliacao registro = dao.buscar(id);
        autenticacao.exigirDono(request, registro.getIdUsuario());
        if (!registro.getIdEstacionamento().equals(dto.getIdEstacionamento()))
            throw new ConflitoException("O vínculo com o estacionamento é permanente.");
        mapper.map(dto, registro);
        dao.atualizar(registro);
        return saida(dao.buscar(id));
    }

    @DeleteMapping("/avaliacoes/{id}")
    public ResponseEntity<Void> remover(@PathVariable long id, HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Avaliacao registro = dao.buscar(id);
        autenticacao.exigirDono(request, registro.getIdUsuario());
        dao.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estacionamentos/{id}/avaliacoes")
    public List<AvaliacaoResponseDto> doLocal(@PathVariable long id) throws SQLException {
        locais.buscar(id);
        return dao.doLocal(id).stream().map(this::saida).toList();
    }

    @PostMapping("/estacionamentos/{id}/avaliacoes")
    public ResponseEntity<AvaliacaoResponseDto> cadastrarNoLocal(@PathVariable long id,
            @Valid @RequestBody AvaliacaoLocalRequestDto dados, HttpServletRequest request) throws SQLException {
        AvaliacaoRequestDto dto = mapper.map(dados, AvaliacaoRequestDto.class);
        dto.setIdEstacionamento(id);
        return cadastrar(dto, request);
    }

    private AvaliacaoResponseDto saida(Avaliacao registro) {
        return mapper.map(registro, AvaliacaoResponseDto.class);
    }
}
