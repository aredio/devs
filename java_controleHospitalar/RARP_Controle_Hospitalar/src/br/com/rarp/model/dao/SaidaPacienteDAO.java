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
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.SaidaPaciente;
import br.com.rarp.model.Usuario;

public class SaidaPacienteDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if (!SistemaCtrl.getInstance().tabelaExiste("movimentacao"))
			throw new Exception("Crie a tabela de movimentação antes de criar a tabela de encaminhamento");
		
		if (!SistemaCtrl.getInstance().tabelaExiste("entradapaciente"))
			throw new Exception("Crie a tabela de entrada de pacientes antes de criar a tabela de encaminhamento");
		
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "saidapaciente(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "estadopaciente VARCHAR, ";
		sql += "codigo_mov INTEGER REFERENCES movimentacao(codigo), ";
		sql += "codigo_entrada INTEGER REFERENCES entradapaciente(codigo), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}
	
	public List<SaidaPaciente> consultar(String consulta) throws Exception {
		List<SaidaPaciente> saidas = new ArrayList<SaidaPaciente>();
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT "
					+ "SAI.codigo AS codigo_saida, "
					+ "SAI.codigo_entrada, "
					+ "SAI.estadopaciente, "
					+ "MOV.data dtmovimentacao, "
					+ "MOV.hora hrmovimentacao, "
					+ "SAI.status AS status_saida, "
					+ "MOV.codigo_usuario "
					+ "FROM saidapaciente SAI "
					+ "LEFT JOIN movimentacao MOV ON SAI.codigo_mov = MOV.codigo "
					+ "WHERE "
					+ consulta;
			PreparedStatement ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				SaidaPaciente saidaPaciente = new SaidaPaciente();
				saidaPaciente.setCodigo(rs.getInt("codigo_saida"));
				saidaPaciente.setEstadoPaciente(rs.getString("estadopaciente"));
				saidaPaciente.setDtMovimentacao(rs.getDate("dtmovimentacao").toLocalDate());
				saidaPaciente.setHrMovimentacao(rs.getTime("hrmovimentacao").toLocalTime());
				saidaPaciente.setEntradaPaciente(new EntradaPacienteDAO().getEntrada(rs.getInt("codigo_entrada")));
				saidaPaciente.setStatus(rs.getBoolean("status_saida"));
				saidaPaciente.setUsuario(new UsuarioDAO().getUsuario(conexao, rs.getInt("codigo_usuario")));
				saidas.add(saidaPaciente);
			}
			return saidas;	
		} finally {
			//conexao.close();
		}
	}

	public void salvar(SaidaPaciente saidaPaciente) throws Exception {
		if(saidaPaciente != null) {
			if(saidaPaciente.getCodigo() == 0)
				inserir(saidaPaciente);
			else
				alterar(saidaPaciente);
		}
	}

	private void alterar(SaidaPaciente saidaPaciente) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {    		
			String sql = "UPDATE "
					+ "saidapaciente "
					+ "SET "
					+ "estadopaciente = ?, "
					+ "codigo_entrada = ?, "
					+ "status = ? "
					+ "WHERE "
					+ "codigo = ?";
			ps = conexao.prepareStatement(sql);
			
    		ps.setString(1, saidaPaciente.getEstadoPaciente());
    		
    		if(saidaPaciente.getEntradaPaciente() != null && saidaPaciente.getEntradaPaciente().getCodigo() > 0)
    			ps.setInt(2, saidaPaciente.getEntradaPaciente().getCodigo());
    		else
    			ps.setNull(2, Types.INTEGER);
    		
    		ps.setBoolean(3, saidaPaciente.isStatus());
    		ps.setInt(4, saidaPaciente.getCodigo());
			
			ps.executeUpdate();
			
			saidaPaciente.setCodigo(SQLDAO.getCodigoMovimentacao(conexao, "saidapaciente", saidaPaciente.getCodigo()));
			if(saidaPaciente.getCodigo() > 0)
				new MovimentacaoDAO().salvar(conexao, saidaPaciente);
    		
			ps.close();
			conexao.commit();
		} catch (Exception e) {
			conexao.rollback();
			throw e;
		} finally {
			//conexao.close();
		} 
	}

	private void inserir(SaidaPaciente saidaPaciente) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {
			String sql = "INSERT INTO saidapaciente(estadopaciente, codigo_mov, codigo_entrada, status) VALUES(?,?,?,?)";
			ps = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			
			MovimentacaoDAO movimentacaoDAO =  new MovimentacaoDAO();
    		movimentacaoDAO.salvar(conexao, saidaPaciente);
    		
    		ps.setString(1, saidaPaciente.getEstadoPaciente());
    		ps.setInt(2, saidaPaciente.getCodigo());
    		
    		if(saidaPaciente.getEntradaPaciente() != null && saidaPaciente.getEntradaPaciente().getCodigo() > 0)
    			ps.setInt(3, saidaPaciente.getEntradaPaciente().getCodigo());
    		else
    			ps.setNull(3, Types.INTEGER);
    		
    		ps.setBoolean(4, saidaPaciente.isStatus());
			
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				saidaPaciente.setCodigo(rs.getInt(1));
			
			ps.close();
			conexao.commit();
		} catch (Exception e) {
			conexao.rollback();
			throw e;
		} finally {
			//conexao.close();
		} 
	}

	public List<SaidaPaciente> consultar(String campo, String comparacao, String termo) throws Exception {
		return consultar(campo + comparacao + termo);
	}

	public List<SaidaPaciente> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			EntradaPaciente entrada, Usuario usuario, String estadoPaciente, Boolean statusAux) throws ClassNotFoundException, SQLException, Exception {
		List<SaidaPaciente> saidas = new ArrayList<SaidaPaciente>();
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT "
					+ "SAI.codigo AS codigo_saida, "
					+ "SAI.codigo_entrada, "
					+ "SAI.estadopaciente, "
					+ "MOV.data dtmovimentacao, "
					+ "MOV.hora hrmovimentacao, "
					+ "SAI.status AS status_saida, "
					+ "MOV.codigo_usuario "
					+ "FROM saidapaciente SAI "
					+ "LEFT JOIN movimentacao MOV ON SAI.codigo_mov = MOV.codigo "
					+ "WHERE "
					+ "SAI.codigo > 0";
			if(dataIni != null)
				sql += " AND MOV.data >= ?";
			if(dataFin != null)
				sql += " AND MOV.data <= ?";
			if(horaIni != null)
				sql += " AND MOV.hora >= ?";
			if(horaFin != null)
				sql += " AND MOV.hora <= ?";
			if(entrada != null)
				sql += " AND SAI.codigo_entrada = ?";
			if(usuario != null)
				sql += " AND MOV.codigo_usuario = ?";
			if(statusAux != null)
				sql += " AND SAI.status = ?";
			if(estadoPaciente != null && !estadoPaciente.isEmpty())
				sql += " AND (SAI.estadopaciente LIKE ?)";
			PreparedStatement ps = conexao.prepareStatement(sql);
			int paramsCount = 0;
			if(dataIni != null)
				ps.setDate(++paramsCount, Date.valueOf(dataIni));
			if(dataFin != null)
				ps.setDate(++paramsCount, Date.valueOf(dataFin));
			if(horaIni != null)
				ps.setTime(++paramsCount, Time.valueOf(horaIni));
			if(horaFin != null)
				ps.setTime(++paramsCount, Time.valueOf(horaFin));
			if(entrada != null)
				ps.setInt(++paramsCount, entrada.getCodigo());
			if(usuario != null)
				ps.setInt(++paramsCount, usuario.getCodigo());
			if(statusAux != null)
				ps.setBoolean(++paramsCount, statusAux);
			if(estadoPaciente != null && !estadoPaciente.isEmpty())
				ps.setString(++paramsCount, "%" + estadoPaciente + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				SaidaPaciente saidaPaciente = new SaidaPaciente();
				saidaPaciente.setCodigo(rs.getInt("codigo_saida"));
				saidaPaciente.setEstadoPaciente(rs.getString("estadopaciente"));
				saidaPaciente.setDtMovimentacao(rs.getDate("dtmovimentacao").toLocalDate());
				saidaPaciente.setHrMovimentacao(rs.getTime("hrmovimentacao").toLocalTime());
				saidaPaciente.setEntradaPaciente(new EntradaPacienteDAO().getEntrada(rs.getInt("codigo_entrada")));
				saidaPaciente.setStatus(rs.getBoolean("status_saida"));
				saidaPaciente.setUsuario(new UsuarioDAO().getUsuario(conexao, rs.getInt("codigo_usuario")));
				saidas.add(saidaPaciente);
			}
			return saidas;	
		} finally {
			//conexao.close();
		}
	}
}
