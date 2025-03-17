package br.com.rarp.model;

public class ReceitaMedica {

	private int codigo;
	private String descricao;

	public ReceitaMedica(String text) {
		descricao = text;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
