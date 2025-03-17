package br.com.rarp.model.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Estado;

public class EstadoDAO {
	public static void criarRegistrosPadroes() throws ClassNotFoundException, SQLException, Exception {
		List<Estado> estados = new ArrayList<>();
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject) parser.parse(new FileReader("src/br/com/rarp/model/dao/estados-cidades.json"));
			JSONArray estadosJSON = (JSONArray) json.get("estados");
			
			for (Object estado: estadosJSON) {
				JSONObject estadoJSON = (JSONObject) estado;
				estados.add(new Estado((String) estadoJSON.get("nome"), (String) estadoJSON.get("sigla")));
			}
		} catch (FileNotFoundException e) {
			System.out.println("O arquivo não foi encontrado");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Não foi possível ler o arquivo");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("O arquivo não está no formato JSON");
			e.printStackTrace();
		}
		new EstadoDAO().salvar(estados);
	}

	private void salvar(List<Estado> estados) throws ClassNotFoundException, SQLException, Exception {
		if (estados.size() > 0) {
			String sql = "INSERT INTO estado (nome, uf)";
			sql += "SELECT ?, ? ";
			sql += "WHERE NOT EXISTS (SELECT uf FROM estado WHERE uf = ?)";

			PreparedStatement ps = SistemaCtrl.getInstance().getConexao().getConexao().prepareStatement(sql);
			try {
				int i = 0;
				for (Estado e : estados) {
					ps.setString(1, e.getNome());
					ps.setString(2, e.getUF());
					ps.setString(3, e.getUF());
					ps.addBatch();
					i++;

					if (i == estados.size()) {
						ps.executeBatch();
					}
				}
				ps.executeBatch();
			} finally {
				ps.close();
			} 
		}  
	}

	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "estado(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "uf CHAR(2) NOT NULL, ";
		sql += "nome VARCHAR(100))";
		st.executeUpdate(sql);
	}

	public List<Estado> consultar(String campo, String comparacao, String termo) throws SQLException, Exception {
		List<Estado> estados = new ArrayList<>();
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT codigo, uf, nome FROM estado WHERE " + campo + comparacao + termo;
			ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Estado estado = new Estado();
				estados.add(estado);
				estado.setCodigo(rs.getInt("codigo"));
				estado.setUF(rs.getString("uf"));
				estado.setNome(rs.getString("nome"));
			}
			ps.close();
		} finally {
			// conexao.close();
		}
		return estados;
	}

	public Estado get(Integer codigo) throws SQLException, Exception {
		if (codigo > 0) {
			List<Estado> estados = new ArrayList<>();
			estados = consultar("codigo", " = ", codigo + "");
			if (estados.size() > 0)
				return estados.get(0);
		}
		return null;
	}
}
