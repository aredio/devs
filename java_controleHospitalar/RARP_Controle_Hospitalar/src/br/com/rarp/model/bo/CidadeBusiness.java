package br.com.rarp.model.bo;

import java.sql.SQLException;
import java.util.List;

import br.com.rarp.model.Cidade;
import br.com.rarp.model.dao.CidadeDAO;

public class CidadeBusiness {

	public void salvar(Cidade cidade) throws Exception {
		validarCidade(cidade);
		CidadeDAO cidadeDAO = new CidadeDAO();
		cidadeDAO.salvar(cidade);
	}

	private void validarCidade(Cidade cidade) throws SQLException, Exception {
		if(cidade.getCodigo() <= 0) {
			CidadeDAO cidadeDAO = new CidadeDAO();
			List<Cidade> cidades = cidadeDAO.consultar("cid.nome", " = ", cidade.getNome());
			if(cidades.size() > 0)
				throw new Exception("Esta cidade ja está cadastrada.");
		}
	}

	public List<Cidade> consultar(String campo, String comparacao, String termo) throws SQLException, Exception {
		return new CidadeDAO().consultar(campo, comparacao, termo);
	}

}
