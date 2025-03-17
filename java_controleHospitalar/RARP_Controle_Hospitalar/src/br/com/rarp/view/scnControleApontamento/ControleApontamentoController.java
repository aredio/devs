package br.com.rarp.view.scnControleApontamento;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTimePicker;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.utils.MyAppointmentGroup;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class ControleApontamentoController extends Application implements Initializable {
	private static Stage stage;

	@FXML
	private AnchorPane pnlPrincipal;

	@FXML
	private JFXTimePicker txtHoraInicial;

	@FXML
	private JFXTimePicker txtHoraFinal;

	@FXML
	private JFXDatePicker txtData;

	@FXML
	private JFXTextArea txtDescricao;

	@FXML
	private JFXComboBox<MyAppointmentGroup> cmbCor;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnVoltar;

	private static Appointment appointment;

	@Override
	public void start(Stage stage) throws Exception {
		setStage(stage);
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("ControleApontamento.fxml"))));
		stage.setTitle("Cadastro de apontamento");
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();

				if (event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
		stage.setMinWidth(370);
		stage.setMinHeight(320);
	}

	public Stage getStage() {
		return stage;
	}

	@SuppressWarnings("static-access")
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepararTela();
		if (appointment != null)
			preencherTela();
	}

	@SuppressWarnings("static-access")
	public void abrir(Appointment param) throws Exception {
		this.appointment = param;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	private void prepararTela() {
		cmbCor.getItems().setAll(SistemaCtrl.getInstance().getApontamentos());
		
		cmbCor.onActionProperty().set((event) -> {
			cmbCor.setBackground(new Background(new BackgroundFill(cmbCor.getValue().getPaint(), null, null)));
		});
		
		cmbCor.setCellFactory(new Callback<ListView<MyAppointmentGroup>, ListCell<MyAppointmentGroup>>() {
			
			@Override
			public ListCell<MyAppointmentGroup> call(ListView<MyAppointmentGroup> param) {
				
				final ListCell<MyAppointmentGroup> cell = new ListCell<MyAppointmentGroup>() {
                    @Override 
                    public void updateItem(MyAppointmentGroup item, boolean empty) {
                    	super.updateItem(item, empty);
                        if (item != null) { 
                            setText(item.getDescription());    
                            //setTextFill(item.getPaint());
                            setBackground(new Background(new BackgroundFill(item.getPaint(), null, null)));
                        }
                        else {
                            setText("<Sem cor>");
                        }
                    }
				};
				return cell;
			}
		});
	}

	public void inserir() throws Exception {
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	private void preencherObjeto() {
		appointment.setAppointmentGroup(cmbCor.getSelectionModel().getSelectedItem());
		appointment.setDescription(txtDescricao.getText());
		appointment.setEndLocalDateTime(LocalDateTime.of(txtData.getValue(), txtHoraFinal.getValue()));
		appointment.setStartLocalDateTime(LocalDateTime.of(txtData.getValue(), txtHoraInicial.getValue()));
	}

	private void preencherTela() {
		cmbCor.getSelectionModel().select(cmbCor.getItems().indexOf(appointment.getAppointmentGroup()));
		txtDescricao.setText(appointment.getDescription());
		txtData.setValue(appointment.getStartLocalDateTime().toLocalDate());
		txtHoraFinal.setValue(appointment.getEndLocalDateTime().toLocalTime());
		txtHoraInicial.setValue(appointment.getStartLocalDateTime().toLocalTime());
	}

	@FXML
	private void salvar(ActionEvent event) {
		preencherObjeto();
		voltar(event);
	}

	@FXML
	private void voltar(ActionEvent event) {
		stage.hide();
	}
}