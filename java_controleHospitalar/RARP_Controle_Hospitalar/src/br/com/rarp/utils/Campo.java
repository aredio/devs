package br.com.rarp.utils;

import br.com.rarp.enums.TipoCampo;

public class Campo  {
	
	private String nome;
	private String descricao;
	private TipoCampo tipo;
	
	public Campo(String nome, String descricao, TipoCampo tipo) {
		this.nome = nome;
		this.descricao = descricao;
		this.setTipo(tipo);
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public TipoCampo getTipo() {
		return tipo;
	}

	public void setTipo(TipoCampo tipo) {
		this.tipo = tipo;
	}

}
