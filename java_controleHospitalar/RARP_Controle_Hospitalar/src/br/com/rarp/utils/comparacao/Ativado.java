package br.com.rarp.utils.comparacao;

import br.com.rarp.interfaces.Comparacao;

public class Ativado implements Comparacao {

	@Override
	public String getComparacao() {
		return " = ";
	}

	@Override
	public String getTermo(String termo) {
		if(termo.toLowerCase().equals("ativado"))
			return "TRUE";
		else
			return "FALSE";
	}
	
	@Override
	public String toString() {
		return "Igual";
	}

}
