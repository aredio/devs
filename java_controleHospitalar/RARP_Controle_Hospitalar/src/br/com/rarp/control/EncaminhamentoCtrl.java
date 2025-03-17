package br.com.rarp.control;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.model.Encaminhamento;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Leito;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.bo.EncaminhamentoBusiness;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EncaminhamentoCtrl {
	
	private Encaminhamento encaminhamento;

	public Encaminhamento getEncaminhamento() {
		return encaminhamento;
	}

	public void setEncaminhamento(Object encaminhamento) {
		this.encaminhamento = (Encaminhamento) encaminhamento;
	}

	public boolean salvar(EncaminhamentoCtrl encaminhamentoCtrl) throws Exception {
		if (encaminhamento == null)
			throw new Exception("O encaminhamento não foi instânciada");
		
		if (confirmarDesativacao()) {
			if (encaminhamento.isStatus())
				validarDadosObrigatorios();
			EncaminhamentoBusiness encaminhamentoBusiness = new EncaminhamentoBusiness();
			encaminhamentoBusiness.salvar(encaminhamento);
			return true;
		} else {
			return false;
		}
	}
	
	public EncaminhamentoCtrl clone() {
		EncaminhamentoCtrl encaminhamentoCtrl = new EncaminhamentoCtrl();
		encaminhamentoCtrl.setEncaminhamento(encaminhamento.clone());
		return encaminhamentoCtrl;
	}

	private void validarDadosObrigatorios() throws Exception {
		if(encaminhamento.getEntradaPaciente() == null)
			throw new Exception("Para realizar um encaminhamento é necessário informar a entrada do paciente que será encaminhado");
		if(encaminhamento.getHrMovimentacao() == null)
			throw new Exception("Para realizar um encaminhamento é necessário informar o horário do encaminhamento");
		if(encaminhamento.getDtMovimentacao() == null)
			throw new Exception("Para realizar um encaminhamento é necessário informar a data do encaminhamento");
		if(encaminhamento.getOrigem() == null)
			throw new Exception("Para realizar um encaminhamento é necessário informar o leito de origem");
		if(encaminhamento.getDestino() == null)
			throw new Exception("Para realizar um encaminhamento é necessário informar o leito de destino");
	}

	private boolean confirmarDesativacao() {
		if(encaminhamento != null && !encaminhamento.isStatus())
			return Utilitarios.pergunta("Tem certeza que vocé deseja desativar esta entrada de paciente?");
		return true;
	}

	public void novoEncaminhamento() {
		encaminhamento = new Encaminhamento();
	}

	public ObservableList<Encaminhamento> consultar(Campo campo, Comparacao comparacao, String termo) throws Exception {
		return FXCollections.observableList(new EncaminhamentoBusiness().consultar(campo.getNome(), comparacao.getComparacao(), comparacao.getTermo(termo)));
	}

	public List<Encaminhamento> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			Leito origem, Leito destino, EntradaPaciente entrada, Usuario usuario, String status) throws ClassNotFoundException, Exception {
		Boolean statusAux = null;
		if (status != null) {
			switch (status) {
			case "Ativado":
				statusAux = true;
				break;

			case "Desativado":
				statusAux = false;
				break;
			}
		}
		return new EncaminhamentoBusiness().consultar(dataIni, dataFin, horaIni, horaFin,
				origem, destino, entrada, usuario, statusAux);
	}

}
