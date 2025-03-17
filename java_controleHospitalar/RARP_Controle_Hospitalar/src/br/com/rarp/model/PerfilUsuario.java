package br.com.rarp.model;

import java.util.ArrayList;
import java.util.List;

public class PerfilUsuario {

	private int codigo;
	private String nome;
	private List<Tela> telas;
	private boolean status;
	
	public PerfilUsuario() {
		telas = new ArrayList<>();
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
	public List<Tela> getTelas() {
		return telas;
	}
	public void setTelas(List<Tela> telas) {
		this.telas = telas;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return nome;
	}

}
