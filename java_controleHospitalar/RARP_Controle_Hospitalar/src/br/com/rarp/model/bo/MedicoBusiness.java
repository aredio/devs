package br.com.rarp.model.bo;

import java.time.LocalDate;
import java.util.List;

import br.com.rarp.model.Medico;
import br.com.rarp.model.dao.MedicoDAO;
import br.com.rarp.utils.Utilitarios;

public class MedicoBusiness {

	public void salvar(Medico medico) throws Exception {
		if(medico == null)
			throw new Exception("O medico n�o foi inst�nciado");
		
		if(medico.isStatus())
			validarMedico(medico);

		MedicoDAO medicoDAO = new MedicoDAO();
		medicoDAO.salvar(medico);
	}

	private void validarMedico(Medico medico) throws Exception {
		if(medico.getDtNascimento().isAfter(LocalDate.now()))
			throw new Exception("A data informada deve ser menor que a data atual");
		if(!Utilitarios.isCPF(medico.getCpfSemMascara()))
			throw new Exception("CPF inválido");
		if (medico.getCodigoMedico() == 0)
			if (!new MedicoDAO().consultar("MED.codigo_funcionario ", " = ", String.valueOf(medico.getCodigo())).isEmpty()) {
				throw new Exception("Fucionario ja relaciona a um medico");
			}
	}

	public List<Medico> consultar(String campo, String comparacao, String termo) throws Exception {
		// TODO Auto-generated method stub
		MedicoDAO medicoDAO = new MedicoDAO();
		return medicoDAO.consultar(campo, comparacao, termo);

	}

}
