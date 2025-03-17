package br.com.rarp.view.scnCadastroUsuario;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.rarp.control.FuncionarioCtrl;
import br.com.rarp.control.PerfilUsuarioCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.control.UsuarioCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.PerfilUsuario;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.utils.comparacao.Ativado;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.SwitchButton;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CadastroUsuarioController extends Application implements Initializable {

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
    private TextField txtUsuario;
    
    @FXML 
    private ComboBox<PerfilUsuario> cmbPerfilUsuario;
    
    @FXML 
    private ComboBox<Funcionario> cmbFuncionario;
    
    @FXML 
    private SwitchButton sbAtivado;
    
    @FXML
    private Label lblSenha;

    @FXML
    private TextField txtSenha;

    @FXML
    private Label lblSenha1;

    @FXML
    private TextField txtSenha1;

    @FXML
    private Label lblSenha2;

    @FXML
    private TextField txtSenha2;

    @FXML
    private SwitchButton sbNovaSenha;

    @FXML
    private AnchorPane pnlPrincipal;
    
    @FXML
    private Label lblsbNovaSenha;
    
	private static UsuarioCtrl usuarioCtrl;
	private static boolean visualizando;

	@Override
	public void start(Stage stage) throws Exception {
		setStage(stage);
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CadastroUsuario.fxml"))));
		stage.setTitle("Cadastro de Usuários");
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();
				
				if(event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
		//stage.setMinWidth(520);
		//stage.setMinHeight(476);
		stage.setResizable(false);
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		CadastroUsuarioController.stage = stage;
	}
	
	private void limparCampos() {
		txtCodigo.clear();
		txtNome.clear();
		txtUsuario.clear();
		txtSenha.clear();
		txtSenha1.clear();
		txtSenha2.clear();
		
		cmbFuncionario.getSelectionModel().clearSelection();
		cmbPerfilUsuario.getSelectionModel().clearSelection();
		sbNovaSenha.setValue(false);
		sbAtivado.setValue(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prepararTela();
		if(usuarioCtrl != null && usuarioCtrl.getClass() != null)
			preencheTela();
		if(visualizando)
			bloquearTela();
	}
	
	private void prepararTela() {
		sbAtivado.switchOnProperty().set(true);
		txtCodigo.setDisable(true);
		try {
			cmbPerfilUsuario.setItems(new PerfilUsuarioCtrl().consultar(new Campo("status", "", TipoCampo.booleano), new Ativado(), "ativado"));
			cmbFuncionario.setItems(new FuncionarioCtrl().consultar(new Campo("func.status", "", TipoCampo.booleano), new Ativado(), "ativado"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(usuarioCtrl == null) {
			sbNovaSenha.setVisible(false);
			lblsbNovaSenha.setVisible(false);
			lblSenha.setDisable(false);
			txtSenha.setDisable(false);
			lblSenha1.setDisable(false);
			txtSenha1.setDisable(false);
			lblSenha2.setDisable(true);
			txtSenha2.setDisable(true);
		} else {
			lblSenha1.setText("Nova Senha(Obrigatório):");
			txtSenha.setDisable(true);
			txtSenha.setDisable(true);
			lblSenha1.setDisable(true);
			txtSenha1.setDisable(true);
			lblSenha2.setDisable(true);
			txtSenha2.setDisable(true);
		}
		sbNovaSenha.switchOnProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				novaSenha();
			}
		});
	}
	
    @FXML
    void novaSenha() {
    	if(sbNovaSenha.getValue()) {
    		lblSenha.setDisable(false);
			txtSenha.setDisable(false);
			lblSenha1.setDisable(false);
			txtSenha1.setDisable(false);
			lblSenha2.setDisable(false);
			txtSenha2.setDisable(false);
    	} else {
    		lblSenha.setDisable(true);
			txtSenha.setDisable(true);
			lblSenha1.setDisable(true);
			txtSenha1.setDisable(true);
			lblSenha2.setDisable(true);
			txtSenha2.setDisable(true);
    	}
    }

	private void bloquearTela() {
		btnSalvar.setDisable(visualizando);
	}

	public void inserir() throws Exception {
		usuarioCtrl = null;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();		
	}
	
	@SuppressWarnings("static-access")
	public void alterar(UsuarioCtrl usuarioCtrl) throws Exception {
		this.usuarioCtrl = usuarioCtrl;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}
	
	@SuppressWarnings("static-access")
	public void visualizar(UsuarioCtrl usuarioCtrl) throws Exception {
		visualizando = true;
		this.usuarioCtrl = usuarioCtrl;
		start(SistemaCtrl.getInstance().getStage());
		stage.showAndWait();
	}
	
    @FXML
    private void salvar(ActionEvent event) {
		try {
			preencherObjeto();
			if(usuarioCtrl.salvar()) {
				Utilitarios.message("Usuário salvo com sucesso.");
				limparCampos();
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar perfil de usuario.\n"
						   + "Descrição: " + e.getMessage());
		}
    }
    
	@FXML
    private void voltar(ActionEvent event) {
		usuarioCtrl = null;
    	stage.hide();
    	visualizando = false;
    }

    private void preencherObjeto() throws Exception {    	
		if (usuarioCtrl == null)
			usuarioCtrl = new UsuarioCtrl();
		else if (usuarioCtrl.getUsuario() != null && !sbNovaSenha.isDisable() && sbNovaSenha.getValue()) {
			if (txtSenha.getText().hashCode() != usuarioCtrl.getUsuario().getSenha())
				throw new Exception("A senha atual não confere com a senha digitada");
			
			if (sbNovaSenha.isVisible() && sbNovaSenha.getValue() && txtSenha1.getText().hashCode() != txtSenha2.getText().hashCode())
				throw new Exception("A nova senha e a confirmação da nova senha não conferem");
		}
		
		if(usuarioCtrl.getUsuario() == null)
			usuarioCtrl.novoUsuario();
		
		usuarioCtrl.getUsuario().setCodigo(Utilitarios.strToInt(txtCodigo.getText()));
		usuarioCtrl.getUsuario().setNome(txtNome.getText());
		usuarioCtrl.getUsuario().setUsuario(txtUsuario.getText());
		usuarioCtrl.getUsuario().setPerfilUsuario(cmbPerfilUsuario.getValue());
		usuarioCtrl.getUsuario().setFuncionario(cmbFuncionario.getValue());
		usuarioCtrl.getUsuario().setSenha(txtSenha1.getText().hashCode());
		usuarioCtrl.getUsuario().setStatus(sbAtivado.switchOnProperty().get());
	}
    
	private void preencheTela() {
		txtCodigo.setText(usuarioCtrl.getUsuario().getCodigo() + "");
		txtNome.setText(usuarioCtrl.getUsuario().getNome());
		txtUsuario.setText(usuarioCtrl.getUsuario().getUsuario());
		cmbFuncionario.setValue(usuarioCtrl.getUsuario().getFuncionario());
		cmbPerfilUsuario.setValue(usuarioCtrl.getUsuario().getPerfilUsuario());
		sbAtivado.switchOnProperty().set(usuarioCtrl.getUsuario().isStatus());
	}

}