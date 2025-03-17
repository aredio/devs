package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.Funcao;
import br.com.rarp.model.Cargo;
import br.com.rarp.model.Cidade;
import br.com.rarp.model.Estado;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.PessoaFisica;

public class FuncionarioDAO {

	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if (!SistemaCtrl.getInstance().tabelaExiste("pessoafisica"))
			throw new Exception("Crie a tabela de pessoa fisica antes de criar a tabela de funcionarios");

		if (!SistemaCtrl.getInstance().tabelaExiste("cargo"))
			throw new Exception("Crie a tabela de cargo antes de criar a tabela de funcionarios");
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "funcionario(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "dataAdmissao TIMESTAMP, ";
		sql += "ctps VARCHAR(20), ";
		sql += "salarioContratual NUMERIC(13, 2), ";
		sql += "codigo_cargo INTEGER NOT NULL REFERENCES cargo(codigo), ";
		sql += "codigo_pf INTEGER REFERENCES pessoaFisica(codigo), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}

	public List<Funcionario> consultar(String campo, String comparacao, String termo) throws Exception {
		List<Funcionario> funcionarios = new ArrayList<>();
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		String sql = "SELECT " 
				+ "FUNC.codigo AS codigo_func, " 
				+ "FUNC.dataAdmissao, "
				+ "FUNC.ctps, "
				+ "FUNC.salarioContratual, "
				+ "FUNC.status AS status_func, "
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
				+ "ES.nome AS nome_estado, "
				+ "CA.codigo AS codigo_cargo, "
				+ "CA.nome AS nome_cargo, "
				+ "CA.funcao, "
				+ "CA.nivel, "
				+ "CA.requisitos, "
				+ "CA.status AS status_cargo "
				+ "FROM funcionario AS FUNC "
				+ "LEFT JOIN cargo AS CA ON FUNC.codigo_cargo = CA.codigo "
				+ "LEFT JOIN pessoafisica AS PF ON FUNC.codigo_pf = PF.codigo "
				+ "LEFT JOIN pessoa AS PE ON PF.codigo_pessoa = PE.codigo "
				+ "LEFT JOIN cidade AS CID ON PE.codigo_cidade = CID.codigo "
				+ "LEFT JOIN estado AS ES ON CID.codigo_estado = ES.codigo " + "WHERE " + campo + comparacao + termo;
		ps = conexao.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Funcionario funcionario = new Funcionario();
			funcionarios.add(funcionario);
			
			funcionario.setCodigo(rs.getInt("codigo_func"));
			if(rs.getDate("dataAdmissao") != null)
				funcionario.setDtAdmissao(rs.getDate("dataAdmissao").toLocalDate());
			funcionario.setCTPS(rs.getString("ctps"));
			funcionario.setSalarioContratual(rs.getDouble("salarioContratual"));
			funcionario.setStatus(rs.getBoolean("status_func"));
			funcionario.setCpf(rs.getString("cpf"));
			funcionario.setRg(rs.getString("rg"));
			funcionario.setSexo(rs.getString("sexo"));
			funcionario.setPossuiNecessidades(rs.getBoolean("possuinecessidades"));
			funcionario.setCertidaoNascimento(rs.getString("certidaonascimento"));
			funcionario.setNome(rs.getString("nome_pessoa"));
			funcionario.setLogradouro(rs.getString("logradouro"));
			if(rs.getDate("datanascimento") != null)
				funcionario.setDtNascimento(rs.getDate("datanascimento").toLocalDate());
			funcionario.setComplemento(rs.getString("complemento"));
			funcionario.setNumero(rs.getString("numero"));
			funcionario.setBairro(rs.getString("bairro"));
			funcionario.setCep(rs.getString("cep"));
			
			Cidade cidade = new Cidade();
			cidade.setCodigo(rs.getInt("codigo_cidade"));
			cidade.setNome(rs.getString("nome_cidade"));
			cidade.setStatus(rs.getBoolean("status_cidade"));

			Estado estado = new Estado();
			estado.setCodigo(rs.getInt("codigo_estado"));
			estado.setNome(rs.getString("nome_estado"));
			estado.setUF(rs.getString("uf"));

			cidade.setEstado(estado);
			funcionario.setCidade(cidade);
			
			Cargo cargo = new Cargo();
			cargo.setCodigo(rs.getInt("codigo_cargo"));
			cargo.setNome(rs.getString("nome_cargo"));
			switch (rs.getString("funcao")) {
			case "Limpeza":
				cargo.setFuncao(Funcao.limpeza);
				break;
				
			case "Atendente":
				cargo.setFuncao(Funcao.atendente);
				break;
				
			case "Enfermeira":
				cargo.setFuncao(Funcao.enfermeira);
				break;
			
			case "Medico":
				cargo.setFuncao(Funcao.medico);
				break;
				
			default:
				cargo.setFuncao(Funcao.outros);
				break;
			}
			cargo.setNivel(rs.getString("nivel"));
			cargo.setRequisitos(rs.getString("requisitos"));
			cargo.setStatus(rs.getBoolean("status_cargo"));
			funcionario.setCargo(cargo);
			
			TelefoneDAO telefoneDAO = new TelefoneDAO();
			funcionario.setTelefones(telefoneDAO.getTelefones(rs.getInt("codigo_pessoa")));
		}
		ps.close();
		return funcionarios;
	}

	public Funcionario getFuncionario(int codigo) throws Exception {
		if (codigo > 0) {
			List<Funcionario> funcionarios = consultar("func.codigo", " = ", codigo + "");
			if (funcionarios.size() > 0)
				return funcionarios.get(0);
		}
		return null;
	}

	public void salvar(Funcionario funcionario) throws Exception {
		if (funcionario.getCodigo() == 0)
			inserir(funcionario);
		else
			alterar(funcionario);
	}

	private void inserir(Funcionario funcionario) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {
			PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO();
			pessoaFisicaDAO.salvar(funcionario, conexao);
			
			String sql = "INSERT INTO funcionario(dataAdmissao, salarioContratual, ctps, codigo_cargo, codigo_pf, status) VALUES(?,?,?,?,?,?)";
			ps = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setDate(1, Date.valueOf(funcionario.getDtAdmissao()));
			ps.setDouble(2, funcionario.getSalarioContratual());
			ps.setString(3, funcionario.getCTPS());
			if(funcionario.getCargo() != null)
				ps.setInt(4, funcionario.getCargo().getCodigo());
			else
				ps.setNull(4, Types.INTEGER);
			ps.setInt(5, funcionario.getCodigo());
			ps.setBoolean(6, funcionario.isStatus());

			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				funcionario.setCodigo(rs.getInt("codigo"));
			ps.close();
			conexao.commit();
		} catch (Exception e) {
			conexao.rollback();
		} finally {
			//conexao.close();
		}
	}

	private void alterar(Funcionario funcionario) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {
			String sql = "UPDATE funcionario SET dataAdmissao = ?, salarioContratual = ?, ctps = ?, codigo_cargo = ?, status = ? WHERE codigo = ?";
			ps = conexao.prepareStatement(sql);
			ps.setDate(1, Date.valueOf(funcionario.getDtAdmissao()));
			ps.setDouble(2, funcionario.getSalarioContratual());
			ps.setString(3, funcionario.getCTPS());
			if(funcionario.getCargo() != null)
				ps.setInt(4, funcionario.getCargo().getCodigo());
			else
				ps.setNull(4, Types.INTEGER);
			ps.setBoolean(5, funcionario.isStatus());
			ps.setInt(6, funcionario.getCodigo());
			ps.executeUpdate();
			ps.close();
			
			ResultSet rs = conexao.createStatement().executeQuery("SELECT codigo_pf FROM funcionario WHERE codigo = " + funcionario.getCodigo());
			if(rs.next()) {
				PessoaFisica pf = funcionario.clone();
				pf.setCodigo(rs.getInt("codigo_pf"));
				PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO();
				pessoaFisicaDAO.salvar(pf, conexao);
			}
			conexao.commit();
		}catch (Exception e) {
			conexao.rollback();
		} finally {
			//conexao.close();
		}
	}

}
