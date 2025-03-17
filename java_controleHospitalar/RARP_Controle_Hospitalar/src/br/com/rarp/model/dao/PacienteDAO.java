package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Cidade;
import br.com.rarp.model.Estado;
import br.com.rarp.model.Paciente;
import br.com.rarp.model.PessoaFisica;

public class PacienteDAO {
	
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if (!SistemaCtrl.getInstance().tabelaExiste("pessoafisica"))
			throw new Exception("Crie a tabela de pessoa fisica antes de criar a tabela de pacientes");
		
		if (!SistemaCtrl.getInstance().tabelaExiste("convenio"))
			throw new Exception("Crie a tabela de pessoa convênio antes de criar a tabela de pacientes");

		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "paciente(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "codigo_convenio INTEGER REFERENCES convenio(codigo), ";
		sql += "codigo_pf INTEGER REFERENCES pessoaFisica(codigo), ";
		sql += "codigo_resp INTEGER REFERENCES paciente(codigo), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}

	public List<Paciente> consultar(String campo, String comparacao, String termo) throws Exception {
		return consultar(campo + comparacao + termo);
	}
	
	public Paciente getPaciente(int codigo) throws Exception {
		if(codigo > 0) {
			List<Paciente> pacientes = consultar("pac.codigo", " = ", codigo + "");
			if(pacientes.size() > 0)
				return pacientes.get(0);
		}
		return null;
	}

	public void salvar(Paciente paciente) throws Exception {
		if(paciente != null)
			if(paciente.getCodigo() == 0)
				inserir(paciente);
			else
				alterar(paciente);
	}

	private void alterar(Paciente paciente) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {
			String sql = "UPDATE paciente SET codigo_convenio = ?, codigo_resp = ?, status = ? WHERE codigo = ?";
			ps = conexao.prepareStatement(sql);
			if(paciente.getConvenio() != null)
				ps.setInt(1, paciente.getConvenio().getCodigo());
			else
				ps.setNull(1, Types.INTEGER);
			
			if(paciente.getResponsavel() != null)
				ps.setInt(2, paciente.getResponsavel().getCodigo());
			else
				ps.setNull(2, Types.INTEGER);
			ps.setBoolean(3, paciente.isStatus());
			ps.setInt(4, paciente.getCodigo());

			ps.executeUpdate();
			ps.close();
			
			ResultSet rs = conexao.createStatement().executeQuery("SELECT codigo_pf FROM paciente WHERE codigo = " + paciente.getCodigo());
			if(rs.next()) {
				PessoaFisica pf = paciente.clone();
				pf.setCodigo(rs.getInt("codigo_pf"));
				PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO();
				pessoaFisicaDAO.salvar(pf, conexao);
			}
			conexao.commit();
		} catch (Exception e) {
			conexao.rollback();
		} finally {
			//conexao.close();
		}
	}

	private void inserir(Paciente paciente) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {
			PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO();
			pessoaFisicaDAO.salvar(paciente, conexao);
			
			String sql = "INSERT INTO paciente(codigo_convenio, codigo_pf, codigo_resp, status) VALUES(?,?,?,?)";
			ps = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			if(paciente.getConvenio() != null)
				ps.setInt(1, paciente.getConvenio().getCodigo());
			else
				ps.setNull(1, Types.INTEGER);
			
			ps.setInt(2, paciente.getCodigo());
			if(paciente.getResponsavel() != null)
				ps.setInt(3, paciente.getResponsavel().getCodigo());
			else
				ps.setNull(3, Types.INTEGER);
			ps.setBoolean(4, paciente.isStatus());

			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				paciente.setCodigo(rs.getInt("codigo"));
			ps.close();
			conexao.commit();
		} catch (Exception e) {
			conexao.rollback();
		} finally {
			//conexao.close();
		}
	}

	public List<Paciente> getPacientesSemResponsavel() throws Exception {
		return consultar("PAC.codigo_resp IS NULL AND PAC.status = TRUE");
	}

	private List<Paciente> consultar(String comparacao) throws Exception {
		List<Paciente> pacientes = new ArrayList<>();
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT " 
					+ "PAC.codigo AS codigo_pac, " 
					+ "PAC.codigo_convenio, "
					+ "PAC.codigo_resp, "
					+ "PAC.status AS status_pac, "
					+ "PF.cpf, " 
					+ "PF.rg, "
					+ "PF.sexo, "
					+ "PF.possuinecessidades, " 
					+ "PF.certidaonascimento, " 
					+ "PF.status AS status_pf, "
					+ "PE.codigo AS codigo_pessoa, " 
					+ "PE.nome AS nome_pessoa, "
					+ "PE.logradouro, "
					+ "PE.datanascimento, "
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
					+ "ES.nome AS nome_estado "
					+ "FROM paciente AS PAC "
					+ "LEFT JOIN pessoafisica AS PF ON PAC.codigo_pf = PF.codigo "
					+ "LEFT JOIN pessoa AS PE ON PF.codigo_pessoa = PE.codigo "
					+ "LEFT JOIN cidade AS CID ON PE.codigo_cidade = CID.codigo "
					+ "LEFT JOIN estado AS ES ON CID.codigo_estado = ES.codigo " + "WHERE " + comparacao;
			ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Paciente paciente = new Paciente();
				pacientes.add(paciente);
				
				paciente.setCodigo(rs.getInt("codigo_pac"));
				ConvenioDAO convenioDAO = new ConvenioDAO();
				paciente.setConvenio(convenioDAO.get(rs.getInt("codigo_convenio")));
				paciente.setResponsavel(getPaciente(rs.getInt("codigo_resp")));
				paciente.setStatus(rs.getBoolean("status_pac"));
				paciente.setCpf(rs.getString("cpf"));
				paciente.setRg(rs.getString("rg"));
				paciente.setSexo(rs.getString("sexo"));
				paciente.setPossuiNecessidades(rs.getBoolean("possuinecessidades"));
				paciente.setCertidaoNascimento(rs.getString("certidaonascimento"));
				paciente.setNome(rs.getString("nome_pessoa"));
				paciente.setLogradouro(rs.getString("logradouro"));
				if(rs.getDate("datanascimento") != null)
					paciente.setDtNascimento(rs.getDate("datanascimento").toLocalDate());
				paciente.setComplemento(rs.getString("complemento"));
				paciente.setNumero(rs.getString("numero"));
				paciente.setBairro(rs.getString("bairro"));
				paciente.setCep(rs.getString("cep"));
				
				Cidade cidade = new Cidade();
				cidade.setCodigo(rs.getInt("codigo_cidade"));
				cidade.setNome(rs.getString("nome_cidade"));
				cidade.setStatus(rs.getBoolean("status_cidade"));

				Estado estado = new Estado();
				estado.setCodigo(rs.getInt("codigo_estado"));
				estado.setNome(rs.getString("nome_estado"));
				estado.setUF(rs.getString("uf"));

				cidade.setEstado(estado);
				paciente.setCidade(cidade);
				
				TelefoneDAO telefoneDAO = new TelefoneDAO();
				paciente.setTelefones(telefoneDAO.getTelefones(rs.getInt("codigo_pessoa")));
			}
			ps.close();
		} finally {
			//conexao.close();
		}
		return pacientes;
	}

}
