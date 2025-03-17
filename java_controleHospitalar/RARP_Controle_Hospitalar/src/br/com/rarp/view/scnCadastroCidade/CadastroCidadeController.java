package br.com.rarp.view.scnCadastroCidade;

import java.net.URL;
import java.util.ResourceBundle;
import br.com.rarp.control.CidadeCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Estado;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.SwitchButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CadastroCidadeController extends Application implements Initializable {

	private static boolean visualizando;
	
	private static CidadeCtrl cidadeCtrl;
	
	private static Stage stage;

	@FXML
	private IntegerTextField txtCodigo;
	
    @FXML
    private TextField txtNome;

    @FXML
    private IntegerTextField txtIBGE;

    @FXML
    private ComboBox<Estado> cmbEstado;

	@FXML
	private SwitchButton sbAtivado;
	
	@FXML
    private Button btnSalvar;
	
	@FXML
    private Button btnVoltar;
	
    @FXML
    private AnchorPane pnlPrincipal;
	
	@Override
	public void start(Stage stage) throws Exception {
		setStage(stage);
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CadastroCidade.fxml"))));
		stage.setTitle("Cadastro de Cidades");
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();
				
				if(event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
		stage.setMinWidth(380);
		stage.setMinHeight(270);
	}

	public Stage getStage() {
		return stage;
	}

	@SuppressWarnings("static-access")
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@SuppressWarnings("static-access")
	public void alterar(CidadeCtrl cidadeCtrl) throws Exception {
		this.cidadeCtrl = cidadeCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtCodigo.requestFocus();
		pnlPrincipal.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getTarget() != null 
						&& event.getTarget() instanceof Node 
						&& ((Node) event.getTarget()).getId() != null
						&& event.getCode() == KeyCode.TAB) {
					String id = ((Node) event.getTarget()).getId();
					if (!event.isShiftDown()) {
						if(id.equals("cmbEstado"))
							btnSalvar.requestFocus();
						
						if(id.equals("btnSalvar"))
							txtNome.requestFocus();
					}
					if (event.isShiftDown()) {	
						if(id.equals("txtNome"))
							btnSalvar.requestFocus();
						
						if(id.equals("btnSalvar"))
							cmbEstado.requestFocus();
					} 
				}
			}
		});
		prepararTela();
		if (cidadeCtrl != null && cidadeCtrl.getCidade() != null)
			preencherTela();
		if (visualizando)
			bloquearTela();
	}

	private void prepararTela() {
		try {
			sbAtivado.setValue(true);
			txtCodigo.setDisable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inserir() throws Exception {
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	private void limparCampos() {
		txtCodigo.clear();
		txtNome.clear();
		txtIBGE.clear();
		cmbEstado.getSelectionModel().select(-1);
		sbAtivado.setValue(true);
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	private void preencherObjeto() {
		if (cidadeCtrl == null) {
			cidadeCtrl = new CidadeCtrl();
		}
		if (cidadeCtrl.getCidade() == null) {
			cidadeCtrl.novaCidade();
		}
		cidadeCtrl.getCidade().setCodigo(txtCodigo.getValue());
		cidadeCtrl.getCidade().setNome(txtNome.getText());
		cidadeCtrl.getCidade().setCodigoIBGE(txtIBGE.getValue());
		cidadeCtrl.getCidade().setEstado(cmbEstado.getSelectionModel().getSelectedItem());
		cidadeCtrl.getCidade().setStatus(sbAtivado.getValue());
	}

	private void preencherTela() {
		if (cidadeCtrl != null && cidadeCtrl.getCidade() != null) {
			txtCodigo.setText(cidadeCtrl.getCidade().getCodigo() + "");
			txtNome.setText(cidadeCtrl.getCidade().getNome());
			txtIBGE.setValue(cidadeCtrl.getCidade().getCodigoIBGE());
			if(cidadeCtrl.getCidade().getEstado() != null)
				cmbEstado.getSelectionModel().select(cidadeCtrl.getCidade().getEstado());
			sbAtivado.setValue(cidadeCtrl.getCidade().isStatus());
		}
	}

	@SuppressWarnings("static-access")
	public void visualizar(CidadeCtrl cidadeCtrl) throws Exception {
		visualizando = true;
		this.cidadeCtrl = cidadeCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@FXML
	private void salvar() {
		preencherObjeto();
		try {
			if(cidadeCtrl.salvar()) {
				Utilitarios.message("Cidade salva com sucesso.");
				limparCampos();
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar a cidade.\n" + "Descrição: " + e.getMessage());
		}
	}
	
    @FXML
    private void voltar(ActionEvent event) {
    	cidadeCtrl = null;
    	stage.hide();
    	visualizando = false;
    }
}