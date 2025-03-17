package br.com.rarp.view.scnManutencao.limpeza;

import java.time.format.DateTimeFormatter;

import br.com.rarp.control.LimpezaCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Limpeza;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnControleLimpeza.ControleLimpezaController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class LimpezaController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Controle de limpeza");
		getLblTitle().setTextFill(Paint.valueOf("#FFFFFF"));
		getLblTitle().setStyle("-fx-background-color: #00008B;"
				+ "-fx-font-weight: bold");

		TableColumn<Limpeza, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Limpeza,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Limpeza, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue() != null) {
					value = param.getValue().getCodigo() + "";
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<Limpeza, String> funcionarioLimpeza = new TableColumn<>("Responsável");
		funcionarioLimpeza.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Limpeza,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Limpeza, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getFuncionarioLimpeza() != null) {
					value = param.getValue().getFuncionarioLimpeza().getNome();
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<Limpeza, String> data = new TableColumn<>("Data");
		data.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Limpeza,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Limpeza, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getDtMovimentacao() != null) {
					value = param.getValue().getDtMovimentacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<Limpeza, String> hora = new TableColumn<>("Hora");
		hora.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Limpeza,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Limpeza, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getHrMovimentacao() != null) {
					value = param.getValue().getHrMovimentacao().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<Limpeza, String> status = new TableColumn<>("Status");
		status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Limpeza,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Limpeza, String> param) {
				if(param != null && param.getValue() != null && param.getValue().isStatus())
					return new SimpleStringProperty("Ativado");
				else
					return new SimpleStringProperty("Desativado");
			}
		});
		
		codigo.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.15));
		funcionarioLimpeza.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.30));
		data.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.2));
		hora.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.2));
		status.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.15));

		tblManutencao.getColumns().setAll(codigo, funcionarioLimpeza, data, hora, status);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());
	}

	public void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("LIM.codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("MOV.data", "Data", TipoCampo.data));
		cmbCampo.getItems().add(new Campo("LIM.status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		LimpezaCtrl limpezaCtrl = new LimpezaCtrl();
		try {
			tblManutencao.setItems(limpezaCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue()
							: txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar as limpezas.\n" + "Descrição: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarControleLimpeza(TipoMovimentacao.insercao);
			ControleLimpezaController controler = new ControleLimpezaController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarControleLimpeza(TipoMovimentacao.alteracao);
			ControleLimpezaController controller = new ControleLimpezaController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				LimpezaCtrl limpezaCtrl = new LimpezaCtrl();
				limpezaCtrl.setLimpeza(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(limpezaCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarControleLimpeza(TipoMovimentacao.visualizaco);
			ControleLimpezaController controller = new ControleLimpezaController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				LimpezaCtrl limpezaCtrl = new LimpezaCtrl();
				limpezaCtrl.setLimpeza(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(limpezaCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}

