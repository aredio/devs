package br.com.rarp.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Movimentacao {

	private int codigo;
	private LocalDate dtMovimentacao;
	private LocalTime hrMovimentacao;
	private Usuario usuario;
	private boolean status;
	
	public LocalDate getDtMovimentacao() {
		return dtMovimentacao;
	}
	public void setDtMovimentacao(LocalDate dtMovimentacao) {
		this.dtMovimentacao = dtMovimentacao;
	}
	public LocalTime getHrMovimentacao() {
		return hrMovimentacao;
	}
	public void setHrMovimentacao(LocalTime hrMovimentacao) {
		this.hrMovimentacao = hrMovimentacao;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	

}
