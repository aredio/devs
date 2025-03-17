package br.com.rarp.control;

import javafx.scene.control.Alert;

public class Wai {
	Alert alert = new Alert(null);

	public Wai() {
		// TODO Auto-generated constructor stub
		alert.setTitle("Procurando");
        alert.setHeaderText("Aguarde");
	}
	
	public void abrir() {
		alert.show();
	}
	public void  fechar() {
		if (alert != null)
			alert.close();
		
		alert = null;
	}
}
