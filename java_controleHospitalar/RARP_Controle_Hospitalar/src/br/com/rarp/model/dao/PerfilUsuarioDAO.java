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

public class PerfilUsuarioDAO {
	public static void criarRegistrosPadroes() throws ClassNotFoundException, SQLException, Exception {
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "INSERT INTO perfilusuario(codigo, nome, status) "
				+ "VALUES(1, 'Administrador', 'TRUE') "
				+ "ON CONFLICT (codigo) "
				+ "DO UPDATE SET "
				+ "codigo = 1, "
				+ "nome = 'Administrador', "
				+ "status = 'TRUE' ";
		st.executeUpdate(sql);
		
		for(Tela t: SistemaCtrl.getInstance().getTelas()) {
			sql = "INSERT INTO tela_perfilusuario("
					+ "codigo_tela, "
					+ "codigo_perfilusuario, "
					+ "podeInserir, "
					+ "podeAlterar, "
					+ "podeVisualizar, "
					+ "podeDesativar, "
					+ "status) "
					+ "VALUES("
					+ t.getCodigo() + ", "
					+ "1, "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE') "
					+ "ON CONFLICT (codigo,codigo_perfilusuario,codigo_tela) "
					+ "DO UPDATE SET "
					+ "codigo_tela = " + t.getCodigo() + ", "
					+ "codigo_perfilusuario = 1, "
					+ "podeInserir = 'TRUE', "
					+ "podeAlterar = 'TRUE', "
					+ "podeVisualizar = 'TRUE', "
					+ "podeDesativar = 'TRUE', "
					+ "status  = 'TRUE' ";
			st.executeUpdate(sql);
		}
		
		sql = "INSERT INTO perfilusuario(codigo, nome, status) "
				+ "VALUES(2, 'Atendente', 'TRUE') "
				+ "ON CONFLICT (codigo) "
				+ "DO UPDATE SET "
				+ "codigo = 2, "
				+ "nome = 'Atendente', "
				+ "status = 'TRUE' ";
		st.executeUpdate(sql);
		
		for(Tela t: SistemaCtrl.getInstance().getTelas()) {
			if(t.getCodigo() != 14
					&& t.getCodigo() != 7
					&& t.getCodigo() != 11
					&& t.getCodigo() != 13
					&& t.getCodigo() != 12)
				continue;
			
			sql = "INSERT INTO tela_perfilusuario("
					+ "codigo_tela, "
					+ "codigo_perfilusuario, "
					+ "podeInserir, "
					+ "podeAlterar, "
					+ "podeVisualizar, "
					+ "podeDesativar, "
					+ "status) "
					+ "VALUES("
					+ t.getCodigo() + ", "
					+ "2, "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE') "
					+ "ON CONFLICT (codigo,codigo_perfilusuario,codigo_tela) "
					+ "DO UPDATE SET "
					+ "codigo_tela = " + t.getCodigo() + ", "
					+ "codigo_perfilusuario = 2, "
					+ "podeInserir = 'TRUE', "
					+ "podeAlterar = 'TRUE', "
					+ "podeVisualizar = 'TRUE', "
					+ "podeDesativar = 'TRUE', "
					+ "status  = 'TRUE' ";
			st.executeUpdate(sql);
		}

		sql = "INSERT INTO perfilusuario(codigo, nome, status) "
				+ "VALUES(3, 'Médico', 'TRUE') "
				+ "ON CONFLICT (codigo) "
				+ "DO UPDATE SET "
				+ "codigo = 3, "
				+ "nome = 'Médico', "
				+ "status = 'TRUE' ";
		st.executeUpdate(sql);
		
		for(Tela t: SistemaCtrl.getInstance().getTelas()) {
			if(t.getCodigo() != 14
					&& t.getCodigo() != 7
					&& t.getCodigo() != 11
					&& t.getCodigo() != 13
					&& t.getCodigo() != 12
					&& t.getCodigo() != 17)
				continue;
			
			sql = "INSERT INTO tela_perfilusuario("
					+ "codigo_tela, "
					+ "codigo_perfilusuario, "
					+ "podeInserir, "
					+ "podeAlterar, "
					+ "podeVisualizar, "
					+ "podeDesativar, "
					+ "status) "
					+ "VALUES("
					+ t.getCodigo() + ", "
					+ "3, "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE') "
					+ "ON CONFLICT (codigo,codigo_perfilusuario,codigo_tela) "
					+ "DO UPDATE SET "
					+ "codigo_tela = " + t.getCodigo() + ", "
					+ "codigo_perfilusuario = 3, "
					+ "podeInserir = 'TRUE', "
					+ "podeAlterar = 'TRUE', "
					+ "podeVisualizar = 'TRUE', "
					+ "podeDesativar = 'TRUE', "
					+ "status  = 'TRUE' ";
			st.executeUpdate(sql);
		}
		
		sql = "INSERT INTO perfilusuario(codigo, nome, status) "
				+ "VALUES(4, 'Funcionário de Limpeza', 'TRUE') "
				+ "ON CONFLICT (codigo) "
				+ "DO UPDATE SET "
				+ "codigo = 4, "
				+ "nome = 'Funcionário de Limpeza', "
				+ "status = 'TRUE' ";
		st.executeUpdate(sql);
		
		for(Tela t: SistemaCtrl.getInstance().getTelas()) {
			if(t.getCodigo() != 15)
				continue;
			
			sql = "INSERT INTO tela_perfilusuario("
					+ "codigo_tela, "
					+ "codigo_perfilusuario, "
					+ "podeInserir, "
					+ "podeAlterar, "
					+ "podeVisualizar, "
					+ "podeDesativar, "
					+ "status) "
					+ "VALUES("
					+ t.getCodigo() + ", "
					+ "4, "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE', "
					+ "'TRUE') "
					+ "ON CONFLICT (codigo,codigo_perfilusuario,codigo_tela) "
					+ "DO UPDATE SET "
					+ "codigo_tela = " + t.getCodigo() + ", "
					+ "codigo_perfilusuario = 4, "
					+ "podeInserir = 'TRUE', "
					+ "podeAlterar = 'TRUE', "
					+ "podeVisualizar = 'TRUE', "
					+ "podeDesativar = 'TRUE', "
					+ "status  = 'TRUE' ";
			st.executeUpdate(sql);
		}
	}
	
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {			
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "perfilUsuario(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "nome VARCHAR(100), ";
		sql += "status boolean)";
		st.executeUpdate(sql);
	}
	
	public PerfilUsuario consultar(int codigo) throws SQLException, Exception {
		List<PerfilUsuario> perfilUsuarios =  consultar("codigo", " = ", codigo + "");
		if(perfilUsuarios.size() > 0)
			return perfilUsuarios.get(0);
		else
			return null;
	}
	
	public List<PerfilUsuario> consultar(String campo, String comparacao, String termo) throws SQLException, Exception {
		List<PerfilUsuario> perfilUsuarios = new ArrayList<>();
        PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        try {
        	String sql = "SELECT codigo, nome, status FROM perfilusuario WHERE " + campo + comparacao + termo;
            ps = conexao.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	PerfilUsuario perfilUsuario = new PerfilUsuario();
            	perfilUsuario.setCodigo(rs.getInt("codigo"));
            	perfilUsuario.setNome(rs.getString("nome"));
            	perfilUsuario.setStatus(rs.getBoolean("status"));
            	perfilUsuario.setTelas(new TelaDAO().getTelas(perfilUsuario));
            	perfilUsuarios.add(perfilUsuario);
            }
            ps.close();
        } finally{
            //conexao.close();
        }
		return perfilUsuarios;
	}

	public void salvar(PerfilUsuario perfilUsuario) throws Exception {
		if(perfilUsuario != null) 
			if(perfilUsuario.getCodigo() == 0)
				inserir(perfilUsuario);
			else
				alterar(perfilUsuario);
	}
	
	private void inserir(PerfilUsuario perfilUsuario) throws Exception {
        PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        try {
            String sql= "INSERT INTO perfilusuario(nome, status) VALUES(?,?)";
            ps = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, perfilUsuario.getNome());
            ps.setBoolean(2, perfilUsuario.isStatus());            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				perfilUsuario.setCodigo(rs.getInt("codigo"));
			ps.close();
            
            TelaDAO telaDAO = new TelaDAO();
            telaDAO.salvar(perfilUsuario);
            ps.close();
        } finally{
            //conexao.close();
        }
	}
	
	private void alterar(PerfilUsuario perfilUsuario) throws Exception {
        PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        try {
            String sql= "UPDATE perfilusuario SET nome=?, status=? WHERE codigo=?";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, perfilUsuario.getNome());
            ps.setBoolean(2, perfilUsuario.isStatus());
            ps.setInt(3, perfilUsuario.getCodigo());
            ps.executeUpdate();   
            TelaDAO telaDAO = new TelaDAO();
            telaDAO.salvar(perfilUsuario);
            ps.close();
        } finally{
            //conexao.close();
        }		
	}
	
}
