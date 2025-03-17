package br.com.rarp.control;

import java.util.List;

import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.model.Convenio;
import br.com.rarp.model.bo.ConvenioBusiness;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConvenioCtrl {
	
	private Convenio convenio;

	public ObservableList<Convenio> consultar(Campo campo, Comparacao comparacao, String termo) throws Exception {
		ConvenioBusiness convenioBusiness = new ConvenioBusiness();
		return FXCollections.observableList(
				convenioBusiness.consultar(campo.getNome(), comparacao.getComparacao(), comparacao.getTermo(termo)));
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Object convenio) {
		this.convenio = (Convenio) convenio;
	}

	public void novoConvenio() {
		convenio = new Convenio();
	}

	public boolean salvar() throws Exception {
		if(convenio == null)
			throw new Exception("O convênio não foi instânciado");
		
		if (confirmarDesativacao()) {
			ConvenioBusiness convenioBusiness = new ConvenioBusiness();
			if(convenio.isStatus())
				validarDadosObrigatorios();
			convenioBusiness.salvar(convenio);
			return true;
		} else {
			return false;
		}
	}

	private void validarDadosObrigatorios() throws Exception {
		if (convenio == null) 
			novoConvenio();

		if (convenio.getNome().isEmpty()) 
			throw new Exception("Para cadastrar um paciente é necessário informar o nome");
			
		if (convenio.getCnpj().isEmpty()) 
			throw new Exception("Para cadastrar um paciente é necessário informar o CNPJ");
		
		if (convenio.getANS().isEmpty()) 
			throw new Exception("Para cadastrar um paciente é necessário informar o codigo de registro da ANS");
		
		if (convenio.getTipo() == -1)
			throw new Exception("Para cadastrar um paciente é necessário informar o tipo do convênio");
	}
	
	private boolean confirmarDesativacao() {
		if(convenio != null && !convenio.isStatus())
			return Utilitarios.pergunta("Tem certeza que você deseja desativar este conênio?");
		return true;
	}

	public List<Convenio> getConvenios() throws Exception {
		ConvenioBusiness convenioBusiness = new ConvenioBusiness();
		return convenioBusiness.consultar("conv.status", " = ", "TRUE");
	}

}
