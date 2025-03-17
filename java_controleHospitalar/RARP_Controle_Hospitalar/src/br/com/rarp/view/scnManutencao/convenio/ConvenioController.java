package br.com.rarp.view.scnManutencao.convenio;

import br.com.rarp.control.ConvenioCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Convenio;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroConvenio.CadastroConvenioController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ConvenioController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Manutenção de Convênios");
		getLblTitle().setStyle("-fx-background-color: #f9dd02;"
				+ "-fx-font-weight: bold");

		TableColumn<Convenio, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		TableColumn<Convenio, String> nome = new TableColumn<>("Nome");
		nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		TableColumn<Convenio, String> cpf = new TableColumn<>("CNPJ");
		cpf.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
		TableColumn<Convenio, String> telefone = new TableColumn<>("Telefone");
		telefone.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Convenio,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Convenio, String> param) {
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
		TableColumn<Convenio, String> ans = new TableColumn<>("Registro da ANS");
		ans.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Convenio,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Convenio, String> param) {
				String value = "";
				if(param.getValue() != null)
					value = param.getValue().getANS();
				return new SimpleStringProperty(value);
			}
		});
		
		nome.setPrefWidth(250);
		cpf.setPrefWidth(200);
		telefone.setPrefWidth(250);
		ans.setPrefWidth(150);

		tblManutencao.getColumns().addAll(codigo, nome, cpf, telefone, ans);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());

	}

	public void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("conv.codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("pe.nome", "Nome", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("pj.cnpj", "CNPJ", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("conv.ans", "Registro da ANS", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("conv.status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		ConvenioCtrl convenioCtrl = new ConvenioCtrl();
		try {
			tblManutencao.setItems(convenioCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue()
							: txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar os convénios.\n" + "Descrição: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoConvenio(TipoMovimentacao.insercao);
			CadastroConvenioController controler = new CadastroConvenioController();
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
			CadastroConvenioController controller = new CadastroConvenioController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				ConvenioCtrl convenioCtrl = new ConvenioCtrl();
				convenioCtrl.setConvenio(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(convenioCtrl);
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
			CadastroConvenioController controller = new CadastroConvenioController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				ConvenioCtrl convenioCtrl = new ConvenioCtrl();
				convenioCtrl.setConvenio(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(convenioCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}
