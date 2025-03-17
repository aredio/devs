package br.com.rarp.control;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import br.com.rarp.enums.TipoCampo;
import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Medico;
import br.com.rarp.model.Paciente;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.bo.EntradaPacienteBusiness;
import br.com.rarp.model.dao.EntradaPacienteDAO;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.utils.comparacao.Maior;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EntradaPacienteCtrl {

	private EntradaPaciente entradaPaciente;

	public EntradaPaciente getEntradaPaciente() {
		return entradaPaciente;
	}

	public void setEntradaPaciente(EntradaPaciente entradaPaciente) {
		this.entradaPaciente = entradaPaciente;
	}

	public ObservableList<EntradaPaciente> consultar(Campo campo, Comparacao comparacao, String termo) throws Exception {
		EntradaPacienteBusiness entradaPacienteBusiness = new EntradaPacienteBusiness();
		return FXCollections.observableList(
				entradaPacienteBusiness.consultar(campo.getNome(), comparacao.getComparacao(), comparacao.getTermo(termo)));
	}

	public void setEntradaPaciente(Object entradaPaciente) {
		this.entradaPaciente = (EntradaPaciente) entradaPaciente;
	}

	public boolean salvar() throws Exception {
		if (entradaPaciente == null)
			throw new Exception("A entrada de paciente não foi instânciada");

		if (confirmarDesativacao()) {
			if (entradaPaciente.isStatus())
				validarDadosObrigatorios();
			EntradaPacienteBusiness entradaPacienteBusiness = new EntradaPacienteBusiness();
			entradaPacienteBusiness.salvar(entradaPaciente);
			return true;
		} else {
			return false;
		}
	}

	private void validarDadosObrigatorios() throws Exception {
		if (entradaPaciente != null) {
			if (entradaPaciente.getDtMovimentacao() == null)
				throw new Exception("Para cadastrar uma entrada de paciente é necessário informar a data");

			if (entradaPaciente.getHrMovimentacao() == null)
				throw new Exception("Para cadastrar uma entrada de paciente é necessário informar a hora");

			if (entradaPaciente.getAtendente() == null)
				throw new Exception("Para cadastrar uma entrada de paciente é necessário informar a atendente");
			
			if (entradaPaciente.getPaciente() == null)
				throw new Exception("Para cadastrar uma entrada de paciente é necessário informar o paciente");
		}
	}

	private boolean confirmarDesativacao() {
		if(entradaPaciente != null && !entradaPaciente.isStatus())
			return Utilitarios.pergunta("Tem certeza que você deseja desativar esta entrada de paciente?");
		return true;
	}

	public void novaEntradaPaciente() {
		entradaPaciente = new EntradaPaciente();
	}

	public List<EntradaPaciente> getEntradasPaciente() throws Exception {
		return consultar(new Campo("ENT.codigo", "", TipoCampo.texto), new Maior(), "0");
	}

	public List<EntradaPaciente> getEntradasAbertas() throws Exception {
		return new EntradaPacienteDAO().getEntradasAbertas();
	}

	public List<EntradaPaciente> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			Funcionario atendente, Funcionario enfermeira, Medico medico, Paciente paciente, Usuario usuario, String preTriagem, String status) throws ClassNotFoundException, Exception {
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
		return new EntradaPacienteBusiness().consultar(dataIni, dataFin, horaIni, horaFin,
				atendente, enfermeira, medico, paciente, usuario, preTriagem, statusAux);
	}

}
