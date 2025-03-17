package br.com.rarp.view.scnManutencao.espaco;

import br.com.rarp.control.EspacoCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Espaco;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroEspaco.CadastroEspacoController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class EspacoController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Manutenção de Espaços");
		getLblTitle().setStyle("-fx-background-color: #FDE10C;"
				+ "-fx-font-weight: bold");
		
		TableColumn<Espaco, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		
		TableColumn<Espaco, String> numero = new TableColumn<>("Nome");
		numero.setCellValueFactory(new PropertyValueFactory<>("nome"));
		numero.setPrefWidth(150);
		
		TableColumn<Espaco, String> bloco = new TableColumn<>("Bloco");
		bloco.setCellValueFactory(new PropertyValueFactory<>("bloco"));
		
		TableColumn<Espaco, String> andar = new TableColumn<>("Andar");
		andar.setCellValueFactory(new PropertyValueFactory<>("andar"));
		

		tblManutencao.getColumns().addAll(codigo, numero, bloco, andar);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());
	}

	private void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("nome", "Nome", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("bloco", "Bloco", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("andar", "Andar", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		EspacoCtrl espacoCtrl = new EspacoCtrl();
		try {
			tblManutencao.setItems(espacoCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue()
							: txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar os espaços.\n" + "Descrição: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoEspaco(TipoMovimentacao.insercao);
			CadastroEspacoController controler = new CadastroEspacoController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoEspaco(TipoMovimentacao.alteracao);
			CadastroEspacoController controller = new CadastroEspacoController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				EspacoCtrl espacoCtrl = new EspacoCtrl();
				espacoCtrl.setUsuario(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(espacoCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoEspaco(TipoMovimentacao.visualizaco);
			CadastroEspacoController controller = new CadastroEspacoController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				EspacoCtrl espacoCtrl = new EspacoCtrl();
				espacoCtrl.setUsuario(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(espacoCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

}
