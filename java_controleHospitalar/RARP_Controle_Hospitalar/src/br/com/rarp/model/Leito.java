package br.com.rarp.model;

public class Leito {

	private int codigo;
	private int numero;
	private boolean status;
	private Espaco espaco;
	private Paciente paciente;
	private boolean sujo;
	
	public Leito(int numero) {
		this.numero = numero;
		this.status = true;
		this.sujo = false;
	}
	
	public Leito() {
		this.status = true;
	}

	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Override
	public boolean equals(Object obj) {
		return numero == ((Leito) obj).getNumero();
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	
	@Override
	public String toString() {
		return (getEspaco() != null && getEspaco().getNome() != null ? getEspaco().getNome() + " - " : "") + "leito: " + numero;
	}

	public Espaco getEspaco() {
		return espaco;
	}

	public void setEspaco(Espaco espaco) {
		this.espaco = espaco;
	}

	public boolean isSujo() {
		return sujo;
	}

	public void setSujo(boolean sujo) {
		this.sujo = sujo;
	}
	
	@Override
	public Leito clone() {
		Leito leito = new Leito();
		leito.setCodigo(getCodigo());
		leito.setEspaco(getEspaco());
		leito.setNumero(getNumero());
		leito.setPaciente(getPaciente());
		leito.setStatus(isStatus());
		leito.setSujo(isSujo());
		return leito;
	}

}
