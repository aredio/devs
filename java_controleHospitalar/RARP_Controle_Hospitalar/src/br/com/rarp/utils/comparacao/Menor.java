package br.com.rarp.utils.comparacao;

import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.utils.Utilitarios;

public class Menor implements Comparacao {

	@Override
	public String getComparacao() {
		return " < ";
	}

	@Override
	public String getTermo(String termo) throws Exception {
		if(termo.isEmpty())
			throw new Exception("O termo da consulta é obrigatorio");
		return Utilitarios.formatStringSQL(termo);
	}
	
	@Override
	public String toString() {
		return "Menor";
	}

}
