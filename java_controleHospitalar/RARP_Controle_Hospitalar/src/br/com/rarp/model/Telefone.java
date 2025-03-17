package br.com.rarp.model;

public class Telefone {

	private int Codigo;
	private String numero;

	public int getCodigo() {
		return Codigo;
	}

	public void setCodigo(int codigo) {
		Codigo = codigo;
	}

	public String getNumero() {
		String numero = getNumeroSemMascara();
		if(numero.length() == 8)
			numero = numero.substring(0, 4) + "-" + numero.substring(4, 8);
		else if(numero.length() == 10)
			numero = "(" + numero.substring(0, 2) + ") " + numero.substring(2, 6) + "-" + numero.substring(6, 10);
		else if(numero.length() == 11)
			numero = "(" + numero.substring(0, 2) + ") " + numero.substring(2, 7) + "-" + numero.substring(7, 11);
		return numero;
	}
	
	public String getNumeroSemMascara() {
		return numero.replaceAll("[\\D]", "");
	}

	public void setNumero(String numero) {
		this.numero = numero.replaceAll("[\\D]", "");
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return numero == ((Telefone) obj).getNumeroSemMascara();
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return getNumero();
	}
}
