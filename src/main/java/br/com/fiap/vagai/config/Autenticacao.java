package br.com.fiap.vagai.config;

import br.com.fiap.vagai.dao.UsuarioDao;
import br.com.fiap.vagai.exception.*;
import br.com.fiap.vagai.model.Usuario;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class Autenticacao {
    private final UsuarioDao usuarios;

    public Autenticacao(UsuarioDao usuarios) {
        this.usuarios = usuarios;
    }

    public void iniciar(HttpServletRequest request, Usuario usuario) {
        HttpSession anterior = request.getSession(false);
        if (anterior != null) anterior.invalidate();
        HttpSession nova = request.getSession(true);
        nova.setAttribute("id", usuario.getId());
        nova.setAttribute("credencial", usuario.getSenhaHash());
    }

    public Usuario atual(HttpServletRequest request) throws SQLException {
        HttpSession sessao = request.getSession(false);
        if (sessao == null || sessao.getAttribute("id") == null) throw new NaoAutenticadoException();
        try {
            Usuario usuario = usuarios.buscar((Long) sessao.getAttribute("id"));
            if (usuario.getSenhaHash().equals(sessao.getAttribute("credencial"))) return usuario;
        } catch (EntidadeNaoEncontradaException removido) {
            // A conta foi excluída durante a sessão.
        }
        sessao.invalidate();
        throw new NaoAutenticadoException();
    }

    public void exigirDono(HttpServletRequest request, Long dono) throws SQLException {
        if (!Objects.equals(atual(request).getId(), dono)) throw new SemPermissaoException();
    }
}
