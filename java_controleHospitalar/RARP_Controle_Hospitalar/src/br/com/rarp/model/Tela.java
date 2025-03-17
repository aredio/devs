package br.com.rarp.model;

public class Tela {

	private int codigo;
	private String nome;
	private String descricao;
	private boolean podeInserir;
	private boolean podeAlterar;
	private boolean podeVisualizar;
	private boolean podeDesativar;
	private boolean status;
	
	public Tela() {}
	
	public Tela(int codigo, String nome, String descricao) {
		this.codigo = codigo;
		this.nome = nome;
		this.descricao = descricao;
	}
	
	public Tela(String nome) {
		this.nome = nome;
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
	public boolean isPodeInserir() {
		return podeInserir;
	}
	public void setPodeInserir(boolean podeInserir) {
		this.podeInserir = podeInserir;
	}
	public boolean isPodeAlterar() {
		return podeAlterar;
	}
	public void setPodeAlterar(boolean podeAlterar) {
		this.podeAlterar = podeAlterar;
	}
	public boolean isPodeVisualizar() {
		return podeVisualizar;
	}
	public void setPodeVisualizar(boolean podeVisualizar) {
		this.podeVisualizar = podeVisualizar;
	}
	public boolean isPodeDesativar() {
		return podeDesativar;
	}
	public void setPodeDesativar(boolean podeDesativar) {
		this.podeDesativar = podeDesativar;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return descricao;
	}
	
	@Override
	public boolean equals(Object obj) {
		try {
			Tela tela = (Tela) obj;
			return tela.getNome().equals(this.getNome());
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
