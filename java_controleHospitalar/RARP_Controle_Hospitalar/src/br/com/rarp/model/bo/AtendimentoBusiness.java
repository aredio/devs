package br.com.rarp.model.bo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import br.com.rarp.enums.StatusAtendimento;
import br.com.rarp.model.Atendimento;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.dao.AtendimentoDAO;

public class AtendimentoBusiness {

	public void salvar(Atendimento atendimento) throws Exception {
		if(atendimento == null)
			throw new Exception("A entrada de paciente não foi instânciada");
		
		if(atendimento.isStatus())
			validarAtendimento(atendimento);
		else
			validarDesativacao(atendimento);
		AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
		atendimentoDAO.salvar(atendimento);
	}

	public void validarAtendimento(Atendimento atendimento) throws Exception {
		if(atendimento != null) {
			if(atendimento.getDtMovimentacao().isAfter(LocalDate.now()))
				throw new Exception("A data informada deve ser menor que a data atual");
			
			if(atendimento.getDtMovimentacao().isEqual(LocalDate.now()) && atendimento.getHrMovimentacao().isAfter(LocalTime.now()))
				throw new Exception("A hora informada deve ser menor que a hora atual");
			
			if(atendimento.getCodigo() == 0) {
				if (atendimento.getDataAtendimento().isBefore(LocalDate.now()) && atendimento.getStatusAtendimento() != StatusAtendimento.realizado) 
					throw new Exception("Não é possível cadastrar um atendimento não realizado com data retroativa");
				
				if (atendimento.getDataAtendimento().isEqual(LocalDate.now()) && atendimento.getHoraFim().isBefore(LocalTime.now()) && atendimento.getStatusAtendimento() != StatusAtendimento.realizado) 
					throw new Exception("Não é possível cadastrar um atendimento não realizado com hora retroativa");
			}
		}
	}

	public List<Atendimento> consultar(String campo, String comparacao, String termo) throws ClassNotFoundException, Exception {
		return new AtendimentoDAO().consultar(campo, comparacao, termo);
	}

	public void validarDesativacao(Atendimento a) throws Exception {
		if(a.getStatusAtendimento() == StatusAtendimento.realizado)
			throw new Exception("Não é possível desativar um atendimento já realizado");
	}

	public List<Atendimento> getByFuncionario(Funcionario value) throws ClassNotFoundException, SQLException, Exception {
		return new AtendimentoDAO().consultar("ATE.codigo_funcionario = " + value.getCodigo() + " AND ATE.status = 'TRUE'");
	}

	public List<Atendimento> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			LocalDate dataIniAtend, LocalTime horaIniAtend, LocalDate dataFinAtend, LocalTime horaFinAtend,
			EntradaPaciente entrada, Funcionario responsavel, Usuario usuario, StatusAtendimento statusAtendimento,
			Boolean statusAux) throws ClassNotFoundException, Exception {
		if(horaIniAtend != null && dataIniAtend == null)
			throw new Exception("Para informar a hora de inicio dos atendimentos é neccessário informar a data de inicio");
		if(horaIniAtend != null && dataIniAtend == null)
			throw new Exception("Para informar a hora de termino dos atendimentos é neccessário informar a data termino");
		return new AtendimentoDAO().consultar(dataIni, dataFin, horaIni, horaFin,
			dataIniAtend, horaIniAtend, dataFinAtend, horaFinAtend,
			entrada, responsavel, usuario, statusAtendimento,
			statusAux);
	}

}
