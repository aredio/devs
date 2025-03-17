package br.com.rarp.model.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Propriedades {
	private static final Propriedades INSTANCE = new Propriedades();
	private TypedProperties prop;
	private String url;
	private String database;
	private String user;
	private String password;
	private String lastUsername;
	
	private String host;
	private String porta;
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	@SuppressWarnings("resource")
	private Propriedades() {
		prop = new TypedProperties();
		try {
			 File file = new File("./properties/RARP.Properties");

		        if(!file.exists()){

		        	file.createNewFile();

		        }
		       
			prop.load(new FileInputStream("./properties/RARP.Properties"));
			getPropriedades();
		} catch (FileNotFoundException e) {
			try {
				new BufferedWriter(new FileWriter("./properties/RARP.Properties")).write("");
			} catch (IOException e1) {
				System.out.print("Erro ao criar arquivo de propriedades");
			}
			try {
				prop.load(new FileInputStream("./properties/RARP.Properties"));
			} catch (IOException e1) {
				System.out.print("Erro ao criar arquivo de propriedades");
			}
		} catch (IOException e) {
			System.out.print("Erro ao criar arquivo de propriedades");
		}
	}

	public static Propriedades getInstance() {
		return INSTANCE;
	}

	public void getPropriedades() {	
		database = prop.getProperty("conexao.database", "rarp");
		user = prop.getProperty("conexao.user", "postgres");
		password = prop.getProperty("conexao.password", "");

		lastUsername = prop.getProperty("login.lastUsername", "");
		host = prop.getProperty("conexao.host", "localhost");
		porta= prop.getProperty("conexao.porta", "5432");
		url = "jdbc:postgresql://"+host+":"+porta+"/";
	}

	public void setPropriedades() {
		//Propriedades da conex�o
		prop.setProperty("conexao.database", database);
		prop.setProperty("conexao.user", user);
		prop.setProperty("conexao.password", password);
		prop.setProperty("conexao.host", host);
		prop.setProperty("conexao.porta", porta);
		 
		//Propriedades do login
		prop.setProperty("login.lastUsername", lastUsername);
		
		//Opções do sistema

		 
		try {
			prop.store(new FileOutputStream("./properties/RARP.Properties"), "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TypedProperties getProp() {
		return prop;
	}

	public void setProp(TypedProperties prop) {
		this.prop = prop;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastUsername() {
		return lastUsername;
	}

	public void setLastUsername(String lastUsername) {
		this.lastUsername = lastUsername;
	}
	
}
