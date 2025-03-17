package br.com.rarp.view.scnControleEntrada;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import br.com.rarp.control.AtendimentoCtrl;
import br.com.rarp.control.ConvenioCtrl;
import br.com.rarp.control.EntradaPacienteCtrl;
import br.com.rarp.control.FuncionarioCtrl;
import br.com.rarp.control.MedicoCtrl;
import br.com.rarp.control.PacienteCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.Funcao;
import br.com.rarp.model.Atendimento;
import br.com.rarp.model.Convenio;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Medico;
import br.com.rarp.model.Paciente;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnCadastroFuncionario.CadastroFuncionarioController;
import br.com.rarp.view.scnCadastroMedico.CadastroMedicoController;
import br.com.rarp.view.scnCadastroPaciente.CadastroPacienteController;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.SwitchButton;
import br.com.rarp.view.scnControleAtendimento.ControleAtendimentoController;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.LocalTimeTextField;

public class ControleEntradaController extends Application implements Initializable {

	private static Stage stage;
	private static EntradaPacienteCtrl entradaPacienteCtrl;
	private static boolean visualizando;
	
    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnVoltar;

    @FXML
    private IntegerTextField txtCodigo;

    @FXML
    private SwitchButton sbAtivado;
    
	@FXML
    private SwitchButton sbEmergencia;

    @FXML
    private AutoCompleteComboBox<Paciente> cmbPaciente;

    @FXML
    private AutoCompleteComboBox<Medico> cmbMedico;

    @FXML
    private AutoCompleteComboBox<Funcionario> cmbEnfermeira;
    
    @FXML
    private AutoCompleteComboBox<Convenio> cmbConvenio;

    @FXML
    private TextArea txtPreTriagem;

    @FXML
    private TableView<Atendimento> tblAtendimentos;

    @FXML
    private Button btnInserir;

    @FXML
    private Button btnAlterar;

    @FXML
    private Button btnRemover;

    @FXML
    private DatePicker txtData;

    @FXML
    private LocalTimeTextField txtHora;

    @FXML
    private AutoCompleteComboBox<Funcionario> cmbAtendente;

    @FXML
    private SwitchButton sbAlta;
    
    @FXML
    private Label lblMedico;
    
    @FXML
    private Label lblPreTriagem;
    
    @FXML
    private Label lblAtendimentos;
    
    @FXML
    private TableColumn<Atendimento, String> cmnResponsavel;

    @FXML
    private TableColumn<Atendimento, String> cmnData;

    @FXML
    private TableColumn<Atendimento, String> cmnHorarioInicial;

    @FXML
    private TableColumn<Atendimento, String> cmnHorarioFinal;
    
    @FXML
    private Label lblPaciente;
    
    @FXML
    void inserirFuncionario(ActionEvent event) {
    	try {
			new CadastroFuncionarioController().inserir();
			prepararTela();
		} catch (Exception e) {
			Utilitarios.erro("Não foi possivel inserir um funcionário.\n" + e.getMessage());
			e.printStackTrace();
		}  	
    }

    @FXML
    void inserirMedico(ActionEvent event) {
    	try {
			new CadastroMedicoController().inserir();
			prepararTela();
		} catch (Exception e) {
			Utilitarios.erro("Não foi possível inserir um médico.\n" + e.getMessage());
			e.printStackTrace();
		}  	
    }

    @FXML
    void inserirPaciente(ActionEvent event) {
    	try {
			new CadastroPacienteController().inserir();
			prepararTela();
		} catch (Exception e) {
			Utilitarios.erro("Não foi possível inserir um paciente.\n" + e.getMessage());
			e.printStackTrace();
		}  
    }
    
    @FXML
    void inserirAtendimento(ActionEvent event) throws Exception {
    	if(cmbMedico.getValue() == null) {
    		Utilitarios.atencao("Para inserir um atendimento é necessário selecionar um médico.");
    		cmbMedico.requestFocus();
    	} else {
	    	ControleAtendimentoController controller = new ControleAtendimentoController();
	    	AtendimentoCtrl atendimentoCtrl = new AtendimentoCtrl();
	    	atendimentoCtrl.novoAtendimento();
	    	atendimentoCtrl.getAtendimento().setResponsavel(cmbMedico.getValue());
	    	atendimentoCtrl.setAtendimentos(tblAtendimentos.getItems());
	    	atendimentoCtrl.getAtendimento().setStatus(true);
	    	if(controller.abrir(atendimentoCtrl) && !tblAtendimentos.getItems().contains(atendimentoCtrl.getAtendimento()))
	    		tblAtendimentos.getItems().add(atendimentoCtrl.getAtendimento());
	    	tblAtendimentos.refresh();
    	}
    }

    @FXML
    void alterarAtendimento(ActionEvent event) throws Exception {
    	ControleAtendimentoController controller = new ControleAtendimentoController();
    	AtendimentoCtrl atendimentoCtrl = new AtendimentoCtrl();
    	atendimentoCtrl.setAtendimentos(tblAtendimentos.getItems());
    	atendimentoCtrl.setAtendimento(tblAtendimentos.getSelectionModel().getSelectedItem());
    	controller.abrir(atendimentoCtrl);
    	tblAtendimentos.refresh();
    }

    @FXML
    void removerAtendimento(ActionEvent event) {
    	if(Utilitarios.pergunta("Tem certeza que deseja remover este atendimento?"))
    		tblAtendimentos.getItems().remove(tblAtendimentos.getSelectionModel().getSelectedItem());
    }

    @FXML
    void salvar(ActionEvent event) {
		preencherObjeto();
		try {
			if(entradaPacienteCtrl.salvar()) {
				Utilitarios.message("Entrada de paciente salva com sucesso.");
				limparCampos();
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar a entrada de paciente.\n" + "Descrição: " + e.getMessage());
		}
		entradaPacienteCtrl = null;
    }
	
	private void limparCampos() {
		txtCodigo.setText("");
		txtData.setValue(LocalDate.now());
		txtHora.setLocalTime(LocalTime.now());
		txtPreTriagem.setText("");
		cmbAtendente.setValue(null);
		cmbEnfermeira.setValue(null);
		cmbMedico.setValue(null);
		cmbPaciente.setValue(null);
		sbAlta.setValue(false);
		sbAtivado.setValue(true);
		tblAtendimentos.getItems().clear();
		cmbConvenio.setValue(null);
		prepararTela();
	}

	private void preencherObjeto() {
		if(entradaPacienteCtrl == null) 
			entradaPacienteCtrl = new EntradaPacienteCtrl();
		entradaPacienteCtrl.novaEntradaPaciente();
		entradaPacienteCtrl.getEntradaPaciente().setCodigo(txtCodigo.getValue());
		entradaPacienteCtrl.getEntradaPaciente().setDtMovimentacao(txtData.getValue());
		entradaPacienteCtrl.getEntradaPaciente().setHrMovimentacao(txtHora.getLocalTime());
		entradaPacienteCtrl.getEntradaPaciente().setPreTriagem(txtPreTriagem.getText());
		entradaPacienteCtrl.getEntradaPaciente().setAtendente(cmbAtendente.getValue());
		entradaPacienteCtrl.getEntradaPaciente().setEnfermeira(cmbEnfermeira.getValue());
		entradaPacienteCtrl.getEntradaPaciente().setMedico(cmbMedico.getValue());
		entradaPacienteCtrl.getEntradaPaciente().setPaciente(cmbPaciente.getValue());
		entradaPacienteCtrl.getEntradaPaciente().setAlta(sbAlta.getValue());
		entradaPacienteCtrl.getEntradaPaciente().setStatus(sbAtivado.getValue());
		entradaPacienteCtrl.getEntradaPaciente().setEmergencia(sbEmergencia.getValue());
		entradaPacienteCtrl.getEntradaPaciente().setAtendimentos(tblAtendimentos.getItems());
		entradaPacienteCtrl.getEntradaPaciente().setUsuario(SistemaCtrl.getInstance().getUsuarioSessao());
	}

	@Override
	public void start(Stage stage) throws Exception {
		setStage(stage);
		stage.setScene(new Scene((Parent) FXMLLoader.load(getClass().getResource("ControleEntrada.fxml"))));
		stage.setTitle("Cadastro de Entrada de Pacientes");
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();
				
				if(event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
		stage.setMinWidth(800);
		stage.setMinHeight(600);
	}
	
	@FXML
	private void voltar(ActionEvent actionEvent) {
		entradaPacienteCtrl = null;
		stage.hide();
		stage = null;
		setVisualizando(false);
	}

	public Stage getStage() {
		return stage;
	}

	@SuppressWarnings("static-access")
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void inserir() throws Exception {
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}
	
	@SuppressWarnings("static-access")
	public void alterar(EntradaPacienteCtrl entradaPacienteCtrl) throws Exception {
		this.entradaPacienteCtrl = entradaPacienteCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepararTela();
		if (entradaPacienteCtrl != null && entradaPacienteCtrl.getEntradaPaciente() != null)
			preencherTela();
		if (visualizando)
			bloquearTela();
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	private void preencherTela() {
		if (entradaPacienteCtrl != null && entradaPacienteCtrl.getEntradaPaciente() != null) {
			txtCodigo.setValue(entradaPacienteCtrl.getEntradaPaciente().getCodigo());
			if(entradaPacienteCtrl.getEntradaPaciente().getDtMovimentacao() != null)
				txtData.setValue(entradaPacienteCtrl.getEntradaPaciente().getDtMovimentacao());
			if(entradaPacienteCtrl.getEntradaPaciente().getHrMovimentacao() != null)
				txtHora.setLocalTime(entradaPacienteCtrl.getEntradaPaciente().getHrMovimentacao());
			txtPreTriagem.setText(entradaPacienteCtrl.getEntradaPaciente().getPreTriagem());
			cmbAtendente.getSelectionModel().select(entradaPacienteCtrl.getEntradaPaciente().getAtendente());
			cmbEnfermeira.getSelectionModel().select(entradaPacienteCtrl.getEntradaPaciente().getEnfermeira());
			cmbMedico.getSelectionModel().select(entradaPacienteCtrl.getEntradaPaciente().getMedico());
			cmbPaciente.getSelectionModel().select(entradaPacienteCtrl.getEntradaPaciente().getPaciente());
			cmbConvenio.setValue(entradaPacienteCtrl.getEntradaPaciente().getConvenio());
			sbAlta.setValue(entradaPacienteCtrl.getEntradaPaciente().isAlta());
			sbAtivado.setValue(entradaPacienteCtrl.getEntradaPaciente().isStatus());
			tblAtendimentos.getItems().setAll(FXCollections.observableList(entradaPacienteCtrl.getEntradaPaciente().getAtendimentos()));
			sbEmergencia.setValue(entradaPacienteCtrl.getEntradaPaciente().isEmergencia());
		}
	}

	private void prepararTela() {
		txtCodigo.setOnKeyPressed(Utilitarios.getBloquear());
		sbAtivado.setValue(true);
		
		cmnResponsavel.prefWidthProperty().bind(tblAtendimentos.widthProperty().multiply(0.35));
		cmnData.prefWidthProperty().bind(tblAtendimentos.widthProperty().multiply(0.2));
		cmnHorarioInicial.prefWidthProperty().bind(tblAtendimentos.widthProperty().multiply(0.225));
		cmnHorarioFinal.prefWidthProperty().bind(tblAtendimentos.widthProperty().multiply(0.225));
		
	    cmnResponsavel.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Atendimento,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getResponsavel() != null)
					value = param.getValue().getResponsavel().getNome();
				return new SimpleStringProperty(value);
			}
		});
		cmnData.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Atendimento,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getDataAtendimento() != null) {
					value = param.getValue().getDataAtendimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				}
				return new SimpleStringProperty(value);
			}
		});
		cmnHorarioInicial.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Atendimento,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getHoraIni() != null) {
					value = param.getValue().getHoraIni().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				}
				return new SimpleStringProperty(value);
			}
		});
		cmnHorarioFinal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Atendimento,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
				String value = "";
				if(param.getValue() != null 
						&& param.getValue().getHoraFim() != null) {
					value = param.getValue().getHoraFim().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				}
				return new SimpleStringProperty(value);
			}
		});
		
		try {
			Funcionario func = cmbAtendente.getValue(); 
			cmbAtendente.setItems(new FuncionarioCtrl().getFuncionarios(Funcao.atendente));
			cmbAtendente.setValue(func);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbConvenio.getItems().setAll(new ConvenioCtrl().getConvenios());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			Funcionario func = cmbEnfermeira.getValue();
			cmbEnfermeira.setItems(new FuncionarioCtrl().getFuncionarios(Funcao.enfermeira));
			cmbEnfermeira.setValue(func);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Medico medico = cmbMedico.getValue();
			cmbMedico.setItems(new MedicoCtrl().getMedicos());
			cmbMedico.setValue(medico);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbPaciente.setItems(new PacienteCtrl().getPacientes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (SistemaCtrl.getInstance().getUsuarioSessao() != null 
				&& SistemaCtrl.getInstance().getUsuarioSessao().getFuncionario() != null
				&& SistemaCtrl.getInstance().getUsuarioSessao().getFuncionario().getCargo() != null) {
			switch (SistemaCtrl.getInstance().getUsuarioSessao().getFuncionario().getCargo().getFuncao()) {
			case atendente:
				cmbAtendente.setValue(SistemaCtrl.getInstance().getUsuarioSessao().getFuncionario());
				break;
			case enfermeira:
				cmbEnfermeira.setValue(SistemaCtrl.getInstance().getUsuarioSessao().getFuncionario());
				break;
			case medico:
				cmbMedico.setValue(new MedicoCtrl().getMedicoByFuncionario(SistemaCtrl.getInstance().getUsuarioSessao().getFuncionario()));
				break;
			default:
				break;
			}
		}
		txtData.setValue(LocalDate.now());
		txtHora.setLocalTime(LocalTime.now());
		tblAtendimentos.getItems().addListener(new ListChangeListener<Atendimento>() {

			@Override
			public void onChanged(Change<? extends Atendimento> c) {
				setObrigatoriedadeMedico(sbAlta.getValue() || tblAtendimentos.getItems().size() > 0);
			}

		});
		sbAlta.switchOnProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				setObrigatoriedadeAtendimentos(newValue);
				setObrigatoriedadeMedico(newValue || tblAtendimentos.getItems().size() > 0);
			}
		});
		sbEmergencia.switchOnProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				setObrigatoriedadeMedico(newValue || !tblAtendimentos.getItems().isEmpty());
				setObrigatoriedadeAtendimentos(!newValue && sbAlta.getValue());
			}
		});
	}
	
	private void setObrigatoriedadeMedico(boolean obrigatorio) {
		if(obrigatorio) {
			lblMedico.setText("Médico(Obrigatório):");
			lblMedico.getStyleClass().add("obrigatorio");
		} else {
			lblMedico.setText("Médico:");
			lblMedico.getStyleClass().removeAll("obrigatorio");
		}
	}
	
	private void setObrigatoriedadeAtendimentos(boolean obrigatorio) {
		if(obrigatorio) {
			lblAtendimentos.setText("Atendimentos(Obrigatório):");
			lblAtendimentos.getStyleClass().add("obrigatorio");
		} else {
			lblAtendimentos.setText("Atendimentos:");
			lblAtendimentos.getStyleClass().removeAll("obrigatorio");
		}
	}

	public static boolean isVisualizando() {
		return visualizando;
	}

	public static void setVisualizando(boolean visualizando) {
		ControleEntradaController.visualizando = visualizando;
	}

	@SuppressWarnings("static-access")
	public void visualizar(EntradaPacienteCtrl entradaPacienteCtrl) throws Exception {
		visualizando = true;
		this.entradaPacienteCtrl = entradaPacienteCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

}
