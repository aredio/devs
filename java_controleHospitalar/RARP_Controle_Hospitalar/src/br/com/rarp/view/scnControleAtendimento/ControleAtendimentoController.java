package br.com.rarp.view.scnControleAtendimento;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import br.com.rarp.control.AtendimentoCtrl;
import br.com.rarp.control.EntradaPacienteCtrl;
import br.com.rarp.control.FuncionarioCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.StatusAtendimento;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.ReceitaMedica;
import br.com.rarp.model.Sintoma;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroFuncionario.CadastroFuncionarioController;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.SwitchButton;
import br.com.rarp.view.scnControleApontamento.ControleApontamentoController;
import br.com.rarp.view.scnControleEntrada.ControleEntradaController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.scene.control.LocalTimeTextField;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

public class ControleAtendimentoController extends Application implements Initializable {

	private static Stage stage;

	private static AtendimentoCtrl atendimentoCtrl;
	private static boolean visualizando;
	private static boolean salvar = true;
	private static Appointment appointment;

	@FXML
	private IntegerTextField txtCodigo;

	@FXML
	private SwitchButton sbAtivado;

	@FXML
	private DatePicker txtData;

	@FXML
	private LocalTimeTextField txtHora;

	@FXML
	private AutoCompleteComboBox<Funcionario> cmbFuncionario;

	@FXML
	private DatePicker txtDataAtendimento;

	@FXML
	private LocalTimeTextField txtHoraInicial;

	@FXML
	private LocalTimeTextField txtHoraFinal;

	@FXML
	private Agenda agdAtendimento;

	@FXML
	private TextField txtSintoma;

	@FXML
	private ListView<Sintoma> lsSintomas;

	@FXML
	private TextArea txtReceita;

	@FXML
	private AutoCompleteComboBox<EntradaPaciente> cmbEntradaPaciente;

	@FXML
	private TextArea txtDetalheMedico;

	@FXML
	private TextArea txtDescricao;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnVoltar;
	
    @FXML
    private AnchorPane pnlPrincipal;
    
    @FXML
    private TabPane tbpAtendimento;
	
    @FXML
    private AutoCompleteComboBox<StatusAtendimento> cmbStatusAtendimento;
    

    @FXML
    void inserirEntrada(ActionEvent event) {
    	try {
			new ControleEntradaController().inserir();
			prepararTela();
		} catch (Exception e) {
			Utilitarios.erro("Não foi possível inserir uma entrada.\n" + e.getMessage());
			e.printStackTrace();
		}  
    }

    @FXML
    void inserirFuncionario(ActionEvent event) {
    	try {
			new CadastroFuncionarioController().inserir();
			prepararTela();
		} catch (Exception e) {
			Utilitarios.erro("Não foi possível inserir uma funcionário.\n" + e.getMessage());
			e.printStackTrace();
		}  
    }

	@FXML
	void adicionarSintoma(MouseEvent event) {
		if (!txtSintoma.getText().isEmpty())
			lsSintomas.getItems().add(new Sintoma(txtSintoma.getText()));
	}

	@FXML
	void removerSintoma(MouseEvent event) {
		lsSintomas.getItems().remove(lsSintomas.getSelectionModel().getSelectedIndex());
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("ControleAtendimento.fxml"))));
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
		stage.setMinWidth(800);
		stage.setMinHeight(565);
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
		txtData.setValue(null);
		txtData.setValue(LocalDate.now());
		txtHora.setLocalTime(LocalTime.now());
		txtDescricao.clear();
		txtDetalheMedico.clear();
		txtHoraFinal.setLocalTime(null);
		txtHoraInicial.setLocalTime(null);
		txtReceita.clear();
		txtSintoma.clear();
		cmbEntradaPaciente.getSelectionModel().select(-1);
		cmbFuncionario.getSelectionModel().select(-1);
		cmbStatusAtendimento.getSelectionModel().select(0);
		lsSintomas.getItems().clear();
		sbAtivado.switchOnProperty().set(true);
		agdAtendimento.appointments().clear();
		appointment = null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepararTela();
		if (atendimentoCtrl != null && atendimentoCtrl.getAtendimento() != null)
			preencherTela();
		if (visualizando)
			bloquearTela();
	}

	public void atualizarCampos() {
		if (appointment == null) {
			txtDataAtendimento.setPromptText("");
			txtHoraInicial.setPromptText("");
			txtHoraFinal.setPromptText("");
		} else {
			txtDataAtendimento.setValue(Utilitarios.localDateTimeToLocalDate(appointment.getStartLocalDateTime()));
			txtHoraInicial.setLocalTime(appointment.getStartLocalDateTime().toLocalTime());
			txtHoraFinal.setLocalTime(appointment.getEndLocalDateTime().toLocalTime());
		}

	}

	ChangeListener<LocalTime> horaChange = (observable, oldValue, newValue) -> {
		if (txtDataAtendimento.getValue() != null && txtHoraFinal.getLocalTime() != null
				&& txtHoraInicial.getLocalTime() != null)
			if (appointment == null) {
				appointment = new Agenda.AppointmentImplLocal()
						.withStartLocalDateTime(
								LocalDateTime.of(txtDataAtendimento.getValue(), txtHoraInicial.getLocalTime()))
						.withEndLocalDateTime(
								LocalDateTime.of(txtDataAtendimento.getValue(), txtHoraFinal.getLocalTime()))
						.withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"))
						.withSummary("A agendar").withDescription("A agendar");
				agdAtendimento.appointments().add(appointment);
			} else {
				appointment.setStartLocalDateTime(
						LocalDateTime.of(txtDataAtendimento.getValue(), txtHoraInicial.getLocalTime()));
				appointment.setEndLocalDateTime(
						LocalDateTime.of(txtDataAtendimento.getValue(), txtHoraFinal.getLocalTime()));
				if(!agdAtendimento.appointments().contains(appointment))
					agdAtendimento.appointments().add(appointment);
				agdAtendimento.refresh();
			}
	};

	ChangeListener<LocalDate> dataChange = (observable, oldValue, newValue) -> {
		if (txtDataAtendimento.getValue() != null) {
			agdAtendimento.setDisplayedLocalDateTime(LocalDateTime.of(newValue, LocalTime.now()));
			agdAtendimento.refresh();
		}
		if (txtDataAtendimento.getValue() != null && txtHoraFinal.getLocalTime() != null
				&& txtHoraInicial.getLocalTime() != null) {
			if (appointment == null) {
				appointment = new Agenda.AppointmentImplLocal()
						.withStartLocalDateTime(
								LocalDateTime.of(txtDataAtendimento.getValue(), txtHoraInicial.getLocalTime()))
						.withEndLocalDateTime(
								LocalDateTime.of(txtDataAtendimento.getValue(), txtHoraFinal.getLocalTime()))
						.withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"))
						.withSummary("A agendar").withDescription("A agendar");
				agdAtendimento.appointments().add(appointment);
				agdAtendimento.setDisplayedLocalDateTime(LocalDateTime.of(newValue, LocalTime.now()));
				agdAtendimento.refresh();
			} else {
				appointment.setStartLocalDateTime(
						LocalDateTime.of(txtDataAtendimento.getValue(), txtHoraInicial.getLocalTime()));
				appointment.setEndLocalDateTime(
						LocalDateTime.of(txtDataAtendimento.getValue(), txtHoraFinal.getLocalTime()));
				agdAtendimento.refresh();
			}
		}
	};

	private void prepararTela() {
		sbAtivado.switchOnProperty().set(true);
		txtCodigo.setDisable(true);
		txtData.setValue(LocalDate.now());
		txtHora.setLocalTime(LocalTime.now());
		txtHoraFinal.localTimeProperty().addListener(horaChange);
		txtHoraInicial.localTimeProperty().addListener(horaChange);
		txtDataAtendimento.valueProperty().addListener(dataChange);
		cmbStatusAtendimento.getItems().setAll(StatusAtendimento.values());
		cmbStatusAtendimento.getSelectionModel().select(0);
		agdAtendimento.setSkin(new AgendaWeekSkin(agdAtendimento));
		
		cmbFuncionario.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					if(atendimentoCtrl == null)
						atendimentoCtrl = new AtendimentoCtrl();
					if(cmbFuncionario.getValue() != null)
						agdAtendimento.appointments().setAll(atendimentoCtrl.getAppointmentByFuncionario(cmbFuncionario.getValue()));
					agdAtendimento.refresh();
					agdAtendimento.setAllowDragging(agdAtendimento.appointments().size() == 0);
					agdAtendimento.setAllowResize(agdAtendimento.appointments().size() == 0);
				} catch (Exception e) {
					Utilitarios.atencao("Não foi possível obter os atendimentos agendados."
							+ "\nMotivo: " + e.getMessage());
					e.printStackTrace();
				} 
			}
		});
		
		pnlPrincipal.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getTarget() != null 
						&& event.getTarget() instanceof Node 
						&& ((Node) event.getTarget()).getId() != null
						&& event.getCode() == KeyCode.TAB) {
					String id = ((Node) event.getTarget()).getId();
					if (!event.isShiftDown()) {
						if(id.contains("txtHoraFinal"))	{
							tbpAtendimento.getSelectionModel().select(1);
							cmbEntradaPaciente.requestFocus();
						}
						
						if(id.contains("txtDescricao")) {
							btnSalvar.requestFocus();
						}
						
						if(id.contains("btnSalvar")) {
							tbpAtendimento.getSelectionModel().select(0);
							txtData.requestFocus();
						}
					}
					if (event.isShiftDown()) {	
						if(id.contains("btnSalvar")) {
							tbpAtendimento.getSelectionModel().select(1);
							txtDescricao.requestFocus();
						}
						
						if(id.contains("cmbEntradaPaciente")) {
							tbpAtendimento.getSelectionModel().select(1);
							txtHoraFinal.requestFocus();
						}
						
						if(id.contains("txtData")) {
							btnSalvar.requestFocus();
						}
					} 
				}
			}
		});
		
		try {
			cmbFuncionario.getItems().setAll(new FuncionarioCtrl().getFuncionarios());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			cmbEntradaPaciente.getItems().setAll(new EntradaPacienteCtrl().getEntradasPaciente());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		agdAtendimento.editAppointmentCallbackProperty().set((param) -> {
			if (param == appointment) {
				ControleApontamentoController controller = new ControleApontamentoController();
				try {
					controller.abrir(appointment);
				} catch (Exception e) {
					e.printStackTrace();
				}
				atualizarCampos();
				agdAtendimento.refresh();
			}
			return null;
		});

		agdAtendimento.appointmentChangedCallbackProperty().set(new Callback<Agenda.Appointment, Void>() {

			@Override
			public Void call(Appointment param) {
				if (param.equals(appointment)) {
					atualizarCampos();
					agdAtendimento.refresh();
				}
				return null;
			}
		});

		agdAtendimento.newAppointmentCallbackProperty().set((localDateTimeRange) -> {
			if(horarioLivre(localDateTimeRange)) {
				txtDataAtendimento
						.setValue(Utilitarios.localDateTimeToLocalDate(localDateTimeRange.getStartLocalDateTime()));
				txtHoraInicial.setLocalTime(localDateTimeRange.getStartLocalDateTime().toLocalTime());
				txtHoraFinal.setLocalTime(localDateTimeRange.getEndLocalDateTime().toLocalTime());
				if (appointment == null) {
					appointment = newAppointment();
					return appointment;	
				}
			}
			return null;
		});

	}

	private boolean horarioLivre(LocalDateTimeRange localDateTimeRange) {
		for(Appointment ap: agdAtendimento.appointments()) {
			if(ap.equals(appointment))
				continue;
			if(localDateTimeRange.getStartLocalDateTime().isAfter(ap.getStartLocalDateTime()) 
				&& localDateTimeRange.getStartLocalDateTime().isBefore(ap.getEndLocalDateTime()))
				return false;
			if(localDateTimeRange.getEndLocalDateTime().isAfter(ap.getStartLocalDateTime()) 
				&& localDateTimeRange.getEndLocalDateTime().isBefore(ap.getEndLocalDateTime()))
				return false;
			if(localDateTimeRange.getStartLocalDateTime().isBefore(ap.getStartLocalDateTime()) 
				&& localDateTimeRange.getEndLocalDateTime().isAfter(ap.getEndLocalDateTime()))
				return false;
		}
		return true;
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	public void inserir() throws Exception {
		atendimentoCtrl = null;
		visualizando = false;
		salvar = true;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void alterar(AtendimentoCtrl atendimentoCtrl) throws Exception {
		this.atendimentoCtrl = atendimentoCtrl;
		salvar = true;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void visualizar(AtendimentoCtrl atendimentoCtrl) throws Exception {
		visualizando = true;
		salvar = true;
		this.atendimentoCtrl = atendimentoCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@FXML
	private void salvar(ActionEvent event) {
		preencherObjeto();
		try {
			if (salvar && atendimentoCtrl.salvar()) {
				Utilitarios.message("Atendimento salvo com sucesso.");
				limparCampos();
			}
			if (!salvar)
				voltar(event);
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar atendimento.\n" + "Descri��o: " + e.getMessage());
		}
	}

	@FXML
	private void voltar(ActionEvent event) {
		if(event.getSource().equals(btnVoltar))
			atendimentoCtrl = null;
		appointment = null;
		stage.hide();
		visualizando = false;
	}

	private void preencherObjeto() {
		if (atendimentoCtrl == null)
			atendimentoCtrl = new AtendimentoCtrl();

		if (atendimentoCtrl.getAtendimento() == null)
			atendimentoCtrl.novoAtendimento();

		atendimentoCtrl.getAtendimento().setCodigo(txtCodigo.getValue());
		atendimentoCtrl.getAtendimento().setDataAtendimento(txtDataAtendimento.getValue());
		atendimentoCtrl.getAtendimento().setDescricao(txtDescricao.getText());
		atendimentoCtrl.getAtendimento().setDetalheMedico(txtDetalheMedico.getText());
		atendimentoCtrl.getAtendimento().setDtMovimentacao(txtData.getValue());
		atendimentoCtrl.getAtendimento().setEntradaPaciente(cmbEntradaPaciente.getValue());
		atendimentoCtrl.getAtendimento().setHoraFim(txtHoraFinal.getLocalTime());
		atendimentoCtrl.getAtendimento().setHoraIni(txtHoraInicial.getLocalTime());
		atendimentoCtrl.getAtendimento().setHrMovimentacao(txtHora.getLocalTime());
		atendimentoCtrl.getAtendimento().setReceitaMedica(new ReceitaMedica(txtReceita.getText()));
		atendimentoCtrl.getAtendimento().setResponsavel(cmbFuncionario.getValue());
		atendimentoCtrl.getAtendimento().setSintomas(lsSintomas.getItems());
		atendimentoCtrl.getAtendimento().setStatusAtendimento(cmbStatusAtendimento.getValue());
		if(appointment != null && appointment.getAppointmentGroup() != null)
			atendimentoCtrl.getAtendimento().setStyleClass(appointment.getAppointmentGroup().getStyleClass());
		atendimentoCtrl.getAtendimento().setStatus(sbAtivado.getValue());
		atendimentoCtrl.getAtendimento().setUsuario(SistemaCtrl.getInstance().getUsuarioSessao());
	}
	
	private Appointment newAppointment() {
		return new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.of(txtDataAtendimento.getValue(), txtHoraInicial.getLocalTime()))
				.withEndLocalDateTime(LocalDateTime.of(txtDataAtendimento.getValue(), txtHoraFinal.getLocalTime()))
				.withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group23"))
				.withSummary("A agendar").withDescription("A agendar");
	}

	private void preencherTela() {
		if(atendimentoCtrl.getAtendimento().getResponsavel() != null)
			cmbFuncionario.getSelectionModel().select(atendimentoCtrl.getAtendimento().getResponsavel());
		cmbFuncionario.getOnAction().handle(new ActionEvent());
		txtCodigo.setText(atendimentoCtrl.getAtendimento().getCodigo() + "");
		if(atendimentoCtrl.getAtendimento().getDtMovimentacao() != null)
			txtData.setValue(atendimentoCtrl.getAtendimento().getDtMovimentacao());
		if(atendimentoCtrl.getAtendimento().getDataAtendimento() != null)
			txtDataAtendimento.setValue(atendimentoCtrl.getAtendimento().getDataAtendimento());
		txtDescricao.setText(atendimentoCtrl.getAtendimento().getDescricao());
		txtDetalheMedico.setText(atendimentoCtrl.getAtendimento().getDetalheMedico());
		if(atendimentoCtrl.getAtendimento().getHrMovimentacao() != null)
			txtHora.setLocalTime(atendimentoCtrl.getAtendimento().getHrMovimentacao());
		if(atendimentoCtrl.getAtendimento().getHoraFim() != null)
			txtHoraFinal.setLocalTime(atendimentoCtrl.getAtendimento().getHoraFim());
		if(atendimentoCtrl.getAtendimento().getHoraIni() != null)	
			txtHoraInicial.setLocalTime(atendimentoCtrl.getAtendimento().getHoraIni());
		if(atendimentoCtrl.getAtendimento().getReceitaMedica() != null)
			txtReceita.setText(atendimentoCtrl.getAtendimento().getReceitaMedica().getDescricao());
		if(atendimentoCtrl.getAtendimento().getSintomas() != null)
			lsSintomas.getItems().addAll(atendimentoCtrl.getAtendimento().getSintomas());
		if(atendimentoCtrl.getAtendimento().getEntradaPaciente() != null)
			cmbEntradaPaciente.getSelectionModel().select(atendimentoCtrl.getAtendimento().getEntradaPaciente());
		if(atendimentoCtrl.getAtendimento().getStatusAtendimento() != null)
			cmbStatusAtendimento.getSelectionModel().select(atendimentoCtrl.getAtendimento().getStatusAtendimento());
		sbAtivado.setValue(atendimentoCtrl.getAtendimento().isStatus());
	}

	@SuppressWarnings("static-access")
	public boolean abrir(AtendimentoCtrl atendimentoCtrl) throws Exception {
		this.atendimentoCtrl = atendimentoCtrl;
		salvar = false;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
		atendimentoCtrl = this.atendimentoCtrl;
		return atendimentoCtrl != null;
	}
}
