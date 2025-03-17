package br.com.rarp.model.bo;

import java.util.List;

import br.com.rarp.model.Especialidade;
import br.com.rarp.model.dao.EspecialidadeDAO;

public class EspecialidadeBusiness {

	public void salvar(Especialidade especialidade) throws Exception {
		if(especialidade == null)
			throw new Exception("A especialidade não foi instânciada");
		
		if(especialidade.isStatus())
			validarEspecialidade(especialidade);
		EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();
		especialidadeDAO.salvar(especialidade);
	}

	private void validarEspecialidade(Especialidade especialidade) {
		//Validar a especialidade
	}

	public List<Especialidade> consultar(String campo, String comparacao, String termo) throws Exception {
		EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();
		return especialidadeDAO.consultar(campo, comparacao, termo);
	}

}
