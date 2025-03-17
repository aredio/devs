package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Pessoa;
import br.com.rarp.model.PessoaFisica;

public class PessoaFisicaDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if (!SistemaCtrl.getInstance().tabelaExiste("pessoa"))
			throw new Exception("Crie a tabela de pessoa antes de criar a tabela de pessoa fisica");

		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "pessoaFisica(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "cpf CHAR(11), ";
		sql += "rg VARCHAR(20), ";
		sql += "sexo VARCHAR(20), ";
		sql += "possuiNecessidades BOOLEAN, ";
		sql += "certidaoNascimento VARCHAR(50), ";
		sql += "codigo_pessoa INTEGER REFERENCES pessoa(codigo), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}

	public void salvar(PessoaFisica pessoaFisica, Connection connection) throws Exception {
		if (pessoaFisica.getCodigo() == 0)
			inserir(pessoaFisica, connection);
		else
			alterar(pessoaFisica, connection);
	}

	private void alterar(PessoaFisica pessoaFisica, Connection connection) throws Exception {
		PreparedStatement ps;

		String sql = "UPDATE pessoafisica SET cpf=?, rg=?, sexo=?, possuiNecessidades=?, certidaoNascimento=?, status=? WHERE codigo = ?";
		ps = connection.prepareStatement(sql);
		ps.setString(1, pessoaFisica.getCpfSemMascara());
		ps.setString(2, pessoaFisica.getRg());
		ps.setString(3, pessoaFisica.getSexo());
		ps.setBoolean(4, pessoaFisica.isPossuiNecessidades());
		ps.setString(5, pessoaFisica.getCertidaoNascimento());
		ps.setBoolean(6, pessoaFisica.isStatus());
		ps.setInt(7, pessoaFisica.getCodigo());

		ps.executeUpdate();
		ps.close();

		ResultSet rs = connection.createStatement()
				.executeQuery("SELECT codigo_pessoa FROM pessoafisica WHERE codigo = " + pessoaFisica.getCodigo());
		if (rs.next()) {
			Pessoa pessoa = pessoaFisica.clone();
			pessoa.setCodigo(rs.getInt("codigo_pessoa"));
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoaDAO.salvar(pessoa, connection);
		}
	}

	private void inserir(PessoaFisica pessoaFisica, Connection connection) throws Exception {
		PreparedStatement ps;
		PessoaDAO pessoaDAO = new PessoaDAO();
		pessoaDAO.salvar(pessoaFisica, connection);

		String sql = "INSERT INTO pessoaFisica(cpf, rg, sexo, possuiNecessidades, certidaoNascimento,  codigo_pessoa, status) VALUES(?,?,?,?,?,?,?)";
		ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		ps.setString(1, pessoaFisica.getCpfSemMascara());
		ps.setString(2, pessoaFisica.getRg());
		ps.setString(3, pessoaFisica.getSexo());
		ps.setBoolean(4, pessoaFisica.isPossuiNecessidades());
		ps.setString(5, pessoaFisica.getCertidaoNascimento());
		ps.setInt(6, pessoaFisica.getCodigo());
		ps.setBoolean(7, pessoaFisica.isStatus());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next())
			pessoaFisica.setCodigo(rs.getInt(1));
		ps.close();
	}
}
