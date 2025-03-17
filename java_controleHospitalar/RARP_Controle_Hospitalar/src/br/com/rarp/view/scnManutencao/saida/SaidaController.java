package br.com.rarp.view.scnManutencao.saida;

import java.time.format.DateTimeFormatter;

import br.com.rarp.control.SaidaPacienteCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.SaidaPaciente;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnControleSaida.ControleSaidaController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class SaidaController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Controle de Saída de Paciente");
		getLblTitle().setTextFill(Paint.valueOf("#FFFFFF"));
		getLblTitle().setStyle("-fx-background-color: #4682B4;"
				+ "-fx-font-weight: bold");

		TableColumn<SaidaPaciente, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SaidaPaciente,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<SaidaPaciente, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue() != null) {
					value = param.getValue().getCodigo() + "";
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<SaidaPaciente, String> data = new TableColumn<>("Data");
		data.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SaidaPaciente,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<SaidaPaciente, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getDtMovimentacao() != null) {
					value = param.getValue().getDtMovimentacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<SaidaPaciente, String> hora = new TableColumn<>("Hora");
		hora.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SaidaPaciente, String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<SaidaPaciente, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getHrMovimentacao() != null) {
					value = param.getValue().getHrMovimentacao().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<SaidaPaciente, String> status = new TableColumn<>("Status");
		status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SaidaPaciente,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<SaidaPaciente, String> param) {
				if(param != null && param.getValue() != null && param.getValue().isStatus())
					return new SimpleStringProperty("Ativado");
				else
					return new SimpleStringProperty("Desativado");
			}
		});
		
		codigo.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.15));
		data.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.2));
		hora.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.2));
		status.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.15));

		tblManutencao.getColumns().setAll(codigo, data, hora, status);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());
	}

	public void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("SAI.codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("MOV.data", "Data da Saída", TipoCampo.data));
		cmbCampo.getItems().add(new Campo("SAI.status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		SaidaPacienteCtrl saidaPacienteCtrl = new SaidaPacienteCtrl();
		try {
			configurarTermo();
			tblManutencao.setItems(saidaPacienteCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar as saidas de pacientes.\n" + "Descrição: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarControleSaida(TipoMovimentacao.insercao);
			ControleSaidaController controler = new ControleSaidaController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarControleSaida(TipoMovimentacao.alteracao);
			ControleSaidaController controller = new ControleSaidaController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				SaidaPacienteCtrl saidaPacienteCtrl = new SaidaPacienteCtrl();
				saidaPacienteCtrl.setSaidaPaciente(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(saidaPacienteCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarControleSaida(TipoMovimentacao.visualizaco);
			ControleSaidaController controller = new ControleSaidaController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				SaidaPacienteCtrl saidaPacienteCtrl = new SaidaPacienteCtrl();
				saidaPacienteCtrl.setSaidaPaciente(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(saidaPacienteCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}

