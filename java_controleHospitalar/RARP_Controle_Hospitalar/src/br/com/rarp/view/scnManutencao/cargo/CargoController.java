package br.com.rarp.view.scnManutencao.cargo;

import br.com.rarp.control.CargoCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Cargo;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroCargo.CadastroCargoController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class CargoController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Manutenção de Cargos");
		getLblTitle().setTextFill(Paint.valueOf("#FFFFFF"));
		getLblTitle().setStyle("-fx-background-color: #8F929C;"
				+ "-fx-font-weight: bold");

		TableColumn<Cargo, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		
		TableColumn<Cargo, String> nome = new TableColumn<>("Nome");
		nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		TableColumn<Cargo, String> funcao = new TableColumn<>("Função");
		funcao.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cargo,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Cargo, String> param) {
				String value = "";
				if(param != null && param.getValue() != null && param.getValue().getFuncao() != null)
					value = param.getValue().getFuncao().toString();
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<Cargo, String> nivel = new TableColumn<>("Nível");
		nivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
		
		TableColumn<Cargo, String> status = new TableColumn<>("Status");
		status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cargo,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Cargo, String> param) {
				if(param != null && param.getValue() != null && param.getValue().isStatus())
					return new SimpleStringProperty("Ativado");
				else
					return new SimpleStringProperty("Desativado");
			}
		});
		
		codigo.setPrefWidth(100);
		nome.setPrefWidth(300);
		funcao.setPrefWidth(300);
		nivel.setPrefWidth(200);
		status.setPrefWidth(200);

		tblManutencao.getColumns().addAll(codigo, nome, funcao, nivel, status);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());

	}

	public void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("nome", "Nome", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("funcao", "Função", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("nivel", "Nível", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		CargoCtrl cargoCtrl = new CargoCtrl();
		try {
			tblManutencao.setItems(cargoCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue()
							: txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar os cargos.\n" + "Descrição: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoCargo(TipoMovimentacao.insercao);
			CadastroCargoController controler = new CadastroCargoController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoCargo(TipoMovimentacao.alteracao);
			CadastroCargoController controller = new CadastroCargoController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				CargoCtrl cargoCtrl = new CargoCtrl();
				cargoCtrl.setCargo(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(cargoCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoCargo(TipoMovimentacao.visualizaco);
			CadastroCargoController controller = new CadastroCargoController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				CargoCtrl cargoCtrl = new CargoCtrl();
				cargoCtrl.setCargo(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(cargoCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}

