package br.com.rarp.view.scnManutencao.funcionario;

import br.com.rarp.control.FuncionarioCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Funcionario;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroFuncionario.CadastroFuncionarioController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class FuncionarioController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() {
		getLblTitle().setText("Manutenção de Funcionários");
		getLblTitle().setStyle("-fx-background-color: #5AFF57;"
				+ "-fx-font-weight: bold");

		TableColumn<Funcionario, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		TableColumn<Funcionario, String> nome = new TableColumn<>("Nome");
		nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		TableColumn<Funcionario, String> cpf = new TableColumn<>("CPF");
		cpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		TableColumn<Funcionario, String> cargo = new TableColumn<>("Cargo");
		cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
		TableColumn<Funcionario, String> telefone = new TableColumn<>("Telefone");
		telefone.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Funcionario,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Funcionario, String> param) {
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
		cargo.setPrefWidth(250);
		telefone.setPrefWidth(250);

		tblManutencao.getColumns().addAll(codigo, nome, cpf, cargo, telefone);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());

	}

	public void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("func.codigo", "Código", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("pe.nome", "Nome", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("pf.cpf", "CPF", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("ca.nome", "Nome do Cargo", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("func.status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		FuncionarioCtrl funcionarioCtrl = new FuncionarioCtrl();
		try {
			tblManutencao.setItems(funcionarioCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue()
							: txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar as entradas de pacientes.\n" + "Descri��o: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoFuncionario(TipoMovimentacao.insercao);
			CadastroFuncionarioController controler = new CadastroFuncionarioController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoFuncionario(TipoMovimentacao.alteracao);
			CadastroFuncionarioController controller = new CadastroFuncionarioController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				FuncionarioCtrl funcionarioCtrl = new FuncionarioCtrl();
				funcionarioCtrl.setFuncionario(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(funcionarioCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoFuncionario(TipoMovimentacao.visualizaco);
			CadastroFuncionarioController controller = new CadastroFuncionarioController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				FuncionarioCtrl funcionarioCtrl = new FuncionarioCtrl();
				funcionarioCtrl.setFuncionario(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(funcionarioCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}
