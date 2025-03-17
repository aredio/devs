package br.com.rarp.control;

import java.sql.SQLException;
import br.com.rarp.interfaces.Comparacao;
import br.com.rarp.model.PerfilUsuario;
import br.com.rarp.model.Tela;
import br.com.rarp.model.bo.PerfilUsuarioBusiness;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PerfilUsuarioCtrl extends Object {
	PerfilUsuario perfilUsuario;

	public PerfilUsuario getPerfilUsuario() {
		return perfilUsuario;
	}
	
	public void novoPerfilUsuario() {
		perfilUsuario = new PerfilUsuario();
	}

	public boolean salvar() throws Exception {
		if (perfilUsuario == null)
			throw new Exception("O perfil de usuário não foi instânciado");
		
		if (confirmarDesativacao()) {
			if(perfilUsuario.isStatus())
				validarDadosObrigatorios();
			PerfilUsuarioBusiness perfilUsuarioBusiness = new PerfilUsuarioBusiness();
			perfilUsuarioBusiness.salvar(perfilUsuario);
			return true;
		} else {
			return false;
		}
	}
	
	private boolean confirmarDesativacao() {
		if(perfilUsuario != null && !perfilUsuario.isStatus())
			return Utilitarios.pergunta("Tem certeza que você deseja desativar este perfil de usuário?");
		return true;
	}

	private void validarDadosObrigatorios() throws Exception {
		if(perfilUsuario.getNome().equals("")) 
			throw new Exception("Para cadastrar um perfil de usuário é necessário informar o nome");
		
		int telasPermitidas = 0;
		for(Tela tela: perfilUsuario.getTelas()) {
			if(tela.isStatus()) {
				telasPermitidas++;
				break;
			}
		}
		if(telasPermitidas == 0)
			throw new Exception("Para cadastrar um perfil de usuário e necessário ter ao menos uma tela permitida");
	}

	public ObservableList<PerfilUsuario> consultar(Campo campo, Comparacao comparacao, String termo) throws SQLException, Exception {
		PerfilUsuarioBusiness perfilUsuarioBusiness = new PerfilUsuarioBusiness();
		return FXCollections.observableArrayList(perfilUsuarioBusiness.consultar(campo.getNome(), comparacao.getComparacao(), comparacao.getTermo(termo)));
	}

	public void setPerfilUsuario(Object perfilUsuario) {
		this.perfilUsuario = (PerfilUsuario) perfilUsuario;
	}

}
