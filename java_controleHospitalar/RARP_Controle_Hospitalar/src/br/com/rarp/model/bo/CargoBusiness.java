package br.com.rarp.model.bo;

import java.util.List;

import br.com.rarp.model.Cargo;
import br.com.rarp.model.dao.CargoDAO;

public class CargoBusiness {

	public void salvar(Cargo cargo) throws Exception {
		if(cargo == null)
			throw new Exception("A espaço não foi instânciada");
		
		if(cargo.isStatus())
			validarCargo(cargo);
		CargoDAO cargoDAO = new CargoDAO();
		cargoDAO.salvar(cargo);
	}

	private void validarCargo(Cargo cargo) {
		// Valida o cargo aki
	}

	public List<Cargo> consultar(String campo, String comparacao, String termo) throws Exception {
		CargoDAO cargoDAO = new CargoDAO();
		return cargoDAO.consultar(campo, comparacao, termo);
	}

}
