package br.com.rarp.view.scnManutencao.medico;

import br.com.rarp.control.MedicoCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Medico;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroMedico.CadastroMedicoController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class MedicoController extends ManutencaoController {

	
	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Manutenção de Medicos");

		TableColumn<Medico, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigoMedico"));
		TableColumn<Medico, String> nome = new TableColumn<>("Nome");
		nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		TableColumn<Medico, String> CRM = new TableColumn<>("CRM");
		CRM.setCellValueFactory(new PropertyValueFactory<>("CRM"));
		
		codigo.setPrefWidth(100);
		nome.setPrefWidth(1000);
		CRM.setPrefWidth(200);
		
		tblManutencao.getColumns().addAll(codigo, nome,CRM);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());
	}

	public void adicionarCampos() {
		// Adicionar todos os campos que s�o strings num�ricos ou booleanos,
		// para pesquisa.
		cmbCampo.getItems().add(new Campo("MED.codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("PE.nome", "Nome", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("MED.crm", "CRM", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("MED.status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		MedicoCtrl medicoCtrl = new MedicoCtrl();
		try {
			tblManutencao.setItems(medicoCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue()
							: txtTermo.getText()));
			
			
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar as especialidades.\n" + "Descricão: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoEspecialidade(TipoMovimentacao.insercao);
			CadastroMedicoController controler = new CadastroMedicoController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoEspecialidade(TipoMovimentacao.alteracao);
			CadastroMedicoController controller = new CadastroMedicoController();
			
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				MedicoCtrl medicoCtrl = new MedicoCtrl();
				medicoCtrl.setMedico((Medico)tblManutencao .getSelectionModel().getSelectedItem());
				controller.alterar(medicoCtrl);
				pesquisar();
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoEspecialidade(TipoMovimentacao.visualizaco);
			CadastroMedicoController controller = new CadastroMedicoController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				MedicoCtrl medicoCtrl = new MedicoCtrl();
				medicoCtrl.setMedico((Medico) tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(medicoCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}
