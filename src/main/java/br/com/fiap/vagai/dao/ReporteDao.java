package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Reporte;
import org.springframework.stereotype.Repository;
import br.com.fiap.vagai.factory.ConnectionFactory;
import br.com.fiap.vagai.exception.EntidadeNaoEncontradaException;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

@Repository
public class ReporteDao {
    private final ConnectionFactory factory;

    public ReporteDao(ConnectionFactory factory) {
        this.factory = factory;
    }

    private Reporte getReporte(ResultSet linha) throws SQLException {
        Reporte registro = new Reporte();
        registro.setId(linha.getLong("id_reporte"));
        registro.setIdUsuario(linha.getLong("id_usuario"));
        registro.setIdEstacionamento(linha.getLong("id_estacionamento"));
        registro.setTipo(linha.getString("tp_reporte"));
        registro.setObservacao(linha.getString("ds_observacao"));
        registro.setData(linha.getTimestamp("dt_reporte").toLocalDateTime());
        return registro;
    }

    public Reporte buscar(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_reporte WHERE id_reporte = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new EntidadeNaoEncontradaException();
                return getReporte(rs);
            }
        }
    }

    public List<Reporte> listar() throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_reporte ORDER BY id_reporte")) {

            try (ResultSet rs = stmt.executeQuery()) {
                List<Reporte> lista = new ArrayList<>();
                while (rs.next()) lista.add(getReporte(rs));
                return lista;
            }
        }
    }

    public List<Reporte> doLocal(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_reporte WHERE id_estacionamento = ? ORDER BY id_reporte DESC")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Reporte> lista = new ArrayList<>();
                while (rs.next()) lista.add(getReporte(rs));
                return lista;
            }
        }
    }

    public void cadastrar(Reporte registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT sq_tb_reporte.NEXTVAL FROM dual");
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            registro.setId(rs.getLong(1));
        }
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("""
                INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte,
                    ds_observacao, dt_reporte)
                VALUES (?, ?, ?, ?,
                    ?, ?)
                """)) {
            stmt.setLong(1, registro.getId());
            stmt.setLong(2, registro.getIdUsuario());
            stmt.setLong(3, registro.getIdEstacionamento());
            stmt.setString(4, registro.getTipo());
            stmt.setString(5, registro.getObservacao());
            stmt.setTimestamp(6, Timestamp.valueOf(registro.getData()));
            stmt.executeUpdate();
        }
    }

    public void atualizar(Reporte registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("UPDATE tb_reporte SET tp_reporte = ?, ds_observacao = ? WHERE id_reporte = ?")) {
            stmt.setString(1, registro.getTipo());
            stmt.setString(2, registro.getObservacao());
            stmt.setLong(3, registro.getId());
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }

    public void remover(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("DELETE FROM tb_reporte WHERE id_reporte = ?")) {
            stmt.setLong(1, id);
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }
}
