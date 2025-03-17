package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.Funcao;
import br.com.rarp.model.Cargo;
import br.com.rarp.model.Cidade;
import br.com.rarp.model.Especialidade;
import br.com.rarp.model.Estado;
import br.com.rarp.model.Medico;

public class MedicoDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if (!SistemaCtrl.getInstance().tabelaExiste("funcionario"))
			throw new Exception("Crie a tabela de funcionario  antes de criar a tabela de medicos");
		
		
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "medico(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "codigo_funcionario integer NOT NULL REFERENCES funcionario(codigo), ";
		sql += "CRM VARCHAR(15), ";
		sql += "status boolean,  ";
		sql += "CONSTRAINT medico_funcionario UNIQUE (codigo_funcionario)";
		sql += ")";
		st.executeUpdate(sql);
		st.close();

		st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "medico_especialidade(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "codigo_medico integer NOT NULL REFERENCES medico(codigo), ";
		sql += "codigo_especialidade integer NOT NULL REFERENCES especialidade(codigo), ";
		sql += "CONSTRAINT medico_especialidades UNIQUE (codigo_especialidade, codigo_medico)";
		sql += ")";

		st.executeUpdate(sql);
	}

	public void salvar(Medico medico) throws Exception {

		if (medico != null) {
			if (medico.getCodigoMedico() > 0) {
				alterar(medico);

			} else {
				inserir(medico);
			}

		}

	}

	public void inserir(Medico medico) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {

			String sql = "INSERT INTO medico(crm,status,codigo_funcionario) VALUES(?,?,?)";
			ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, medico.getCRM());
			ps.setBoolean(2, medico.isStatus());
			ps.setInt(3, medico.getCodigo());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				medico.setCodigoMedico(rs.getInt("codigo"));

			ps.close();
			salvarEspecialidades(medico);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Erro ao salvar Medico");

		} finally {

			//conexao.close();
		}
	}

	private void alterar(Medico medico) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "UPDATE medico SET crm=? , codigo_funcionario=?, status=?  WHERE codigo=?";
			ps = conexao.prepareStatement(sql);
			ps.setString(1, medico.getCRM());
			ps.setInt(2, medico.getCodigo());
			ps.setBoolean(3, medico.isStatus());
			ps.setInt(4, medico.getCodigoMedico());
			ps.executeUpdate();
			ps.close();
			salvarEspecialidades(medico);
		} catch (Exception e) {
			throw new Exception("Erro ao salvar Medico");
		} finally {

			//conexao.close();
		}
	}

	public List<Medico> consultar(String campo, String comparacao, String termo) throws Exception {

		List<Medico> medicos = new ArrayList<>();
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			/**
			 * nome,status,codigo_medico
			 */

			String sql = "SELECT " + "MED.codigo AS codigo_med,"
					+ "MED.codigo_funcionario  ,MED.crm , MED.status AS status_medico," + "FUNC.codigo AS codigo_func, "
					+ "FUNC.dataAdmissao, " + "FUNC.ctps, " + "FUNC.salarioContratual, "
					+ "FUNC.status AS status_func, " + "PF.cpf, " + "PF.rg, " + "PF.sexo, " + "PF.possuinecessidades, "
					+ "PF.certidaonascimento, " + "PF.status AS status_pf, " + "PE.codigo AS codigo_pessoa, "
					+ "PE.nome AS nome_pessoa, " + "PE.logradouro, " + "PE.datanascimento, " + "PE.complemento, "
					+ "PE.numero, " + "PE.bairro, " + "PE.cep, " + "PE.status AS status_pessoa, "
					+ "CID.codigo AS codigo_cidade, " + "CID.nome AS nome_cidade, " + "CID.status AS status_cidade, "
					+ "ES.codigo AS codigo_estado, " + "ES.uf, " + "ES.nome AS nome_estado, "
					+ "CA.codigo AS codigo_cargo, " + "CA.nome AS nome_cargo, " + "CA.funcao, " + "CA.nivel, "
					+ "CA.requisitos, " + "CA.status AS status_cargo " + "FROM " + "medico AS MED "
					+ "LEFT JOIN funcionario AS FUNC ON MED.codigo_funcionario = FUNC.codigo "
					+ "LEFT JOIN cargo AS CA ON FUNC.codigo_cargo = CA.codigo "
					+ "LEFT JOIN pessoafisica AS PF ON FUNC.codigo_pf = PF.codigo "
					+ "LEFT JOIN pessoa AS PE ON PF.codigo_pessoa = PE.codigo "
					+ "LEFT JOIN cidade AS CID ON PE.codigo_cidade = CID.codigo "
					+ "LEFT JOIN estado AS ES ON CID.codigo_estado = ES.codigo " + "WHERE " + campo + comparacao
					+ termo;
			ps = conexao.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Medico medico = new Medico();

				medico.setCodigoMedico(rs.getInt("codigo_med"));
				medico.setCodigo(rs.getInt("codigo_funcionario"));
				if (rs.getDate("dataAdmissao") != null)
					medico.setDtAdmissao(rs.getDate("dataAdmissao").toLocalDate());
				medico.setCTPS(rs.getString("ctps"));
				medico.setSalarioContratual(rs.getDouble("salarioContratual"));
				medico.setStatus(rs.getBoolean("status_medico"));
				medico.setCpf(rs.getString("cpf"));
				medico.setRg(rs.getString("rg"));
				medico.setSexo(rs.getString("sexo"));
				medico.setPossuiNecessidades(rs.getBoolean("possuinecessidades"));
				medico.setCertidaoNascimento(rs.getString("certidaonascimento"));
				medico.setNome(rs.getString("nome_pessoa"));
				medico.setLogradouro(rs.getString("logradouro"));
				if (rs.getDate("datanascimento") != null)
					medico.setDtNascimento(rs.getDate("datanascimento").toLocalDate());
				medico.setComplemento(rs.getString("complemento"));
				medico.setNumero(rs.getString("numero"));
				medico.setBairro(rs.getString("bairro"));
				medico.setCep(rs.getString("cep"));

				Cidade cidade = new Cidade();
				cidade.setCodigo(rs.getInt("codigo_cidade"));
				cidade.setNome(rs.getString("nome_cidade"));
				cidade.setStatus(rs.getBoolean("status_cidade"));

				Estado estado = new Estado();
				estado.setCodigo(rs.getInt("codigo_estado"));
				estado.setNome(rs.getString("nome_estado"));
				estado.setUF(rs.getString("uf"));

				cidade.setEstado(estado);
				medico.setCidade(cidade);

				Cargo cargo = new Cargo();
				cargo.setCodigo(rs.getInt("codigo_cargo"));
				cargo.setNome(rs.getString("nome_cargo"));
				switch (rs.getString("funcao")) {
				case "Atendente":
					cargo.setFuncao(Funcao.atendente);
					break;
					
				case "Enfermeira":
					cargo.setFuncao(Funcao.enfermeira);
					break;
					
				case "Limpeza":
					cargo.setFuncao(Funcao.limpeza);
					break;

				default:
					cargo.setFuncao(Funcao.outros);
					break;
				}
				cargo.setNivel(rs.getString("nivel"));
				cargo.setRequisitos(rs.getString("requisitos"));
				cargo.setStatus(rs.getBoolean("status_cargo"));
				medico.setCargo(cargo);

				TelefoneDAO telefoneDAO = new TelefoneDAO();
				medico.setTelefones(telefoneDAO.getTelefones(rs.getInt("codigo_pessoa")));

				medico.setCRM(rs.getString("crm"));

				medico.setEspecialidades(new EspecialidadeDAO().getEspecialidesByMedico(medico));
				medicos.add(medico);
			}

			ps.close();
		} catch (Exception e) {

			System.out.println(e.getMessage());
			throw new Exception("Erro a consultar medico");
		} finally {
			//conexao.close();
		}
		return medicos;
	}

	private void salvarEspecialidades(Medico medico) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			/**
			 * INSERT INTO public.medico_especialidade( codigo_medico, codigo_especialidade)
			 * select 1,1 where not exists (select codigo_medico, codigo_especialidade from
			 * public.medico_especialidade where codigo_medico = 1 and codigo_especialidade
			 * =1 )
			 */

			String sql;
			sql = "INSERT INTO medico_especialidade(";

			sql += "codigo_medico, codigo_especialidade)";

			for (int i = 0; i < medico.getEspecialidades().size(); i++) {

				if (i != 0)
					sql += " union";
				sql += " select";
				sql += "  ?,?  where not exists (";
				sql += "select codigo_medico, codigo_especialidade from  medico_especialidade";
				sql += " where codigo_medico = ? and codigo_especialidade = ? )";

			}

			ps = conexao.prepareStatement(sql);

			int i = 1;
			for (Especialidade especialidade : medico.getEspecialidades()) {
				ps.setInt(i++, medico.getCodigoMedico());
				ps.setInt(i++, especialidade.getCodigo());
				ps.setInt(i++, medico.getCodigoMedico());
				ps.setInt(i++, especialidade.getCodigo());
			}

			ps.executeUpdate();
			ps.close();

			sql = "delete from medico_especialidade where codigo_medico = ? ";
			sql += " and codigo_especialidade not  in (";

			for (i = 0; i < medico.getEspecialidades().size(); i++) {
				if (i == 0) {
					sql += String.valueOf(medico.getEspecialidades().get(i).getCodigo());
				} else {
					sql += "," + String.valueOf(medico.getEspecialidades().get(i).getCodigo());
				}

			}
			sql += ")";
			ps = conexao.prepareStatement(sql);
			ps.setInt(1, medico.getCodigoMedico());
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getMessage());
			throw new Exception("Erro ao salvar Medico");
		} finally {

			//conexao.close();
		}

	}

	public Medico getMedico(int codigo) throws Exception {
		if (codigo > 0) {
			List<Medico> medicos = consultar("MED.codigo", " = ", codigo + "");
			if (medicos != null && medicos.size() > 0)
				return medicos.get(0);
		}
		return null;
	}

}
