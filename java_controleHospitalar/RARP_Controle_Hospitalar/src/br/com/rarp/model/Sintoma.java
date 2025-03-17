package br.com.rarp.model;

public class Sintoma {
	private int codigo;
	private String descricao;
	
	public Sintoma(String text) {
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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return descricao;
	}
}
