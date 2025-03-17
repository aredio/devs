package br.com.rarp.view.scnManutencao.perfilUsuario;

import br.com.rarp.control.PerfilUsuarioCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.PerfilUsuario;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroPerfilUsuario.CadastroPerfilUsuarioController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class PerfilUsuarioController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Manutenção de Perfil de Usuário");
		getLblTitle().setStyle("-fx-background-color: #F34227;"
				+ "-fx-font-weight: bold");
		
		TableColumn<PerfilUsuario, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		codigo.setPrefWidth(100);
		
		TableColumn<PerfilUsuario, String> nome = new TableColumn<>("Nome");
		nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		nome.setPrefWidth(500);
		
		tblManutencao.getColumns().addAll(codigo, nome);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());
	}

	private void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("nome", "Nome", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		PerfilUsuarioCtrl perfilUsuarioCtrl = new PerfilUsuarioCtrl();
		try {
			tblManutencao.setItems(perfilUsuarioCtrl.consultar(
					cmbCampo.getSelectionModel().getSelectedItem(), 
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue() : txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar os perfis de usuario.\n"
					   + "Descri��o: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoPerfilUsuario(TipoMovimentacao.insercao);
			CadastroPerfilUsuarioController controller = new CadastroPerfilUsuarioController();
			controller.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoPerfilUsuario(TipoMovimentacao.alteracao);
			CadastroPerfilUsuarioController controller = new CadastroPerfilUsuarioController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				PerfilUsuarioCtrl perfilUsuarioCtrl = new PerfilUsuarioCtrl();
				perfilUsuarioCtrl.setPerfilUsuario(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(perfilUsuarioCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoPerfilUsuario(TipoMovimentacao.visualizaco);
			CadastroPerfilUsuarioController controller = new CadastroPerfilUsuarioController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				PerfilUsuarioCtrl perfilUsuarioCtrl = new PerfilUsuarioCtrl();
				perfilUsuarioCtrl.setPerfilUsuario(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(perfilUsuarioCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

}
