package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Avaliacao;
import org.springframework.stereotype.Repository;
import br.com.fiap.vagai.factory.ConnectionFactory;
import br.com.fiap.vagai.exception.EntidadeNaoEncontradaException;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

@Repository
public class AvaliacaoDao {
    private final ConnectionFactory factory;

    public AvaliacaoDao(ConnectionFactory factory) {
        this.factory = factory;
    }

    private Avaliacao getAvaliacao(ResultSet linha) throws SQLException {
        Avaliacao registro = new Avaliacao();
        registro.setId(linha.getLong("id_avaliacao"));
        registro.setIdUsuario(linha.getLong("id_usuario"));
        registro.setIdEstacionamento(linha.getLong("id_estacionamento"));
        registro.setNota(linha.getInt("nr_nota"));
        registro.setComentario(linha.getString("ds_comentario"));
        registro.setData(linha.getTimestamp("dt_avaliacao").toLocalDateTime());
        return registro;
    }

    public Avaliacao buscar(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_avaliacao WHERE id_avaliacao = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new EntidadeNaoEncontradaException();
                return getAvaliacao(rs);
            }
        }
    }

    public List<Avaliacao> listar() throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_avaliacao ORDER BY id_avaliacao")) {

            try (ResultSet rs = stmt.executeQuery()) {
                List<Avaliacao> lista = new ArrayList<>();
                while (rs.next()) lista.add(getAvaliacao(rs));
                return lista;
            }
        }
    }

    public List<Avaliacao> doLocal(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_avaliacao WHERE id_estacionamento = ? ORDER BY id_avaliacao DESC")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Avaliacao> lista = new ArrayList<>();
                while (rs.next()) lista.add(getAvaliacao(rs));
                return lista;
            }
        }
    }

    public void cadastrar(Avaliacao registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT sq_tb_avaliacao.NEXTVAL FROM dual");
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            registro.setId(rs.getLong(1));
        }
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("""
                INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota,
                    ds_comentario, dt_avaliacao)
                VALUES (?, ?, ?, ?,
                    ?, ?)
                """)) {
            stmt.setLong(1, registro.getId());
            stmt.setLong(2, registro.getIdUsuario());
            stmt.setLong(3, registro.getIdEstacionamento());
            stmt.setInt(4, registro.getNota());
            stmt.setString(5, registro.getComentario());
            stmt.setTimestamp(6, Timestamp.valueOf(registro.getData()));
            stmt.executeUpdate();
        }
    }

    public void atualizar(Avaliacao registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("UPDATE tb_avaliacao SET nr_nota = ?, ds_comentario = ? WHERE id_avaliacao = ?")) {
            stmt.setInt(1, registro.getNota());
            stmt.setString(2, registro.getComentario());
            stmt.setLong(3, registro.getId());
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }

    public void remover(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("DELETE FROM tb_avaliacao WHERE id_avaliacao = ?")) {
            stmt.setLong(1, id);
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }
}
