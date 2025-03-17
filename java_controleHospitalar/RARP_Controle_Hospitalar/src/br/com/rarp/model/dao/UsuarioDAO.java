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
import br.com.rarp.model.Usuario;

public class UsuarioDAO {

	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
		if (!SistemaCtrl.getInstance().tabelaExiste("funcionario"))
			throw new Exception("Crie a tabela de funcionarios antes de criar a tabela de usuarios");

		if (!SistemaCtrl.getInstance().tabelaExiste("perfilusuario"))
			throw new Exception("Crie a tabela de perfil de usuarios antes de criar a tabela de usuarios");

		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "usuario(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "nome VARCHAR(200), ";
		sql += "usuario VARCHAR(100) NOT NULL UNIQUE, ";
		sql += "password INTEGER, ";
		sql += "codigo_funcionario INTEGER REFERENCES funcionario(codigo), ";
		sql += "codigo_perfilusuario Integer REFERENCES perfilusuario(codigo), ";
		sql += "status BOOLEAN)";
		st.executeUpdate(sql);
	}

	public void salvar(Usuario usuario) throws Exception {
		if (usuario.getCodigo() == 0)
			inserir(usuario);
		else
			alterar(usuario);
	}

	private void inserir(Usuario usuario) throws Exception {
		PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        try {
        	String sql= "INSERT INTO usuario(nome, usuario, password, codigo_funcionario, codigo_perfilusuario, status) VALUES(?,?,?,?,?,?)";
            ps = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getUsuario());
            ps.setInt(3, usuario.getSenha());

            if(usuario.getFuncionario() != null)
            	ps.setInt(4, usuario.getFuncionario().getCodigo());
            else
            	ps.setNull(4, Types.INTEGER);
            
            if(usuario.getPerfilUsuario() != null)
            	ps.setInt(5, usuario.getPerfilUsuario().getCodigo());
            else
            	ps.setNull(5, Types.INTEGER);
            
            ps.setBoolean(6, usuario.isStatus());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				usuario.setCodigo(rs.getInt(1));
			ps.close();
        } finally {
        	//conexao.close();
		}
	}

	private void alterar(Usuario usuario) throws Exception {
		PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        try {
        	String sql= "Update usuario SET nome=?, usuario=?, password=?, codigo_funcionario=?, codigo_perfilusuario=?, status=? WHERE codigo=?";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getUsuario());
            ps.setInt(3, usuario.getSenha());
            if(usuario.getFuncionario() != null)
            	ps.setInt(4, usuario.getFuncionario().getCodigo());
            else
            	ps.setNull(4, Types.INTEGER);
            
            if(usuario.getPerfilUsuario() != null)
            	ps.setInt(5, usuario.getPerfilUsuario().getCodigo());
            else
            	ps.setNull(5, Types.INTEGER);
            ps.setBoolean(6, usuario.isStatus());
            ps.setInt(7, usuario.getCodigo());
            ps.executeUpdate();
            ps.close();
        } finally {
        	//conexao.close();
		}
	}

	public List<Usuario> consultar(Connection conexao, String campo, String comparacao, String termo) throws Exception {
		List<Usuario> usuarios = new ArrayList<>();
        PreparedStatement ps;      
    	String sql = "SELECT usuario.codigo, usuario.nome, usuario.usuario, usuario.password, usuario.codigo_funcionario, usuario.codigo_perfilusuario as cf , usuario.status FROM usuario WHERE " + campo + comparacao + termo;
        ps = conexao.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
        	Usuario usuario = new Usuario();
        	usuario.setCodigo(rs.getInt("codigo"));
        	usuario.setNome(rs.getString("nome"));
        	usuario.setUsuario(rs.getString("usuario"));
        	usuario.setSenha(rs.getInt("password"));
        	
        	PerfilUsuarioDAO perfilUsuarioDAO = new PerfilUsuarioDAO();
        	usuario.setPerfilUsuario(perfilUsuarioDAO.consultar(rs.getInt("cf")));
        	
        	FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        	usuario.setFuncionario(funcionarioDAO.getFuncionario(rs.getInt("codigo_funcionario")));
        			
        	usuario.setStatus(rs.getBoolean("status"));
        	usuarios.add(usuario);
        }
        ps.close();
		return usuarios;
	}

	public Usuario getUsuario(Connection conexao, int codigo) throws Exception {
		if (codigo > 0) {
			List<Usuario> usuarios = consultar(conexao, "codigo", " = ", "" + codigo);
			return usuarios.get(0);
		}
		return null;
	}

}
