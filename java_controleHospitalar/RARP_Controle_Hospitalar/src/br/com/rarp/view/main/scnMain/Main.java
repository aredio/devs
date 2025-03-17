package br.com.rarp.view.main.scnMain;

import br.com.rarp.utils.Utilitarios;

public class Main {
	
	public static void main(String[] args) {
		try {			
			MainController.abrir();
		} catch (Exception e) {
			e.printStackTrace();
			Utilitarios.erro("Erro ao inicializar a aplicação. "
					+ e.getMessage()
					+ "\n\tEntre em contato com o grupo RARP telefone: (62)"
					+ " 98526-4619\\ (62) 98548-3271");		
		}
	}
	

}
