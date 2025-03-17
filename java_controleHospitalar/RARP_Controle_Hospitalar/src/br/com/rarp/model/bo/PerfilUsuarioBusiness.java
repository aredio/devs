package br.com.rarp.model.bo;

import java.sql.SQLException;
import java.util.List;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.PerfilUsuario;
import br.com.rarp.model.dao.PerfilUsuarioDAO;
import br.com.rarp.model.dao.UsuarioDAO;

public class PerfilUsuarioBusiness {

	public void salvar(PerfilUsuario perfilUsuario) throws Exception {
		if(perfilUsuario == null)
			throw new Exception("O perfil de usuário não foi instânciado");
			
		if(perfilUsuario.isStatus())
			validarPerfilUsuario(perfilUsuario);
		else
			validarDesativacao(perfilUsuario);
		PerfilUsuarioDAO perfilUsuarioDAO = new PerfilUsuarioDAO();
		perfilUsuarioDAO.salvar(perfilUsuario);
		if(SistemaCtrl.getInstance().getUsuarioSessao() != null)
			SistemaCtrl.getInstance().setUsuarioSessao(new UsuarioDAO().getUsuario(SistemaCtrl.getInstance().getConexao().getConexao(), SistemaCtrl.getInstance().getUsuarioSessao().getCodigo()));
	}

	private void validarDesativacao(PerfilUsuario perfilUsuario) throws Exception {
		if(new UsuarioDAO().consultar(SistemaCtrl.getInstance().getConexao().getConexao(), "codigo_perfilusuario", " = ", perfilUsuario.getCodigo() + "").size() > 0)
			throw new Exception("Não é possível desativar um perfil de usuário que possui usuários");
	}

	private void validarPerfilUsuario(PerfilUsuario perfilUsuario) {
		// valida o perfil de usu�rio
	}

	public List<PerfilUsuario> consultar(String campo, String comparacao, String termo) throws SQLException, Exception {
		PerfilUsuarioDAO perfilUsuarioDAO = new PerfilUsuarioDAO();
		return perfilUsuarioDAO.consultar(campo, comparacao, termo);
	}

}
