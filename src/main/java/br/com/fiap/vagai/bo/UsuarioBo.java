package br.com.fiap.vagai.bo;

import br.com.fiap.vagai.config.Senha;
import br.com.fiap.vagai.dao.UsuarioDao;
import br.com.fiap.vagai.dto.LoginRequestDto;
import br.com.fiap.vagai.dto.UsuarioCadastroRequestDto;
import br.com.fiap.vagai.dto.UsuarioAtualizacaoRequestDto;
import br.com.fiap.vagai.exception.ConflitoException;
import br.com.fiap.vagai.exception.NaoAutenticadoException;
import br.com.fiap.vagai.model.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Transactional(rollbackFor = SQLException.class)
public class UsuarioBo {
    private final UsuarioDao dao;

    public UsuarioBo(UsuarioDao dao) {
        this.dao = dao;
    }

    public Usuario cadastrar(UsuarioCadastroRequestDto dto) throws SQLException {
        String nome = dto.getNome().strip();
        if (!dao.porNome(nome).isEmpty()) throw new ConflitoException("Nome de usuário já cadastrado.");
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setSenhaHash(Senha.gerar(dto.getSenha()));
        usuario.setCadastro(LocalDateTime.now(ZoneOffset.UTC).withNano(0));
        dao.cadastrar(usuario);
        return usuario;
    }

    public Usuario login(LoginRequestDto dto) throws SQLException {
        Usuario usuario = dao.porNome(dto.getNome().strip()).stream()
                .findFirst().orElseThrow(NaoAutenticadoException::new);
        if (!Senha.conferir(dto.getSenha(), usuario.getSenhaHash())) throw new NaoAutenticadoException();
        return usuario;
    }

    public Usuario atualizar(long id, UsuarioAtualizacaoRequestDto dto) throws SQLException {
        Usuario usuario = dao.buscar(id);
        usuario.setNome(dto.getNome().strip());
        usuario.setSenhaHash(Senha.gerar(dto.getSenha()));
        dao.atualizar(usuario);
        return usuario;
    }
}
