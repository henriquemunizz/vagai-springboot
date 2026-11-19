package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Favorito;
import org.springframework.stereotype.Repository;
import br.com.fiap.vagai.factory.ConnectionFactory;
import br.com.fiap.vagai.exception.EntidadeNaoEncontradaException;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

@Repository
public class FavoritoDao {
    private final ConnectionFactory factory;

    public FavoritoDao(ConnectionFactory factory) {
        this.factory = factory;
    }

    private Favorito getFavorito(ResultSet linha) throws SQLException {
        Favorito registro = new Favorito();
        registro.setId(linha.getLong("id_usuario_favorito"));
        registro.setIdUsuario(linha.getLong("id_usuario"));
        registro.setIdEstacionamento(linha.getLong("id_estacionamento"));
        registro.setData(linha.getTimestamp("dt_favorito").toLocalDateTime());
        return registro;
    }

    public Favorito buscar(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_usuario_favorito WHERE id_usuario_favorito = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new EntidadeNaoEncontradaException();
                return getFavorito(rs);
            }
        }
    }

    public List<Favorito> doUsuario(long usuario) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_usuario_favorito WHERE id_usuario = ? ORDER BY id_usuario_favorito")) {
            stmt.setLong(1, usuario);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Favorito> lista = new ArrayList<>();
                while (rs.next()) lista.add(getFavorito(rs));
                return lista;
            }
        }
    }

    public void cadastrar(Favorito registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT sq_tb_usuario_favorito.NEXTVAL FROM dual");
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            registro.setId(rs.getLong(1));
        }
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("""
                INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
                VALUES (?, ?, ?, ?)
                """)) {
            stmt.setLong(1, registro.getId());
            stmt.setLong(2, registro.getIdUsuario());
            stmt.setLong(3, registro.getIdEstacionamento());
            stmt.setTimestamp(4, Timestamp.valueOf(registro.getData()));
            stmt.executeUpdate();
        }
    }

    public void atualizar(Favorito registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("UPDATE tb_usuario_favorito SET id_estacionamento = ? WHERE id_usuario_favorito = ?")) {
            stmt.setLong(1, registro.getIdEstacionamento());
            stmt.setLong(2, registro.getId());
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }

    public void remover(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("DELETE FROM tb_usuario_favorito WHERE id_usuario_favorito = ?")) {
            stmt.setLong(1, id);
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }
}
