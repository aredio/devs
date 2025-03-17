package br.com.rarp.view.scnCadastroCargo;

import java.net.URL;
import java.util.ResourceBundle;
import br.com.rarp.control.CargoCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.Funcao;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.SwitchButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CadastroCargoController extends Application implements Initializable {

	private static boolean visualizando;
	private static Stage stage;
	
    @FXML
    private AnchorPane pnlPrincipal;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnVoltar;

	@FXML
	private SwitchButton sbAtivado;
	
    @FXML
    private TextField txtNome;

    @FXML
    private AutoCompleteComboBox<Funcao> cmbFuncao;

    @FXML
    private TextField txtNivel;

    @FXML
    private TextArea txtRequisitos;
	
	@FXML
	private IntegerTextField txtCodigo;
	
	private static CargoCtrl cargoCtrl;

	@Override
	public void start(Stage stage) throws Exception {
		setStage(stage);
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CadastroCargo.fxml"))));
		stage.setTitle("Cadastro de Cargo");
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();
				
				if(event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
	}

	public Stage getStage() {
		return stage;
	}

	@SuppressWarnings("static-access")
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@SuppressWarnings("static-access")
	public void alterar(CargoCtrl cargoCtrl) throws Exception {
		this.cargoCtrl = cargoCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepararTela();
		if (cargoCtrl != null && cargoCtrl.getCargo() != null)
			preencherTela();
		
		if (visualizando)
			bloquearTela();
	}
	
	private void prepararTela() {
		sbAtivado.setValue(true);
		txtCodigo.setDisable(true);
		cmbFuncao.setItems(FXCollections.observableArrayList(Funcao.values()));
		
		pnlPrincipal.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getTarget() != null 
						&& event.getTarget() instanceof Node 
						&& ((Node) event.getTarget()).getId() != null
						&& event.getCode() == KeyCode.TAB) {
					String id = ((Node) event.getTarget()).getId();
					if (!event.isShiftDown()) {
						
						if(id.equals("txtRequisitos")) {
							btnSalvar.requestFocus();
						}
					}
					if (event.isShiftDown()) {	
						if(id.equals("btnSalvar")) {
							txtRequisitos.requestFocus();
						}
					} 
				}
			}
		});
	}

	public void inserir() throws Exception {
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	private void limparCampos() {
		txtCodigo.clear();
		txtNome.clear();
		cmbFuncao.getSelectionModel().select(-1);
		txtNivel.clear();
		txtRequisitos.clear();
		sbAtivado.setValue(true);
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	private void preencherObjeto() {
		if (cargoCtrl == null) {
			cargoCtrl = new CargoCtrl();
		}
		if (cargoCtrl.getCargo() == null) {
			cargoCtrl.novoCargo();
		}
		
		cargoCtrl.getCargo().setCodigo(txtCodigo.getValue());
		cargoCtrl.getCargo().setNome(txtNome.getText());
		cargoCtrl.getCargo().setFuncao(cmbFuncao.getSelectionModel().getSelectedItem());
		cargoCtrl.getCargo().setNivel(txtNivel.getText());
		cargoCtrl.getCargo().setRequisitos(txtRequisitos.getText());
		cargoCtrl.getCargo().setStatus(sbAtivado.getValue());
	}

	private void preencherTela() {
		txtCodigo.setValue(cargoCtrl.getCargo().getCodigo());
		txtNome.setText(cargoCtrl.getCargo().getNome());
		cmbFuncao.getSelectionModel().select(cargoCtrl.getCargo().getFuncao());
		txtNivel.setText(cargoCtrl.getCargo().getNivel());
		txtRequisitos.setText(cargoCtrl.getCargo().getRequisitos());
		sbAtivado.setValue(cargoCtrl.getCargo().isStatus());
	}

	@SuppressWarnings("static-access")
	public void visualizar(CargoCtrl cargoCtrl) throws Exception {
		visualizando = true;
		this.cargoCtrl = cargoCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@FXML
	private void salvar() {
		preencherObjeto();
		try {
			if(cargoCtrl.salvar()) {
				Utilitarios.message("Cargo salvo com sucesso.");
				limparCampos();
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar o cargo.\n" + "Descrição: " + e.getMessage());
		}
	}
	
    @FXML
    private void voltar(ActionEvent event) {
    	cargoCtrl = null;
    	stage.hide();
    	visualizando = false;
    }
}