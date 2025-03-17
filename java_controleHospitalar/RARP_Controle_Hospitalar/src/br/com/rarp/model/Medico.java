package br.com.rarp.model;

import java.util.List;

/**
 * 
 * @author rober
 *classe edico
 */
public class Medico extends Funcionario {

	private int codigoMedico;
	private String CRM;
	private List<Especialidade> especialidades;
	private boolean status ;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getCRM() {
		return CRM;
	}
	public void setCRM(String cRM) {
		CRM = cRM;
	}
	public List<Especialidade> getEspecialidades() {
		return especialidades;
	}
	public void setEspecialidades(List<Especialidade> especialidades) {
		this.especialidades = especialidades;
	}

	public int getCodigoMedico() {
		return codigoMedico;
	}
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}
}
