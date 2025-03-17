package br.com.rarp.view.scnCadastroFuncionario;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import br.com.rarp.control.CargoCtrl;
import br.com.rarp.control.CidadeCtrl;
import br.com.rarp.control.FuncionarioCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.model.Cargo;
import br.com.rarp.model.Cidade;
import br.com.rarp.model.Telefone;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.utils.comparacao.Ativado;
import br.com.rarp.view.scnCadastroCargo.CadastroCargoController;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.MaskTextField;
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
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDateTextField;

public class CadastroFuncionarioController extends Application implements Initializable {

	private static boolean visualizando;
	private static Stage stage;
	
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
	private LocalDateTextField txtDataNasc;

	@FXML
	private TextField txtLogradouro;

	@FXML
	private TextField txtComplemento;

	@FXML
	private TextField txtNumero;

	@FXML
	private TextField txtBairro;

	@FXML
	private AutoCompleteComboBox<Cidade> cmbCidade;

	@FXML
	private MaskTextField txtCEP;

	@FXML
	private MaskTextField txtTelefone;

	@FXML
	private RadioButton rbSim;

	@FXML
	private RadioButton rbNao;

	@FXML
	private TextField txtCTPS;

	@FXML
	private LocalDateTextField txtDataAdmissao;

	@FXML
	private TextField txtSalarioContratual;

	@FXML
	private AutoCompleteComboBox<Cargo> cmbCargo;

	@FXML
	private IntegerTextField txtCodigo;

	private static FuncionarioCtrl funcionarioCtrl;

	@FXML
	private SwitchButton sbAtivado;

	@FXML
	private ListView<Telefone> lsTelefones;
	
    @FXML
    void inserirCargo(ActionEvent event) throws Exception {
    	try {
			new CadastroCargoController().inserir();
			prepararTela();
		} catch (Exception e) {
			Utilitarios.erro("Não foi possível inserir um cargo.\n" + e.getMessage());
			e.printStackTrace();
		}  	
    }

	@Override
	public void start(Stage stage) throws Exception {
		setStage(stage);
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CadastroFuncionario.fxml"))));
		stage.setTitle("Cadastro de Funcionários");
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();
				
				if(event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
		stage.setMinWidth(620);
		stage.setMinHeight(534);
	}

	public Stage getStage() {
		return stage;
	}

	@SuppressWarnings("static-access")
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void inserir() throws Exception {
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}
	
	@SuppressWarnings("static-access")
	public void visualizar(FuncionarioCtrl funcionarioCtrl) throws Exception {
		visualizando = true;
		this.funcionarioCtrl = funcionarioCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void alterar(FuncionarioCtrl funcionarioCtrl) throws Exception {
		visualizando = false;
		this.funcionarioCtrl = funcionarioCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepararTela();
		if (funcionarioCtrl != null && funcionarioCtrl.getFuncionario() != null)
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
			txtDataAdmissao.setLocalDate(LocalDate.now());
			txtDataAdmissao.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			cmbCidade.setItems(new CidadeCtrl().consultar(new Campo("status", "", TipoCampo.booleano), new Ativado(), "Ativado"));
			cmbCargo.setItems(new CargoCtrl().consultar(new Campo("status", "", TipoCampo.booleano), new Ativado(), "Ativado"));
			
			ToggleGroup tgPossuiNecessidades = new ToggleGroup();
			tgPossuiNecessidades.getToggles().add(rbSim);
			tgPossuiNecessidades.getToggles().add(rbNao);
			tgPossuiNecessidades.selectToggle(rbNao);
			
			ToggleGroup tgSexo = new ToggleGroup();
			tgSexo.getToggles().add(rbMasculino);
			tgSexo.getToggles().add(rbFeminimo);
			tgSexo.selectToggle(rbMasculino);
			
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
								txtCTPS.requestFocus();
								event.consume();
							}
							
							if(id.equals("lsTelefones")) {
								btnSalvar.requestFocus();
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
							
							if(id.equals("txtCTPS")) {
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
		cmbCargo.getSelectionModel().select(-1);
		txtCEP.clear();
		txtCodigo.clear();
		txtComplemento.clear();
		txtCPF.clear();
		txtCTPS.clear();
		txtDataAdmissao.setLocalDate(LocalDate.now());
		txtDataNasc.setPromptText("");
		txtLogradouro.clear();
		txtNome.clear();
		txtNumero.clear();
		txtRG.clear();
		txtSalarioContratual.clear();
		txtTelefone.clear();
		cmbCidade.getSelectionModel().select(-1);
		rbMasculino.setSelected(true);
		rbNao.setSelected(true);
		sbAtivado.setValue(true);
		lsTelefones.getItems().clear();
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	private void preencherObjeto() {
		if (funcionarioCtrl == null) {
			funcionarioCtrl = new FuncionarioCtrl();
		}
		if (funcionarioCtrl.getFuncionario() == null) {
			funcionarioCtrl.novoFuncionario();
		}
		funcionarioCtrl.getFuncionario().setCodigo(txtCodigo.getValue());
		funcionarioCtrl.getFuncionario().setBairro(txtBairro.getText());
		funcionarioCtrl.getFuncionario().setCargo(cmbCargo.getSelectionModel().getSelectedItem());
		funcionarioCtrl.getFuncionario().setCep(txtCEP.getText());
		funcionarioCtrl.getFuncionario().setCpf(txtCPF.getText());
		funcionarioCtrl.getFuncionario().setCTPS(txtCTPS.getText());
		if (txtDataNasc.getLocalDate() != null)
			funcionarioCtrl.getFuncionario().setDtNascimento(txtDataNasc.getLocalDate());
		funcionarioCtrl.getFuncionario().setCidade(cmbCidade.getSelectionModel().getSelectedItem());
		funcionarioCtrl.getFuncionario().setNumero(txtNumero.getText());
		funcionarioCtrl.getFuncionario().setRg(txtRG.getText());
		funcionarioCtrl.getFuncionario().setSalarioContratual(Utilitarios.strToDouble(txtSalarioContratual.getText()));
		funcionarioCtrl.getFuncionario().setComplemento(txtComplemento.getText());
		funcionarioCtrl.getFuncionario().setLogradouro(txtLogradouro.getText());
		funcionarioCtrl.getFuncionario().setStatus(sbAtivado.getValue());
		funcionarioCtrl.getFuncionario().setNome(txtNome.getText());
		funcionarioCtrl.getFuncionario().setDtAdmissao(txtDataAdmissao.getLocalDate());
		funcionarioCtrl.getFuncionario().setPossuiNecessidades(rbSim.isSelected());
		funcionarioCtrl.getFuncionario().setSexo(rbMasculino.isSelected() ? "M" : "F");
		funcionarioCtrl.getFuncionario().setTelefones(lsTelefones.getItems());
	}

	private void preencherTela() {
		if (funcionarioCtrl != null && funcionarioCtrl.getFuncionario() != null) {
			txtBairro.setText(funcionarioCtrl.getFuncionario().getBairro());
			txtCEP.setText(funcionarioCtrl.getFuncionario().getCep());
			txtCodigo.setText(funcionarioCtrl.getFuncionario().getCodigo() + "");
			txtComplemento.setText(funcionarioCtrl.getFuncionario().getComplemento());
			txtCPF.setText(funcionarioCtrl.getFuncionario().getCpf());
			txtCTPS.setText(funcionarioCtrl.getFuncionario().getCTPS());
			txtDataAdmissao.setLocalDate(funcionarioCtrl.getFuncionario().getDtAdmissao());
			txtDataNasc.setLocalDate(funcionarioCtrl.getFuncionario().getDtNascimento());
			txtLogradouro.setText(funcionarioCtrl.getFuncionario().getLogradouro());
			txtNome.setText(funcionarioCtrl.getFuncionario().getNome());
			txtNumero.setText(funcionarioCtrl.getFuncionario().getNumero());
			txtRG.setText(funcionarioCtrl.getFuncionario().getRg());
			txtSalarioContratual.setText(funcionarioCtrl.getFuncionario().getSalarioContratual() + "");
			cmbCargo.getSelectionModel().select(funcionarioCtrl.getFuncionario().getCargo());
			cmbCidade.getSelectionModel().select(funcionarioCtrl.getFuncionario().getCidade());
			rbSim.setSelected(funcionarioCtrl.getFuncionario().isPossuiNecessidades());
			rbMasculino.setSelected(funcionarioCtrl.getFuncionario().getSexo() == "M");
			lsTelefones.setItems(FXCollections.observableList(funcionarioCtrl.getFuncionario().getTelefones()));
			sbAtivado.setValue(funcionarioCtrl.getFuncionario().isStatus());
		}
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
			if(funcionarioCtrl.salvar()) {
				Utilitarios.message("Funcionário salvo com sucesso.");
				limparCampos();
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar o funcionário.\n" + "Descrição: " + e.getMessage());
		}
	}
	
    @FXML
    private void voltar(ActionEvent event) {
    	funcionarioCtrl = null;
    	stage.hide();
    	visualizando = false;
    }
}