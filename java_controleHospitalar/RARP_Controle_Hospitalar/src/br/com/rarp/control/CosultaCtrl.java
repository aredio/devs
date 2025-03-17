package br.com.rarp.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.com.rarp.interfaces.Consulta;
import org.com.rarp.interfaces.PessoaFisica;
import org.com.rarp.interfaces.Requisicao;
import org.com.rarp.interfaces.Resposta;
import org.com.rarp.soap.ConsultaSOAP;

import br.com.rarp.model.Atendimento;
import br.com.rarp.model.EntradaPacienteWS;
import br.com.rarp.model.Medico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CosultaCtrl {
	private ConsultaSOAP consultaSOAP = new ConsultaSOAP();
	private Consulta consulta = consultaSOAP.getConsultaSOAPPort();
	private PessoaFisica pessoaFisica = new PessoaFisica();
	private Requisicao requisicao = new Requisicao();

	@SuppressWarnings("rawtypes")
	public ObservableList consultar() throws Exception {
		try {
			List<Resposta> list;
			consulta.sevidorOn(SistemaCtrl.getInstance().getConfiguracoes().getUsuarioRARP());
			
			requisicao.setUsuario(SistemaCtrl.getInstance().getConfiguracoes().getUsuarioRARP());
			requisicao.setPessoaFisica(pessoaFisica);
			list = consulta.consultar(requisicao);

			List<EntradaPacienteWS> listEn = new ArrayList<>();
			if ((list != null) && (list.size() > 0)) {
				
				for (Resposta resposta : list) {
					
					boolean valida = false;
					for (EntradaPacienteWS entradaPacienteWS : listEn) {
						if((resposta.getHistorico().getPessoaJuridica() == null) ||(resposta.getHistorico().getPessoaJuridica().getNome() == null)
								||(resposta.getHistorico().getPessoaJuridica().getNome().equals("")) || ( entradaPacienteWS.getHospital().equals(resposta.getHistorico().getPessoaJuridica().getNome()))) {
							valida = true;
							break;
						}
							
					}
					
				if (valida)
					continue;
					
					for(org.com.rarp.interfaces.EntradaPaciente entradaPaciente : resposta.getHistorico().getEntradaPacientes()) {
						EntradaPacienteWS entrada = new EntradaPacienteWS();
						entrada.setAlta(entradaPaciente.isAlta());
						Date utilDate = entradaPaciente.getDtMovimentacao().toGregorianCalendar().getTime();
						
						entrada.setDtMovimentacao( LocalDateTime.ofInstant( utilDate.toInstant(), ZoneId.systemDefault() ).toLocalDate());
						entrada.setEmergencia(entradaPaciente.isEmergencia());
						entrada.setPreTriagem(entradaPaciente.getPreTriagem());
						//entrada.setHrMovimentacao(LocalTime.parse(entradaPaciente.getHrMovimentacao().toXMLFormat()));
						
						for (org.com.rarp.interfaces.Atendimento atendimentoWS : entradaPaciente.getAtendimentos()) {
						
							Atendimento atendimento = new Atendimento();
							
							//atendimento.setDataAtendimento( LocalDate.parse(atendimentoWS.getDtMovimentacao().toXMLFormat()));
							atendimento.setDescricao(atendimentoWS.getDescricao());
							atendimento.setDetalheMedico(atendimento.getDetalheMedico());
							
							if (entrada.getAtendimentos() == null ){
								entrada.setAtendimentos(new ArrayList<Atendimento>());
							}
							
							entrada.setDescrioes(entrada.getDescrioes() +" "+atendimento.getDetalheMedico() );
							entrada.getAtendimentos().add(atendimento);
						}
						if (entradaPaciente.getMedico() != null ) {
							entrada.setMedico(new Medico());
							entrada.getMedico().setNome(entradaPaciente.getMedico().getNome());
							
						}
						
						if (resposta.getHistorico().getPessoaJuridica() != null) {
							entrada.setHospital(resposta.getHistorico().getPessoaJuridica().getNome());
						}
						listEn.add(entrada);
					}
				}
			}
			return FXCollections.observableArrayList(listEn);

		} catch (Exception e) {
			throw new Exception("Falha realizar consulta");
		}
	}
	
	public  Object copyAttributesFromTo(Object a, Object b) throws IllegalArgumentException, IllegalAccessException {
		Field[] fieldsFromFirstClass = a.getClass().getDeclaredFields();
		Field[] fieldsFromSecondClass = b.getClass().getDeclaredFields();
		for (Field currentFieldFromTheFirstClass : fieldsFromFirstClass) {
			for (Field currentFieldFromTheSecondClass : fieldsFromSecondClass) {
				String nameOfTheFirstField = currentFieldFromTheFirstClass.getName();
				String nameOfTheSecondField = currentFieldFromTheSecondClass.getName();
				
				//if (currentFieldFromTheFirstClass.getType().getName() == currentFieldFromTheSecondClass.getType().getName())
				
				if (nameOfTheFirstField.equals(nameOfTheSecondField)) {
					currentFieldFromTheFirstClass.setAccessible(true);
					currentFieldFromTheSecondClass.setAccessible(true);
					currentFieldFromTheFirstClass.set(a, currentFieldFromTheSecondClass.get(b));
				}
			}
		}
		return a;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public Requisicao getRequisicao() {
		return requisicao;
	}

	public void setRequisicao(Requisicao requisicao) {
		this.requisicao = requisicao;
	}

}
