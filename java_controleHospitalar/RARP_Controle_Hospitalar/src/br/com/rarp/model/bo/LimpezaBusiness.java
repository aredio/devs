package br.com.rarp.model.bo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Leito;
import br.com.rarp.model.Limpeza;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.dao.LimpezaDAO;

public class LimpezaBusiness {

	public void salvar(Limpeza limpeza, Limpeza limpezaAnt) throws Exception {
		try {
			LimpezaDAO limpezaDAO = new LimpezaDAO();
			if(limpezaAnt != null)
				for(Leito l: limpezaAnt.getLeitos())
					l.setSujo(true);
			if(limpeza.isStatus()) {
				validarLimpeza(limpeza);
				for(Leito l: limpeza.getLeitos())
					l.setSujo(false);
			} 
			limpezaDAO.salvar(limpeza, limpezaAnt);
		} catch (Exception e) {
			for(Leito l: limpeza.getLeitos())
				l.setSujo(true);
			throw new Exception(e.getMessage());
		}
	}

	private void validarLimpeza(Limpeza limpeza) throws Exception {
		if(limpeza.getDtMovimentacao().isAfter(LocalDate.now()))
			throw new Exception("A data informada deve ser menor que a data atual");
		
		if(limpeza.getDtMovimentacao().isEqual(LocalDate.now()) && limpeza.getHrMovimentacao().isAfter(LocalTime.now()))
			throw new Exception("A hora informada deve ser menor que a hora atual");
	}

	public List<Limpeza> consultar(String campo, String comparacao, String termo) throws Exception {
		return new LimpezaDAO().consultar(campo, comparacao, termo);
	}

	public List<Limpeza> consultar(LocalDate dataIni, LocalDate dataFin, LocalTime horaIni, LocalTime horaFin,
			Funcionario funcionarioLimpeza, List<Leito> leitos, Usuario usuario, Boolean status) throws ClassNotFoundException, SQLException, Exception {
		return new LimpezaDAO().consultar(dataIni, dataFin, horaIni, horaFin,
				funcionarioLimpeza, leitos, usuario, status);
	}

}
