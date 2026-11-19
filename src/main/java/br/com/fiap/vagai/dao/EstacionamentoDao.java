package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Estacionamento;
import org.springframework.stereotype.Repository;
import br.com.fiap.vagai.factory.ConnectionFactory;
import br.com.fiap.vagai.exception.EntidadeNaoEncontradaException;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

@Repository
public class EstacionamentoDao {
    private final ConnectionFactory factory;

    public EstacionamentoDao(ConnectionFactory factory) {
        this.factory = factory;
    }

    private Estacionamento getEstacionamento(ResultSet linha) throws SQLException {
        Estacionamento registro = new Estacionamento();
        registro.setId(linha.getLong("id_estacionamento"));
        registro.setIdUsuarioCriador(linha.getObject("id_usuario_criador") == null ? null : linha.getLong("id_usuario_criador"));
        registro.setNome(linha.getString("nm_estacionamento"));
        registro.setDescricao(linha.getString("ds_estacionamento"));
        registro.setTipo(linha.getString("tp_estacionamento"));
        registro.setAtivo(linha.getInt("st_ativo"));
        registro.setRua(linha.getString("nm_rua"));
        registro.setBairro(linha.getString("nm_bairro"));
        registro.setNumero(linha.getString("ds_numero"));
        registro.setPrimeiraHora(linha.getBigDecimal("vl_primeira_hora"));
        registro.setDemaisHoras(linha.getBigDecimal("vl_demais_horas"));
        return registro;
    }

    public Estacionamento buscar(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_estacionamento WHERE id_estacionamento = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new EntidadeNaoEncontradaException();
                return getEstacionamento(rs);
            }
        }
    }

    public Estacionamento bloquear(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM tb_estacionamento WHERE id_estacionamento = ? FOR UPDATE")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new EntidadeNaoEncontradaException();
                return getEstacionamento(rs);
            }
        }
    }

    public List<Estacionamento> listar(String rua, String bairro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("""
                SELECT * FROM tb_estacionamento
                WHERE st_ativo = 1 AND
                    (? IS NULL OR INSTR(UPPER(nm_rua), UPPER(?)) > 0) AND
                    (? IS NULL OR INSTR(UPPER(nm_bairro), UPPER(?)) > 0)
                ORDER BY UPPER(nm_rua), UPPER(nm_bairro), ds_numero, id_estacionamento
                """)) {
            stmt.setString(1, rua);
            stmt.setString(2, rua);
            stmt.setString(3, bairro);
            stmt.setString(4, bairro);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Estacionamento> lista = new ArrayList<>();
                while (rs.next()) lista.add(getEstacionamento(rs));
                return lista;
            }
        }
    }

    public void cadastrar(Estacionamento registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT sq_tb_estacionamento.NEXTVAL FROM dual");
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            registro.setId(rs.getLong(1));
        }
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("""
                INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, ds_estacionamento,
                    tp_estacionamento, nm_rua, nm_bairro, ds_numero,
                    vl_primeira_hora, vl_demais_horas, st_ativo)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """)) {
            stmt.setLong(1, registro.getId());
            if (registro.getIdUsuarioCriador() == null) stmt.setNull(2, Types.NUMERIC);
            else stmt.setLong(2, registro.getIdUsuarioCriador());
            stmt.setString(3, registro.getNome());
            stmt.setString(4, registro.getDescricao());
            stmt.setString(5, registro.getTipo());
            stmt.setString(6, registro.getRua());
            stmt.setString(7, registro.getBairro());
            stmt.setString(8, registro.getNumero());
            stmt.setBigDecimal(9, registro.getPrimeiraHora());
            stmt.setBigDecimal(10, registro.getDemaisHoras());
            stmt.setInt(11, registro.getAtivo());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Estacionamento registro) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("""
                UPDATE tb_estacionamento SET nm_estacionamento = ?, ds_estacionamento = ?, tp_estacionamento = ?, nm_rua = ?,
                    nm_bairro = ?, ds_numero = ?, vl_primeira_hora = ?,
                    vl_demais_horas = ?, st_ativo = ?
                WHERE id_estacionamento = ?
                """)) {
            stmt.setString(1, registro.getNome());
            stmt.setString(2, registro.getDescricao());
            stmt.setString(3, registro.getTipo());
            stmt.setString(4, registro.getRua());
            stmt.setString(5, registro.getBairro());
            stmt.setString(6, registro.getNumero());
            stmt.setBigDecimal(7, registro.getPrimeiraHora());
            stmt.setBigDecimal(8, registro.getDemaisHoras());
            stmt.setInt(9, registro.getAtivo());
            stmt.setLong(10, registro.getId());
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }

    public void remover(long id) throws SQLException {
        try (Connection conexao = factory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("DELETE FROM tb_estacionamento WHERE id_estacionamento = ?")) {
            stmt.setLong(1, id);
            if (stmt.executeUpdate() == 0) throw new EntidadeNaoEncontradaException();
        }
    }
}
