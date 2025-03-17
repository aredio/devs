package br.com.rarp.control;

import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.model.Especialidade;
import br.com.rarp.model.bo.EspecialidadeBusiness;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EspecialidadeCtrl {
	private Especialidade especialidade;

	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Object especialida) {
		this.especialidade = (Especialidade) especialida;
	}
	
	private void validaCamposObrigatorios() throws Exception {
		if (especialidade == null )
			novaEspecialidade();
		
		if (especialidade.getNome().isEmpty()) {
			throw new Exception("Para cadastrar uma especialidade é necessário informar um nome");
		}
	}
	
	public boolean salvar() throws Exception {
		if (especialidade == null)
			throw new Exception("A especialidade não foi instânciada");
		
		if (confirmarDesativacao()) {
			if(especialidade.isStatus())
				validaCamposObrigatorios();
			EspecialidadeBusiness especialidadeBusiness = new EspecialidadeBusiness();
			especialidadeBusiness.salvar(especialidade);
			return true;
		} else {
			return false;
		}	
	}
	
	private boolean confirmarDesativacao() {
		if(especialidade != null && !especialidade.isStatus())
			return Utilitarios.pergunta("Tem certeza que você deseja desativar este Especialidade ?");
		return true;
	}

	public void novaEspecialidade() {
		this.especialidade  = new Especialidade();
	}


	public ObservableList<Especialidade> consultar(Campo campo, Comparacao comparacao, String termo) throws Exception {
		// TODO Auto-generated method stub
		
		EspecialidadeBusiness especialidadeBusiness = new EspecialidadeBusiness();
		return FXCollections.observableArrayList(especialidadeBusiness.consultar(campo.getNome(), comparacao.getComparacao(), comparacao.getTermo(termo)));
	}
	


	public ObservableList<Especialidade> getEspecialidades() throws Exception {
		// TODO Auto-generated method stub
		
		EspecialidadeBusiness especialidadeBusiness = new EspecialidadeBusiness();
		return FXCollections.observableArrayList(especialidadeBusiness.consultar("codigo"," > "," 0 "));
	}

}
