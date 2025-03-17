package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Atendimento;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Medico;
import br.com.rarp.model.Paciente;
import br.com.rarp.model.SaidaPaciente;
import br.com.rarp.model.Usuario;

public class EntradaPacienteDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if (!SistemaCtrl.getInstance().tabelaExiste("paciente"))
			throw new Exception("Crie a tabela de paciente antes de criar a tabela de entrada de pacientes");
		
		if (!SistemaCtrl.getInstance().tabelaExiste("funcionario"))
			throw new Exception("Crie a tabela de funcionario antes de criar a tabela de entrada de pacientes");
		
		if (!SistemaCtrl.getInstance().tabelaExiste("convenio"))
			throw new Exception("Crie a tabela de convênio antes de criar a tabela de entrada de pacientes");
		
		if (!SistemaCtrl.getInstance().tabelaExiste("medico"))
			throw new Exception("Crie a tabela de medicos antes de criar a tabela de entrada de pacientes");
		
		if (!SistemaCtrl.getInstance().tabelaExiste("movimentacao"))
			throw new Exception("Crie a tabela de movimentacao antes de criar a tabela de entrada de pacientes");
		
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "entradapaciente(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "codigo_paciente INTEGER REFERENCES paciente(codigo), ";
		sql += "atendente_funcionario INTEGER REFERENCES funcionario(codigo), ";
		sql += "enfermeira_funcionario INTEGER REFERENCES funcionario(codigo), ";
		sql += "codigo_medico INTEGER REFERENCES medico(codigo), ";
		//sql += "codigo_conv INTEGER REFERENCES convenio(codigo), ";
		sql += "codigo_mov INTEGER REFERENCES movimentacao(codigo), ";
		sql += "pretriagem VARCHAR, ";
		sql += "alta BOOLEAN, ";
		sql += "emergencia BOOLEAN, ";
		sql += "status boolean)";
		st.executeUpdate(sql);
		
		sql = "ALTER TABLE entradapaciente ADD IF NOT EXISTS codigo_conv INTEGER REFERENCES convenio(codigo)";
		st.executeUpdate(sql);
		
		st.close();
	}

	public void salvar(EntradaPaciente entradaPaciente) throws Exception {
		if(entradaPaciente != null)
			if(entradaPaciente.getCodigo() == 0)
				inserir(entradaPaciente);
			else
				alterar(entradaPaciente);
				
	}

	private void alterar(EntradaPaciente entradaPaciente) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {
			MovimentacaoDAO movimentacaoDAO =  new MovimentacaoDAO();
    		movimentacaoDAO.salvar(conexao, entradaPaciente);
    		
    		AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
			atendimentoDAO.salvar(conexao, entradaPaciente);
			
			EncaminhamentoDAO encaminhamentoDAO = new EncaminhamentoDAO();
			encaminhamentoDAO.salvar(conexao, entradaPaciente);
    		
			String sql = "UPDATE "
					+ "entradapaciente "
					+ "SET "
					+ "codigo_paciente = ?, "
					+ "atendente_funcionario = ?, "
					+ "enfermeira_funcionario = ?, "
					+ "codigo_medico = ?, "
					+ "pretriagem = ?, "
					+ "alta = ?, "
					+ "emergencia = ?, "
					+ "status = ?, "
					+ "codigo_conv = ? "
					+ "WHERE "
					+ "codigo = ?";
			ps = conexao.prepareStatement(sql);
    		
			if(entradaPaciente.getPaciente() != null)
				ps.setInt(1, entradaPaciente.getPaciente().getCodigo());
			else
				ps.setNull(1, Types.INTEGER);
			
			if(entradaPaciente.getAtendente() != null)
				ps.setInt(2, entradaPaciente.getAtendente() .getCodigo());
			else
				ps.setNull(2, Types.INTEGER);
			
			if(entradaPaciente.getEnfermeira() != null)
				ps.setInt(3, entradaPaciente.getEnfermeira().getCodigo());
			else
				ps.setNull(3, Types.INTEGER);
			
			if(entradaPaciente.getMedico() != null)
				ps.setInt(4, entradaPaciente.getMedico().getCodigoMedico());
			else
				ps.setNull(4, Types.INTEGER);
			
			if(entradaPaciente.getPreTriagem() != null)
				ps.setString(5, entradaPaciente.getPreTriagem());
			else
				ps.setNull(5, Types.VARCHAR);
			
			ps.setBoolean(6, entradaPaciente.isAlta());
			ps.setBoolean(7, entradaPaciente.isEmergencia());
			ps.setBoolean(8, entradaPaciente.isStatus());	
			if(entradaPaciente.getConvenio() != null)
				ps.setInt(9, entradaPaciente.getConvenio().getCodigo());
			else
				ps.setNull(9, Types.INTEGER);
			ps.setInt(10, entradaPaciente.getCodigo());
			
			ps.executeUpdate();
			entradaPaciente.setCodigo(SQLDAO.getCodigoMovimentacao(conexao, "entradapaciente", entradaPaciente.getCodigo()));
			if(entradaPaciente.getCodigo() > 0)
				new MovimentacaoDAO().salvar(conexao, entradaPaciente);
			ps.close();
			conexao.commit();
		} catch (Exception e) {
			conexao.rollback();
			throw e;
		} finally {
			//conexao.close();
		} 
	}

	private void inserir(EntradaPaciente entradaPaciente) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {
			String sql = "INSERT INTO entradapaciente(codigo_paciente, atendente_funcionario, enfermeira_funcionario, codigo_medico, pretriagem, alta, emergencia, codigo_mov, status, codigo_conv) VALUES(?,?,?,?,?,?,?,?,?,?)";
			ps = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			
			MovimentacaoDAO movimentacaoDAO =  new MovimentacaoDAO();
    		movimentacaoDAO.salvar(conexao, entradaPaciente);
			
			if(entradaPaciente.getPaciente() != null)
				ps.setInt(1, entradaPaciente.getPaciente().getCodigo());
			else
				ps.setNull(1, Types.INTEGER);
			
			if(entradaPaciente.getAtendente() != null)
				ps.setInt(2, entradaPaciente.getAtendente() .getCodigo());
			else
				ps.setNull(2, Types.INTEGER);
			
			if(entradaPaciente.getEnfermeira() != null)
				ps.setInt(3, entradaPaciente.getEnfermeira().getCodigo());
			else
				ps.setNull(3, Types.INTEGER);
			
			if(entradaPaciente.getMedico() != null)
				ps.setInt(4, entradaPaciente.getMedico().getCodigoMedico());
			else
				ps.setNull(4, Types.INTEGER);
			
			if(entradaPaciente.getPreTriagem() != null)
				ps.setString(5, entradaPaciente.getPreTriagem());
			else
				ps.setNull(5, Types.VARCHAR);
			
			ps.setBoolean(6, entradaPaciente.isAlta());
			ps.setBoolean(7, entradaPaciente.isEmergencia());
			ps.setInt(8, entradaPaciente.getCodigo());
			ps.setBoolean(9, entradaPaciente.isStatus());
			if(entradaPaciente.getConvenio() != null)
				ps.setInt(10, entradaPaciente.getConvenio().getCodigo());
			else
				ps.setNull(10, Types.INTEGER);
			
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				entradaPaciente.setCodigo(rs.getInt(1));
			
			for(Atendimento a: entradaPaciente.getAtendimentos())
				a.setEntradaPaciente(entradaPaciente);
			
			AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
			atendimentoDAO.salvar(conexao, entradaPaciente);
			
			EncaminhamentoDAO encaminhamentoDAO = new EncaminhamentoDAO();
			encaminhamentoDAO.salvar(conexao, entradaPaciente);
			
			ps.close();
			conexao.commit();
		} catch (Exception e) {
			conexao.rollback();
			throw e;
		} finally {
			//conexao.close();
		} 
	}

	public List<EntradaPaciente> consultar(String campo, String comparacao, String termo) throws Exception {
		return consultar(campo + comparacao + termo);
	}

	public List<EntradaPaciente> consultar(String consulta) throws Exception {
		List<EntradaPaciente> entradas = new ArrayList<EntradaPaciente>();
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT "
					+ "ENT.codigo AS codigo_entrada, "
					+ "ENT.pretriagem, "
					+ "ENT.emergencia, "
					+ "ENT.alta, "
					+ "ENT.status AS status_entrada, "
					+ "MOV.data, "
					+ "MOV.hora, "
					+ "MOV.codigo AS codigo_mov, "
					+ "USU.codigo AS codigo_usuario, "
					+ "MED.codigo AS codigo_medico, "
					+ "ENF.codigo AS codigo_enfermeira, "
					+ "PAC.codigo AS codigo_paciente, "
					+ "SAI.codigo AS codigo_saida, "
					+ "ATE.codigo AS codigo_atendente,"
					+ "ENT.codigo_conv "
					+ "FROM entradapaciente ENT "
					+ "LEFT JOIN movimentacao MOV ON ENT.codigo_mov = MOV.codigo "
					+ "LEFT JOIN medico MED ON ENT.codigo_medico = MED.codigo "
					+ "LEFT JOIN funcionario ENF ON ENT.enfermeira_funcionario = ENF.codigo "
					+ "LEFT JOIN paciente PAC ON ENT.codigo_paciente = PAC.codigo "
					+ "LEFT JOIN funcionario ATE ON ENT.atendente_funcionario = ATE.codigo "
					+ "LEFT JOIN usuario USU ON MOV.codigo_usuario = USU.codigo "
					+ "LEFT JOIN saidapaciente SAI ON SAI.codigo_entrada = ENT.codigo "
					+ "WHERE "
					+ consulta;
			PreparedStatement ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				EntradaPaciente entradaPaciente = new EntradaPaciente();
				entradaPaciente.setCodigo(rs.getInt("codigo_entrada"));
				entradaPaciente.setPreTriagem(rs.getString("pretriagem"));
				entradaPaciente.setEmergencia(rs.getBoolean("emergencia"));
				entradaPaciente.setAlta(rs.getBoolean("alta"));
				entradaPaciente.setStatus(rs.getBoolean("status_entrada"));
				if(rs.getDate("data") != null)
					entradaPaciente.setDtMovimentacao(rs.getDate("data").toLocalDate());
				if(rs.getDate("hora") != null)
					entradaPaciente.setHrMovimentacao(rs.getTime("hora").toLocalTime());
				entradaPaciente.setUsuario(new UsuarioDAO().getUsuario(conexao, rs.getInt("codigo_usuario")));	
				entradaPaciente.setMedico(new MedicoDAO().getMedico(rs.getInt("codigo_medico")));
				entradaPaciente.setEnfermeira(new FuncionarioDAO().getFuncionario(rs.getInt("codigo_enfermeira")));
				entradaPaciente.setPaciente(new PacienteDAO().getPaciente(rs.getInt("codigo_paciente")));
				entradaPaciente.setAtendente(new FuncionarioDAO().getFuncionario(rs.getInt("codigo_atendente")));
				entradaPaciente.setAtendimentos(new AtendimentoDAO().getAtendimentos(entradaPaciente.getCodigo()));
				entradaPaciente.setConvenio(new ConvenioDAO().get(rs.getInt("codigo_conv")));
				entradaPaciente.setSaidaPaciente(new SaidaPaciente());
				entradaPaciente.getSaidaPaciente().setCodigo(rs.getInt("codigo_saida"));
				entradas.add(entradaPaciente);
			}
			return entradas;	
		} finally {
			//conexao.close();
		}
	}

	public List<EntradaPaciente> getEntradasByPaciente(Paciente paciente) throws Exception {
		if(paciente != null) 
			return consultar("ENT.codigo_paciente = " + paciente.getCodigo() + " AND ENT.status = TRUE");
		return new ArrayList<>();
	}

	public List<EntradaPaciente> getEntradasAbertas() throws Exception {
		return consultar("SAI.codigo IS NULL AND alta");
	}

	public EntradaPaciente getEntrada(int codigo) throws Exception {
		if(codigo > 0) {
			List<EntradaPaciente> entradaPacientes = consultar("ENT.codigo = " + codigo);
			if(entradaPacientes.size() > 0)
				return entradaPacientes.get(0);
		}
		return null;
	}

	public List<EntradaPaciente> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			Funcionario atendente, Funcionario enfermeira, Medico medico, Paciente paciente, Usuario usuario,
			String preTriagem, Boolean status) throws ClassNotFoundException, Exception {
		List<EntradaPaciente> entradas = new ArrayList<EntradaPaciente>();
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			Integer vcount = 0;
			String sql = "SELECT "
					+ "ENT.codigo AS codigo_entrada, "
					+ "ENT.pretriagem, "
					+ "ENT.emergencia, "
					+ "ENT.alta, "
					+ "ENT.status AS status_entrada, "
					+ "MOV.data, "
					+ "MOV.hora, "
					+ "MOV.codigo AS codigo_mov, "
					+ "USU.codigo AS codigo_usuario, "
					+ "MED.codigo AS codigo_medico, "
					+ "ENF.codigo AS codigo_enfermeira, "
					+ "PAC.codigo AS codigo_paciente, "
					+ "SAI.codigo AS codigo_saida, "
					+ "ATE.codigo AS codigo_atendente, "
					+ "ENT.codigo_conv "
					+ "FROM entradapaciente ENT "
					+ "LEFT JOIN movimentacao MOV ON ENT.codigo_mov = MOV.codigo "
					+ "LEFT JOIN medico MED ON ENT.codigo_medico = MED.codigo "
					+ "LEFT JOIN funcionario ENF ON ENT.enfermeira_funcionario = ENF.codigo "
					+ "LEFT JOIN paciente PAC ON ENT.codigo_paciente = PAC.codigo "
					+ "LEFT JOIN funcionario ATE ON ENT.atendente_funcionario = ATE.codigo "
					+ "LEFT JOIN usuario USU ON MOV.codigo_usuario = USU.codigo "
					+ "LEFT JOIN saidapaciente SAI ON SAI.codigo_entrada = ENT.codigo "
					+ "WHERE "
					+ "ENT.codigo > -1 ";
					if(dataIni != null)
						sql += "AND (MOV.data >= ?) ";
					if(dataFin != null) 
						sql += "AND (MOV.data <= ?) ";
					if(horaIni != null) 
						sql += "AND (MOV.hora >= ?) ";
					if(horaFin != null) 
						sql += "AND (MOV.hora <= ?) ";
					if(atendente != null)
						sql += "AND (ATE.codigo = ?) ";
					if(enfermeira != null)
						sql += "AND (ENF.codigo = ?) ";
					if(medico != null) 
						sql += "AND (MED.codigo = ?) ";
					if(paciente != null) 
						sql += "AND (PAC.codigo = ?) ";
					if(usuario != null) 
						sql += "AND (USU.codigo = ?) ";
					if(preTriagem != null && !preTriagem.isEmpty())
						sql += "AND (ENT.pretriagem LIKE ?)";
					if(status != null)
						sql += "AND (ENT.status = ?)";
			PreparedStatement ps = conexao.prepareStatement(sql);
			if(dataIni != null)
				ps.setDate(++vcount, Date.valueOf(dataIni));
			if(dataFin != null)
				ps.setDate(++vcount, Date.valueOf(dataFin));
			if(horaIni != null)
				ps.setTime(++vcount, Time.valueOf(horaIni));
			if(horaFin != null)
				ps.setTime(++vcount, Time.valueOf(horaFin));
			if(atendente != null)
				ps.setInt(++vcount, atendente.getCodigo());
			if(enfermeira != null)
				ps.setInt(++vcount, enfermeira.getCodigo());
			if(medico != null)
				ps.setInt(++vcount, medico.getCodigoMedico());
			if(paciente != null)
				ps.setInt(++vcount, paciente.getCodigo());
			if(usuario != null)
				ps.setInt(++vcount, usuario.getCodigo());
			if(preTriagem != null && !preTriagem.isEmpty())
				ps.setString(++vcount, "%" + preTriagem + "%");
			if(status != null)
				ps.setBoolean(++vcount, status);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				EntradaPaciente entradaPaciente = new EntradaPaciente();
				entradaPaciente.setCodigo(rs.getInt("codigo_entrada"));
				entradaPaciente.setPreTriagem(rs.getString("pretriagem"));
				entradaPaciente.setEmergencia(rs.getBoolean("emergencia"));
				entradaPaciente.setAlta(rs.getBoolean("alta"));
				entradaPaciente.setStatus(rs.getBoolean("status_entrada"));
				if(rs.getDate("data") != null)
					entradaPaciente.setDtMovimentacao(rs.getDate("data").toLocalDate());
				if(rs.getDate("hora") != null)
					entradaPaciente.setHrMovimentacao(rs.getTime("hora").toLocalTime());
				entradaPaciente.setUsuario(new UsuarioDAO().getUsuario(conexao, rs.getInt("codigo_usuario")));	
				entradaPaciente.setMedico(new MedicoDAO().getMedico(rs.getInt("codigo_medico")));
				entradaPaciente.setEnfermeira(new FuncionarioDAO().getFuncionario(rs.getInt("codigo_enfermeira")));
				entradaPaciente.setPaciente(new PacienteDAO().getPaciente(rs.getInt("codigo_paciente")));
				entradaPaciente.setAtendente(new FuncionarioDAO().getFuncionario(rs.getInt("codigo_atendente")));
				entradaPaciente.setAtendimentos(new AtendimentoDAO().getAtendimentos(entradaPaciente.getCodigo()));
				entradaPaciente.setConvenio(new ConvenioDAO().get(rs.getInt("codigo_conv")));
				entradaPaciente.setSaidaPaciente(new SaidaPaciente());
				entradaPaciente.getSaidaPaciente().setCodigo(rs.getInt("codigo_saida"));
				entradas.add(entradaPaciente);
			}
			return entradas;	
		} finally {
			//conexao.close();
		}
	}

	public EntradaPaciente getEntradaSemListas(int codigo) throws Exception {
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT "
					+ "ENT.codigo AS codigo_entrada, "
					+ "ENT.pretriagem, "
					+ "ENT.emergencia, "
					+ "ENT.alta, "
					+ "ENT.status AS status_entrada, "
					+ "MOV.data, "
					+ "MOV.hora, "
					+ "MOV.codigo AS codigo_mov, "
					+ "USU.codigo AS codigo_usuario, "
					+ "MED.codigo AS codigo_medico, "
					+ "ENF.codigo AS codigo_enfermeira, "
					+ "PAC.codigo AS codigo_paciente, "
					+ "SAI.codigo AS codigo_saida, "
					+ "ATE.codigo AS codigo_atendente "
					+ "FROM entradapaciente ENT "
					+ "LEFT JOIN movimentacao MOV ON ENT.codigo_mov = MOV.codigo "
					+ "LEFT JOIN medico MED ON ENT.codigo_medico = MED.codigo "
					+ "LEFT JOIN funcionario ENF ON ENT.enfermeira_funcionario = ENF.codigo "
					+ "LEFT JOIN paciente PAC ON ENT.codigo_paciente = PAC.codigo "
					+ "LEFT JOIN funcionario ATE ON ENT.atendente_funcionario = ATE.codigo "
					+ "LEFT JOIN usuario USU ON MOV.codigo_usuario = USU.codigo "
					+ "LEFT JOIN saidapaciente SAI ON SAI.codigo_entrada = ENT.codigo "
					+ "WHERE ENT.codigo = " + codigo;
			PreparedStatement ps = conexao.prepareStatement(sql);
			EntradaPaciente entradaPaciente = new EntradaPaciente();
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				entradaPaciente.setCodigo(rs.getInt("codigo_entrada"));
				entradaPaciente.setPreTriagem(rs.getString("pretriagem"));
				entradaPaciente.setEmergencia(rs.getBoolean("emergencia"));
				entradaPaciente.setAlta(rs.getBoolean("alta"));
				entradaPaciente.setStatus(rs.getBoolean("status_entrada"));
				if(rs.getDate("data") != null)
					entradaPaciente.setDtMovimentacao(rs.getDate("data").toLocalDate());
				if(rs.getDate("hora") != null)
					entradaPaciente.setHrMovimentacao(rs.getTime("hora").toLocalTime());
				entradaPaciente.setUsuario(new UsuarioDAO().getUsuario(conexao, rs.getInt("codigo_usuario")));	
				entradaPaciente.setMedico(new MedicoDAO().getMedico(rs.getInt("codigo_medico")));
				entradaPaciente.setEnfermeira(new FuncionarioDAO().getFuncionario(rs.getInt("codigo_enfermeira")));
				entradaPaciente.setPaciente(new PacienteDAO().getPaciente(rs.getInt("codigo_paciente")));
				entradaPaciente.setAtendente(new FuncionarioDAO().getFuncionario(rs.getInt("codigo_atendente")));
				entradaPaciente.setSaidaPaciente(new SaidaPaciente());
				entradaPaciente.getSaidaPaciente().setCodigo(rs.getInt("codigo_saida"));
			}
			return entradaPaciente;	
		} finally {
			//conexao.close();
		}
	}

}
