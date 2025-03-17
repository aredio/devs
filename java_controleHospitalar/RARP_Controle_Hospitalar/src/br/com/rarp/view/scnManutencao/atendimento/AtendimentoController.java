package br.com.rarp.view.scnManutencao.atendimento;

import java.time.format.DateTimeFormatter;

import br.com.rarp.control.AtendimentoCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Atendimento;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnControleAtendimento.ControleAtendimentoController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class AtendimentoController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Controle de Atendimentos");
		getLblTitle().setTextFill(Paint.valueOf("#000000"));
		getLblTitle().setStyle("-fx-background-color: #FF4500;"
				+ "-fx-font-weight: bold");

		TableColumn<Atendimento, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Atendimento,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue() != null) {
					value = param.getValue().getCodigo() + "";
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<Atendimento, String> responsavel = new TableColumn<>("Responsável");
		responsavel.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Atendimento,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getResponsavel() != null) {
					value = param.getValue().getResponsavel().getNome();
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<Atendimento, String> data = new TableColumn<>("Data");
		data.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Atendimento,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getDataAtendimento() != null) {
					value = param.getValue().getDataAtendimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<Atendimento, String> horarioInicial = new TableColumn<>("Hora inicial");
		horarioInicial.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Atendimento,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getHoraIni() != null) {
					value = param.getValue().getHoraIni().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<Atendimento, String> horarioFinal = new TableColumn<>("Hora Final");
		horarioFinal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Atendimento,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getHoraFim() != null) {
					value = param.getValue().getHoraFim().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<Atendimento, String> status = new TableColumn<>("Status");
		status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Atendimento,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
				if(param != null && param.getValue() != null && param.getValue().isStatus())
					return new SimpleStringProperty("Ativado");
				else
					return new SimpleStringProperty("Desativado");
			}
		});
		
		codigo.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.13));
		responsavel.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.3));
		data.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.15));
		horarioInicial.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.13));
		horarioFinal.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.13));
		status.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.1));

		tblManutencao.getColumns().setAll(codigo, responsavel, data, horarioInicial, horarioFinal, status);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());
	}

	public void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("ATE.codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("MOV.data", "Data", TipoCampo.data));
		cmbCampo.getItems().add(new Campo("ATE.status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		AtendimentoCtrl atendimentoCtrl = new AtendimentoCtrl();
		try {
			tblManutencao.setItems(atendimentoCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue()
							: txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar os atendimentos.\n" + "Descrição: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarControleAtendimento(TipoMovimentacao.insercao);
			ControleAtendimentoController controler = new ControleAtendimentoController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarControleAtendimento(TipoMovimentacao.alteracao);
			ControleAtendimentoController controller = new ControleAtendimentoController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				AtendimentoCtrl atendimentoCtrl = new AtendimentoCtrl();
				atendimentoCtrl.setAtendimento(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(atendimentoCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarControleAtendimento(TipoMovimentacao.visualizaco);
			ControleAtendimentoController controller = new ControleAtendimentoController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				AtendimentoCtrl atendimentoCtrl = new AtendimentoCtrl();
				atendimentoCtrl.setAtendimento(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(atendimentoCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}

