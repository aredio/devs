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
import br.com.rarp.model.Espaco;
import br.com.rarp.model.Leito;
import br.com.rarp.model.Paciente;

public class LeitoDAO {
	
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if(!SistemaCtrl.getInstance().tabelaExiste("espaco"))
			throw new Exception("Crie a tabela de espaços antes de criar a tabela de leitos");
	
		if(!SistemaCtrl.getInstance().tabelaExiste("paciente"))
			throw new Exception("Crie a tabela de paciente antes de criar a tabela de leitos");
		
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "leito(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "numero INTEGER, ";
		sql += "sujo BOOLEAN, ";
		sql += "codigo_espaco INTEGER REFERENCES espaco(codigo), ";
		sql += "codigo_paciente INTEGER REFERENCES paciente(codigo), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}
	
	public void salvar(Espaco espaco) throws Exception {	
		for(Leito l: espaco.getLeitos()) {
			l.setEspaco(new Espaco());
			l.getEspaco().setCodigo(espaco.getCodigo());;
		}
		salvar(espaco.getLeitos());
	}
	
	public void salvar(List<Leito> leitos) throws Exception {
		List<Leito> leitosInserir = new ArrayList<>();
		List<Leito> leitosAlterar = new ArrayList<>();
		
		for(Leito l: leitos) {
			if(l.getCodigo() == 0)
				leitosInserir.add(l);
			else
				leitosAlterar.add(l);
		}	
		inserir(leitosInserir);
		alterar(leitosAlterar);
	}
	
	private void inserir(List<Leito> leitos) throws Exception {
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		String sql= "INSERT INTO leito(numero, codigo_espaco, status, codigo_paciente, sujo) VALUES(?,?,?,?,?)";
		PreparedStatement ps = conexao.prepareStatement(sql);
        try {
        	int i = 0;
        	for (Leito leito : leitos) {
        		ps.setInt(1, leito.getNumero());
        		ps.setInt(2, leito.getEspaco().getCodigo());
        		ps.setBoolean(3, leito.isStatus());
        		if(leito.getPaciente() != null)
        			ps.setInt(4, leito.getPaciente().getCodigo());
        		else
        			ps.setNull(4, Types.INTEGER);
        		ps.setBoolean(5, leito.isSujo());
        		ps.addBatch();
                i++;

                if (i == leitos.size()) {
                	ps.executeBatch();
                }
            }
        	ps.executeBatch();
		} finally {
			ps.close();
			//conexao.close();
		}         
	}
	
	private void alterar(List<Leito> leitos) throws Exception {
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		String sql= "UPDATE leito SET numero=?, codigo_espaco=?, status=?, codigo_paciente=?, sujo=? WHERE codigo=?";
		PreparedStatement ps = conexao.prepareStatement(sql);
        try {
        	int i = 0;
        	for (Leito leito : leitos) { 
        		ps.setInt(1, leito.getNumero());
        		ps.setInt(2, leito.getEspaco().getCodigo());
        		ps.setBoolean(3, leito.isStatus());
        		if(leito.getPaciente() != null)
        			ps.setInt(4, leito.getPaciente().getCodigo());
        		else
        			ps.setNull(4, Types.INTEGER);
        		ps.setBoolean(5, leito.isSujo());
        		ps.setInt(6, leito.getCodigo());
        		ps.addBatch();
                i++;

                if (i == leitos.size()) {
                	ps.executeBatch();
                }
            }
        	ps.executeBatch();
		} finally {
			ps.close();
			//conexao.close();
		}  
	}

	public List<Leito> getLeitos(Espaco espaco) throws Exception {
		if(espaco != null)
			return consultar("codigo_espaco = " + espaco.getCodigo() + " AND LEI.status = TRUE ORDER BY numero ASC");
		return null;
	}
	
	public List<Leito> consultar(String condicao) throws Exception {
		List<Leito> leitos = new ArrayList<>();
        PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        try {
        	String sql = "SELECT "
        			+ "LEI.codigo codigo_leito, "
        			+ "LEI.codigo_espaco, "
        			+ "ESP.nome, "
        			+ "LEI.numero, "
        			+ "LEI.sujo, "
        			+ "LEI.status status_leito, "
        			+ "LEI.codigo_paciente "
        			+ "FROM leito LEI "
        			+ "LEFT JOIN espaco ESP ON LEI.codigo_espaco = ESP.codigo "
        			+ "WHERE "
        			+ "" + condicao;
            ps = conexao.prepareStatement(sql);
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
            ps.close();
        } finally{
            //conexao.close();
        }
		return leitos;		
	}
	
	public List<Leito> consultar(String campo, String comparacao, String termo) throws Exception {
		return consultar(campo + comparacao + termo);		
	}

	public void salvar(Leito leito) throws Exception {
		Espaco espaco = leito.getEspaco();
		if(espaco == null)
			espaco = new Espaco();
		espaco.getLeitos().add(leito);
		salvar(espaco);
	}

	public void salvar(Connection conexao, Leito leito) throws Exception {
		if(leito != null)
			if(leito.getCodigo() == 0)
				inserir(conexao, leito);
			else
				alterar(conexao, leito);
		
	}
	
	private void inserir(Connection conexao, Leito leito) throws Exception {
		String sql= "INSERT INTO leito(numero, codigo_espaco, status, codigo_paciente, sujo) VALUES(?,?,?,?)";
		PreparedStatement ps = conexao.prepareStatement(sql);
        try {
        		ps.setInt(1, leito.getNumero());
        		if(leito.getEspaco() != null)
        			ps.setInt(2, leito.getEspaco().getCodigo());
        		else
        			ps.setNull(2, Types.INTEGER);
        		ps.setBoolean(3, leito.isStatus());
        		if(leito.getPaciente() != null)
        			ps.setInt(4, leito.getPaciente().getCodigo());
        		else
        			ps.setNull(4, Types.INTEGER);
        		ps.setBoolean(5, leito.isSujo());
        		ps.executeUpdate();

		} finally {
			ps.close();
			//conexao.close();
		}         
	}
	
	private void alterar(Connection conexao, Leito leito) throws Exception {
		String sql= "UPDATE leito SET numero=?, codigo_espaco=?, status=?, codigo_paciente=?, sujo=? WHERE codigo=?";
		PreparedStatement ps = conexao.prepareStatement(sql);
        try {
    		ps.setInt(1, leito.getNumero());
    		if(leito.getEspaco() != null)
    			ps.setInt(2, leito.getEspaco().getCodigo());
    		else
    			ps.setNull(2, Types.INTEGER);
    		ps.setBoolean(3, leito.isStatus());
    		if(leito.getPaciente() != null)
    			ps.setInt(4, leito.getPaciente().getCodigo());
    		else
    			ps.setNull(4, Types.INTEGER);
    		ps.setBoolean(5, leito.isSujo());
    		ps.setInt(6, leito.getCodigo());
    		ps.executeUpdate();
		} finally {
			ps.close();
		}  
	}

	public List<Leito> getLeitosLivres(Espaco espaco) throws Exception {
		if(espaco != null && espaco.getCodigo() > 0) {
			String consulta = "codigo_espaco = " + espaco.getCodigo() + " AND codigo_paciente IS NULL AND NOT sujo AND LEI.status = TRUE";
			return consultar(consulta);
		}
		return null;
	}

	public List<Leito> getLeitosCheios(Espaco espaco, Paciente paciente) throws Exception {
		if(espaco != null && espaco.getCodigo() > 0) {
			String consulta = "codigo_espaco = " + espaco.getCodigo() + " AND codigo_paciente IS NOT NULL AND LEI.status = TRUE";
			if(paciente != null && paciente.getCodigo() > 0)
				consulta += " AND codigo_paciente = " + paciente.getCodigo();
			return consultar(consulta);
		}
		return null;
	}

	public Leito getLeito(int codigo) throws Exception {
		String consulta = "LEI.codigo = " + codigo;
		List<Leito> leitos = consultar(consulta);
		if(leitos.size() > 0)
			return leitos.get(0);
		return null;
	}

}
