package br.com.rarp.model;

public class PessoaJuridica extends Pessoa {

	private String cnpj;
	private String razaoSocial;

	public String getCnpj() {
		String cnpj = getCnpjSemMascara();
		if(cnpj == null)
			return cnpj;
		
		return String.format("%s.%s.%s/%s-%s",
				cnpj.substring(0, 2), 
				cnpj.substring(2, 5), 
				cnpj.substring(5, 8), 
				cnpj.substring(8, 12), 
				cnpj.substring(12, 14));
	}
	
	public String getCnpjSemMascara() {
		if(cnpj != null)
			return cnpj.replaceAll("[\\D]", "");
		else
			return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
		if(cnpj != null)
			this.cnpj = cnpj.replaceAll("[\\D]", "");
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	
	@Override
	public PessoaJuridica clone() throws CloneNotSupportedException {
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setBairro(getBairro());
		pessoaJuridica.setCep(getCep());
		pessoaJuridica.setCidade(getCidade());
		pessoaJuridica.setCnpj(getCnpj());
		pessoaJuridica.setCodigo(getCodigo());
		pessoaJuridica.setComplemento(getComplemento());
		pessoaJuridica.setDtNascimento(getDtNascimento());
		pessoaJuridica.setLogradouro(getLogradouro());
		pessoaJuridica.setNome(getNome());
		pessoaJuridica.setNumero(getNumero());
		pessoaJuridica.setRazaoSocial(getRazaoSocial());
		pessoaJuridica.setStatus(isStatus());
		pessoaJuridica.setTelefones(getTelefones());
		return pessoaJuridica;
	}
}
