package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Movimentacao;

public class MovimentacaoDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "movimentacao(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "data DATE, ";
		sql += "hora TIME, ";
		sql += "codigo_usuario INTEGER REFERENCES usuario(codigo), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}
	
	public void salvar(Connection connection, Movimentacao movimentacao) throws Exception {
		if(movimentacao.getCodigo() == 0)
			inserir(connection, movimentacao);
		else
			alterar(connection, movimentacao);
	}

	private void alterar(Connection connection, Movimentacao movimentacao) throws Exception {
		String sql = "UPDATE movimentacao SET data = ?, hora = ?, codigo_usuario = ?, status = ? WHERE codigo = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setDate(1, Date.valueOf(movimentacao.getDtMovimentacao()));
		ps.setTime(2, Time.valueOf(movimentacao.getHrMovimentacao()));
		if(movimentacao.getUsuario() != null)
			ps.setInt(3, movimentacao.getUsuario().getCodigo());
		else
			ps.setNull(3, Types.INTEGER);
		ps.setBoolean(4, movimentacao.isStatus());
		ps.setInt(5, movimentacao.getCodigo());
		ps.executeUpdate();
		ps.close();
	}

	private void inserir(Connection connection, Movimentacao movimentacao) throws SQLException {
		String sql = "INSERT INTO movimentacao(data, hora, codigo_usuario, status) VALUES(?,?,?,?)";
		PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		ps.setDate(1, Date.valueOf(movimentacao.getDtMovimentacao()));
		ps.setTime(2, Time.valueOf(movimentacao.getHrMovimentacao()));
		if(movimentacao.getUsuario() != null)
			ps.setInt(3, movimentacao.getUsuario().getCodigo());
		else
			ps.setNull(3, Types.INTEGER);
		ps.setBoolean(4, movimentacao.isStatus());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if(rs.next())
			movimentacao.setCodigo(rs.getInt(1));
		ps.close();
	}
}
