package br.com.rarp.control;

import br.com.rarp.enums.Funcao;
import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.bo.FuncionarioBusiness;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FuncionarioCtrl {
	private Funcionario funcionario;

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Object object) {
		this.funcionario = (Funcionario) object;
	}

	public boolean salvar() throws Exception {
		if (funcionario == null)
			throw new Exception("O funcionário não foi instânciado");
		
		if (confirmarDesativacao()) {
			if(funcionario.isStatus())
				validarDadosObrigatorios();
			FuncionarioBusiness funcionarioBusiness = new FuncionarioBusiness();
			funcionarioBusiness.salvar(funcionario);
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<Funcionario> consultar(Campo campo, Comparacao comparacao, String termo) throws Exception {
		FuncionarioBusiness funcionarioBusiness = new FuncionarioBusiness();
		return FXCollections.observableList(
				funcionarioBusiness.consultar(campo.getNome(), comparacao.getComparacao(), comparacao.getTermo(termo)));
	}

	private void validarDadosObrigatorios() throws Exception {
		if (funcionario != null) {
			if (funcionario.getNome().isEmpty()) {
				throw new Exception("Para cadastrar um funcionário é necessário informar o nome");
			}
			if (funcionario.getCpf().isEmpty()) {
				throw new Exception("Para cadastrar um funcionário é necessário informar o CPF");
			}
			if (funcionario.getCargo() == null) {
				throw new Exception("Para cadastrar um funcionário é necessário informar o cargo");
			} 
		}
	}
	
	private boolean confirmarDesativacao() {
		if(funcionario != null && !funcionario.isStatus())
			return Utilitarios.pergunta("Tem certeza que você deseja desativar este funcionário?");
		return true;
	}

	public void novoFuncionario() {
		funcionario = new Funcionario();
	}
	
	public ObservableList<Funcionario> getFuncionarios() throws Exception {
			FuncionarioBusiness funcionarioBusiness = new FuncionarioBusiness();
			return FXCollections.observableList(
					funcionarioBusiness.consultar("FUNC.codigo", " > ", "0"));
	}

	public ObservableList<Funcionario> getFuncionarios(Funcao funcao) throws Exception {
		FuncionarioBusiness funcionarioBusiness = new FuncionarioBusiness();
		return FXCollections.observableList(
				funcionarioBusiness.consultar("CA.funcao", " = ", "'" + funcao.toString() + "'"));
	}
}
