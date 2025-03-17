package br.com.rarp.model;

import java.util.List;

public class FuncionarioLimpeza extends Funcionario {

	private List<Limpeza> limpeza;

	public List<Limpeza> getLimpeza() {
		return limpeza;
	}

	public void setLimpeza(List<Limpeza> limpeza) {
		this.limpeza = limpeza;
	}

	
}
