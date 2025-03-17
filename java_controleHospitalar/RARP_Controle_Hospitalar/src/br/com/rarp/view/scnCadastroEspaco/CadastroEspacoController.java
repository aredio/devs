package br.com.rarp.view.scnCadastroEspaco;

import java.net.URL;
import java.util.ResourceBundle;
import br.com.rarp.control.EspacoCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Leito;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnComponents.ImageCard;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.SelectionNode;
import br.com.rarp.view.scnComponents.SwitchButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class CadastroEspacoController extends Application implements Initializable {

	private static Stage stage;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnVoltar;

	@FXML
	private IntegerTextField txtCodigo;

    @FXML
    private TextField txtNome;

	@FXML
	private SwitchButton sbAtivado;

	@FXML
	private IntegerTextField txtNumero;

	@FXML
	private TextField txtBloco;

	@FXML
	private TextField txtAndar;

	@FXML
	private IntegerTextField txtNumeroLeito;

	@FXML
	private Button btnAdicionar;

	@FXML
	private Button btnRemover;

	@FXML
	private ScrollPane sclLeitos;

	@FXML
	private SelectionNode<ImageCard> pnlLeitos;

	private static EspacoCtrl espacoCtrl;
	private static boolean visualizando;

	@FXML
	private void adicionar(ActionEvent event) {
		ImageCard imageCard = new ImageCard();
		imageCard.getPathImage().set(getClass().getResource("../img/patient128x128.png").toString());
		if(txtNumeroLeito.getText().isEmpty())
			txtNumeroLeito.setText("1");
		imageCard.setLeito(new Leito(Utilitarios.strToInt(txtNumeroLeito.getText(), 1)));
		if(pnlLeitos.getItems().contains(imageCard))
			Utilitarios.atencao("Este leito ja foi inserido");
		else {
			pnlLeitos.getItems().add(imageCard);
			txtNumeroLeito.setValue(txtNumeroLeito.getValue() + 1);
		}
	}

	@FXML
	private void remover(ActionEvent event) {
		if(Utilitarios.pergunta("Tem certeza que deseja remover esses leitos?"))
			pnlLeitos.removeSelecteds();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CadastroEspaco.fxml"))));
		stage.setTitle("Cadastro de Espaço");
		setStage(stage);
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();
				
				if(event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
		stage.setResizable(false);
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		CadastroEspacoController.stage = stage;
	}

	private void limparCampos() {
		txtCodigo.clear();
		txtNumeroLeito.clear();
		txtNome.clear();
		txtAndar.clear();
		txtBloco.clear();
		pnlLeitos.getChildren().clear();
		sbAtivado.switchOnProperty().set(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sbAtivado.switchOnProperty().set(true);
		txtCodigo.setDisable(true);
		if (espacoCtrl != null && espacoCtrl.getClass() != null)
			preencherTela();
		if (visualizando)
			bloquearTela();
		pnlLeitos.prefWidthProperty().bind(sclLeitos.prefWidthProperty());
		pnlLeitos.prefHeightProperty().bind(sclLeitos.prefHeightProperty());
		pnlLeitos.prefWrapLengthProperty().bind(sclLeitos.prefWidthProperty());
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	public void inserir() throws Exception {
		espacoCtrl = null;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void alterar(EspacoCtrl espacoCtrl) throws Exception {
		this.espacoCtrl = espacoCtrl;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void visualizar(EspacoCtrl espacoCtrl) throws Exception {
		visualizando = true;
		this.espacoCtrl = espacoCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@FXML
	private void salvar(ActionEvent event) {
		preencherObjeto();
		try {
			if(espacoCtrl.salvar()) {
				Utilitarios.message("Espaço salvo com sucesso.");
				limparCampos();
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar espaço.\n"
						   + "Descrição: " + e.getMessage());
		}
	}

	@FXML
	private void voltar(ActionEvent event) {
		espacoCtrl = null;
		stage.hide();
		visualizando = false;
	}

	private void preencherObjeto() {
		if (espacoCtrl == null)
			espacoCtrl = new EspacoCtrl();

		if (espacoCtrl.getEspaco() == null)
			espacoCtrl.novoEspaco();

		espacoCtrl.getEspaco().setCodigo(txtCodigo.getValue());
		espacoCtrl.getEspaco().setAndar(txtAndar.getText());
		espacoCtrl.getEspaco().setNome(txtNome.getText());
		espacoCtrl.getEspaco().setBloco(txtBloco.getText());
		for(Leito l: espacoCtrl.getEspaco().getLeitos())
			l.setStatus(false);
		
		for(ImageCard img: pnlLeitos.getItems()) {
			if(espacoCtrl.getEspaco().getLeitos().contains(img.getLeito()))
				espacoCtrl.getEspaco().getLeitos().get(espacoCtrl.getEspaco().getLeitos().indexOf(img.getLeito())).setStatus(true);
			else
				espacoCtrl.getEspaco().getLeitos().add(img.getLeito());
		}
		espacoCtrl.getEspaco().setStatus(sbAtivado.switchOnProperty().get());
	}

	private void preencherTela() {
		txtCodigo.setText(espacoCtrl.getEspaco().getCodigo() + "");
		txtNome.setText(espacoCtrl.getEspaco().getNome());
		txtBloco.setText(espacoCtrl.getEspaco().getBloco());
		txtAndar.setText(espacoCtrl.getEspaco().getAndar());

		for (Leito l: espacoCtrl.getEspaco().getLeitos()) {
			ImageCard img = new ImageCard();
			img.getPathImage().set(getClass().getResource("../img/patient128x128.png").toString());
			img.setLeito(l);
			pnlLeitos.getItems().add(img);
		}

		sbAtivado.switchOnProperty().set(espacoCtrl.getEspaco().isStatus());
	}

}
