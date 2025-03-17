package br.com.rarp.view.scnManutencao.paciente;

import br.com.rarp.control.PacienteCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Paciente;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroPaciente.CadastroPacienteController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class PacienteController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Manutenção de Pacientes");
		getLblTitle().setStyle("-fx-background-color: #0BDFF2;"
				+ "-fx-font-weight: bold");

		TableColumn<Paciente, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		TableColumn<Paciente, String> nome = new TableColumn<>("Nome");
		nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		TableColumn<Paciente, String> cpf = new TableColumn<>("CPF");
		cpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		TableColumn<Paciente, String> telefone = new TableColumn<>("Telefone");
		telefone.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Paciente,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Paciente, String> param) {
				String value = "";
				if(param.getValue() != null && param.getValue().getTelefones() != null)
					for (int i = 0; i < param.getValue().getTelefones().size() ; i++) {
						if(i == (param.getValue().getTelefones().size() - 1))
							value += param.getValue().getTelefones().get(i).getNumero();
						else
							value += param.getValue().getTelefones().get(i).getNumero() + " - ";
					}			
				return new SimpleStringProperty(value);
			}
		});
		
		nome.setPrefWidth(250);
		cpf.setPrefWidth(200);
		telefone.setPrefWidth(250);

		tblManutencao.getColumns().addAll(codigo, nome, cpf, telefone);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());

	}

	public void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("pac.codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("pe.nome", "Nome", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("pf.cpf", "CPF", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("pac.status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		PacienteCtrl pacienteCtrl = new PacienteCtrl();
		try {
			tblManutencao.setItems(pacienteCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue()
							: txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar os pacientes.\n" + "Descrição: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoUsuario(TipoMovimentacao.insercao);
			CadastroPacienteController controler = new CadastroPacienteController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoUsuario(TipoMovimentacao.alteracao);
			CadastroPacienteController controller = new CadastroPacienteController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				PacienteCtrl pacienteCtrl = new PacienteCtrl();
				pacienteCtrl.setPaciente(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(pacienteCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoUsuario(TipoMovimentacao.visualizaco);
			CadastroPacienteController controller = new CadastroPacienteController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				PacienteCtrl pacienteCtrl = new PacienteCtrl();
				pacienteCtrl.setPaciente(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(pacienteCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}
