package br.com.rarp.model.bo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.StatusAtendimento;
import br.com.rarp.model.Atendimento;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Espaco;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Leito;
import br.com.rarp.model.Medico;
import br.com.rarp.model.Paciente;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.dao.EntradaPacienteDAO;
import br.com.rarp.model.dao.EspacoDAO;
import br.com.rarp.model.dao.LeitoDAO;

public class EntradaPacienteBusiness {

	public void salvar(EntradaPaciente entradaPaciente) throws Exception {
		if(entradaPaciente == null)
			throw new Exception("A entrada de paciente não foi instânciada");
		
		if(entradaPaciente.isStatus())
			validarEntradaPaciente(entradaPaciente);
		else
			validarDesativacao(entradaPaciente);
		
		//Coloca o paciente em um leito da recep��o
		Espaco recepcao = new EspacoDAO().getRecepcao();
		if(recepcao == null) {
			recepcao = SistemaCtrl.getInstance().getRecepcao();
			recepcao.getLeitos().get(0).setPaciente(entradaPaciente.getPaciente());
			if(new LeitoDAO().consultar("codigo_paciente = " + entradaPaciente.getPaciente().getCodigo()).size() == 0)
				new EspacoDAO().salvar(recepcao);
		} else {
			Leito livre = new Leito(1);
			for(Leito leito: recepcao.getLeitos()) {
				livre.setNumero(livre.getNumero() + 1);
				if(leito.getPaciente() == null) {
					livre = leito;
					break;
				}
			}
			livre.setPaciente(entradaPaciente.getPaciente());
			livre.setEspaco(recepcao);
			LeitoDAO leitoDAO = new LeitoDAO();
			if(leitoDAO.consultar("codigo_paciente = " + entradaPaciente.getPaciente().getCodigo()).size() == 0)
				leitoDAO.salvar(livre);
		}
		EntradaPacienteDAO entradaPacienteDAO = new EntradaPacienteDAO();
		entradaPacienteDAO.salvar(entradaPaciente);		
	}
	
	private void validarDesativacao(EntradaPaciente entradaPaciente) throws Exception {
		for(Atendimento a: entradaPaciente.getAtendimentos()) {
			try {
			new AtendimentoBusiness().validarDesativacao(a);
			} catch (Exception e) {
				throw new Exception("Não foi possível desativar uma entrada de paciente que possui atendimentos realizados");
			}
			a.setStatus(false);
		}
		
		if(entradaPaciente.getEncaminhamentos().size() > 0)
			throw new Exception("Não ? possível desativar uma entrada de paciente que possui encaminhamentos realizados");
	}

	private void validarEntradaPaciente(EntradaPaciente entradaPaciente) throws Exception {
		if (entradaPaciente != null) {
			if(entradaPaciente.getDtMovimentacao().isAfter(LocalDate.now()))
				throw new Exception("A data informada deve ser menor que a data atual");
			
			if(entradaPaciente.getDtMovimentacao().isEqual(LocalDate.now()) && entradaPaciente.getHrMovimentacao().isAfter(LocalTime.now()))
				throw new Exception("A hora informada deve ser menor que a hora atual");
			
			if (entradaPaciente.getMedico() == null && entradaPaciente.getAtendimentos().size() > 0)
				throw new Exception("Para cadastrar os atendimentos para esta entrada de paciente é necessário informar o médico");			
			
			if (entradaPaciente.getConvenio() != null && entradaPaciente.getConvenio().isAutorizado())
				throw new Exception("O convênio selecionado não foi autorizado pela autorização do hospital");
			
			if(entradaPaciente.isAlta()) {
				if (entradaPaciente.getMedico() == null)
					throw new Exception("Para cadastrar uma entrada de paciente e necessário informar o médico");
				
				boolean realizado = true;
				for(Atendimento a: entradaPaciente.getAtendimentos()) {
					if(a.isStatus() && a.getStatusAtendimento() != StatusAtendimento.realizado) {
						realizado = false;
						break;
					}
				}	
				
				if(!realizado)
					throw new Exception("Para dar alta ao paciente é necessário que todos os atendimentos agendados tenha sido realizados");		
			}
			
			for(Atendimento atendimento: entradaPaciente.getAtendimentos())
				new AtendimentoBusiness().validarAtendimento(atendimento);

			List<EntradaPaciente> entradas = new EntradaPacienteDAO().getEntradasByPaciente(entradaPaciente.getPaciente());
			for (EntradaPaciente e : entradas)
				if ((e.getSaidaPaciente() == null || e.getSaidaPaciente().getCodigo() <= 0) && e.getCodigo() != entradaPaciente.getCodigo())
					throw new Exception("Não será possível cadastrar um entrada para este paciente, pois o mesmo possui uma entrada em aberto.");
		}
	}

	public List<EntradaPaciente> consultar(String campo, String comparacao, String termo) throws Exception {
		EntradaPacienteDAO entradaPacienteDAO = new EntradaPacienteDAO();
		return entradaPacienteDAO.consultar(campo, comparacao, termo);
	}

	public List<EntradaPaciente> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			Funcionario atendente, Funcionario enfermeira, Medico medico, Paciente paciente, Usuario usuario,
			String preTriagem, Boolean status) throws ClassNotFoundException, Exception {
		return new EntradaPacienteDAO().consultar(dataIni, dataFin, horaIni, horaFin,
				atendente, enfermeira, medico, paciente, usuario, preTriagem, status);
	}

}
