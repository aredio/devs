package br.com.rarp.model;

public class Paciente extends PessoaFisica {
	private Convenio convenio;
	private Paciente responsavel;

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public Paciente getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Paciente responsavel) {
		this.responsavel = responsavel;
	}
	
	@Override
	public String toString() {
		return getNome();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Paciente)
			return ((Paciente) obj).getCodigo() == getCodigo();
		return false;
	}
	
}
