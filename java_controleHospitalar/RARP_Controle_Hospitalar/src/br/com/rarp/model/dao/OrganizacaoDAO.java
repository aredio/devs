package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Cidade;
import br.com.rarp.model.Organizacao;
import br.com.rarp.model.PessoaJuridica;
import br.com.rarp.model.Estado;

public class OrganizacaoDAO {

	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {

		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "organizacao(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "codigo_pj integer NOT NULL REFERENCES pessoaJuridica(codigo), ";
		sql += "CONSTRAINT organizacao_pj UNIQUE (codigo_pj) ";
		sql += ")";

		try {
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Erro ao criar tabela de configuracoes");

		}

	}

	public void salvar() throws Exception {
		if(SistemaCtrl.getInstance().getOrganizacao().getCodigo() == 0)
			inserir(SistemaCtrl.getInstance().getOrganizacao());
		else
			alterar(SistemaCtrl.getInstance().getOrganizacao());
	}
	
	private void alterar(Organizacao organizacao) throws Exception {
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			ResultSet rs = conexao.createStatement().executeQuery("SELECT codigo_pj FROM organizacao WHERE codigo = " + organizacao.getCodigo());
			if(rs.next()) {
				PessoaJuridica pj = organizacao.clone();
				pj.setCodigo(rs.getInt("codigo_pj"));
				PessoaJuridicaDAO pessoaJuridicaDAO = new PessoaJuridicaDAO();
				pessoaJuridicaDAO.salvar(pj);
			}
		} finally {
			//conexao.close();
		}
	}

	private void inserir(Organizacao organizacao) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			PessoaJuridicaDAO pessoaJuridicaDAO = new PessoaJuridicaDAO();
			pessoaJuridicaDAO.salvar(organizacao);
			
			String sql = "INSERT INTO organizacao(codigo_pj) VALUES(?)";
			ps = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, organizacao.getCodigo());

			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				organizacao.setCodigo(rs.getInt("codigo"));
			ps.close();
		} finally {
			//conexao.close();
		}
	}
	
	public void getOrganizacao() throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT organizacao.codigo AS codigo_org, PJ.razaosocial, " + "PJ.cnpj, " + "PE.codigo AS codigo_pessoa, "
					+ "PE.nome AS nome_pessoa, " + "PE.datanascimento, " + "PE.logradouro, " + "PE.complemento, "
					+ "PE.numero, " + "PE.bairro, " + "PE.cep, " + "PE.status AS status_pessoa, "
					+ "CID.codigo AS codigo_cidade, " + "CID.nome AS nome_cidade, " + "CID.status AS status_cidade, "
					+ "ES.uf, " + "ES.nome AS nome_estado " + "FROM  organizacao "
					+ "LEFT JOIN pessoajuridica AS PJ ON organizacao.codigo_pj = PJ.codigo "
					+ "LEFT JOIN pessoa AS PE ON PJ.codigo_pessoa = PE.codigo "
					+ "LEFT JOIN cidade AS CID ON PE.codigo_cidade = CID.codigo "
					+ "LEFT JOIN estado AS ES ON CID.codigo_estado = ES.codigo  ";
			ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				SistemaCtrl.getInstance().getOrganizacao().setCodigo(rs.getInt("codigo_org"));
				SistemaCtrl.getInstance().getOrganizacao().setRazaoSocial(rs.getString("razaosocial"));
				SistemaCtrl.getInstance().getOrganizacao().setCnpj(rs.getString("cnpj"));
				SistemaCtrl.getInstance().getOrganizacao().setNome(rs.getString("nome_pessoa"));
				if(rs.getDate("datanascimento") != null)
					SistemaCtrl.getInstance().getOrganizacao().setDtNascimento(rs.getDate("datanascimento").toLocalDate());
				SistemaCtrl.getInstance().getOrganizacao().setLogradouro(rs.getString("logradouro"));
				SistemaCtrl.getInstance().getOrganizacao().setComplemento(rs.getString("complemento"));
				SistemaCtrl.getInstance().getOrganizacao().setNumero(rs.getString("numero"));
				SistemaCtrl.getInstance().getOrganizacao().setBairro(rs.getString("bairro"));
				SistemaCtrl.getInstance().getOrganizacao().setCep(rs.getString("cep"));

				Cidade cidade = new Cidade();
				cidade.setCodigo(rs.getInt("codigo_cidade"));
				cidade.setNome(rs.getString("nome_cidade"));
				cidade.setStatus(rs.getBoolean("status_cidade"));

				Estado estado = new Estado();
				estado.setNome(rs.getString("nome_estado"));
				estado.setUF(rs.getString("uf"));

				cidade.setEstado(estado);
				SistemaCtrl.getInstance().getOrganizacao().setCidade(cidade);

				TelefoneDAO telefoneDAO = new TelefoneDAO();
				SistemaCtrl.getInstance().getOrganizacao().setTelefones(telefoneDAO.getTelefones(rs.getInt("codigo_pessoa")));
				
			}
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception

			throw new Exception("falha ao consultar empresa");
		} finally {
			//conexao.close();
		}

	}

}
