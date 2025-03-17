package br.com.rarp.model;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EntradaPaciente extends Movimentacao {

	private String preTriagem;
	private Medico medico;
	private Paciente paciente;
	private Funcionario atendente;
	private Funcionario enfermeira;
	private Convenio convenio;
	private List<Atendimento> atendimentos;
	private List<Encaminhamento> encaminhamentos;
	private SaidaPaciente saidaPaciente;
	private boolean alta;
	private boolean emergencia;
	
	public String getPreTriagem() {
		return preTriagem;
	}
	public void setPreTriagem(String preTriagem) {
		this.preTriagem = preTriagem;
	}
	public Medico getMedico() {
		return medico;
	}
	public void setMedico(Medico medico) {
		this.medico = medico;
	}
	public Paciente getPaciente() {
		return paciente;
	}
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	public List<Encaminhamento> getEncaminhamentos() {
		return encaminhamentos;
	}
	public void setEncaminhamentos(List<Encaminhamento> encaminhamentos) {
		this.encaminhamentos = encaminhamentos;
	}
	public Funcionario getAtendente() {
		return atendente;
	}
	public void setAtendente(Funcionario atendente) {
		this.atendente = atendente;
	}
	public List<Atendimento> getAtendimentos() {
		return atendimentos;
	}
	public void setAtendimentos(List<Atendimento> agendamentos) {
		this.atendimentos = agendamentos;
	}
	public Funcionario getEnfermeira() {
		return enfermeira;
	}
	public void setEnfermeira(Funcionario enfermeira) {
		this.enfermeira = enfermeira;
	}
	public boolean isAlta() {
		return alta;
	}
	public void setAlta(boolean alta) {
		this.alta = alta;
	}
	public boolean isEmergencia() {
		return emergencia;
	}
	public void setEmergencia(boolean emergencia) {
		this.emergencia = emergencia;
	}

	@Override
	public String toString() {
		if(getPaciente() != null)
			return getPaciente().getNome() 
				+ " - " + getDtMovimentacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) 
				+ " " + getHrMovimentacao().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		return "";
	}
	public SaidaPaciente getSaidaPaciente() {
		return saidaPaciente;
	}
	public void setSaidaPaciente(SaidaPaciente saidaPaciente) {
		this.saidaPaciente = saidaPaciente;
	}
	public Convenio getConvenio() {
		return convenio;
	}
	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}
}
