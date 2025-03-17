package br.com.rarp.control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.rarp.enums.StatusAtendimento;
import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.model.Atendimento;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.bo.AtendimentoBusiness;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentImpl;

public class AtendimentoCtrl {
	private Atendimento atendimento;
	private List<Atendimento> atendimentos = new ArrayList<>();

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Object atendimento) {
		this.atendimento = (Atendimento) atendimento;
	}

	public void novoAtendimento() {
		atendimento = new Atendimento();
	}

	public boolean salvar() throws Exception {
		if (atendimento == null)
			throw new Exception("O atendimento não foi instânciada");

		if (confirmarDesativacao()) {
			if (atendimento.isStatus())
				validarDadosObrigatorios();
			AtendimentoBusiness atendimentoBusiness = new AtendimentoBusiness();
			atendimentoBusiness.salvar(atendimento);
			return true;
		} else {
			return false;
		}
	}

	private boolean confirmarDesativacao() {
		if (atendimento != null && !atendimento.isStatus())
			return Utilitarios.pergunta("Tem certeza que você deseja desativar este atendimento?");
		return true;
	}

	private void validarDadosObrigatorios() throws Exception {
		if (atendimento.getDtMovimentacao() == null)
			throw new Exception("Para cadastrar um atendimento é necessário informar a data");

		if (atendimento.getHrMovimentacao() == null)
			throw new Exception("Para cadastrar um atendimento é necessário informar a hora");

		if (atendimento.getResponsavel() == null)
			throw new Exception(
					"Para cadastrar um atendimento é necessário informar o funcionário responsável pelo atendimento");

		if (atendimento.getDataAtendimento() == null)
			throw new Exception("Para cadastrar um atendimento é necessário informar a data do atendimento");

		if (atendimento.getHoraIni() == null)
			throw new Exception("Para cadastrar um atendimento é necessário informar a hora do inicio do atendimento");

		if (atendimento.getHoraFim() == null)
			throw new Exception("Para cadastrar um atendimento é necessário informar a hora do fim do atendimento");

		if (atendimento.getEntradaPaciente() == null)
			throw new Exception("Para cadastrar um atendimento é necessário informar a entrada de paciente");
	}

	public ObservableList<Atendimento> consultar(Campo campo, Comparacao comparacao, String termo)
			throws ClassNotFoundException, Exception {
		return FXCollections.observableList(new AtendimentoBusiness().consultar(campo.getNome(),
				comparacao.getComparacao(), comparacao.getTermo(termo)));
	}

	@Override
	public AtendimentoCtrl clone() throws CloneNotSupportedException {
		AtendimentoCtrl a = new AtendimentoCtrl();
		a.setAtendimento(atendimento.clone());
		return a;
	}

	public List<Appointment> getAppointmentByFuncionario(Funcionario value)
			throws ClassNotFoundException, SQLException, Exception {
		List<Appointment> appointments = new ArrayList<>();
		List<Atendimento> atendimentos = new AtendimentoBusiness().getByFuncionario(value);
		for (Atendimento a : this.atendimentos) {
			if (a.getResponsavel().equals(value) && !atendimentos.contains(a))
				atendimentos.add(a);
		}
		if (atendimentos != null && atendimentos.size() > 0)
			for (Atendimento a : atendimentos) {
				if (!a.equals(atendimento)) {
					Appointment appointment = new AppointmentImpl();
					appointment.setStartLocalDateTime(LocalDateTime.of(a.getDataAtendimento(), a.getHoraIni()));
					appointment.setEndLocalDateTime(LocalDateTime.of(a.getDataAtendimento(), a.getHoraFim()));
					appointment.setDescription(a.getDescricao());
					appointment.setAppointmentGroup(new Agenda.AppointmentGroupImpl());
					appointment.getAppointmentGroup().setStyleClass("group7");
					appointments.add(appointment);
				}
			}
		return appointments;
	}

	public List<Atendimento> getAtendimentos() {
		return atendimentos;
	}

	public void setAtendimentos(List<Atendimento> atendimentos) {
		this.atendimentos = atendimentos;
	}

	public List<Atendimento> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			LocalDate dataIniAtend, LocalTime horaIniAtend, LocalDate dataFinAtend, LocalTime horaFinAtend,
			EntradaPaciente entrada, Funcionario responsavel, Usuario usuario, StatusAtendimento statusAtendimento,
			String status) throws ClassNotFoundException, Exception {
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
		return new AtendimentoBusiness().consultar(dataIni, dataFin, horaIni, horaFin, dataIniAtend, horaIniAtend,
				dataFinAtend, horaFinAtend, entrada, responsavel, usuario, statusAtendimento, statusAux);
	}
}
