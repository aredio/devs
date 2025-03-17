package br.com.rarp.view.scnControleEncaminhamento;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import br.com.rarp.control.EncaminhamentoCtrl;
import br.com.rarp.control.EntradaPacienteCtrl;
import br.com.rarp.control.EspacoCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Espaco;
import br.com.rarp.model.Leito;
import br.com.rarp.model.Paciente;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroEspaco.CadastroEspacoController;
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

public class ControleEncaminhamentoController extends Application implements Initializable {
	
	private static Stage stage;

    @FXML
    private IntegerTextField txtCodigo;
    
    @FXML
    private DatePicker txtData;

    @FXML
    private LocalTimeTextField txtHora;

    @FXML
    private SwitchButton sbAtivado;
    
    @FXML
    private AutoCompleteComboBox<Espaco> cmbOrigem;

    @FXML
    private AutoCompleteComboBox<Espaco> cmbDestino;
    
    @FXML
    private AutoCompleteComboBox<EntradaPaciente> cmbEntradaPaciente;
    
    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnVoltar;
    
    @FXML
    private SelectionNode<ImageCard> pnlOrigem;

    @FXML
    private SelectionNode<ImageCard> pnlDestino;

	private static EncaminhamentoCtrl encaminhamentoCtrl;
	private EncaminhamentoCtrl encaminhamentoCtrlAntigo;
	private static boolean visualizando;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("ControleEncaminhamento.fxml"))));
		stage.setTitle("Cadastro de Encaminhamentos");
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
		stage.setMinWidth(650);
		stage.setMinHeight(500);
	}
	

    @FXML
    void inserirEspaco(ActionEvent event) {
    	try {
			new CadastroEspacoController().inserir();
			prepararTela();
		} catch (Exception e) {
			Utilitarios.erro("Não foi possível inserir um espaço.\n" + e.getMessage());
			e.printStackTrace();
		}  
    }

	public Stage getStage() {
		return stage;
	}

	@SuppressWarnings("static-access")
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void limparCampos() {
		txtData.setValue(LocalDate.now());
		txtHora.setLocalTime(LocalTime.now());
		txtCodigo.clear();
		cmbDestino.getItems().clear();
		cmbOrigem.getItems().clear();
		try {
			cmbOrigem.getItems().setAll(new EspacoCtrl().getEspacosCheios(null));
			cmbDestino.getItems().setAll(new EspacoCtrl().getEspacosLivres(null));		
		} catch (Exception e) {
			e.printStackTrace();
		}
		cmbEntradaPaciente.getSelectionModel().select(-1);
		pnlDestino.getItems().clear();
		pnlOrigem.getItems().clear();
		sbAtivado.switchOnProperty().set(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		prepararTela();
		if (encaminhamentoCtrl != null && encaminhamentoCtrl.getEncaminhamento() != null)
			preencherTela();
		if (visualizando)
			bloquearTela();
	}
	
	public void filtrarOrigem(Paciente paciente) {
		if(paciente != null && encaminhamentoCtrl == null) {
			pnlOrigem.getItems().clear();
			try {
				cmbOrigem.getSelectionModel().clearSelection();
				cmbOrigem.getItems().setAll(new EspacoCtrl().getEspacosCheios(paciente));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void prepararTela() {
		txtData.setValue(LocalDate.now());
		txtHora.setLocalTime(LocalTime.now());
		sbAtivado.switchOnProperty().set(true);
		txtCodigo.setDisable(true);
		try {
			if(encaminhamentoCtrl == null) {
				cmbOrigem.getItems().setAll(new EspacoCtrl().getEspacosCheios(null));
				cmbDestino.getItems().setAll(new EspacoCtrl().getEspacosLivres(null));
			} else {
				cmbOrigem.getItems().setAll(new EspacoCtrl().getEspacos());
				cmbDestino.getItems().setAll(new EspacoCtrl().getEspacos());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbEntradaPaciente.getItems().setAll(new EntradaPacienteCtrl().getEntradasPaciente());
		} catch (Exception e) {
			e.printStackTrace();
		}
		cmbOrigem.setOnAction(onChange);
		cmbDestino.setOnAction(onChange);
		cmbEntradaPaciente.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			filtrarOrigem(newValue.getPaciente());
		});
	}
	
	EventHandler<ActionEvent> onChange = (event) -> {
		if(event.getSource() == cmbOrigem && cmbOrigem.getValue() != null) {
			pnlOrigem.getItems().clear();
			for (Leito l: cmbOrigem.getValue().getLeitos()) {
				ImageCard img = new ImageCard();
				img.getPathImage().set(getClass().getResource("../img/patient128x128.png").toString());
				img.setLeito(l);
				pnlOrigem.getItems().add(img);
			}
		}
		
		if(event.getSource() == cmbDestino && cmbDestino.getValue() != null) {
			pnlDestino.getItems().clear();
			for (Leito l: cmbDestino.getValue().getLeitos()) {
				ImageCard img = new ImageCard();
				img.getPathImage().set(getClass().getResource("../img/leitos.png").toString());
				img.setLeito(l);
				pnlDestino.getItems().add(img);
			}
		}
	};

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	public void inserir() throws Exception {
		encaminhamentoCtrl = null;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void alterar(EncaminhamentoCtrl encaminhamentoCtrl) throws Exception {
		this.encaminhamentoCtrl = encaminhamentoCtrl;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void visualizar(EncaminhamentoCtrl encaminhamentoCtrl) throws Exception {
		visualizando = true;
		this.encaminhamentoCtrl = encaminhamentoCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@FXML
	private void salvar(ActionEvent event) {	
		try {
			if(encaminhamentoCtrl != null)
				encaminhamentoCtrlAntigo = encaminhamentoCtrl.clone();
			preencherObjeto();
			if(encaminhamentoCtrl.salvar(encaminhamentoCtrlAntigo)) {
				Utilitarios.message("Encaminhamento salvo com sucesso.");
				limparCampos();
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar espaço.\n"
						   + "Descrição: " + e.getMessage());
		}
		encaminhamentoCtrl = null;
	}

	@FXML
	private void voltar(ActionEvent event) {
		encaminhamentoCtrl = null;
		stage.hide();
		visualizando = false;
	}

	private void preencherObjeto() {
		if (encaminhamentoCtrl == null)
			encaminhamentoCtrl = new EncaminhamentoCtrl();

		if (encaminhamentoCtrl.getEncaminhamento() == null)
			encaminhamentoCtrl.novoEncaminhamento();
		
		encaminhamentoCtrl.getEncaminhamento().setDtMovimentacao(txtData.getValue());
		encaminhamentoCtrl.getEncaminhamento().setHrMovimentacao(txtHora.getLocalTime());
		encaminhamentoCtrl.getEncaminhamento().setEntradaPaciente(cmbEntradaPaciente.getValue());
		encaminhamentoCtrl.getEncaminhamento().setCodigo(txtCodigo.getValue());
		if(pnlDestino.getValue() != null)
			encaminhamentoCtrl.getEncaminhamento().setDestino(pnlDestino.getValue().getLeito());
		if(pnlOrigem.getValue() != null)
			encaminhamentoCtrl.getEncaminhamento().setOrigem(pnlOrigem.getValue().getLeito());
		encaminhamentoCtrl.getEncaminhamento().setStatus(sbAtivado.getValue());
		encaminhamentoCtrl.getEncaminhamento().setUsuario(SistemaCtrl.getInstance().getUsuarioSessao());
	}

	private void preencherTela() {
		txtCodigo.setText(encaminhamentoCtrl.getEncaminhamento().getCodigo() + "");
		cmbEntradaPaciente.setValue(encaminhamentoCtrl.getEncaminhamento().getEntradaPaciente());
		cmbOrigem.getSelectionModel().select(encaminhamentoCtrl.getEncaminhamento().getOrigem().getEspaco());
		cmbDestino.getSelectionModel().select(encaminhamentoCtrl.getEncaminhamento().getDestino().getEspaco());
		onChange.handle(new ActionEvent(cmbOrigem, null));
		onChange.handle(new ActionEvent(cmbDestino, null));
		txtData.setValue(encaminhamentoCtrl.getEncaminhamento().getDtMovimentacao());
		txtHora.setLocalTime(encaminhamentoCtrl.getEncaminhamento().getHrMovimentacao());
		
		ImageCard origem = new ImageCard();
		origem.getPathImage().set(getClass().getResource("../img/patient128x128.png").toString());
		origem.setLeito(encaminhamentoCtrl.getEncaminhamento().getOrigem());
		
		ImageCard destino = new ImageCard();
		destino.getPathImage().set(getClass().getResource("../img/patient128x128.png").toString());
		destino.setLeito(encaminhamentoCtrl.getEncaminhamento().getDestino());
		
		pnlOrigem.getSelectionModel().select(origem);
		pnlDestino.getSelectionModel().select(destino);
		sbAtivado.setValue(encaminhamentoCtrl.getEncaminhamento().isStatus());
	}

}
