package br.com.rarp.control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.SaidaPaciente;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.bo.SaidaPacienteBusiness;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SaidaPacienteCtrl {
	private SaidaPaciente saidaPaciente;

	public void novaSaidaPaciente() {
		setSaidaPaciente(new SaidaPaciente());
	}

	public SaidaPaciente getSaidaPaciente() {
		return saidaPaciente;
	}

	public void setSaidaPaciente(Object saidaPaciente) {
		this.saidaPaciente = (SaidaPaciente) saidaPaciente;
	}

	public ObservableList<SaidaPaciente> consultar(Campo campo, Comparacao comparacao, String termo) throws Exception {
		return FXCollections.observableList(new SaidaPacienteBusiness().consultar(campo.getNome(),
				comparacao.getComparacao(), comparacao.getTermo(termo)));
	}

	public boolean salvar() throws Exception {
		if (saidaPaciente == null)
			throw new Exception("A saída de paciente não foi instânciada");

		if (confirmarDesativacao()) {
			if (saidaPaciente.isStatus())
				validarDadosObrigatorios();
			SaidaPacienteBusiness saidaPacienteBusiness = new SaidaPacienteBusiness();
			saidaPacienteBusiness.salvar(saidaPaciente);
			return true;
		} else {
			return false;
		}
	}

	private void validarDadosObrigatorios() throws Exception {
		if (saidaPaciente != null) {
			if (saidaPaciente.getDtMovimentacao() == null)
				throw new Exception("Para cadastrar uma saída de paciente é necessário informar a data");
			if (saidaPaciente.getHrMovimentacao() == null)
				throw new Exception("Para cadastrar uma saída de paciente é necessário informar a hora");
			if (saidaPaciente.getEntradaPaciente() == null)
				throw new Exception(
						"Para cadastrar uma saída de paciente é necessário informar a entrada de paciente relacionada");
			if (saidaPaciente.getEstadoPaciente().isEmpty())
				throw new Exception("Para cadastrar uma saída de paciente é necessário informar o estado do paciente");
		}
	}

	private boolean confirmarDesativacao() {
		if (!saidaPaciente.isStatus())
			return Utilitarios.pergunta("Tem certeza que você deseja desativar esta saida de paciente?");
		return true;
	}

	public List<SaidaPaciente> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			EntradaPaciente entrada, Usuario usuario, String estadoPaciente, String status) throws ClassNotFoundException, SQLException, Exception {
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
		return new SaidaPacienteBusiness().consultar(dataIni, dataFin, horaIni, horaFin, entrada, usuario,
				estadoPaciente, statusAux);
	}
}
