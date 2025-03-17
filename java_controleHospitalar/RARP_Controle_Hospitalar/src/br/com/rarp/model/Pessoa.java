package br.com.rarp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pessoa {

	private int codigo;
	private String nome;
	private LocalDate dtNascimento;
	private String logradouro;
	private String complemento;
	private String numero;
	private String bairro;
	private String cep;
	private List<Telefone> telefones;
	private Cidade cidade;
	private boolean status;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Pessoa() {
		cidade = new Cidade();
		telefones = new ArrayList<>();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		String cep = getCepSemMascara();
		if(cep != null && cep.length() == 8)
			cep = String.format("%s-%s", cep.substring(0, 5), cep.substring(5));
		return cep;
	}
	
	public String getCepSemMascara() {
		if(cep != null)
			return cep.replaceAll("[\\D]", "");
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
		if(cep != null)
			this.cep = cep.replaceAll("[\\D]", "");
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public LocalDate getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(LocalDate dtNascimento) {
		this.dtNascimento = dtNascimento;
	}
}
