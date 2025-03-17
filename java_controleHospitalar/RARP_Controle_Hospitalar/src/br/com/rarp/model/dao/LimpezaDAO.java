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
import br.com.rarp.model.Espaco;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Leito;
import br.com.rarp.model.Limpeza;
import br.com.rarp.model.Usuario;

public class LimpezaDAO {
	
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if(!SistemaCtrl.getInstance().tabelaExiste("movimentacao"))
			throw new Exception("Crie a tabela de movimentação antes de criar a tabela de limpeza e limpeza_leitos");
	
		if(!SistemaCtrl.getInstance().tabelaExiste("leito"))
			throw new Exception("Crie a tabela de leito antes de criar a tabela de limpeza e limpeza_leitos");
		
		if(!SistemaCtrl.getInstance().tabelaExiste("funcionario"))
			throw new Exception("Crie a tabela de funcionário antes de criar a tabela de limpeza e limpeza_leitos");

		
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "limpeza(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "codigo_mov INTEGER REFERENCES movimentacao(codigo), ";
		sql += "codigo_funcionario INTEGER REFERENCES funcionario(codigo), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
		st.close();
		
		st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "limpeza_leito(";
		sql += "codigo_limpeza INTEGER REFERENCES limpeza(codigo), ";
		sql += "codigo_leito INTEGER REFERENCES leito(codigo), ";
		sql += "CONSTRAINT limpeza_leito_pkey PRIMARY KEY(codigo_limpeza, codigo_leito))";
		st.executeUpdate(sql);
		st.close();
	}
	
	public List<Leito> getLeitosLimpeza(Connection connection, int codigo) throws Exception {
		List<Leito> leitos = new ArrayList<>();
    	String sql = "SELECT "
    			+ "LEI.codigo codigo_leito, "
    			+ "LEI.codigo_espaco, "
    			+ "ESP.nome, "
    			+ "LEI.numero, "
    			+ "LEI.sujo, "
    			+ "LEI.status status_leito, "
    			+ "LEI.codigo_paciente "
    			+ "FROM "
    			+ "limpeza_leito LL "
    			+ "LEFT JOIN leito LEI ON LL.codigo_leito = LEI.codigo "
    			+ "LEFT JOIN espaco ESP ON LEI.codigo_espaco = ESP.codigo "
    			+ "WHERE "
    			+ "LL.codigo_limpeza = " + codigo;
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
        	Leito leito = new Leito();
        	leito.setCodigo(rs.getInt("codigo_leito"));
        	leito.setNumero(rs.getInt("numero"));
        	leito.setStatus(rs.getBoolean("status_leito"));
        	leito.setSujo(rs.getBoolean("sujo"));
        	leito.setPaciente(new PacienteDAO().getPaciente(rs.getInt("codigo_paciente")));
        	leito.setEspaco(new Espaco());
        	leito.getEspaco().setCodigo(rs.getInt("codigo_espaco"));
        	leito.getEspaco().setNome(rs.getString("nome"));
        	leitos.add(leito);
		}
		return leitos;
	}
	
	public List<Limpeza> consultar(String consulta) throws Exception {
		List<Limpeza> limpezas = new ArrayList<>();
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {
			String sql = "SELECT "
					+ "LIM.codigo AS codigo_limpeza, "
					+ "LIM.codigo_mov, "
					+ "MOV.data AS dtmovimentacao, "
					+ "MOV.hora AS hrmovimentacao, "
					+ "MOV.codigo_usuario, "
					+ "LIM.codigo_funcionario, "
					+ "LIM.status AS status_limpeza "
					+ "FROM "
					+ "limpeza LIM "
					+ "LEFT JOIN movimentacao MOV ON LIM.codigo_mov = MOV.codigo "
					+ "WHERE "
					+ consulta;
			PreparedStatement ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Limpeza limpeza = new Limpeza();
				limpeza.setCodigo(rs.getInt("codigo_limpeza"));
				limpeza.setDtMovimentacao(rs.getDate("dtMovimentacao").toLocalDate());
				limpeza.setHrMovimentacao(rs.getTime("hrmovimentacao").toLocalTime());
				limpeza.setFuncionarioLimpeza(new FuncionarioDAO().getFuncionario(rs.getInt("codigo_funcionario")));
				limpeza.setUsuario(new UsuarioDAO().getUsuario(conexao, rs.getInt("codigo_usuario")));
				limpeza.setStatus(rs.getBoolean("status_limpeza"));
				limpeza.setLeitos(getLeitosLimpeza(conexao, limpeza.getCodigo()));
				limpezas.add(limpeza);
			}
			ps.close();
			conexao.commit();
		} catch (Exception e) {
			conexao.rollback();
		} finally {
			//conexao.close();
		} 
		return limpezas;
	}

	public void salvar(Limpeza limpeza, Limpeza limpezaAnt) throws Exception {
		if(limpeza != null)
			if(limpeza.getCodigo() == 0)
				inserir(limpeza);
			else
				alterar(limpeza, limpezaAnt);
	}

	private void alterar(Limpeza limpeza, Limpeza limpezaAnt) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {
    		for(Leito l : limpezaAnt.getLeitos())
    			new LeitoDAO().salvar(conexao, l);
    		
			String sql = "UPDATE "
					+ "limpeza "
					+ "SET "
					+ "codigo_funcionario = ?, "
					+ "status = ? "
					+ "WHERE "
					+ "codigo = ?";
			ps = conexao.prepareStatement(sql);
			if(limpeza.getFuncionarioLimpeza() != null && limpeza.getFuncionarioLimpeza().getCodigo() > 0)
				ps.setInt(1, limpeza.getFuncionarioLimpeza().getCodigo());
			else
				ps.setNull(1, Types.INTEGER);
    		ps.setBoolean(2, limpeza.isStatus());
    		ps.setInt(3, limpeza.getCodigo());
			ps.executeUpdate();
			ps.close();	
			salvarLeitosLimpeza(conexao, limpeza);
			
			limpeza.setCodigo(SQLDAO.getCodigoMovimentacao(conexao, "limpeza", limpeza.getCodigo()));
			if(limpeza.getCodigo() > 0)
				new MovimentacaoDAO().salvar(conexao, limpeza);
			
			conexao.commit();
		} catch (Exception e) {
			conexao.rollback();
			throw e;
		} finally {
			//conexao.close();
		} 
	}
	
	private void salvarLeitosLimpeza(Connection conexao, Limpeza limpeza) throws Exception {
		if(limpeza != null && conexao != null && limpeza.getCodigo() > 0) {
			String sql = "DELETE FROM limpeza_leito WHERE codigo_limpeza = " + limpeza.getCodigo();
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();
			
			sql = "INSERT INTO limpeza_leito(codigo_limpeza, codigo_leito) ";
			sql += "VALUES(?,?) ";
			sql += "ON CONFLICT (codigo_limpeza, codigo_leito) ";
			sql += "DO UPDATE SET ";
			sql += "codigo_limpeza = ?, ";
			sql += "codigo_leito = ? ";
			
			ps = conexao.prepareStatement(sql);
        	int i = 0;
        	
        	if (limpeza.getCodigo() != 0) {
				for (Leito leito : limpeza.getLeitos()) {
					if (leito.getCodigo() == 0)
						continue;
					ps.setInt(1, limpeza.getCodigo());
					ps.setInt(2, leito.getCodigo());
					ps.setInt(3, limpeza.getCodigo());
					ps.setInt(4, leito.getCodigo());
					new LeitoDAO().salvar(conexao, leito);
					ps.addBatch();
					i++;

					if (i == limpeza.getLeitos().size()) {
						ps.executeBatch();
					}
				} 
			}
			ps.executeBatch();
		}
	}

	private void inserir(Limpeza limpeza) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		conexao.setAutoCommit(false);
		try {
			String sql = "INSERT INTO limpeza(codigo_funcionario, codigo_mov, status) VALUES(?,?,?)";
			ps = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			
			MovimentacaoDAO movimentacaoDAO =  new MovimentacaoDAO();
    		movimentacaoDAO.salvar(conexao, limpeza);
		
    		if(limpeza.getFuncionarioLimpeza() != null && limpeza.getFuncionarioLimpeza().getCodigo() > 0)
    			ps.setInt(1, limpeza.getFuncionarioLimpeza().getCodigo());
    		else
				ps.setNull(1, Types.INTEGER);
    		
    		ps.setInt(2, limpeza.getCodigo());
    		ps.setBoolean(3, limpeza.isStatus());
			
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				limpeza.setCodigo(rs.getInt(1));
			
			ps.close();
			salvarLeitosLimpeza(conexao, limpeza);
			conexao.commit();
		} catch (Exception e) {
			conexao.rollback();
			throw e;
		} finally {
			//conexao.close();
		} 
	}

	public List<Limpeza> consultar(String campo, String comparacao, String termo) throws Exception {
		return consultar(campo + comparacao + termo);
	}

	public List<Limpeza> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			Funcionario funcionarioLimpeza, List<Leito> leitos, Usuario usuario, Boolean status) throws ClassNotFoundException, SQLException, Exception {
		List<Limpeza> limpezas = new ArrayList<>();
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT DISTINCT "
					+ "LIM.codigo AS codigo_limpeza, "
					+ "LIM.codigo_mov, "
					+ "MOV.data AS dtmovimentacao, "
					+ "MOV.hora AS hrmovimentacao, "
					+ "MOV.codigo_usuario, "
					+ "LIM.codigo_funcionario, "
					+ "LIM.status AS status_limpeza "
					+ "FROM "
					+ "limpeza LIM "
					+ "LEFT JOIN movimentacao MOV ON LIM.codigo_mov = MOV.codigo "
					+ "LEFT JOIN limpeza_leito LL ON LL.codigo_limpeza = LIM.codigo "
					+ "WHERE "
					+ "LIM.codigo > 0";
			if(dataIni != null)
				sql += " AND MOV.data >= ?";
			if(dataFin != null)
				sql += " AND MOV.data <= ?";
			if(horaIni != null)
				sql += " AND MOV.hora >= ?";
			if(horaFin != null)
				sql += " AND MOV.hora <= ?";
			if(funcionarioLimpeza != null)
				sql += " AND LIM.codigo_funcionario = ?";
			if(leitos != null && leitos.size() > 0) {
				sql += " AND LL.codigo_leito IN (";
				for (int i = 0; i < leitos.size(); i++)
					sql += "?,";
				sql = sql.substring(0, sql.length() - 1);
				sql += ")";
			}
			if(usuario != null)
				sql += " AND MOV.codigo_usuario = ?";
			if(status != null)
				sql += " AND LIM.status = ?";
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			int paramCount = 0;
			if(dataIni != null)
				ps.setDate(++paramCount, Date.valueOf(dataIni));
			if(dataFin != null)
				ps.setDate(++paramCount, Date.valueOf(dataFin));
			if(horaIni != null)
				ps.setTime(++paramCount, Time.valueOf(horaIni));
			if(horaFin != null)
				ps.setTime(++paramCount, Time.valueOf(horaFin));
			if(funcionarioLimpeza != null)
				ps.setInt(++paramCount, funcionarioLimpeza.getCodigo());
			if(leitos != null) 
				for(Leito l: leitos) 
					ps.setInt(++paramCount, l.getCodigo());
			if(usuario != null)
				ps.setInt(++paramCount, usuario.getCodigo());
			if(status != null)
				ps.setBoolean(++paramCount, status);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Limpeza limpeza = new Limpeza();
				limpeza.setCodigo(rs.getInt("codigo_limpeza"));
				limpeza.setDtMovimentacao(rs.getDate("dtMovimentacao").toLocalDate());
				limpeza.setHrMovimentacao(rs.getTime("hrmovimentacao").toLocalTime());
				limpeza.setFuncionarioLimpeza(new FuncionarioDAO().getFuncionario(rs.getInt("codigo_funcionario")));
				limpeza.setUsuario(new UsuarioDAO().getUsuario(conexao, rs.getInt("codigo_usuario")));
				limpeza.setStatus(rs.getBoolean("status_limpeza"));
				limpeza.setLeitos(getLeitosLimpeza(conexao, limpeza.getCodigo()));
				limpezas.add(limpeza);
			}
			ps.close();
		} finally {
			//conexao.close();
		} 
		return limpezas;
	}

}
