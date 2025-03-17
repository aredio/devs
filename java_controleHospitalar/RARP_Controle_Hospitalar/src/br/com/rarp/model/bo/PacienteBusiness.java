package br.com.rarp.model.bo;

import java.time.LocalDate;
import java.util.List;

import br.com.rarp.model.Paciente;
import br.com.rarp.model.dao.PacienteDAO;
import br.com.rarp.utils.Utilitarios;

public class PacienteBusiness {

	public List<Paciente> consultar(String campo, String comparacao, String termo) throws Exception {
		PacienteDAO pacienteDAO = new PacienteDAO();
		return pacienteDAO.consultar(campo, comparacao, termo);
	}

	public void salvar(Paciente paciente) throws Exception {
		if(paciente == null)
			throw new Exception("O paciente não foi instânciado");
		
		if(paciente.isStatus())
			validarPaciente(paciente);
		PacienteDAO pacienteDAO = new PacienteDAO();
		pacienteDAO.salvar(paciente);
	}

	private void validarPaciente(Paciente paciente) throws Exception {
		if(paciente.getDtNascimento().isAfter(LocalDate.now()))
			throw new Exception("A data informada deve ser menor que a data atual");
		
		if (!Utilitarios.isMaiorIdade(paciente.getDtNascimento()) && paciente.getResponsavel() == null) {
			throw new Exception("Para cadastrar um paciente menor que 18 anos é necessário informar o responsável");
		}
	}

	public List<Paciente> getPacientesSemResponsavel() throws Exception {
		return new PacienteDAO().getPacientesSemResponsavel();
	}

}
