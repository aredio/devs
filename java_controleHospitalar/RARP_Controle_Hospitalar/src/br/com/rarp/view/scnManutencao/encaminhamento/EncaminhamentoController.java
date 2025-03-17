package br.com.rarp.view.scnManutencao.encaminhamento;

import br.com.rarp.control.EncaminhamentoCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Encaminhamento;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnControleEncaminhamento.ControleEncaminhamentoController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class EncaminhamentoController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Controle de Encaminhamentos");
		getLblTitle().setTextFill(Paint.valueOf("#C0FF3E"));
		getLblTitle().setStyle("-fx-background-color: #000000;"
				+ "-fx-font-weight: bold");

		TableColumn<Encaminhamento, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		
		TableColumn<Encaminhamento, String> origem = new TableColumn<>("Leito de origem");
		origem.setCellValueFactory(new PropertyValueFactory<>("origem"));
		
		TableColumn<Encaminhamento, String> destino = new TableColumn<>("Leito de destino");
		destino.setCellValueFactory(new PropertyValueFactory<>("destino"));
		
		TableColumn<Encaminhamento, String> status = new TableColumn<>("Status");
		status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Encaminhamento,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Encaminhamento, String> param) {
				if(param != null && param.getValue() != null && param.getValue().isStatus())
					return new SimpleStringProperty("Ativado");
				else
					return new SimpleStringProperty("Desativado");
			}
		});
		
		codigo.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.2));
		origem.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.3));
		destino.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.3));
		status.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.2));

		tblManutencao.getColumns().addAll(codigo, origem, destino, status);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());
	}

	public void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("ENC.codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("MOV.data", "Data", TipoCampo.data));
		cmbCampo.getItems().add(new Campo("ORI.nome", "Leito de Origem", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("DEST.nome", "Leito de Destino", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("ENC.status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		EncaminhamentoCtrl encaminhamentoCtrl = new EncaminhamentoCtrl();
		try {
			tblManutencao.setItems(encaminhamentoCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue()
							: txtTermo.getText()));
			if(tblManutencao.getItems() == null || (tblManutencao.getItems() != null && tblManutencao.getItems().size() == 0))
				Utilitarios.atencao("Nenhum registro foi encontrado.");
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar os encaminhamentos.\n" + "Descrição: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarControleEncaminhamento(TipoMovimentacao.insercao);
			ControleEncaminhamentoController controler = new ControleEncaminhamentoController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarControleEncaminhamento(TipoMovimentacao.alteracao);
			ControleEncaminhamentoController controller = new ControleEncaminhamentoController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				EncaminhamentoCtrl encaminhamentoCtrl = new EncaminhamentoCtrl();
				encaminhamentoCtrl.setEncaminhamento(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(encaminhamentoCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarControleEncaminhamento(TipoMovimentacao.visualizaco);
			ControleEncaminhamentoController controller = new ControleEncaminhamentoController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				EncaminhamentoCtrl encaminhamentoCtrl = new EncaminhamentoCtrl();
				encaminhamentoCtrl.setEncaminhamento(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(encaminhamentoCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}

