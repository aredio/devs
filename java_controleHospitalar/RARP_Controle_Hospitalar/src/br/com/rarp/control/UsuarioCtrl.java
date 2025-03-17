package br.com.rarp.control;

import java.util.List;

import br.com.rarp.enums.TipoCampo;
import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.bo.UsuarioBusiness;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.utils.comparacao.Ativado;
import br.com.rarp.utils.comparacao.Igual;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UsuarioCtrl {
	private Usuario usuario;
	private List<Usuario> usuarios;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Object object) {
		this.usuario = (Usuario) object;
	}
	
	public void novoUsuario() {
		usuario = new Usuario();
	}

	public boolean salvar() throws Exception {
		if (usuario == null)
			throw new Exception("O usuário não foi instânciado");
		
		if(confirmarDesativacao()) {
			if(usuario.isStatus())
				validarDadosObrigatorios();
			UsuarioBusiness perfilUsuarioBusiness = new UsuarioBusiness();
			perfilUsuarioBusiness.salvar(usuario);
			return true;
		} else {
			return false;
		}
	}
	
	private boolean confirmarDesativacao() {
		if(!usuario.isStatus())
			return Utilitarios.pergunta("Tem certeza que você deseja desativar este usu�rio?");
		return true;
	}


	private void validarDadosObrigatorios() throws Exception {
		if(usuario.getNome().equals(""))
			throw new Exception("Para cadastrar um usuário é necessário informar o nome");
		if(usuario.getUsuario().equals(""))
			throw new Exception("Para cadastrar um usuário é necessário informar o uusuário");
		if(usuario.getPerfilUsuario() == null)
			throw new Exception("Para cadastrar um usuário é necessário informar o perfil de usuário");
		if(usuario.getFuncionario() == null)
			throw new Exception("Para cadastrar um usuário é necessário informar o funcionário");
	}

	@SuppressWarnings("rawtypes")
	public ObservableList consultar(Campo campo, Comparacao comparacao, String termo) throws Exception {
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		return FXCollections.observableArrayList(usuarioBusiness.consultar(campo.getNome(), comparacao.getComparacao(), comparacao.getTermo(termo)));
	}

	public void consultar(String usuario) throws Exception {
		if (!usuario.isEmpty()) {
			UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
			List<Usuario> usuarios = usuarioBusiness.consultar("usuario", new Igual().getComparacao(),
					new Igual().getTermo(usuario));
			if (usuarios != null && usuarios.size() > 0) {
				this.usuario = usuarios.get(0);
			} else {
				this.usuario = null;
			} 
		}
	}

	public boolean isEmpty() throws Exception {
		usuarios = getUsuarios();
		return usuarios.size() == 0;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuarios() throws Exception {
		return consultar(new Campo("status", "", TipoCampo.booleano), new Ativado(), "Ativado");
	}
}
