package br.com.rarp.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.rarp.enums.StatusAtendimento;

public class Atendimento extends Movimentacao {

	private LocalDate dataAtendimento;
	private LocalTime horaIni;
	private LocalTime horaFim;
	private String detalheMedico;
	private String descricao;
	private ReceitaMedica receitaMedica;
	private StatusAtendimento statusAtendimento;
	private EntradaPaciente entradaPaciente;
	private Funcionario responsavel;
	private List<Sintoma> sintomas = new ArrayList<>();
	private String styleClass;

	public LocalDate getDataAtendimento() {
		return dataAtendimento;
	}
	public void setDataAtendimento(LocalDate dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}
	public LocalTime getHoraIni() {
		return horaIni;
	}
	public void setHoraIni(LocalTime horaIni) {
		this.horaIni = horaIni;
	}
	public LocalTime getHoraFim() {
		return horaFim;
	}
	public void setHoraFim(LocalTime horaFim) {
		this.horaFim = horaFim;
	}
	public String getDetalheMedico() {
		return detalheMedico;
	}
	public void setDetalheMedico(String detalheMedico) {
		this.detalheMedico = detalheMedico;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public ReceitaMedica getReceitaMedica() {
		return receitaMedica;
	}
	public void setReceitaMedica(ReceitaMedica receitaMedica) {
		this.receitaMedica = receitaMedica;
	}
	public StatusAtendimento getStatusAtendimento() {
		return statusAtendimento;
	}
	public void setStatusAtendimento(StatusAtendimento statusAtendimento) {
		this.statusAtendimento = statusAtendimento;
	}
	public EntradaPaciente getEntradaPaciente() {
		return entradaPaciente;
	}
	public void setEntradaPaciente(EntradaPaciente entradaPaciente) {
		this.entradaPaciente = entradaPaciente;
	}
	public Funcionario getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(Funcionario responsavel) {
		this.responsavel = responsavel;
	}
	public List<Sintoma> getSintomas() {
		return sintomas;
	}
	public void setSintomas(List<Sintoma> sintomas) {
		this.sintomas = sintomas;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Atendimento && ((Atendimento) obj).getCodigo() > 0)
			return ((Atendimento) obj).getCodigo() == getCodigo();
		else
			return super.equals(obj);
	}
	
	@Override
	public Atendimento clone() {
		Atendimento atendimento = new Atendimento();
		atendimento.setCodigo(getCodigo());
		atendimento.setDataAtendimento(getDataAtendimento());
		atendimento.setDescricao(getDescricao());
		atendimento.setDetalheMedico(getDetalheMedico());
		atendimento.setDtMovimentacao(getDtMovimentacao());
		atendimento.setEntradaPaciente(getEntradaPaciente());
		atendimento.setHoraFim(getHoraFim());
		atendimento.setHoraIni(getHoraIni());
		atendimento.setHrMovimentacao(getHrMovimentacao());
		atendimento.setReceitaMedica(getReceitaMedica());
		atendimento.setResponsavel(getResponsavel());
		atendimento.setSintomas(getSintomas());
		atendimento.setStatus(isStatus());
		atendimento.setStatusAtendimento(getStatusAtendimento());
		atendimento.setUsuario(getUsuario());
		return atendimento;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
