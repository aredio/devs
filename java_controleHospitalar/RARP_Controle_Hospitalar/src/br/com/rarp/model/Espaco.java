package br.com.rarp.model;

import java.util.ArrayList;
import java.util.List;

public class Espaco {

	private int codigo;
	private String nome;
	private String bloco;
	private String andar;
	private List<Leito> leitos = new ArrayList<Leito>();
	private boolean status;
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getBloco() {
		return bloco;
	}
	public void setBloco(String bloco) {
		this.bloco = bloco;
	}
	public String getAndar() {
		return andar;
	}
	public void setAndar(String andar) {
		this.andar = andar;
	}
	public List<Leito> getLeitos() {
		return leitos;
	}
	public void setLeitos(List<Leito> leitos) {
		this.leitos = leitos;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Espaco)
			return ((Espaco) obj).getCodigo() == codigo;
		else
			return false;
	}

}
