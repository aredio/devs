package br.com.rarp.view.scnControleSaida;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import br.com.rarp.control.EntradaPacienteCtrl;
import br.com.rarp.control.SaidaPacienteCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
import br.com.rarp.view.scnComponents.IntegerTextField;
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
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalTimeTextField;

public class ControleSaidaController extends Application implements Initializable {

	private static Stage stage;

	private static SaidaPacienteCtrl saidaPacienteCtrl;
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
    private AutoCompleteComboBox<EntradaPaciente> cmbEntradaPaciente;

    @FXML
    private TextArea txtEstadoPaciente;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnVoltar;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("ControleSaida.fxml"))));
		stage.setTitle("Cadastro de Saída de Pacientes");
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
		cmbEntradaPaciente.setValue(null);
		txtEstadoPaciente.setText("");
		sbAtivado.switchOnProperty().set(true);
		prepararTela();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepararTela();
		if (saidaPacienteCtrl != null && saidaPacienteCtrl.getSaidaPaciente() != null)
			preencherTela();
		if (visualizando)
			bloquearTela();
	}
	
	private void prepararTela() {
		sbAtivado.switchOnProperty().set(true);
		txtCodigo.setDisable(true);
		txtData.setValue(LocalDate.now());
		txtHora.setLocalTime(LocalTime.now());
		try {
			cmbEntradaPaciente.getItems().setAll(new EntradaPacienteCtrl().getEntradasAbertas());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	public void inserir() throws Exception {
		saidaPacienteCtrl = null;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void alterar(SaidaPacienteCtrl saidaPacienteCtrl) throws Exception {
		this.saidaPacienteCtrl = saidaPacienteCtrl;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void visualizar(SaidaPacienteCtrl saidaPacienteCtrl) throws Exception {
		visualizando = true;
		this.saidaPacienteCtrl = saidaPacienteCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@FXML
	private void salvar(ActionEvent event) {
		try {
			preencherObjeto();
			if (saidaPacienteCtrl.salvar()) {
				Utilitarios.message("Saída de paciente salva com sucesso.");
				limparCampos();
			}
			voltar(new ActionEvent());
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar sa�da de paciente.\n" + "Descrição: " + e.getMessage());
		}
		saidaPacienteCtrl = null;
	}

	@FXML
	private void voltar(ActionEvent event) {
		saidaPacienteCtrl = null;
		stage.hide();
		visualizando = false;
	}

	private void preencherObjeto() {
		if (saidaPacienteCtrl == null)
			saidaPacienteCtrl = new SaidaPacienteCtrl();

		if (saidaPacienteCtrl.getSaidaPaciente() == null)
			saidaPacienteCtrl.novaSaidaPaciente();

		saidaPacienteCtrl.getSaidaPaciente().setCodigo(txtCodigo.getValue());
		saidaPacienteCtrl.getSaidaPaciente().setDtMovimentacao(txtData.getValue());
		saidaPacienteCtrl.getSaidaPaciente().setHrMovimentacao(txtHora.getLocalTime());
		saidaPacienteCtrl.getSaidaPaciente().setEntradaPaciente(cmbEntradaPaciente.getValue());
		saidaPacienteCtrl.getSaidaPaciente().setEstadoPaciente(txtEstadoPaciente.getText());
		saidaPacienteCtrl.getSaidaPaciente().setStatus(sbAtivado.getValue());
		saidaPacienteCtrl.getSaidaPaciente().setUsuario(SistemaCtrl.getInstance().getUsuarioSessao());
	}

	private void preencherTela() {
		txtCodigo.setText(saidaPacienteCtrl.getSaidaPaciente().getCodigo() + "");
		if(saidaPacienteCtrl.getSaidaPaciente().getDtMovimentacao() != null)
			txtData.setValue(saidaPacienteCtrl.getSaidaPaciente().getDtMovimentacao());
		if(saidaPacienteCtrl.getSaidaPaciente().getHrMovimentacao() != null)
			txtHora.setLocalTime(saidaPacienteCtrl.getSaidaPaciente().getHrMovimentacao());
		cmbEntradaPaciente.getItems().add(saidaPacienteCtrl.getSaidaPaciente().getEntradaPaciente());
		cmbEntradaPaciente.getSelectionModel().selectLast();
		txtEstadoPaciente.setText(saidaPacienteCtrl.getSaidaPaciente().getEstadoPaciente());
		sbAtivado.setValue(saidaPacienteCtrl.getSaidaPaciente().isStatus());
	}
}
