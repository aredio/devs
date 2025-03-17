package br.com.rarp.model.bo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.SaidaPaciente;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.dao.SaidaPacienteDAO;

public class SaidaPacienteBusiness {

	public void salvar(SaidaPaciente saidaPaciente) throws Exception {
		if(saidaPaciente == null)
			throw new Exception("A saída de paciente não foi instânciada");
		
		if(saidaPaciente.isStatus())
			validarSaidaPaciente(saidaPaciente);
		else
			saidaPaciente.setEntradaPaciente(null);
	
		SaidaPacienteDAO saidaPacienteDAO = new SaidaPacienteDAO();
		saidaPacienteDAO.salvar(saidaPaciente);
	}

	private void validarSaidaPaciente(SaidaPaciente saidaPaciente) throws Exception {
		if(saidaPaciente.getDtMovimentacao().isAfter(LocalDate.now()))
			throw new Exception("A data informada deve ser menor que a data atual");
		
		if(saidaPaciente.getDtMovimentacao().isEqual(LocalDate.now()) && saidaPaciente.getHrMovimentacao().isAfter(LocalTime.now()))
			throw new Exception("A hora informada deve ser menor que a hora atual");
		
		if(saidaPaciente.getEntradaPaciente() != null && !saidaPaciente.getEntradaPaciente().isAlta())
			throw new Exception("O paciente da entrada de paciente relacionada não ganhou alta, portanto não é possível realizar a saída do mesmo");
	}

	public List<SaidaPaciente> consultar(String campo, String comparacao, String termo) throws Exception {
		return new SaidaPacienteDAO().consultar(campo, comparacao, termo);
	}

	public List<SaidaPaciente> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			EntradaPaciente entrada, Usuario usuario, String estadoPaciente, Boolean statusAux) throws ClassNotFoundException, SQLException, Exception {
		return new SaidaPacienteDAO().consultar(dataIni, dataFin, horaIni, horaFin, entrada, usuario,
				estadoPaciente, statusAux);
	}

}
