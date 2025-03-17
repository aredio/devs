package br.com.rarp.model;

public class Cidade {

	private int codigo;
	private String nome;
	private Estado estado;
	private int codigoIBGE;
	private Boolean status;
	
	public Cidade() {
		// TODO Auto-generated constructor stub
	}
	
	public Cidade(String nome, Estado estado) {
		setNome(nome);
		setEstado(estado);
		setStatus(true);
	}

	public Boolean isStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public int getCodigoIBGE() {
		return codigoIBGE;
	}

	public void setCodigoIBGE(int codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	@Override
	public String toString() {
		String retorno = "";
		if(this.estado != null)
			retorno = (nome != null ? nome : "") + " " + this.estado.getUF();
		else
			retorno = (nome != null ? nome : "") ;
		return retorno;
	}
}
