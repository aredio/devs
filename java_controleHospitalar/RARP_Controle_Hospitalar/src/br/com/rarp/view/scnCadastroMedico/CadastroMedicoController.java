package br.com.rarp.view.scnCadastroMedico;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.rarp.control.EspecialidadeCtrl;
import br.com.rarp.control.FuncionarioCtrl;
import br.com.rarp.control.MedicoCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Especialidade;
import br.com.rarp.model.Funcionario;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroEspecialidade.CadastroEspecialidadeController;
import br.com.rarp.view.scnCadastroFuncionario.CadastroFuncionarioController;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.SwitchButton;
import br.com.rarp.view.scnComponents.TextFieldFormatter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CadastroMedicoController extends Application implements Initializable {
	private static boolean visualizando;

	private static Stage stage;

	private static MedicoCtrl medicoCtrl;

	@FXML // fx:id="txtCodig"
	private IntegerTextField txtCodigo; // Value injected by FXMLLoader

	@FXML // fx:id="cmbFuncionario"
	private AutoCompleteComboBox<Funcionario> cmbFuncionario; // Value injected by FXMLLoader

	@FXML // fx:id="cmbEspecialidades"
	private AutoCompleteComboBox<Especialidade> cmbEspecialidades; // Value injected by FXMLLoader

	@FXML // fx:id="tbvEspecialidades"
	private TableView<Especialidade> tbvEspecialidades; // Value injected by FXMLLoader

	@FXML // fx:id="tbcNome"
	private TableColumn<Especialidade, Especialidade> tbcNome; // Value injected by FXMLLoader

	@FXML // fx:id="txtCRM"
	private TextFieldFormatter txtCRM;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnVoltar;

	@FXML
	private Button btnAdd;

	@FXML
	private Button btnRemove;

	@FXML
	private SwitchButton swStatus;

	public void inserir() throws Exception {
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}
	
    @FXML
    void inserirFuncionario(ActionEvent event) {
    	try {
    		if (medicoCtrl != null) {
    			medicoCtrl = new MedicoCtrl();

    		}
			new CadastroFuncionarioController().inserir();
			prepararTela();
		} catch (Exception e) {
			Utilitarios.erro("Não foi possível inserir um funcionário.\n" + e.getMessage());
			e.printStackTrace();
		}  	
    }
    
    @FXML
    void inserirEspecialidade(ActionEvent event) {
    	try {
			new CadastroEspecialidadeController().inserir();
			prepararTela();
		} catch (Exception e) {
			Utilitarios.erro("Não foi possível inserir uma especialidade.\n" + e.getMessage());
			e.printStackTrace();
		}  
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			prepararTela();
			preencherTela();
		} catch (Exception e) {
			Utilitarios.erro("Falha ao preencher tela");
			e.printStackTrace();
		}
		if (visualizando)
			bloquearTela();
	}

	private void prepararTela() throws Exception {
		cmbFuncionario.getItems().setAll(new FuncionarioCtrl().getFuncionarios());
		cmbEspecialidades.getItems().setAll(new EspecialidadeCtrl().getEspecialidades());
		tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
	}

	private void preencherTela() throws Exception {
		if ((medicoCtrl != null) && (medicoCtrl.getMedico() != null)) {
			cmbFuncionario.getSelectionModel().select(medicoCtrl.getMedico());
			cmbEspecialidades.getSelectionModel().select(-1);
			tbvEspecialidades.getItems().setAll(medicoCtrl.getMedico().getEspecialidades());
			txtCRM.setText(medicoCtrl.getMedico().getCRM());
			txtCodigo.setValue(medicoCtrl.getMedico().getCodigoMedico());
		}	
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	@SuppressWarnings("static-access")
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CadastroMedico.fxml"))));
		primaryStage.setTitle("Cadastro de Especialidades");
		this.stage = primaryStage;
		this.stage.setMinWidth(600);
		this.stage.setMaxWidth(400);
	}

	public static boolean isVisualizando() {
		return visualizando;
	}

	public static void setVisualizando(boolean visualizando) {
		CadastroMedicoController.visualizando = visualizando;
	}

	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		CadastroMedicoController.stage = stage;
	}

	@SuppressWarnings("static-access")
	public void alterar(MedicoCtrl medicoCtrl) throws Exception {
		this.medicoCtrl = medicoCtrl;

		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	private void preencherObjeto() {

		if (medicoCtrl == null) {
			medicoCtrl = new MedicoCtrl();

		}

		if (cmbFuncionario.getSelectedValue() != null) {
			medicoCtrl.novoMedico();
			medicoCtrl.getMedico().setCodigo(cmbFuncionario.getSelectionModel().getSelectedItem().getCodigo());

			medicoCtrl.getMedico().setDtAdmissao(cmbFuncionario.getSelectionModel().getSelectedItem().getDtAdmissao());
			medicoCtrl.getMedico().setCTPS(cmbFuncionario.getSelectionModel().getSelectedItem().getCTPS());
			medicoCtrl.getMedico()
					.setSalarioContratual(cmbFuncionario.getSelectionModel().getSelectedItem().getSalarioContratual());
			medicoCtrl.getMedico().setCpf(cmbFuncionario.getSelectionModel().getSelectedItem().getCpf());
			medicoCtrl.getMedico().setRg(cmbFuncionario.getSelectionModel().getSelectedItem().getRg());
			medicoCtrl.getMedico().setSexo(cmbFuncionario.getSelectionModel().getSelectedItem().getSexo());
			medicoCtrl.getMedico()
					.setPossuiNecessidades(cmbFuncionario.getSelectionModel().getSelectedItem().isPossuiNecessidades());
			medicoCtrl.getMedico().setCertidaoNascimento(
					cmbFuncionario.getSelectionModel().getSelectedItem().getCertidaoNascimento());
			medicoCtrl.getMedico().setNome(cmbFuncionario.getSelectionModel().getSelectedItem().getNome());
			medicoCtrl.getMedico().setLogradouro(cmbFuncionario.getSelectionModel().getSelectedItem().getLogradouro());

			medicoCtrl.getMedico()
					.setDtNascimento(cmbFuncionario.getSelectionModel().getSelectedItem().getDtAdmissao());
			medicoCtrl.getMedico()
					.setComplemento(cmbFuncionario.getSelectionModel().getSelectedItem().getComplemento());
			medicoCtrl.getMedico().setNumero(cmbFuncionario.getSelectionModel().getSelectedItem().getNumero());
			medicoCtrl.getMedico().setBairro(cmbFuncionario.getSelectionModel().getSelectedItem().getBairro());
			medicoCtrl.getMedico().setCep(cmbFuncionario.getSelectionModel().getSelectedItem().getCep());

			medicoCtrl.getMedico().setCidade(cmbFuncionario.getSelectionModel().getSelectedItem().getCidade());

			medicoCtrl.getMedico().setCargo(cmbFuncionario.getSelectionModel().getSelectedItem().getCargo());

			medicoCtrl.getMedico().setTelefones(cmbFuncionario.getSelectionModel().getSelectedItem().getTelefones());

			medicoCtrl.getMedico().setCodigoMedico(txtCodigo.getValue());
			medicoCtrl.getMedico()
					.setCRM(txtCRM.getText());
			medicoCtrl.getMedico().setEspecialidades(tbvEspecialidades.getItems());
			medicoCtrl.getMedico().setStatus(swStatus.getValue());
		} else {
			medicoCtrl.setMedico(null);
		}

	}

	@FXML
	private void salvar(Event event) throws Exception {
		try {
			if (!visualizando) {
				preencherObjeto();
				if (medicoCtrl.salvar()) {
					Utilitarios.message("Medico salvo com sucesso.");
					limparCampos();
				}
			} else {
				Utilitarios.atencao("Este cadastro esta aberto apenas para visualiza��o");
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar o Medico.\n" + "Descri��o: " + e.getMessage());
		}
	}

	private void limparCampos() {
		cmbFuncionario.getSelectionModel().select(-1);
		cmbEspecialidades.getSelectionModel().select(-1);
		tbvEspecialidades.getItems().clear();
		txtCRM.setText("");
	}

	@FXML
	private void voltar(ActionEvent event) {
		medicoCtrl = null;
		stage.hide();
		visualizando = false;
	}

	@FXML
	private void add(ActionEvent e) {
		if (cmbEspecialidades.getSelectionModel().getSelectedIndex() == -1) {
			Utilitarios.atencao("Favor Selecione uma Especialidade para ser adicionada a lista");
			return;
		}

		tbvEspecialidades.getItems().add(cmbEspecialidades.getSelectionModel().getSelectedItem());
	}

	@FXML
	private void remover(ActionEvent e) {
		if (tbvEspecialidades.getSelectionModel().getSelectedIndex() == -1) {
			Utilitarios.atencao("Favor Selecione uma Especialidade para ser remover da lista");
			return;
		}
		tbvEspecialidades.getItems().remove(tbvEspecialidades.getSelectionModel().getSelectedIndex());
	}

	@SuppressWarnings("static-access")
	public void visualizar(MedicoCtrl medicoCtrl) throws Exception {
		visualizando = true;
		this.medicoCtrl = medicoCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

}
