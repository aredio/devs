package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Espaco;
import br.com.rarp.model.Paciente;
public class EspacoDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "espaco(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "nome VARCHAR(100), ";
		sql += "bloco VARCHAR(50), ";
		sql += "andar VARCHAR(50), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}
	
	public void salvar(Espaco espaco) throws Exception {
		if(espaco.getCodigo() == 0)
			inserir(espaco);
		else
			alterar(espaco);
	}

	private void inserir(Espaco espaco) throws Exception {
		PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        if(!conexao.getAutoCommit()) {
			conexao.commit();
			conexao.setAutoCommit(true);
		}
        try {
        	String sql= "INSERT INTO espaco(nome, bloco, andar, status) VALUES(?,?,?,?)";
            ps = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, espaco.getNome());
            ps.setString(2, espaco.getBloco());
            ps.setString(3, espaco.getAndar());
            ps.setBoolean(4, espaco.isStatus());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				espaco.setCodigo(rs.getInt(1));
			ps.close();
            
            LeitoDAO leitoDAO = new LeitoDAO();
            leitoDAO.salvar(espaco);
        } finally {
        	//conexao.close();
		}
	}

	private void alterar(Espaco espaco) throws Exception {
		PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        if(!conexao.getAutoCommit()) {
			conexao.commit();
			conexao.setAutoCommit(true);
		}
        try {
        	String sql= "Update espaco SET nome=?, bloco=?, andar=?, status=? WHERE codigo=?";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, espaco.getNome());
            ps.setString(2, espaco.getBloco());
            ps.setString(3, espaco.getAndar());
            ps.setBoolean(4, espaco.isStatus());
            ps.setInt(5, espaco.getCodigo());
            
            LeitoDAO leitoDAO = new LeitoDAO();
            leitoDAO.salvar(espaco);
            
            ps.executeUpdate();
            ps.close();
        } finally {
        	//conexao.close();
		}
	}

	public List<Espaco> consultar(String campo, String comparacao, String termo) throws Exception {
		List<Espaco> espacos = new ArrayList<>();
        PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        try {
        	String sql = "SELECT codigo, nome, bloco, andar, status FROM espaco WHERE " + campo + comparacao + termo;
            ps = conexao.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	Espaco espaco = new Espaco();
            	espaco.setCodigo(rs.getInt("codigo"));
            	espaco.setNome(rs.getString("nome"));
            	espaco.setBloco(rs.getString("bloco"));
            	espaco.setAndar(rs.getString("andar"));
            	espaco.setStatus(rs.getBoolean("status"));
            	
            	LeitoDAO leitoDAO = new LeitoDAO();
            	espaco.setLeitos(leitoDAO.getLeitos(espaco));
            	
            	espacos.add(espaco);
            }
            ps.close();
        } finally{
            //conexao.close();
        }
		return espacos;
	}

	public List<Espaco> getEspacos() throws Exception {
		return consultar("status", " = ", "TRUE");
	}

	public Espaco getRecepcao() throws Exception {
		List<Espaco> espacos = consultar("nome", " = ", "'" + SistemaCtrl.getInstance().getRecepcao().getNome() + "'");
		if(espacos.size() > 0)
			return espacos.get(0);
		return null;
	}

	public List<Espaco> getEspacos(Paciente paciente, boolean livres) throws ClassNotFoundException, Exception {
		List<Espaco> espacos = new ArrayList<>();
        PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        try {
        	String sql = "SELECT codigo, nome, bloco, andar, status FROM espaco WHERE status = TRUE";
            ps = conexao.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	Espaco espaco = new Espaco();
            	espaco.setCodigo(rs.getInt("codigo"));
            	espaco.setNome(rs.getString("nome"));
            	espaco.setBloco(rs.getString("bloco"));
            	espaco.setAndar(rs.getString("andar"));
            	espaco.setStatus(rs.getBoolean("status"));
            	
            	LeitoDAO leitoDAO = new LeitoDAO();
            	if(livres)
            		espaco.setLeitos(leitoDAO.getLeitosLivres(espaco));
            	else
            		espaco.setLeitos(leitoDAO.getLeitosCheios(espaco, paciente));
            	
            	if(espaco.getLeitos().size() > 0)
            		espacos.add(espaco);
            }
            ps.close();
        } finally{
            //conexao.close();
        }
		return espacos;
	}

}
