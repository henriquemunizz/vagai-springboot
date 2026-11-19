package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Regra;
import org.springframework.stereotype.Repository;
import br.com.fiap.vagai.factory.ConnectionFactory;
import br.com.fiap.vagai.exception.EntidadeNaoEncontradaException;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

@Repository
public class RegraDao {
    private final ConnectionFactory factory;

    public RegraDao(ConnectionFactory factory) {
        this.factory = factory;
    }

    private Regra getRegra(ResultSet linha) throws SQLException {
        Regra registro = new Regra();
        registro.setId(linha.getLong("id_regra"));
        registro.setIdEstacionamento(linha.getLong("id_estacionamento"));
        registro.setDescricao(linha.getString("ds_regra"));
        registro.setDiaSemana(linha.getInt("nr_dia_semana"));
        registro.setInicio(linha.getInt("nr_inicio"));
        registro.setFim(linha.getInt("nr_fim"));
        registro.setAtivo(linha.getInt("st_ativo"));
        registro.setTipo(linha.getString("tp_regra"));
        return registro;
    }

    public Regra buscar(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_regra_estacionamento WHERE id_regra = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new EntidadeNaoEncontradaException();
                return getRegra(rs);
            }
        }
    }

    public List<Regra> listar() throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_regra_estacionamento ORDER BY id_regra")) {

            try (ResultSet rs = stmt.executeQuery()) {
                List<Regra> lista = new ArrayList<>();
                while (rs.next()) lista.add(getRegra(rs));
                return lista;
            }
        }
    }

    public List<Regra> doLocal(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_regra_estacionamento WHERE id_estacionamento = ? ORDER BY id_regra DESC")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Regra> lista = new ArrayList<>();
                while (rs.next()) lista.add(getRegra(rs));
                return lista;
            }
        }
    }

    public void cadastrar(Regra registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT sq_tb_regra_estacionamento.NEXTVAL FROM dual");
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            registro.setId(rs.getLong(1));
        }
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("""
                INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, nr_dia_semana,
                    nr_inicio, nr_fim, tp_regra, st_ativo)
                VALUES (?, ?, ?, ?, ?,
                    ?, ?, ?)
                """)) {
            stmt.setLong(1, registro.getId());
            stmt.setLong(2, registro.getIdEstacionamento());
            stmt.setString(3, registro.getDescricao());
            stmt.setInt(4, registro.getDiaSemana());
            stmt.setInt(5, registro.getInicio());
            stmt.setInt(6, registro.getFim());
            stmt.setString(7, registro.getTipo());
            stmt.setInt(8, registro.getAtivo());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Regra registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("""
                UPDATE tb_regra_estacionamento
                SET ds_regra = ?, nr_dia_semana = ?, nr_inicio = ?, nr_fim = ?, tp_regra = ?,
                    st_ativo = ?
                WHERE id_regra = ?
                """)) {
            stmt.setString(1, registro.getDescricao());
            stmt.setInt(2, registro.getDiaSemana());
            stmt.setInt(3, registro.getInicio());
            stmt.setInt(4, registro.getFim());
            stmt.setString(5, registro.getTipo());
            stmt.setInt(6, registro.getAtivo());
            stmt.setLong(7, registro.getId());
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }

    public void remover(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("DELETE FROM tb_regra_estacionamento WHERE id_regra = ?")) {
            stmt.setLong(1, id);
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }
}
