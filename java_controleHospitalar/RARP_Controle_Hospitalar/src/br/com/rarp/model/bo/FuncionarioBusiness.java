package br.com.rarp.model.bo;

import java.time.LocalDate;
import java.util.List;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.dao.FuncionarioDAO;
import br.com.rarp.utils.Utilitarios;

public class FuncionarioBusiness {

	public void salvar(Funcionario funcionario) throws Exception {
		if(funcionario == null)
			throw new Exception("O funcionário não foi instânciado");
		
		if(funcionario.isStatus())
			validarFuncionario(funcionario);
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		funcionarioDAO.salvar(funcionario);
	}

	private void validarFuncionario(Funcionario funcionario) throws Exception {
		if(funcionario.getDtNascimento() != null && funcionario.getDtNascimento().isAfter(LocalDate.now()))
			throw new Exception("A data informada deve ser menor que a data atual");
		
		if(!Utilitarios.isCPF(funcionario.getCpfSemMascara()))
			throw new Exception("CPF inválido");
	}

	public List<Funcionario> consultar(String campo, String comparacao, String termo) throws Exception {
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		return funcionarioDAO.consultar(campo, comparacao, termo);
	}
}
