package br.com.rarp.enums;

public enum StatusAtendimento {
	emAberto("Em Aberto"), emAndamento("Em Andamento"), realizado("Realizado");
	
	private String label;

	private StatusAtendimento(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return label;
	}
}
