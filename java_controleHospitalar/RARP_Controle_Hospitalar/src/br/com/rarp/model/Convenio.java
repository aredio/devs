package br.com.rarp.model;

public class Convenio extends PessoaJuridica {

	private String ANS;
	private int tipo;
	private boolean autorizado;
	
	public String getANS() {
		return ANS;
	}
	public void setANS(String aNS) {
		ANS = aNS;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public Convenio clone() {
		Convenio convenio = new Convenio();
		convenio.setANS(getANS());
		convenio.setTipo(getTipo());
		convenio.setBairro(getBairro());
		convenio.setCep(getCep());
		convenio.setCidade(getCidade());
		convenio.setCnpj(getCnpj());
		convenio.setCodigo(getCodigo());
		convenio.setComplemento(getComplemento());
		convenio.setDtNascimento(getDtNascimento());
		convenio.setLogradouro(getLogradouro());
		convenio.setNome(getNome());
		convenio.setNumero(getNumero());
		convenio.setRazaoSocial(getRazaoSocial());
		convenio.setStatus(isStatus());
		convenio.setTelefones(getTelefones());
		return convenio;
	}
	public boolean isAutorizado() {
		return autorizado;
	}
	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getNome() != null ? getNome() : "";
	}

}
