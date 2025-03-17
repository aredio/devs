package br.com.rarp.view.scnCadastroPaciente;

import java.net.URL;
import java.util.ResourceBundle;
import br.com.rarp.control.CidadeCtrl;
import br.com.rarp.control.ConvenioCtrl;
import br.com.rarp.control.PacienteCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.model.Cidade;
import br.com.rarp.model.Convenio;
import br.com.rarp.model.Paciente;
import br.com.rarp.model.Telefone;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.utils.comparacao.Ativado;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.MaskTextField;
import br.com.rarp.view.scnComponents.SwitchButton;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CadastroPacienteController extends Application implements Initializable {

	private static boolean visualizando;
	
	private static Stage stage;
	
    @FXML
    private Label lblResponsavel;
	
	@FXML
	private TabPane tbPane;
	
    @FXML
    private AnchorPane pnlPrincipal;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnVoltar;

	@FXML
	private TextField txtNome;

	@FXML
	private MaskTextField txtCPF;

	@FXML
	private TextField txtRG;

	@FXML
	private RadioButton rbFeminimo;

	@FXML
	private RadioButton rbMasculino;

	@FXML
	private DatePicker txtDataNasc;

	@FXML
	private TextField txtLogradouro;

	@FXML
	private TextField txtComplemento;

	@FXML
	private TextField txtNumero;

	@FXML
	private TextField txtBairro;

	@FXML
	private ComboBox<Cidade> cmbCidade;
	
	@FXML
	private ComboBox<Paciente> cmbResponsavel;

	@FXML
	private MaskTextField txtCEP;

	@FXML
	private MaskTextField txtTelefone;

	@FXML
	private RadioButton rbSim;

	@FXML
	private RadioButton rbNao;

	@FXML
	private ComboBox<Convenio> cmbConvenio;

	@FXML
	private IntegerTextField txtCodigo;

	private static PacienteCtrl pacienteCtrl;

	@FXML
	private SwitchButton sbAtivado;

	@FXML
	private ListView<Telefone> lsTelefones;

	@Override
	public void start(Stage stage) throws Exception {
		setStage(stage);
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CadastroPaciente.fxml"))));
		stage.setTitle("Cadastro de Pacientes");
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();
				
				if(event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
		stage.setMinWidth(647);
		stage.setMinHeight(538);
		
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
		stage.setResizable(false);
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void visualizar(PacienteCtrl pacienteCtrl) throws Exception {
		visualizando = true;
		this.pacienteCtrl = pacienteCtrl;
		rbNao.setSelected(true);
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void alterar(PacienteCtrl pacienteCtrl) throws Exception {
		this.pacienteCtrl = pacienteCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepararTela();
		if (pacienteCtrl != null && pacienteCtrl.getPaciente() != null)
			preencherTela();
		if (visualizando)
			bloquearTela();
	}

	@SuppressWarnings("unchecked")
	private void prepararTela() {
		try {
			sbAtivado.setValue(true);
			txtCodigo.setDisable(true);
			txtCodigo.setFocusTraversable(true);
			
			tbPane.requestFocus();
			txtNome.requestFocus();
			cmbCidade.setItems(new CidadeCtrl().consultar(new Campo("status", "", TipoCampo.booleano), new Ativado(), "Ativado"));
			cmbConvenio.setItems(new ConvenioCtrl().consultar(new Campo("CONV.status", "", TipoCampo.booleano), new Ativado(), "Ativado"));
			cmbResponsavel.getItems().setAll(new PacienteCtrl().getPacientesSemResponsavel());
			
			ToggleGroup tgPossuiNecessidades = new ToggleGroup();
			tgPossuiNecessidades.getToggles().add(rbSim);
			tgPossuiNecessidades.getToggles().add(rbNao);
			tgPossuiNecessidades.selectToggle(rbNao);
			
			ToggleGroup tgSexo = new ToggleGroup();	
			tgSexo.getToggles().add(rbMasculino);
			tgSexo.getToggles().add(rbFeminimo);
			tgSexo.selectToggle(rbMasculino);
			
			txtDataNasc.focusedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if(!newValue) {
						if (txtDataNasc.getValue() != null && !Utilitarios.isMaiorIdade(txtDataNasc.getValue())) {
							if(lblResponsavel.getStyleClass().indexOf("obrigatorio") == -1) {
								lblResponsavel.getStyleClass().add("obrigatorio");
								lblResponsavel.setText("Responsável(Obrigatório):");
							}
						} else {
							if(lblResponsavel.getStyleClass().indexOf("obrigatorio") != -1) {
								lblResponsavel.getStyleClass().remove("obrigatorio");
								lblResponsavel.setText("Responsável:");
							}
						}
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
							
							if(id.equals("txtBairro")) {
								tbPane.getSelectionModel().select(1);
								cmbConvenio.requestFocus();
								event.consume();
							}
							
							if(id.equals("lsTelefones")) {
								btnSalvar.requestFocus();
								event.consume();
							}
							
							if(id.equals("cmbConvenio")) {
								txtTelefone.requestFocus();
								event.consume();
							}
							
							if(id.equals("btnSalvar")) {
								tbPane.getSelectionModel().select(0);
								txtNome.requestFocus();
								event.consume();
							}
						}
						if (event.isShiftDown()) {	
							if(id.equals("txtNome")) {
								btnSalvar.requestFocus();
								event.consume();
							}
							
							if(id.equals("txtTelefone")) {
								cmbConvenio.requestFocus();
								event.consume();
							}
							
							if(id.equals("cmbConvenio")) {
								tbPane.getSelectionModel().select(0);
								txtBairro.requestFocus();
								event.consume();
							}
							
							if(id.equals("btnSalvar")) {
								tbPane.getSelectionModel().select(1);
								lsTelefones.requestFocus();
								event.consume();
							}
						} 
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void limparCampos() {
		txtBairro.clear();
		cmbConvenio.getSelectionModel().select(-1);
		cmbResponsavel.setValue(null);
		txtCEP.clear();
		txtCodigo.clear();
		txtComplemento.clear();
		txtCPF.clear();
		txtDataNasc.setValue(null);;
		txtLogradouro.clear();
		txtNome.clear();
		txtNumero.clear();
		txtRG.clear();
		txtTelefone.clear();
		cmbCidade.getSelectionModel().select(-1);
		rbMasculino.setSelected(true);
		rbNao.setSelected(true);
		sbAtivado.setValue(true);
		lsTelefones.getItems().clear();
		prepararTela();
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	private void preencherObjeto() {
		if (pacienteCtrl == null) {
			pacienteCtrl = new PacienteCtrl();
		}
		if (pacienteCtrl.getPaciente() == null) {
			pacienteCtrl.novoPaciente();
		}
		pacienteCtrl.getPaciente().setCodigo(txtCodigo.getValue());
		pacienteCtrl.getPaciente().setBairro(txtBairro.getText());
		pacienteCtrl.getPaciente().setConvenio(cmbConvenio.getSelectionModel().getSelectedItem());
		pacienteCtrl.getPaciente().setCep(txtCEP.getText());
		pacienteCtrl.getPaciente().setCpf(txtCPF.getText());
		pacienteCtrl.getPaciente().setResponsavel(cmbResponsavel.getValue());
		if (txtDataNasc.getValue() != null)
			pacienteCtrl.getPaciente().setDtNascimento(txtDataNasc.getValue());
		pacienteCtrl.getPaciente().setCidade(cmbCidade.getSelectionModel().getSelectedItem());
		pacienteCtrl.getPaciente().setNumero(txtNumero.getText());
		pacienteCtrl.getPaciente().setRg(txtRG.getText());
		pacienteCtrl.getPaciente().setComplemento(txtComplemento.getText());
		pacienteCtrl.getPaciente().setLogradouro(txtLogradouro.getText());
		pacienteCtrl.getPaciente().setStatus(sbAtivado.getValue());
		pacienteCtrl.getPaciente().setNome(txtNome.getText());
		pacienteCtrl.getPaciente().setPossuiNecessidades(rbSim.isSelected());
		pacienteCtrl.getPaciente().setSexo(rbMasculino.isSelected() ? "M" : "F");
		pacienteCtrl.getPaciente().setTelefones(lsTelefones.getItems());
	}

	private void preencherTela() {
		txtBairro.setText(pacienteCtrl.getPaciente().getBairro());
		txtCEP.setText(pacienteCtrl.getPaciente().getCep());
		txtCodigo.setText(pacienteCtrl.getPaciente().getCodigo() + "");
		txtComplemento.setText(pacienteCtrl.getPaciente().getComplemento());
		txtCPF.setText(pacienteCtrl.getPaciente().getCpf());
		
		if(pacienteCtrl.getPaciente().getDtNascimento() != null) {
			txtDataNasc.setValue(pacienteCtrl.getPaciente().getDtNascimento());
		}
		
		txtLogradouro.setText(pacienteCtrl.getPaciente().getLogradouro());
		txtNome.setText(pacienteCtrl.getPaciente().getNome());
		txtNumero.setText(pacienteCtrl.getPaciente().getNumero());
		txtRG.setText(pacienteCtrl.getPaciente().getRg());
		cmbConvenio.getSelectionModel().select(pacienteCtrl.getPaciente().getConvenio());
		cmbCidade.getSelectionModel().select(pacienteCtrl.getPaciente().getCidade());
		cmbResponsavel.getSelectionModel().select(pacienteCtrl.getPaciente().getResponsavel());
		rbSim.setSelected(pacienteCtrl.getPaciente().isPossuiNecessidades());
		if(pacienteCtrl.getPaciente().getSexo() == "M")
			rbMasculino.setSelected(true);
		else
			rbFeminimo.setSelected(true);
		lsTelefones.setItems(FXCollections.observableList(pacienteCtrl.getPaciente().getTelefones()));
		sbAtivado.setValue(pacienteCtrl.getPaciente().isStatus());
	}
	
	@FXML
	private void adicionarTelefone() {
		Telefone telefone = new Telefone();
		telefone.setNumero(txtTelefone.getText());
		if (!telefone.getNumero().isEmpty())
			lsTelefones.getItems().add(telefone);
		txtTelefone.setText("");
	}

	@FXML
	private void removerTelefone() {
		lsTelefones.getItems().remove(lsTelefones.getSelectionModel().getSelectedItem());
	}

	@FXML
	private void salvar() {
		preencherObjeto();
		try {
			if(pacienteCtrl.salvar()) {
				Utilitarios.message("Paciente salvo com sucesso.");
				limparCampos();
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar o paciente.\n" + "Descrição: " + e.getMessage());
		}
	}
	
    @FXML
    private void voltar(ActionEvent event) {
    	pacienteCtrl = null;
    	stage.hide();
    	visualizando = false;
    }
}