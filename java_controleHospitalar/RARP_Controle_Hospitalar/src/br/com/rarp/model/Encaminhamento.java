package br.com.rarp.model;

public class Encaminhamento extends Movimentacao {

	private Leito origem;
	private Leito destino;
	private EntradaPaciente entradaPaciente;
	
	public Leito getDestino() {
		return destino;
	}
	public void setDestino(Leito destino) {
		this.destino = destino;
	}
	public Leito getOrigem() {
		return origem;
	}
	public void setOrigem(Leito origem) {
		this.origem = origem;
	}
	public EntradaPaciente getEntradaPaciente() {
		return entradaPaciente;
	}
	public void setEntradaPaciente(EntradaPaciente entradaPaciente) {
		this.entradaPaciente = entradaPaciente;
	}
	
	@Override
	public Encaminhamento clone() {
		Encaminhamento encaminhamento = new Encaminhamento();
		encaminhamento.setCodigo(getCodigo());
		encaminhamento.setDestino(getDestino().clone());
		encaminhamento.setOrigem(getOrigem().clone());
		encaminhamento.setDtMovimentacao(getDtMovimentacao());
		encaminhamento.setEntradaPaciente(getEntradaPaciente());
		encaminhamento.setHrMovimentacao(getHrMovimentacao());
		encaminhamento.setStatus(isStatus());
		encaminhamento.setUsuario(getUsuario());
		return encaminhamento;
	}

}
