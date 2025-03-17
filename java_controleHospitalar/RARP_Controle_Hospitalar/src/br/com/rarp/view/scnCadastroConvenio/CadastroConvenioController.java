package br.com.rarp.view.scnCadastroConvenio;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import br.com.rarp.control.CidadeCtrl;
import br.com.rarp.control.ConvenioCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.model.Cidade;
import br.com.rarp.model.Telefone;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.utils.comparacao.Ativado;
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
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalDateTextField;

public class CadastroConvenioController extends Application implements Initializable {
	
    @FXML
    private AnchorPane pnlPrincipal;

    @FXML
    private TabPane tbPane;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtLogradouro;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtComplemento;

    @FXML
    private AutoCompleteComboBox<Cidade> cmbCidade;

    @FXML
    private MaskTextField txtCEP;

    @FXML
    private LocalDateTextField txtDataNasc;

    @FXML
    private MaskTextField txtCNPJ;

    @FXML
    private TextField txtANS;

    @FXML
    private AutoCompleteComboBox<String> cmbTipo;

    @FXML
    private TextField txtBairro;

    @FXML
    private TextField txtRazaoSocial;

    @FXML
    private MaskTextField txtTelefone;

    @FXML
    private ListView<Telefone> lsTelefones;

    @FXML
    private IntegerTextField txtCodigo;

    @FXML
    private SwitchButton sbAtivado;
    
    @FXML
    private ImageViewButton btnAdd;

    @FXML
    private ImageViewButton btnRemove;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnVoltar;

    @FXML
    private SwitchButton sbAutorizado;
	
	private static ConvenioCtrl convenioCtrl;
	
	private static boolean visualizando;
	
	private static Stage stage;

	@Override
	public void start(Stage stage) throws Exception {
		setStage(stage);
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CadastroConvenio.fxml"))));
		stage.setTitle("Cadastro de Convênios");
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();
				
				if(event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
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
	public void visualizar(ConvenioCtrl convenioCtrl) throws Exception {
		visualizando = true;
		this.convenioCtrl = convenioCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	@SuppressWarnings("static-access")
	public void alterar(ConvenioCtrl convenioCtrl) throws Exception {
		this.convenioCtrl = convenioCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepararTela();
		bloquearTela();
		if (convenioCtrl != null && convenioCtrl.getConvenio() != null)
			preencherTela();
	}

	@SuppressWarnings("unchecked")
	private void prepararTela() {
		try {
			sbAutorizado.setValue(false);
			sbAtivado.setValue(true);
			txtCodigo.setDisable(true);
			txtCodigo.setFocusTraversable(true);
			cmbTipo.getItems().addAll("Particular", "Público");
			
			tbPane.requestFocus();
			txtNome.requestFocus();
			txtDataNasc.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			cmbCidade.setItems(new CidadeCtrl().consultar(new Campo("status", "", TipoCampo.booleano), new Ativado(), "Ativado"));
			
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
								lsTelefones.requestFocus();
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
							
							if(id.equals("txtTelefon")) {
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
		txtANS.clear();
		txtRazaoSocial.clear();
		cmbTipo.getSelectionModel().clearSelection();
		txtBairro.clear();
		txtCEP.clear();
		txtCodigo.clear();
		txtComplemento.clear();
		txtCNPJ.clear();
		txtDataNasc.setText("");
		txtLogradouro.clear();
		txtNome.clear();
		txtNumero.clear();
		txtTelefone.clear();
		cmbCidade.getSelectionModel().select(-1);
		sbAtivado.setValue(true);
		sbAtivado.setValue(false);
		lsTelefones.getItems().clear();
	}

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	private void preencherObjeto() {
		if (convenioCtrl == null) {
			convenioCtrl = new ConvenioCtrl();
		}
		if (convenioCtrl.getConvenio() == null) {
			convenioCtrl.novoConvenio();
		}
		convenioCtrl.getConvenio().setCodigo(txtCodigo.getValue());
		convenioCtrl.getConvenio().setANS(txtANS.getText());
		convenioCtrl.getConvenio().setRazaoSocial(txtRazaoSocial.getText());
		convenioCtrl.getConvenio().setTipo(cmbTipo.getSelectionModel().getSelectedIndex() + 1);
		convenioCtrl.getConvenio().setBairro(txtBairro.getText());
		convenioCtrl.getConvenio().setCep(txtCEP.getText());
		convenioCtrl.getConvenio().setCnpj(txtCNPJ.getText());
		if (txtDataNasc.getLocalDate() != null)
			convenioCtrl.getConvenio().setDtNascimento(txtDataNasc.getLocalDate());
		convenioCtrl.getConvenio().setCidade(cmbCidade.getSelectionModel().getSelectedItem());
		convenioCtrl.getConvenio().setNumero(txtNumero.getText());
		convenioCtrl.getConvenio().setComplemento(txtComplemento.getText());
		convenioCtrl.getConvenio().setLogradouro(txtLogradouro.getText());
		convenioCtrl.getConvenio().setStatus(sbAtivado.getValue());
		convenioCtrl.getConvenio().setNome(txtNome.getText());
		convenioCtrl.getConvenio().setTelefones(lsTelefones.getItems());
		convenioCtrl.getConvenio().setAutorizado(sbAutorizado.getValue());
	}

	private void preencherTela() {
		txtBairro.setText(convenioCtrl.getConvenio().getBairro());
		txtCEP.setText(convenioCtrl.getConvenio().getCep());
		txtCodigo.setText(convenioCtrl.getConvenio().getCodigo() + "");
		txtComplemento.setText(convenioCtrl.getConvenio().getComplemento());
		txtCNPJ.setText(convenioCtrl.getConvenio().getCnpj());
		txtANS.setText(convenioCtrl.getConvenio().getANS());
		txtRazaoSocial.setText(convenioCtrl.getConvenio().getRazaoSocial());
		cmbTipo.getSelectionModel().select(convenioCtrl.getConvenio().getTipo()-1);
		
		if(convenioCtrl.getConvenio().getDtNascimento() != null)
			txtDataNasc.setLocalDate(convenioCtrl.getConvenio().getDtNascimento());
		
		txtLogradouro.setText(convenioCtrl.getConvenio().getLogradouro());
		txtNome.setText(convenioCtrl.getConvenio().getNome());
		txtNumero.setText(convenioCtrl.getConvenio().getNumero());
		cmbCidade.getSelectionModel().select(convenioCtrl.getConvenio().getCidade());
		lsTelefones.setItems(FXCollections.observableList(convenioCtrl.getConvenio().getTelefones()));
		sbAtivado.setValue(convenioCtrl.getConvenio().isStatus());
		sbAutorizado.setValue(convenioCtrl.getConvenio().isAutorizado());
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
			if(convenioCtrl.salvar()) {
				Utilitarios.message("Convênio salvo com sucesso.");
				limparCampos();
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar o convênio.\n" + "Descrição: " + e.getMessage());
		}
	}
	
    @FXML
    private void voltar(ActionEvent event) {
    	convenioCtrl = null;
    	stage.hide();
    	visualizando = false;
    }
}