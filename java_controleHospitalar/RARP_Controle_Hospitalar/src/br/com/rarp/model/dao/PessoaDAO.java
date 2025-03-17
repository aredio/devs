package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Pessoa;

public class PessoaDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if (!SistemaCtrl.getInstance().tabelaExiste("cidade"))
			throw new Exception("Crie a tabela de cidade antes de criar a tabela de pessoa");

		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "pessoa(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "nome VARCHAR(255), ";
		sql += "logradouro VARCHAR(255), ";
		sql += "complemento VARCHAR, ";
		sql += "numero VARCHAR(50), ";
		sql += "bairro VARCHAR(255), ";
		sql += "cep CHAR(8), ";
		sql += "datanascimento TIMESTAMP WITHOUT TIME ZONE, ";
		sql += "codigo_cidade INTEGER REFERENCES cidade(codigo), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}

	public void salvar(Pessoa pessoa, Connection connection) throws Exception {
		if (pessoa.getCodigo() == 0)
			inserir(pessoa, connection);
		else
			alterar(pessoa, connection);
	}

	private void alterar(Pessoa pessoa, Connection connection) throws Exception {
		PreparedStatement ps;
		String sql = "UPDATE pessoa SET nome = ?,logradouro = ?, complemento = ?, numero = ?, bairro = ?, cep = ?, codigo_cidade = ?, datanascimento = ?, status = ? WHERE codigo = ?";
		ps = connection.prepareStatement(sql);
		ps.setString(1, pessoa.getNome());
		ps.setString(2, pessoa.getLogradouro());
		ps.setString(3, pessoa.getComplemento());
		ps.setString(4, pessoa.getNumero());
		ps.setString(5, pessoa.getBairro());
		ps.setString(6, pessoa.getCepSemMascara());
		if (pessoa.getCidade() != null && pessoa.getCidade().getCodigo() > 0)
			ps.setInt(7, pessoa.getCidade().getCodigo());
		else
			ps.setNull(7, Types.INTEGER);
		if (pessoa.getDtNascimento() != null)
			ps.setDate(8, Date.valueOf(pessoa.getDtNascimento()));
		else
			ps.setNull(8, Types.TIMESTAMP);
		ps.setBoolean(9, pessoa.isStatus());
		ps.setInt(10, pessoa.getCodigo());
		ps.executeUpdate();
		ps.close();

		TelefoneDAO telefoneDAO = new TelefoneDAO();
		telefoneDAO.salvar(pessoa.getTelefones(), pessoa.getCodigo(), connection);
	}

	private void inserir(Pessoa pessoa, Connection connection) throws Exception {
		PreparedStatement ps;
		String sql = "INSERT INTO pessoa(nome, logradouro, complemento, numero, bairro, cep, datanascimento, codigo_cidade, status) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		ps.setString(1, pessoa.getNome());
		ps.setString(2, pessoa.getLogradouro());
		ps.setString(3, pessoa.getComplemento());
		ps.setString(4, pessoa.getNumero());
		ps.setString(5, pessoa.getBairro());
		ps.setString(6, pessoa.getCepSemMascara());
		if (pessoa.getDtNascimento() != null)
			ps.setDate(7, Date.valueOf(pessoa.getDtNascimento()));
		else
			ps.setNull(7, Types.TIMESTAMP);
		if (pessoa.getCidade() != null && pessoa.getCidade().getCodigo() > 0)
			ps.setInt(8, pessoa.getCidade().getCodigo());
		else
			ps.setNull(8, Types.INTEGER);
		ps.setBoolean(9, pessoa.isStatus());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next())
			pessoa.setCodigo(rs.getInt(1));
		ps.close();

		TelefoneDAO telefoneDAO = new TelefoneDAO();
		telefoneDAO.salvar(pessoa.getTelefones(), pessoa.getCodigo(), connection);
	}
}
