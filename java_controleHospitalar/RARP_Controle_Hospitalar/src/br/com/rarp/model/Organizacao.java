package br.com.rarp.model;

public class Organizacao extends PessoaJuridica{
	private static Organizacao INSTANCE = new Organizacao();

	private Organizacao() {
		
	}
	
	public static Organizacao getINSTANCE() {
		return INSTANCE;
	}
	
	@Override
	public Organizacao clone()  {
		Organizacao org = new Organizacao();
		org.setBairro(getBairro());
		org.setCep(getCep());
		org.setCidade(getCidade());
		org.setCnpj(getCnpj());
		org.setCodigo(getCodigo());
		org.setComplemento(getComplemento());
		org.setDtNascimento(getDtNascimento());
		org.setLogradouro(getLogradouro());
		org.setNome(getNome());
		org.setNumero(getNumero());
		org.setRazaoSocial(getRazaoSocial());
		org.setStatus(isStatus());
		org.setTelefones(getTelefones());
		return org;
	}
	

}
