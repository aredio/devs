package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.rarp.control.SistemaCtrl;

public class Conexao {
    private Connection conecta;

    public Connection getConexao() throws ClassNotFoundException, SQLException {    	
    	if (conecta != null && !conecta.isClosed())
    		return conecta;
    	
    	
    	String url = SistemaCtrl.getInstance().getPropriedades().getUrl();
    	String baseDados = SistemaCtrl.getInstance().getPropriedades().getDatabase();
    	String usuario = SistemaCtrl.getInstance().getPropriedades().getUser();
    	String senha = SistemaCtrl.getInstance().getPropriedades().getPassword();
    	Class.forName("org.postgresql.Driver");
        conecta = DriverManager.getConnection(url + baseDados, usuario, senha);
        return conecta;
    }

	public void criarDataBase() throws Exception {
    	String url = SistemaCtrl.getInstance().getPropriedades().getUrl();
    	String baseDados = SistemaCtrl.getInstance().getPropriedades().getDatabase();
    	String usuario = SistemaCtrl.getInstance().getPropriedades().getUser();
    	String senha = SistemaCtrl.getInstance().getPropriedades().getPassword();
		Class.forName("org.postgresql.Driver");
        conecta = DriverManager.getConnection(url, usuario, senha);
        Statement stmt = conecta.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT datname FROM pg_database");
        boolean existe = false;
        
        while(rs.next()) {
        	String name = rs.getString(1);
        	if(name == baseDados) {
        		existe = true;
        		break;
        	}
        }
        
        if (!existe)
        	stmt.executeUpdate("CREATE DATABASE " + baseDados);
	}
}
