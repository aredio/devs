package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Pessoa;
import br.com.rarp.model.PessoaJuridica;

public class PessoaJuridicaDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if(!SistemaCtrl.getInstance().tabelaExiste("pessoa"))
			throw new Exception("Crie a tabela de pessoa antes de criar a tabela de pessoa juridica");
		
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "pessoaJuridica(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "razaosocial VARCHAR(200), ";
		sql += "cnpj CHAR(14), ";
		sql += "codigo_pessoa INTEGER REFERENCES pessoa(codigo), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}

	public void salvar(PessoaJuridica pessoaJuridica) throws Exception {
		if(pessoaJuridica != null)
			if(pessoaJuridica.getCodigo() == 0)
				inserir(pessoaJuridica);
			else
				alterar(pessoaJuridica);
	}

	private void alterar(PessoaJuridica pessoaJuridica) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "UPDATE pessoajuridica SET cnpj = ?, razaosocial = ?, status = ? WHERE codigo = ?";
			ps = conexao.prepareStatement(sql);
			ps.setString(1, pessoaJuridica.getCnpjSemMascara());
			ps.setString(2, pessoaJuridica.getRazaoSocial());
			ps.setBoolean(3, pessoaJuridica.isStatus());
			ps.setInt(4, pessoaJuridica.getCodigo());
			
			ps.executeUpdate();
			ps.close();
				
			ResultSet rs = conexao.createStatement().executeQuery("SELECT codigo_pessoa FROM pessoajuridica WHERE codigo = " + pessoaJuridica.getCodigo());
			if (rs.next()) {
				Pessoa pessoa = pessoaJuridica.clone();
				pessoa.setCodigo(rs.getInt("codigo_pessoa"));
				PessoaDAO pessoaDAO = new PessoaDAO();
				pessoaDAO.salvar(pessoa, conexao);
			}
		} finally {
			//conexao.close();
		}
	}

	private void inserir(PessoaJuridica pessoaJuridica) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoaDAO.salvar(pessoaJuridica, conexao);

			String sql = "INSERT INTO pessoajuridica(cnpj, razaosocial, codigo_pessoa, status) VALUES(?,?,?,?)";
			ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, pessoaJuridica.getCnpjSemMascara());
			ps.setString(2, pessoaJuridica.getRazaoSocial());
			ps.setInt(3, pessoaJuridica.getCodigo());
			ps.setBoolean(4, pessoaJuridica.isStatus());
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				pessoaJuridica.setCodigo(rs.getInt("codigo"));
			ps.close();
		} finally {
			//conexao.close();
		}
	}
}
