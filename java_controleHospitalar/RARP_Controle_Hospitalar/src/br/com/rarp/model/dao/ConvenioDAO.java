package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Cidade;
import br.com.rarp.model.Convenio;
import br.com.rarp.model.Estado;
import br.com.rarp.model.PessoaJuridica;

public class ConvenioDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if(!SistemaCtrl.getInstance().tabelaExiste("pessoajuridica"))
			throw new Exception("Crie a tabela de pessoa juridica antes de criar a tabela de convênio");
		
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "convenio(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "ans VARCHAR(50), ";
		sql += "tipo INTEGER, ";
		sql += "codigo_pj INTEGER REFERENCES pessoaJuridica(codigo), ";
		sql += "status boolean)";	
		st.executeUpdate(sql);
		
		sql = "ALTER TABLE convenio ADD IF NOT EXISTS autorizado BOOLEAN";
		st.executeUpdate(sql);
	}

	public List<Convenio> consultar(String campo, String comparacao, String termo) throws Exception {
		List<Convenio> convenios = new ArrayList<Convenio>();
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT " 
					+ "CONV.codigo AS codigo_conv, " 
					+ "CONV.ans,"
					+ "CONV.tipo,"
					+ "CONV.status AS status_conv,"
					+ "PJ.razaosocial, "
					+ "PJ.cnpj, "
					+ "PE.codigo AS codigo_pessoa, "
					+ "PE.nome AS nome_pessoa, "
					+ "PE.datanascimento, "
					+ "PE.logradouro, "
					+ "PE.complemento, " 
					+ "PE.numero, " 
					+ "PE.bairro, " 
					+ "PE.cep, "
					+ "PE.status AS status_pessoa, "
					+ "CID.codigo AS codigo_cidade, " 
					+ "CID.nome AS nome_cidade, "
					+ "CID.status AS status_cidade, "
					+ "ES.codigo AS codigo_estado, "
					+ "ES.uf, "
					+ "ES.nome AS nome_estado,"
					+ "CONV.autorizado "
					+ "FROM convenio AS CONV "
					+ "LEFT JOIN pessoajuridica AS PJ ON CONV.codigo_pj = PJ.codigo "
					+ "LEFT JOIN pessoa AS PE ON PJ.codigo_pessoa = PE.codigo "
					+ "LEFT JOIN cidade AS CID ON PE.codigo_cidade = CID.codigo "
					+ "LEFT JOIN estado AS ES ON CID.codigo_estado = ES.codigo " 
					+ "WHERE " + campo + comparacao + termo;
			ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Convenio convenio = new Convenio();
				convenios.add(convenio);
				
				convenio.setCodigo(rs.getInt("codigo_conv"));
				convenio.setANS(rs.getString("ans"));
				convenio.setTipo(rs.getInt("tipo"));
				convenio.setStatus(rs.getBoolean("status_conv"));
				convenio.setRazaoSocial(rs.getString("razaosocial"));
				convenio.setCnpj(rs.getString("cnpj"));
				convenio.setNome(rs.getString("nome_pessoa"));
				convenio.setDtNascimento(rs.getDate("datanascimento").toLocalDate());
				convenio.setLogradouro(rs.getString("logradouro"));
				convenio.setComplemento(rs.getString("complemento"));
				convenio.setNumero(rs.getString("numero"));
				convenio.setBairro(rs.getString("bairro"));
				convenio.setCep(rs.getString("cep"));
				convenio.setAutorizado(rs.getBoolean("autorizado"));
				
				Cidade cidade = new Cidade();
				cidade.setCodigo(rs.getInt("codigo_cidade"));
				cidade.setNome(rs.getString("nome_cidade"));
				cidade.setStatus(rs.getBoolean("status_cidade"));

				Estado estado = new Estado();
				estado.setCodigo(rs.getInt("codigo_estado"));
				estado.setNome(rs.getString("nome_estado"));
				estado.setUF(rs.getString("uf"));

				cidade.setEstado(estado);
				convenio.setCidade(cidade);
				
				TelefoneDAO telefoneDAO = new TelefoneDAO();
				convenio.setTelefones(telefoneDAO.getTelefones(rs.getInt("codigo_pessoa")));
			}
			ps.close();
		} finally {
			//conexao.close();
		}
		return convenios;
	}
	
	public Convenio get(int codigo) throws Exception {
		if(codigo > 0) {
			List<Convenio> convenios = consultar("conv.codigo", " = ", codigo+"");
			if(convenios.size() > 0)
				return convenios.get(0);
		}
		return null;
	}
	
	public void salvar(Convenio convenio) throws Exception {
		if(convenio != null)
			if(convenio.getCodigo() == 0)
				inserir(convenio);
			else
				alterar(convenio);
	}

	private void alterar(Convenio convenio) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "UPDATE convenio SET ans = ?, tipo = ?, status = ?, autorizado = ? WHERE codigo = ?";
			ps = conexao.prepareStatement(sql);
			ps.setString(1, convenio.getANS());
			ps.setInt(2, convenio.getTipo());
			ps.setBoolean(3, convenio.isStatus());
			ps.setBoolean(4, convenio.isAutorizado());
			ps.setInt(5, convenio.getCodigo());
			ps.executeUpdate();
			ps.close();
			
			ResultSet rs = conexao.createStatement().executeQuery("SELECT codigo_pj FROM convenio WHERE codigo = " + convenio.getCodigo());
			if(rs.next()) {
				PessoaJuridica pj = convenio.clone();
				pj.setCodigo(rs.getInt("codigo_pj"));
				PessoaJuridicaDAO pessoaJuridicaDAO = new PessoaJuridicaDAO();
				pessoaJuridicaDAO.salvar(pj);
			}
		} finally {
			//conexao.close();
		}
	}

	private void inserir(Convenio convenio) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			PessoaJuridicaDAO pessoaJuridicaDAO = new PessoaJuridicaDAO();
			pessoaJuridicaDAO.salvar(convenio);
			
			String sql = "INSERT INTO convenio(ans, tipo, codigo_pj, status, autorizado) VALUES(?,?,?,?,?)";
			ps = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, convenio.getANS());
			ps.setInt(2, convenio.getTipo());
			ps.setInt(3, convenio.getCodigo());
			ps.setBoolean(4, convenio.isStatus());
			ps.setBoolean(5, convenio.isAutorizado());

			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				convenio.setCodigo(rs.getInt("codigo"));
			ps.close();
		} finally {
			//conexao.close();
		}
	}
}
