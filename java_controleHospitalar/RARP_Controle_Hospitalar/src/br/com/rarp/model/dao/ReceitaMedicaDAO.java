package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.ReceitaMedica;

public class ReceitaMedicaDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {	
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "receita(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "descricao VARCHAR)";
		st.executeUpdate(sql);
	}

	public void salvar(Connection connection, ReceitaMedica receitaMedica) throws SQLException {
		if(receitaMedica != null) {
			if(receitaMedica.getCodigo() == 0)
				inserir(connection, receitaMedica);
			else
				alterar(connection, receitaMedica);
		}
	}

	private void alterar(Connection connection, ReceitaMedica receitaMedica) throws SQLException {
		String sql= "UPDATE receita SET "
				+ "descricao = ? "
				+ "WHERE "
				+ "codigo = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, receitaMedica.getDescricao());
		ps.setInt(2, receitaMedica.getCodigo());
		ps.executeUpdate();
		ps.close();
	}

	private void inserir(Connection connection, ReceitaMedica receitaMedica) throws SQLException {
		String sql= "INSERT INTO receita(descricao) VALUES(?)";
		PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		ps.setString(1, receitaMedica.getDescricao());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next())
			receitaMedica.setCodigo(rs.getInt(1));
		ps.close();
	}

	public ReceitaMedica getReceita(int codigo) throws ClassNotFoundException, SQLException, Exception {
		if(codigo > 0) {
			List<ReceitaMedica> receitaMedicas = consultar("REC.codigo", " = ", codigo + "");
			if(receitaMedicas.size() > 1)
				return receitaMedicas.get(0);
		}
		return null;
	}

	private List<ReceitaMedica> consultar(String campo, String comparacao, String termo) throws ClassNotFoundException, SQLException, Exception {
		List<ReceitaMedica> receitaMedicas = new ArrayList<>();
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT "
					+ "REC.codigo, "
					+ "REC.descricao "
					+ "FROM receita AS REC "
					+ "WHERE " + campo + comparacao + termo;
			ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ReceitaMedica receitaMedica = new ReceitaMedica(rs.getString("descricao"));
				receitaMedica.setCodigo(rs.getInt("codigo"));
			}
			ps.close();
		} finally {
			//conexao.close();
		}
		return receitaMedicas;
	}
}
