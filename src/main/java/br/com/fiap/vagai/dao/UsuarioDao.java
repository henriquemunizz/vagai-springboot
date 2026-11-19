package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Usuario;
import org.springframework.stereotype.Repository;
import br.com.fiap.vagai.factory.ConnectionFactory;
import br.com.fiap.vagai.exception.EntidadeNaoEncontradaException;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

@Repository
public class UsuarioDao {
    private final ConnectionFactory factory;

    public UsuarioDao(ConnectionFactory factory) {
        this.factory = factory;
    }

    private Usuario getUsuario(ResultSet linha) throws SQLException {
        Usuario registro = new Usuario();
        registro.setId(linha.getLong("id_usuario"));
        registro.setNome(linha.getString("nm_usuario"));
        registro.setSenhaHash(linha.getString("ds_senha_hash"));
        registro.setCadastro(linha.getTimestamp("dt_cadastro").toLocalDateTime());
        return registro;
    }

    public Usuario buscar(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_usuario WHERE id_usuario = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new EntidadeNaoEncontradaException();
                return getUsuario(rs);
            }
        }
    }

    public List<Usuario> porNome(String nome) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_usuario WHERE nm_usuario = ?")) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Usuario> lista = new ArrayList<>();
                while (rs.next()) lista.add(getUsuario(rs));
                return lista;
            }
        }
    }

    public void cadastrar(Usuario registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT sq_tb_usuario.NEXTVAL FROM dual");
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            registro.setId(rs.getLong(1));
        }
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha_hash, dt_cadastro) VALUES (?, ?, ?, ?)")) {
            stmt.setLong(1, registro.getId());
            stmt.setString(2, registro.getNome());
            stmt.setString(3, registro.getSenhaHash());
            stmt.setTimestamp(4, Timestamp.valueOf(registro.getCadastro()));
            stmt.executeUpdate();
        }
    }

    public void atualizar(Usuario registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("UPDATE tb_usuario SET nm_usuario = ?, ds_senha_hash = ? WHERE id_usuario = ?")) {
            stmt.setString(1, registro.getNome());
            stmt.setString(2, registro.getSenhaHash());
            stmt.setLong(3, registro.getId());
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }

    public void remover(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("DELETE FROM tb_usuario WHERE id_usuario = ?")) {
            stmt.setLong(1, id);
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }
}
