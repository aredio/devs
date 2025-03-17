package br.com.rarp.model;

import br.com.rarp.annotations.IgnorarField;

public class Configuracoes {
	@IgnorarField
	private static Configuracoes INSTANCE = new Configuracoes();
	private boolean controleAcesso;
	private String usuario;
	private String senha;
	private long codigoRARP;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	private Configuracoes() {

	}

	public boolean isControleAcesso() {
		return controleAcesso;
	}

	public void setControleAcesso(boolean controleAcesso) {
		this.controleAcesso = controleAcesso;
	}

	public static Configuracoes getInstance() {
		return INSTANCE;
	}

	public long getCodigoRARP() {
		return codigoRARP;
	}

	public void setCodigoRARP(long codigoRARP) {
		this.codigoRARP = codigoRARP;
	}
	
	public org.com.rarp.interfaces.Usuario getUsuarioRARP() {
		org.com.rarp.interfaces.Usuario usuario = new org.com.rarp.interfaces.Usuario();
		
		usuario.setCodigo(codigoRARP);
		usuario.setNome(this.usuario);
		usuario.setSenha(senha);
		
		return usuario;
	}

}
