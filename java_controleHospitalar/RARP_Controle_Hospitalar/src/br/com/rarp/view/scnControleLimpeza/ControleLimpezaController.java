package br.com.rarp.view.scnControleLimpeza;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import br.com.rarp.control.EspacoCtrl;
import br.com.rarp.control.FuncionarioCtrl;
import br.com.rarp.control.LimpezaCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.Funcao;
import br.com.rarp.model.Espaco;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Leito;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroFuncionario.CadastroFuncionarioController;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
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
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalTimeTextField;

public class ControleLimpezaController extends Application implements Initializable {

	private static Stage stage;

	private static LimpezaCtrl limpezaCtrl;
	private static LimpezaCtrl limpezaCtrlAnt;
	private static boolean visualizando;

    @FXML
    private IntegerTextField txtCodigo;

    @FXML
    private SwitchButton sbAtivado;

    @FXML
    private DatePicker txtData;

    @FXML
    private LocalTimeTextField txtHora;

    @FXML
    private SelectionNode<ImageCard> pnlLeitos;

    @FXML
    private AutoCompleteComboBox<Espaco> cmbEspaco;

    @FXML
    private AutoCompleteComboBox<Funcionario> cmbFuncionarioLimpeza;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnVoltar;
    
    @FXML
    void inserirFuncionario(ActionEvent event) {
    	try {
			new CadastroFuncionarioController().inserir();
			prepararTela();
		} catch (Exception e) {
			Utilitarios.erro("Não foi possvel inserir um funcionário.\n" + e.getMessage());
			e.printStackTrace();
		}  
    }

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("ControleLimpeza.fxml"))));
		stage.setTitle("Cadastro de Atendimentos");
		setStage(stage);
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();

				if (event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
		stage.setMinWidth(650);
		stage.setMinHeight(500);
	}

	public Stage getStage() {
		return stage;
	}

	@SuppressWarnings("static-access")
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void limparCampos() {
		txtCodigo.clear();
		txtData.setPromptText("");
		txtHora.setPromptText("");

		sbAtivado.switchOnProperty().set(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepararTela();
		if (limpezaCtrl != null && limpezaCtrl.getLimpeza() != null)
			preencherTela();
		if (visualizando)
			bloquearTela();
	}
	
	private void prepararTela() {
		sbAtivado.switchOnProperty().set(true);
		txtCodigo.setDisable(true);
		txtData.setValue(LocalDate.now());
		txtHora.setLocalTime(LocalTime.now());
		limpezaCtrlAnt = new LimpezaCtrl();
		
		cmbEspaco.setOnAction((vent) -> {
			if(cmbEspaco.getValue() != null 
				&& cmbEspaco.getValue().getLeitos() != null) {
				pnlLeitos.getItems().clear();
				for (Leito leito: cmbEspaco.getValue().getLeitos()) {
					if(leito.isSujo()) {
						ImageCard img = new ImageCard();
						img.setLeito(leito);
						img.getPathImage().set(getClass().getResource("../img/leitoSujo.png").toString());
						pnlLeitos.getItems().add(img);
					} else if(!leito.isSujo() 
							&& limpezaCtrl != null 
							&& limpezaCtrl.getLimpeza() != null 
							&& limpezaCtrl.getLimpeza().getLeitos() != null 
							&& limpezaCtrl.getLimpeza().getLeitos().contains(leito)) {
						ImageCard img = new ImageCard();
						leito.setSujo(true);
						img.setLeito(leito);
						img.getPathImage().set(getClass().getResource("../img/leitoSujo.png").toString());
						pnlLeitos.getItems().add(img);
					}
				}
			}
		});
		
		try {
			cmbEspaco.getItems().setAll(new EspacoCtrl().getEspacos());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbFuncionarioLimpeza.getItems().setAll(new FuncionarioCtrl().getFuncionarios(Funcao.limpeza));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	public void inserir() throws Exception {
		limpezaCtrl = null;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void alterar(LimpezaCtrl limpezaCtrl) throws Exception {
		this.limpezaCtrl = limpezaCtrl;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void visualizar(LimpezaCtrl limpezaCtrl) throws Exception {
		visualizando = true;
		this.limpezaCtrl = limpezaCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@FXML
	private void salvar(ActionEvent event) {
		try {
			if(limpezaCtrl != null && limpezaCtrl.getLimpeza() != null && limpezaCtrlAnt.getLimpeza() == null) {				
				limpezaCtrlAnt.novaLimpeza();
				for(Leito l: limpezaCtrl.getLimpeza().getLeitos())
					limpezaCtrlAnt.getLimpeza().getLeitos().add(l.clone());
			}
			preencherObjeto();
			if (limpezaCtrl.salvar(limpezaCtrlAnt)) {
				Utilitarios.message("Limpeza salva com sucesso.");
				limparCampos();
			}
			voltar(new ActionEvent());
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar atendimento.\n" + "Descrição: " + e.getMessage());
		}
		limpezaCtrl = null;
	}

	@FXML
	private void voltar(ActionEvent event) {
		limpezaCtrl = null;
		limpezaCtrlAnt = null;
		stage.hide();
		visualizando = false;
	}

	private void preencherObjeto() {
		if (limpezaCtrl == null)
			limpezaCtrl = new LimpezaCtrl();

		if (limpezaCtrl.getLimpeza() == null)
			limpezaCtrl.novaLimpeza();

		limpezaCtrl.getLimpeza().setCodigo(txtCodigo.getValue());
		limpezaCtrl.getLimpeza().setDtMovimentacao(txtData.getValue());
		limpezaCtrl.getLimpeza().setHrMovimentacao(txtHora.getLocalTime());
		limpezaCtrl.getLimpeza().getLeitos().clear();
		for(ImageCard img : pnlLeitos.getSelectionModel().getSelectedItems())
			limpezaCtrl.getLimpeza().getLeitos().add(img.getLeito());
		limpezaCtrl.getLimpeza().setFuncionarioLimpeza(cmbFuncionarioLimpeza.getValue());
		limpezaCtrl.getLimpeza().setStatus(sbAtivado.getValue());
		limpezaCtrl.getLimpeza().setUsuario(SistemaCtrl.getInstance().getUsuarioSessao());
	}

	private void preencherTela() {
		txtCodigo.setText(limpezaCtrl.getLimpeza().getCodigo() + "");
		if(limpezaCtrl.getLimpeza().getDtMovimentacao() != null)
			txtData.setValue(limpezaCtrl.getLimpeza().getDtMovimentacao());
		if(limpezaCtrl.getLimpeza().getHrMovimentacao() != null)
			txtHora.setLocalTime(limpezaCtrl.getLimpeza().getHrMovimentacao());
		cmbFuncionarioLimpeza.setValue(limpezaCtrl.getLimpeza().getFuncionarioLimpeza());
		if(limpezaCtrl.getLimpeza().getLeitos().size() > 0) {
			cmbEspaco.getSelectionModel().select(limpezaCtrl.getLimpeza().getLeitos().get(0).getEspaco());
			cmbEspaco.fireEvent(new ActionEvent());
			for(Leito l: limpezaCtrl.getLimpeza().getLeitos()) {
				ImageCard img = new ImageCard();
				img.setLeito(l);
				pnlLeitos.getSelectionModel().select(img);
			}
		}
		sbAtivado.setValue(limpezaCtrl.getLimpeza().isStatus());
	}
}
