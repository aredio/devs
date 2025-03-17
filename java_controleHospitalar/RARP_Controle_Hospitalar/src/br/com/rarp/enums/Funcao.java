package br.com.rarp.enums;

public enum Funcao {
	limpeza("Limpeza"), enfermeira("Enfermeira"), atendente("Atendente"), medico("Medico"), outros("Outros");
	
	private String label;

	private Funcao(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return label;
	}
	
}
