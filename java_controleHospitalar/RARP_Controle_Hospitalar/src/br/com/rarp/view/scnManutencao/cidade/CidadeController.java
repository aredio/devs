package br.com.rarp.view.scnManutencao.cidade;

import br.com.rarp.control.CidadeCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Cidade;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroCidade.CadastroCidadeController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class CidadeController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Manutenção de Cidades");
		getLblTitle().setTextFill(Paint.valueOf("#FFFFFF"));
		getLblTitle().setStyle("-fx-background-color: #471B1B;"
				+ "-fx-font-weight: bold");
		
		TableColumn<Cidade, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		TableColumn<Cidade, String> nome = new TableColumn<>("Nome");
		nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		TableColumn<Cidade, String> estado = new TableColumn<>("Estado");
		estado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cidade,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Cidade, String> param) {
				String value = "";
				if(param.getValue() != null && param.getValue().getEstado() != null)
					value = param.getValue().getEstado().getNome();
				return new SimpleStringProperty(value);
			}
		});
		
		
		tblManutencao.getColumns().addAll(codigo, nome, estado);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());
	}

	private void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("nome", "Nome", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("uf_estado", "Sigla do Estado", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		CidadeCtrl cidadeCtrl = new CidadeCtrl();
		try {
			tblManutencao.setItems(cidadeCtrl.consultar(
					cmbCampo.getSelectionModel().getSelectedItem(), 
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue() : txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar as cidades.\n"
					   + "Descrição: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoUsuario(TipoMovimentacao.insercao);
			CadastroCidadeController controler = new CadastroCidadeController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoCidade(TipoMovimentacao.alteracao);
			CadastroCidadeController controller = new CadastroCidadeController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				CidadeCtrl cidadeCtrl = new CidadeCtrl();
				cidadeCtrl.setCidade(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(cidadeCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoCidade(TipoMovimentacao.visualizaco);
			CadastroCidadeController controller = new CadastroCidadeController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				CidadeCtrl cidadeCtrl = new CidadeCtrl();
				cidadeCtrl.setCidade(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(cidadeCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}
