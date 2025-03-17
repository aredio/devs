package br.com.rarp.model.bo;

import java.sql.SQLException;
import java.util.List;
import br.com.rarp.model.Estado;
import br.com.rarp.model.dao.EstadoDAO;

public class EstadoBusiness {

	public void salvar(Estado estado) throws Exception {
	
	}

	public List<Estado> consultar(String campo, String comparacao, String termo) throws SQLException, Exception {
		return new EstadoDAO().consultar(campo, comparacao, termo);
	}

}
