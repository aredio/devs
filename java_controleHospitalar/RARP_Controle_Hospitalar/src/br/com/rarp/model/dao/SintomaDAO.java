package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Atendimento;
import br.com.rarp.model.Sintoma;

public class SintomaDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {	
		if (!SistemaCtrl.getInstance().tabelaExiste("atendimento"))
			throw new Exception("Crie a tabela de atendimentos antes de criar a tabela de sintomas");
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "sintoma(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "descricao VARCHAR, ";
		sql += "codigo_atendimento INTEGER REFERENCES atendimento(codigo))";
		st.executeUpdate(sql);
	}

	public void salvar(Connection connection, Atendimento atendimento) throws SQLException {
		remover(connection, atendimento);
		inserir(connection, atendimento);
	}

	private void inserir(Connection connection, Atendimento atendimento) throws SQLException {
		if(atendimento.getSintomas() == null)
			return;
		String sql= "INSERT INTO sintoma( "
				+ "descricao, "
				+ "codigo_atendimento) "
				+ "VALUES(?,?)";
		PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    	int i = 0;  	
    	for (Sintoma s : atendimento.getSintomas()) { 
    		ps.setString(1, s.getDescricao());
    		ps.setInt(2, atendimento.getCodigo());
    		ps.addBatch();
            i++;
            if (i == atendimento.getSintomas().size()) {
            	ps.executeBatch();
            }
        }
    	ps.executeBatch();
    	ResultSet rs = ps.getGeneratedKeys();
    	for (Sintoma s: atendimento.getSintomas()) { 
    		if(rs.next())
    			s.setCodigo(rs.getInt(1));
    		ps.close();
    	}
	}

	private void remover(Connection connection, Atendimento atendimento) throws SQLException {
		String sql= "DELETE FROM sintoma WHERE codigo_atendimento = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, atendimento.getCodigo());
		ps.executeUpdate();
		ps.close();
	}
}
