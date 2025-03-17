package br.com.rarp.model.bo;

import java.util.List;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.dao.UsuarioDAO;

public class UsuarioBusiness {

	public void salvar(Usuario usuario) throws Exception {
		if(usuario == null)
			throw new Exception("O usuúrio não foi instânciado");
		
		if(usuario.isStatus())
			validarUsuario(usuario);
		else
			validarDesativacao(usuario);
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuarioDAO.salvar(usuario);
		if(SistemaCtrl.getInstance().getUsuarioSessao() != null)
			SistemaCtrl.getInstance().setUsuarioSessao(new UsuarioDAO().getUsuario(SistemaCtrl.getInstance().getConexao().getConexao(), SistemaCtrl.getInstance().getUsuarioSessao().getCodigo()));
	}

	private void validarDesativacao(Usuario usuario) throws Exception {
		if(usuario.equals(SistemaCtrl.getInstance().getUsuarioSessao()))
			throw new Exception("O usuário da sessão não pode ser desativado");
	}

	private void validarUsuario(Usuario usuario) {
		// valida o usu�rio
	}

	public List<Usuario> consultar(String campo, String comparacao, String termo) throws Exception {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.consultar(SistemaCtrl.getInstance().getConexao().getConexao(), campo, comparacao, termo);
	}

}
