package br.com.rarp.model;

import br.com.rarp.enums.Funcao;

public class Cargo {

	private int codigo;
	private String nome;
	private Funcao funcao;
	private String requisitos;
	private String nivel;
	private boolean status;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

	public String getRequisitos() {
		return requisitos;
	}

	public void setRequisitos(String requisitos) {
		this.requisitos = requisitos;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	
	@Override
	public boolean equals(Object obj) {
		try {
			return codigo == ((Cargo) obj).getCodigo();
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return nome + (nivel.isEmpty() ? "": " nivel: " + nivel);
	}

	public Funcao getFuncao() {
		return funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

}
