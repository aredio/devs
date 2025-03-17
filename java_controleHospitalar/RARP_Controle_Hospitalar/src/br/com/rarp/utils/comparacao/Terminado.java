package br.com.rarp.utils.comparacao;

import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.utils.Utilitarios;

public class Terminado implements Comparacao {

	@Override
	public String getComparacao() {
		return " LIKE ";
	}

	@Override
	public String getTermo(String termo) throws Exception {
		if(termo.isEmpty())
			throw new Exception("O termo da consulta é obrigatório");
		return "'%" + Utilitarios.formatStringSQL(termo) + "'";
	}

	@Override
	public String toString() {
		return "Terminado";
	}

}
