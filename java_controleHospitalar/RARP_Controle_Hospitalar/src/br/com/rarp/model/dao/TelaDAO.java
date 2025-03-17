package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.PerfilUsuario;
import br.com.rarp.model.Tela;

public class TelaDAO {
	public static void criarRegistrosPadroes() throws Exception {
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		for(Tela t: SistemaCtrl.getInstance().getTelas()) {
			String sql = "INSERT INTO tela(codigo, nome, descricao, status) ";
			sql += "VALUES(";
			sql +=  (SistemaCtrl.getInstance().getTelas().indexOf(t) + 1) + ", ";
			sql +=  "'" + t.getNome() + "', ";
			sql +=  "'" + t.getDescricao() + "', ";
			sql +=  "'TRUE' ";
			sql += ") ";
			sql += "ON CONFLICT (codigo) ";
			sql += "DO UPDATE SET ";
			sql += "nome = '" + t.getNome() + "', ";
			sql += "descricao = '" + t.getDescricao() + "', ";
			sql += "status = 'TRUE'";
			st.executeUpdate(sql);
			
//			sql = "INSERT INTO tela_perfilusuario(";
//			sql += "codigo_tela, ";
//			sql += "codigo_perfilusuario, ";
//			sql += "podeInserir, ";
//			sql += "podeAlterar, ";
//			sql += "podeVisualizar, ";
//			sql += "podeDesativar, ";
//			sql += "status) ";
//			sql += "SELECT ";
//			sql += (SistemaCtrl.getInstance().getTelas().indexOf(t) + 1) + ", ";
//			sql += "1, ";
//			sql += "'TRUE', ";
//			sql += "'TRUE', ";
//			sql += "'TRUE', ";
//			sql += "'TRUE', ";
//			sql += "'TRUE' ";
//			sql += "WHERE NOT EXISTS(";
//			sql += 		"SELECT ";
//			sql += 		"codigo_tela, ";
//			sql += 		"codigo_perfilusuario ";
//			sql += 		"FROM tela_perfilusuario ";
//			sql += 		"WHERE ";
//			sql += 		"codigo_tela = " + (SistemaCtrl.getInstance().getTelas().indexOf(t) + 1);
//			sql += 		" AND ";
//			sql += 		"codigo_perfilusuario = 1";
//			sql += ")";
//			st.executeUpdate(sql);
		}
	}
	
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if(SistemaCtrl.getInstance().tabelaExiste("perfilUsuario"))
			throw new Exception("Crie a tabela de perfil de usuario antes de criar a tabela de tela");
		
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "tela(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "nome VARCHAR(100), ";
		sql += "descricao VARCHAR(255), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "tela_perfilusuario(";
		sql += "codigo SERIAL, ";
		sql += "codigo_tela INTEGER REFERENCES tela(codigo), ";
		sql += "codigo_perfilusuario INTEGER REFERENCES perfilusuario(codigo), ";
		sql += "podeInserir BOOLEAN, ";
		sql += "podeAlterar BOOLEAN, ";
		sql += "podeVisualizar BOOLEAN, ";
		sql += "podeDesativar BOOLEAN, ";
		sql += "status boolean, ";
		sql += "PRIMARY KEY(codigo,codigo_perfilusuario,codigo_tela))";
		st.executeUpdate(sql);
	}

	public void salvar(PerfilUsuario perfilUsuario) throws Exception {
		List<Tela> telasInserir = new ArrayList<>();
		List<Tela> telasAlterar = new ArrayList<>();
		for(Tela tela: perfilUsuario.getTelas()) {
			if(tela != null && tela.getCodigo() < 1 && perfilUsuario.getCodigo() < 1) {
				if(existeRelacionamento(tela, perfilUsuario))
					telasInserir.add(tela);
				else
					telasAlterar.add(tela);
			}
		}
		inserir(telasInserir, perfilUsuario.getCodigo());
		alterar(telasAlterar, perfilUsuario.getCodigo());
	}
	
	private boolean existeRelacionamento(Tela tela, PerfilUsuario perfilUsuario) throws Exception {
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        boolean existe = false;
        String sql = "SELECT codigo_tela, codigo_perfilusuario FROM tela WHERE codigo_tela = " + tela.getCodigo() + ", codigo_perfilusuario = " + perfilUsuario.getCodigo();
        PreparedStatement ps = conexao.prepareStatement(sql);
        try {
            ps.setInt(1, perfilUsuario.getCodigo());
            ResultSet rs = ps.executeQuery();
            existe = rs.next();
            ps.close();
        } catch(Exception e) {
        	ps.close();
            existe = false;
        }
		return existe;		
	}

	public void inserir(List<Tela> telas, int codigo_perfilUsuario) throws Exception {
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		String sql= "INSERT INTO tela_perfilusuario(codigo_tela, codigo_perfilusuario, podeInserir, podeAlterar, podeVisualizar, podeDesativar, status) VALUES(?,?,?,?,?,?,?)";
		PreparedStatement ps = conexao.prepareStatement(sql);
        try {
        	int i = 0;
        	for (Tela tela : telas) { 
        		ps.setInt(1, tela.getCodigo());
        		ps.setInt(2, codigo_perfilUsuario);
        		ps.setBoolean(3, tela.isPodeInserir());
        		ps.setBoolean(4, tela.isPodeAlterar());
        		ps.setBoolean(5, tela.isPodeVisualizar());
        		ps.setBoolean(6, tela.isPodeDesativar());
        		ps.setBoolean(7, tela.isStatus());
        		ps.addBatch();
                i++;

                if (i == telas.size()) {
                	ps.executeBatch();
                }
            }
        	ps.executeBatch();
		} finally {
			ps.close();
		}         
	}
	
	public void alterar(List<Tela> telas, int codigo_perfilUsuario) throws Exception {
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			String sql = "UPDATE tela_perfilusuario SET podeInserir=?, podeAlterar=?, podeVisualizar=?, podeDesativar=?, status=? WHERE codigo_tela=? AND codigo_perfilusuario=?";
			PreparedStatement ps = conexao.prepareStatement(sql);
			try {
				int i = 0;
				for (Tela tela : telas) {
					ps.setBoolean(1, tela.isPodeInserir());
					ps.setBoolean(2, tela.isPodeAlterar());
					ps.setBoolean(3, tela.isPodeVisualizar());
					ps.setBoolean(4, tela.isPodeDesativar());
					ps.setBoolean(5, tela.isStatus());
					ps.setInt(6, tela.getCodigo());
					ps.setInt(7, codigo_perfilUsuario);
					ps.addBatch();
					i++;

					if (i == telas.size()) {
						ps.executeBatch();
					}
				}
				ps.executeBatch();
			} finally {
				ps.close();
			} 
		} finally {
			//conexao.close();
		}  
	}

	public List<Tela> getTelas(PerfilUsuario perfilUsuario) throws Exception {
		List<Tela> telas = new ArrayList<>();
        PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        try {
        	String sql = "SELECT ";
        			sql += "tela.codigo, ";
        			sql += "tela.nome, ";
        			sql += "tela.descricao, ";
        			sql += "tela.status, ";
        			sql += "tela_perfilusuario.podeInserir, ";
        			sql += "tela_perfilusuario.podeAlterar, ";
        			sql += "tela_perfilusuario.podeVisualizar, ";
        			sql += "tela_perfilusuario.podeDesativar ";
        			sql += "FROM ";
        			sql += "tela_perfilusuario ";
        			sql += "LEFT JOIN tela ON tela.codigo = tela_perfilusuario.codigo_tela ";
        			sql += "WHERE codigo_perfilusuario = ?";
            ps = conexao.prepareStatement(sql);
            ps.setInt(1, perfilUsuario.getCodigo());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	Tela tela = new Tela();
            	tela.setCodigo(rs.getInt("codigo"));
            	tela.setNome(rs.getString("nome"));
            	tela.setDescricao(rs.getString("descricao"));
            	tela.setPodeInserir(rs.getBoolean("podeInserir"));
            	tela.setPodeAlterar(rs.getBoolean("podeAlterar"));
            	tela.setPodeDesativar(rs.getBoolean("podeDesativar"));
            	tela.setPodeVisualizar(rs.getBoolean("podeVisualizar"));
            	tela.setStatus(rs.getBoolean("status"));
            	telas.add(tela);
            }
            ps.close();
        } finally{
            //conexao.close();
        }
		return telas;
	}
	
//	public List<Tela> getTelas() throws Exception {
//		List<Tela> telas = new ArrayList<>();
//        PreparedStatement ps;
//        Conexao conexao = SistemaCtrl.getInstance().getConexao();
//        try {
//        	String sql = "SELECT ";
//        			sql += "tela.codigo, ";
//        			sql += "tela.nome, ";
//        			sql += "tela.descricao, ";
//        			sql += "tela.status, ";
//        			sql += "FROM ";
//        			sql += "tela";
//            ps = conexao.getConexao().prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()) {
//            	Tela tela = new Tela();
//            	tela.setCodigo(rs.getInt("codigo"));
//            	tela.setNome(rs.getString("nome"));
//            	tela.setDescricao(rs.getString("descricao"));
//            	tela.setStatus(rs.getBoolean("status"));
//            	telas.add(tela);
//            }
//            ps.close();
//        } finally{
//            conexao.getConexao().close();
//        }
//		return telas;
//	}
}
