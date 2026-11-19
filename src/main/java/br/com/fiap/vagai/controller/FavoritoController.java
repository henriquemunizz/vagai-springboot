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
public class FavoritoController {
    private final FavoritoDao dao;
    private final EstacionamentoDao locais;
    private final Autenticacao autenticacao;
    private final ModelMapper mapper;

    public FavoritoController(FavoritoDao dao, EstacionamentoDao locais, Autenticacao autenticacao, ModelMapper mapper) {
        this.dao = dao;
        this.locais = locais;
        this.autenticacao = autenticacao;
        this.mapper = mapper;
    }

    @GetMapping({"/favoritos", "/usuarios/me/favoritos"})
    public List<FavoritoResponseDto> listar(HttpServletRequest request) throws SQLException {
        return dao.doUsuario(autenticacao.atual(request).getId()).stream().map(this::saida).toList();
    }

    @GetMapping("/favoritos/{id}")
    public FavoritoResponseDto buscar(@PathVariable long id, HttpServletRequest request) throws SQLException {
        long usuario = autenticacao.atual(request).getId();
        Favorito registro = dao.buscar(id);
        if (registro.getIdUsuario() != usuario) throw new SemPermissaoException();
        return saida(registro);
    }

    @PostMapping("/favoritos")
    public ResponseEntity<FavoritoResponseDto> cadastrar(@Valid @RequestBody FavoritoRequestDto dto,
                                          HttpServletRequest request) throws SQLException {
        Usuario usuario = autenticacao.atual(request);
        Favorito registro = mapper.map(dto, Favorito.class);
        registro.setUsuario(usuario);
        registro.setData(LocalDateTime.now(ZoneOffset.UTC).withNano(0));
        registro.setEstacionamento(locais.buscar(dto.getIdEstacionamento()));
        dao.cadastrar(registro);
        return ResponseEntity.created(URI.create("/favoritos/" + registro.getId())).body(saida(registro));
    }

    @PutMapping("/favoritos/{id}")
    public FavoritoResponseDto atualizar(@PathVariable long id, @Valid @RequestBody FavoritoRequestDto dto,
                              HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Favorito registro = dao.buscar(id);
        autenticacao.exigirDono(request, registro.getIdUsuario());
        locais.buscar(dto.getIdEstacionamento());
        mapper.map(dto, registro);
        dao.atualizar(registro);
        return saida(dao.buscar(id));
    }

    @DeleteMapping("/favoritos/{id}")
    public ResponseEntity<Void> remover(@PathVariable long id, HttpServletRequest request) throws SQLException {
        autenticacao.atual(request);
        Favorito registro = dao.buscar(id);
        autenticacao.exigirDono(request, registro.getIdUsuario());
        dao.remover(id);
        return ResponseEntity.noContent().build();
    }

    private FavoritoResponseDto saida(Favorito registro) {
        return mapper.map(registro, FavoritoResponseDto.class);
    }
}
