package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.StatusAtendimento;
import br.com.rarp.model.Atendimento;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Usuario;

public class AtendimentoDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		
		if (!SistemaCtrl.getInstance().tabelaExiste("entradapaciente"))
			throw new Exception("Crie a tabela de entrada de pacientes antes de criar a tabela de atendimento");
		
		if (!SistemaCtrl.getInstance().tabelaExiste("movimentacao"))
			throw new Exception("Crie a tabela de movimenta��es antes de criar a tabela de atendimento");
		
		if (!SistemaCtrl.getInstance().tabelaExiste("receita"))
			throw new Exception("Crie a tabela de receitas antes de criar a tabela de atendimento");
		
		if (!SistemaCtrl.getInstance().tabelaExiste("funcionario"))
			throw new Exception("Crie a tabela de funcionarios antes de criar a tabela de atendimento");
		
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "atendimento(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "dataatendimento DATE, ";
		sql += "horaini TIME, ";
		sql += "horafim TIME, ";
		sql += "detalhemedico VARCHAR, ";
		sql += "descricao VARCHAR, ";
		sql += "statusatendimento VARCHAR, ";
		sql += "styleclass VARCHAR, ";
		sql += "codigo_entrada INTEGER REFERENCES entradapaciente(codigo), ";
		sql += "codigo_mov INTEGER REFERENCES movimentacao(codigo), ";
		sql += "codigo_receita INTEGER REFERENCES receita(codigo), ";
		sql += "codigo_funcionario INTEGER REFERENCES funcionario(codigo), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}
	
	public void salvar(Connection connection, EntradaPaciente entradaPaciente) throws Exception {
		List<Atendimento> listaInserir = new ArrayList<>(), listaAlterar = new ArrayList<>();
		for(Atendimento a: entradaPaciente.getAtendimentos()) {
			a.setEntradaPaciente(entradaPaciente);
			if(a != null) {
				if(a.getCodigo() == 0)
					listaInserir.add(a);
				else
					listaAlterar.add(a);
			}
		}
		inserir(connection, listaInserir);
		alterar(connection, listaAlterar);
	}

	private void alterar(Connection connection, List<Atendimento> atendimentos) throws Exception {
		String sql= "UPDATE atendimento SET dataatendimento = ?, "
				+ "horaini = ?, "
				+ "horafim = ?, "
				+ "detalhemedico = ?, "
				+ "descricao = ?, "
				+ "statusatendimento = ?, "
				+ "codigo_entrada = ?, "
				+ "codigo_receita = ?, "
				+ "codigo_funcionario = ?, "
				+ "styleclass = ?, "
				+ "status = ? "
				+ "WHERE "
				+ "codigo = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
    	int i = 0;
    	for (Atendimento a: atendimentos) { 
    		ReceitaMedicaDAO receitaMedicaDAO = new ReceitaMedicaDAO();
    		receitaMedicaDAO.salvar(connection, a.getReceitaMedica());
    		
    		MovimentacaoDAO movimentacaoDAO =  new MovimentacaoDAO();
    		movimentacaoDAO.salvar(connection, a);
    		
    		ps.setDate(1, Date.valueOf(a.getDataAtendimento()));
    		ps.setTime(2, Time.valueOf(a.getHoraIni()));
    		ps.setTime(3, Time.valueOf(a.getHoraFim()));
    		ps.setString(4, a.getDetalheMedico());
    		ps.setString(5, a.getDescricao());
    		ps.setString(6, a.getStatusAtendimento().toString());
    		if(a.getEntradaPaciente() != null)
    			ps.setInt(7, a.getEntradaPaciente().getCodigo());
    		else
    			ps.setNull(7, Types.INTEGER);
    		
    		if(a.getReceitaMedica() != null)
    			ps.setInt(8, a.getReceitaMedica().getCodigo());
    		else
    			ps.setNull(8, Types.INTEGER);
    		
    		if(a.getResponsavel() != null)
    			ps.setInt(9, a.getResponsavel().getCodigo());
    		else
    			ps.setNull(9, Types.INTEGER);
    		
    		ps.setString(10, a.getStyleClass());
    		ps.setBoolean(11, a.isStatus());
    		ps.setInt(12, a.getCodigo());
    		ps.addBatch();
    		
    		new SintomaDAO().salvar(connection, a);
    		
    		a.setCodigo(SQLDAO.getCodigoMovimentacao(connection, "atendimento", a.getCodigo()));
			if(a.getCodigo() > 0)
				new MovimentacaoDAO().salvar(connection, a);
            i++;
            if (i == atendimentos.size()) {
            	ps.executeBatch();
            }
        }
    	ps.executeBatch();
		ps.close(); 
	}

	private void inserir(Connection connection, List<Atendimento> atendimentos) throws Exception {
		String sql= "INSERT INTO atendimento("
				+ "dataatendimento, "
				+ "horaini, "
				+ "horafim, "
				+ "detalhemedico, "
				+ "descricao, "
				+ "statusatendimento, "
				+ "codigo_entrada, "
				+ "codigo_mov, "
				+ "codigo_receita, "
				+ "codigo_funcionario, "
				+ "styleclass, "
				+ "status "
				+ ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    	int i = 0;
    	for (Atendimento a : atendimentos) { 
    		ReceitaMedicaDAO receitaMedicaDAO = new ReceitaMedicaDAO();
    		receitaMedicaDAO.salvar(connection, a.getReceitaMedica());
    		
    		MovimentacaoDAO movimentacaoDAO =  new MovimentacaoDAO();
    		movimentacaoDAO.salvar(connection, a);
    		
    		ps.setDate(1, Date.valueOf(a.getDataAtendimento()));
    		ps.setTime(2, Time.valueOf(a.getHoraIni()));
    		ps.setTime(3, Time.valueOf(a.getHoraFim()));
    		ps.setString(4, a.getDetalheMedico());
    		ps.setString(5, a.getDescricao());
    		ps.setString(6, a.getStatusAtendimento().toString());
    		if(a.getEntradaPaciente() != null)
    			ps.setInt(7, a.getEntradaPaciente().getCodigo());
    		else
    			ps.setNull(7, Types.INTEGER);
    		
    		ps.setInt(8, a.getCodigo());
    		
    		if(a.getReceitaMedica() != null)
    			ps.setInt(9, a.getReceitaMedica().getCodigo());
    		else
    			ps.setNull(9, Types.INTEGER);
    		
    		if(a.getResponsavel() != null)
    			ps.setInt(10, a.getResponsavel().getCodigo());
    		else
    			ps.setNull(10, Types.INTEGER);
    		
    		ps.setString(11, a.getStyleClass());	
    		ps.setBoolean(12, a.isStatus());
    		
    		ps.addBatch();
            i++;
            if (i == atendimentos.size()) {
            	ps.executeBatch();
            }
        }
    	
    	ps.executeBatch();
    	ResultSet rs = ps.getGeneratedKeys();
    	for (Atendimento a : atendimentos) {
			if (rs.next())
				a.setCodigo(rs.getInt(1));
			
			SintomaDAO sintomaDAO = new SintomaDAO();
    		sintomaDAO.salvar(connection, a);
		}
		ps.close();   
	}

	public void salvar(Atendimento atendimento) throws ClassNotFoundException, SQLException, Exception {
		if(atendimento != null)
			if(atendimento.getCodigo() > 0)
				inserir(atendimento);
			else
				alterar(atendimento);
	}

	private void alterar(Atendimento atendimento) throws ClassNotFoundException, SQLException, Exception {
		List<Atendimento> atendimentos = new ArrayList<>();
		atendimentos.add(atendimento);
		inserir(SistemaCtrl.getInstance().getConexao().getConexao(), atendimentos);
	}

	private void inserir(Atendimento atendimento) throws ClassNotFoundException, SQLException, Exception {
		List<Atendimento> atendimentos = new ArrayList<>();
		atendimentos.add(atendimento);
		alterar(SistemaCtrl.getInstance().getConexao().getConexao(), atendimentos);
	}

	public List<Atendimento> getAtendimentos(int codigo) throws ClassNotFoundException, Exception {
		if(codigo > 0) {
			return consultar("ATE.codigo_entrada", " = ", codigo + "");
		}
		return null;
	}

	public List<Atendimento> consultar(String campo, String comparacao, String termo) throws ClassNotFoundException, Exception {
		return consultar(campo + comparacao + termo);
	}

	public List<Atendimento> consultar(String consulta) throws ClassNotFoundException, SQLException, Exception {
		List<Atendimento> atendimentos = new ArrayList<Atendimento>();
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT "
					+ "ATE.codigo AS codigo_atendimento, "
					+ "ATE.dataatendimento, "
					+ "ATE.horaini, "
					+ "ATE.horafim, "
					+ "ATE.detalhemedico, "
					+ "ATE.descricao AS descricao_atendimento, "
					+ "ATE.statusatendimento, "
					+ "MOV.data AS dtmovimentacao, "
					+ "MOV.hora AS hrmovimentacao, "
					+ "ATE.codigo_entrada, "
					+ "ATE.codigo_receita, "
					+ "ATE.codigo_funcionario, "
					+ "ATE.styleclass, "
					+ "ATE.status AS status_atendimento,"
					+ "MOV.codigo_usuario "
					+ "FROM atendimento ATE "
					+ "LEFT JOIN movimentacao MOV ON ATE.codigo_mov = MOV.codigo "
					+ "LEFT JOIN funcionario FUNC ON ATE.codigo_funcionario = FUNC.codigo "
					+ "LEFT JOIN usuario USU ON MOV.codigo_usuario = USU.codigo "
					+ "LEFT JOIN receita REC ON ATE.codigo_receita = REC.codigo "
					+ "WHERE "
					+ consulta;
			PreparedStatement ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Atendimento atendimento = new Atendimento();
				atendimentos.add(atendimento);
				
				atendimento.setCodigo(rs.getInt("codigo_atendimento"));
				if(rs.getDate("dataatendimento") != null)
					atendimento.setDataAtendimento(rs.getDate("dataatendimento").toLocalDate());
				if(rs.getTime("horaini") != null)
					atendimento.setHoraIni(rs.getTime("horaini").toLocalTime());
				if(rs.getTime("horafim") != null)
					atendimento.setHoraFim(rs.getTime("horafim").toLocalTime());
				atendimento.setDetalheMedico(rs.getString("detalhemedico"));
				atendimento.setDescricao(rs.getString("descricao_atendimento"));
				switch (rs.getString("statusatendimento")) {
				case "Em Aberto":
					atendimento.setStatusAtendimento(StatusAtendimento.emAberto);
					break;
					
				case "Em Andamento":
					atendimento.setStatusAtendimento(StatusAtendimento.emAndamento);
					break;
					
				case "Realizado":
					atendimento.setStatusAtendimento(StatusAtendimento.realizado);
					break;
				}
				if(rs.getDate("dtmovimentacao") != null)
					atendimento.setDtMovimentacao(rs.getDate("dtmovimentacao").toLocalDate());
				if(rs.getDate("hrmovimentacao") != null)
					atendimento.setHrMovimentacao(rs.getTime("hrmovimentacao").toLocalTime());
				atendimento.setUsuario(new UsuarioDAO().getUsuario(conexao, rs.getInt("codigo_usuario")));
				atendimento.setEntradaPaciente(new EntradaPacienteDAO().getEntradaSemListas(rs.getInt("codigo_entrada")));				
				atendimento.setReceitaMedica(new ReceitaMedicaDAO().getReceita(rs.getInt("codigo_receita")));
				atendimento.setResponsavel(new FuncionarioDAO().getFuncionario(rs.getInt("codigo_funcionario")));
				atendimento.setStyleClass(rs.getString("styleclass"));
				atendimento.setStatus(rs.getBoolean("status_atendimento"));
				
			}
			return atendimentos;	
		} finally {
			//conexao.close();
		}
	}

	public List<Atendimento> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			LocalDate dataIniAtend, LocalTime horaIniAtend, LocalDate dataFinAtend, LocalTime horaFinAtend,
			EntradaPaciente entrada, Funcionario responsavel, Usuario usuario, StatusAtendimento statusAtendimento,
			Boolean statusAux) throws ClassNotFoundException, Exception {
		List<Atendimento> atendimentos = new ArrayList<Atendimento>();
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "SELECT "
					+ "ATE.codigo AS codigo_atendimento, "
					+ "ATE.dataatendimento, "
					+ "ATE.horaini, "
					+ "ATE.horafim, "
					+ "ATE.detalhemedico, "
					+ "ATE.descricao AS descricao_atendimento, "
					+ "ATE.statusatendimento, "
					+ "MOV.data AS dtmovimentacao, "
					+ "MOV.hora AS hrmovimentacao, "
					+ "ATE.codigo_entrada, "
					+ "ATE.codigo_receita, "
					+ "ATE.codigo_funcionario, "
					+ "ATE.styleclass, "
					+ "ATE.status AS status_atendimento, "
					+ "MOV.codigo_usuario "
					+ "FROM atendimento ATE "
					+ "LEFT JOIN movimentacao MOV ON ATE.codigo_mov = MOV.codigo "
					+ "LEFT JOIN funcionario FUNC ON ATE.codigo_funcionario = FUNC.codigo "
					+ "LEFT JOIN usuario USU ON MOV.codigo_usuario = USU.codigo "
					+ "LEFT JOIN receita REC ON ATE.codigo_receita = REC.codigo "
					+ "WHERE "
					+ "ATE.codigo > 0";
			if(dataIni != null)
				sql += " AND MOV.data >= ?";
			if(dataFin != null)
				sql += " AND MOV.data <= ?";
			if(horaIni != null)
				sql += " AND MOV.hora >= ?";
			if(horaFin != null)
				sql += " AND MOV.hora <= ?";
			LocalDateTime dataHoraIni = null;
			if(dataIniAtend != null && horaIniAtend != null)
				dataHoraIni = LocalDateTime.of(dataIniAtend, horaIniAtend);
			if(dataIniAtend != null && horaIniAtend == null)
				dataHoraIni = LocalDateTime.of(dataIniAtend.getYear(), dataIniAtend.getMonth(), dataIniAtend.getDayOfMonth(), 0, 0);
			if(dataHoraIni != null)
				sql += " AND (ATE.dataatendimento + ATE.horaini) >= ?";
			
			LocalDateTime dataHoraFin = null;
			if(dataFinAtend != null && horaFinAtend != null)
				dataHoraFin = LocalDateTime.of(dataFinAtend, horaFinAtend);
			if(dataFinAtend != null && horaFinAtend == null)
				dataHoraIni = LocalDateTime.of(dataFinAtend.getYear(), dataFinAtend.getMonth(), dataFinAtend.getDayOfMonth(), 0, 0);
			if(dataHoraFin != null)
				sql += " AND (ATE.dataatendimento + ATE.horafim) <= ?";
			
			if(entrada != null)
				sql += " AND ATE.codigo_entrada = ?";
			if(responsavel != null)
				sql += " AND ATE.codigo_funcionario = ?";
			if(usuario != null)
				sql += " AND MOV.codigo_usuario = ?";
			if(statusAtendimento != null)
				sql += " AND ATE.statusatendimento = ?";
			if(statusAux != null)
				sql += " AND ATE.status = ?";
			
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
			if(dataHoraIni != null)
				ps.setTimestamp(++paramCount, Timestamp.valueOf(dataHoraIni));
			if(dataHoraFin != null)
				ps.setTimestamp(++paramCount, Timestamp.valueOf(dataHoraFin));
			if(entrada != null)
				ps.setInt(++paramCount, entrada.getCodigo());
			if(responsavel != null)
				ps.setInt(++paramCount, responsavel.getCodigo());
			if(usuario != null)
				ps.setInt(++paramCount, usuario.getCodigo());
			if(statusAtendimento != null)
				ps.setString(++paramCount, statusAtendimento.toString());
			if(statusAux != null)
				ps.setBoolean(++paramCount, statusAux);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Atendimento atendimento = new Atendimento();
				atendimentos.add(atendimento);
				
				atendimento.setCodigo(rs.getInt("codigo_atendimento"));
				if(rs.getDate("dataatendimento") != null)
					atendimento.setDataAtendimento(rs.getDate("dataatendimento").toLocalDate());
				if(rs.getTime("horaini") != null)
					atendimento.setHoraIni(rs.getTime("horaini").toLocalTime());
				if(rs.getTime("horafim") != null)
					atendimento.setHoraFim(rs.getTime("horafim").toLocalTime());
				atendimento.setDetalheMedico(rs.getString("detalhemedico"));
				atendimento.setDescricao(rs.getString("descricao_atendimento"));
				switch (rs.getString("statusatendimento")) {
				case "Em Aberto":
					atendimento.setStatusAtendimento(StatusAtendimento.emAberto);
					break;
					
				case "Em Andamento":
					atendimento.setStatusAtendimento(StatusAtendimento.emAndamento);
					break;
					
				case "Realizado":
					atendimento.setStatusAtendimento(StatusAtendimento.realizado);
					break;
				}
				if(rs.getDate("dtmovimentacao") != null)
					atendimento.setDtMovimentacao(rs.getDate("dtmovimentacao").toLocalDate());
				if(rs.getDate("hrmovimentacao") != null)
					atendimento.setHrMovimentacao(rs.getTime("hrmovimentacao").toLocalTime());
				atendimento.setUsuario(new UsuarioDAO().getUsuario(conexao, rs.getInt("codigo_usuario")));
				atendimento.setEntradaPaciente(new EntradaPacienteDAO().getEntradaSemListas(rs.getInt("codigo_entrada")));				
				atendimento.setReceitaMedica(new ReceitaMedicaDAO().getReceita(rs.getInt("codigo_receita")));
				atendimento.setResponsavel(new FuncionarioDAO().getFuncionario(rs.getInt("codigo_funcionario")));
				atendimento.setStyleClass(rs.getString("styleclass"));
				atendimento.setStatus(rs.getBoolean("status_atendimento"));
				
			}
			return atendimentos;	
		} finally {
			//conexao.close();
		}
	}
}
