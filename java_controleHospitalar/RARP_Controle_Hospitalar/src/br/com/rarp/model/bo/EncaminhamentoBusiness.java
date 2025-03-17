package br.com.rarp.model.bo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import br.com.rarp.model.Encaminhamento;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Leito;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.dao.EncaminhamentoDAO;

public class EncaminhamentoBusiness {

	public void salvar(Encaminhamento encaminhamento) throws Exception {
		if(encaminhamento == null)
			throw new Exception("O encaminhamento não foi instânciada");
		
		try {
			if (encaminhamento.isStatus())
				validarEncaminhamento(encaminhamento);
			else
				validarDesativacao(encaminhamento);
			
			encaminhamento.getDestino().setPaciente(encaminhamento.getOrigem().getPaciente());
			encaminhamento.getOrigem().setSujo(true);
			encaminhamento.getOrigem().setPaciente(null);
			
			EncaminhamentoDAO encaminhamentoDAO = new EncaminhamentoDAO();
			encaminhamentoDAO.salvar(encaminhamento);
		} catch (Exception e) {
			encaminhamento.getOrigem().setPaciente(encaminhamento.getDestino().getPaciente());
			encaminhamento.getOrigem().setSujo(false);
			encaminhamento.getDestino().setPaciente(null);
			throw new Exception(e.getMessage());
		}		
	}

	private void validarDesativacao(Encaminhamento encaminhamento) throws Exception {
		throw new Exception("Não é possivel desativar um encaminhamento. Dica: Realize um nome rencaminhamento de estorno");
	}

	private void validarEncaminhamento(Encaminhamento encaminhamento) throws Exception {
		if(encaminhamento.getDtMovimentacao().isAfter(LocalDate.now()))
			throw new Exception("A data informada deve ser menor que a data atual");
		
		if(encaminhamento.getDtMovimentacao().isEqual(LocalDate.now()) && encaminhamento.getHrMovimentacao().isAfter(LocalTime.now()))
			throw new Exception("A hora informada deve ser menor que a hora atual");
		
		if(encaminhamento.getCodigo() != 0)
			throw new Exception("Não é possivel alterar um encaminhamento realizado. Realize um novo encaminhamento.");
		
		if(encaminhamento.getOrigem().getPaciente() == null)
			throw new Exception("O leito de origem deve possuir um paciente para realizar o encaminhamento");
		
		if(encaminhamento.getDestino().getPaciente() != null)
			throw new Exception("O leito de destino deve estar livre para realizar o encaminhamento");
		
		if(encaminhamento.getEntradaPaciente().getSaidaPaciente() != null 
				&& encaminhamento.getEntradaPaciente().getSaidaPaciente().getCodigo() > 0)
			throw new Exception("Não e possível realizar o encaminhamento para um paciente que saiu do hospital");
	}

	public List<Encaminhamento> consultar(String campo, String comparacao, String termo) throws Exception {
		return new EncaminhamentoDAO().consultar(campo, comparacao, termo);
	}

	public List<Encaminhamento> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			Leito origem, Leito destino, EntradaPaciente entrada, Usuario usuario, Boolean statusAux) throws ClassNotFoundException, Exception {		
		return new EncaminhamentoDAO().consultar(dataIni, dataFin, horaIni, horaFin,
				origem, destino, entrada, usuario, statusAux);
	}

}
