package br.com.rarp.model;

public class PessoaFisica extends Pessoa {

	private String cpf;
	private String rg;
	private String sexo;
	private boolean possuiNecessidades;
	private String certidaoNascimento;
	private boolean compartinhaInformacoes;
	private String SUS;
	public boolean isCompartinhaInformacoes() {
		return compartinhaInformacoes;
	}

	public void setCompartinhaInformacoes(boolean compartinhaInformacoes) {
		this.compartinhaInformacoes = compartinhaInformacoes;
	}

	public String getCpf() {
		String cpf = getCpfSemMascara();
		if(cpf != null)
			cpf = String.format("%s.%s.%s-%s", cpf.substring(0, 3), cpf.substring(3, 6), cpf.substring(6, 9), cpf.substring(9));
		return cpf;
	}
	
	public String getCpfSemMascara() {
		if(cpf != null)
			return cpf.replaceAll("[\\D]", "");
		else
			return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
		if(cpf != null)
			this.cpf = cpf.replaceAll("[\\D]", "");	
	}
	
	public String getRg() {
		return rg;
	}
	
	public void setRg(String rg) {
		this.rg = rg;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public boolean isPossuiNecessidades() {
		return possuiNecessidades;
	}
	
	public void setPossuiNecessidades(boolean possuiNecessidades) {
		this.possuiNecessidades = possuiNecessidades;
	}
	
	public String getCertidaoNascimento() {
		return certidaoNascimento;
	}
	
	public void setCertidaoNascimento(String certidaoNascimento) {
		this.certidaoNascimento = certidaoNascimento;
	}
	
	public PessoaFisica clone() throws CloneNotSupportedException {
		PessoaFisica p = new PessoaFisica();
		p.setBairro(getBairro());
		p.setCep(getCep());
		p.setCertidaoNascimento(getCertidaoNascimento());
		p.setCidade(getCidade());
		p.setCodigo(getCodigo());
		p.setComplemento(getComplemento());
		p.setCpf(getCpf());
		p.setDtNascimento(getDtNascimento());
		p.setLogradouro(getLogradouro());
		p.setNome(getNome());
		p.setNumero(getNumero());
		p.setPossuiNecessidades(isPossuiNecessidades());
		p.setStatus(isStatus());
		p.setTelefones(getTelefones());
		return p;
	}

	public String getSUS() {
		return SUS;
	}

	public void setSUS(String sUS) {
		SUS = sUS;
	}
}
