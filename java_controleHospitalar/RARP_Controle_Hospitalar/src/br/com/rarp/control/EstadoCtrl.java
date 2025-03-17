package br.com.rarp.control;

import java.sql.SQLException;

import br.com.rarp.model.Estado;
import br.com.rarp.model.bo.EstadoBusiness;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EstadoCtrl {
	
	public ObservableList<Estado> getEstados() throws SQLException, Exception {
		return FXCollections.observableList(new EstadoBusiness().consultar("codigo", " > ", "0"));
	}

	public boolean salvar() throws Exception {
		return false;
	
	}

}
