package br.com.rarp.model;

public class Estado {

	private int codigo;
	private String nome;
	private String UF;
	
	public Estado(String nome, String sigla) {
		setNome(nome);
		setUF(sigla);
	}

	public Estado() {
		// TODO Auto-generated constructor stub
	}

	public boolean equals(Object estado) {
		if(estado != null && estado instanceof Estado)
			return getUF().equals(((Estado) estado).getUF());
		return false;
	}
	
	public boolean equals(Estado estado) {
		return this.codigo ==  estado.getCodigo();
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getUF() {
		return UF;
	}
	public void setUF(String uF) {
		UF = uF;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	@Override
	public String toString() {
		return UF ;
	}
	
}
