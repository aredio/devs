package br.com.rarp.view.scnLogin;

import java.net.URL;
import java.util.ResourceBundle;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.control.UsuarioCtrl;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnAcesso.AcessoController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController extends Application implements Initializable, EventHandler<KeyEvent> {

	private static Stage stage;

	@FXML
	private AnchorPane pnFrame;
	@FXML
	private AnchorPane pnContent;
	@FXML
	private TextField txtUsuario;
	@FXML
	private PasswordField txtSenha;
	@FXML
	private PasswordField txtNovaSenha;
	@FXML
	private PasswordField txtConfirmaSenha;
	@FXML
	private Label lblNovaSenha;
	@FXML
	private Label lblConfirmaSenha;
	@FXML
	private Button btnCancelar;
	@FXML
	private Button btnEntrar;
	@FXML
	private ProgressBar pgsSplash;

	private static final int MAX_TENTATIVAS = 3;
	private static int tentativas = 0;
	private static UsuarioCtrl usuarioCtrl = new UsuarioCtrl();

	private Node node;

	@SuppressWarnings("static-access")
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Login.fxml"));
        setNode(loader.load());
        stage.setScene(new Scene((Parent) getNode()));
        this.stage = stage;
	}

	public boolean logar() throws Exception {
		tentativas = 0;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
		stage.setResizable(true);
		return SistemaCtrl.getInstance().getUsuarioSessao() != null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtUsuario.setText(SistemaCtrl.getInstance().getPropriedades().getLastUsername());
		pnFrame.setPrefSize(342, 251);
		try {
			usuarioCtrl.consultar(txtUsuario.getText());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		txtUsuario.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				try {
					if (!newValue && !txtUsuario.getText().isEmpty()) {
						usuarioCtrl.consultar(txtUsuario.getText());
						if (usuarioCtrl.getUsuario() != null && (usuarioCtrl.getUsuario().getSenha() == 0)) {
							pnContent.setPrefHeight(pnContent.getPrefHeight() + 116);
							pnFrame.setPrefHeight(pnFrame.getPrefHeight() + 120);
							stage.sizeToScene();
							lblNovaSenha.setVisible(true);
							lblConfirmaSenha.setVisible(true);
							txtNovaSenha.setVisible(true);
							txtConfirmaSenha.setVisible(true);
							txtSenha.setText("");
							txtSenha.setDisable(true);
						} else {
							if (lblNovaSenha.isVisible()) {
								pnContent.setPrefHeight(pnContent.getPrefHeight() - 116);
								pnFrame.setPrefHeight(pnFrame.getPrefHeight() - 120);
								lblNovaSenha.setVisible(false);
								lblConfirmaSenha.setVisible(false);
								txtNovaSenha.setVisible(false);
								txtConfirmaSenha.setVisible(false);
								txtSenha.setDisable(false);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}

	@FXML
	private void cancelar(ActionEvent event) {
		if(stage != null) 
			stage.close();
		else {
			if (AcessoController.getStage() != null)
				AcessoController.getStage().close();
		}
	}

	@FXML
	private void entrar(ActionEvent event) {
		try {
			if (txtNovaSenha.isVisible()) {
				if (txtNovaSenha.getText().isEmpty())
					throw new Exception("Digite a nova senha");
				if (txtConfirmaSenha.getText().isEmpty())
					throw new Exception("Digite a confirmação da nova senha");
				if (!txtConfirmaSenha.getText().equals(txtNovaSenha.getText()))
					throw new Exception("As senhas digitadas são diferentes");
				usuarioCtrl.getUsuario().setSenha(txtNovaSenha.getText().hashCode());
				usuarioCtrl.salvar();
			} else {
				if (usuarioCtrl.getUsuario() == null)
					throw new Exception("Este usuário não existe");
				if (!(usuarioCtrl.getUsuario().getSenha() == txtSenha.getText().hashCode())) {
					tentativas++;
					throw new Exception("Senha incorreta");
				}
			}
			SistemaCtrl.getInstance().setUsuarioSessao(usuarioCtrl.getUsuario());
			SistemaCtrl.getInstance().getPropriedades().setLastUsername(txtUsuario.getText());
			
			if (AcessoController.getStage() != null) {
				AcessoController.getStage().hide();
				AcessoController.setStage(null);
			} else {
				if(this.getStage() != null) 
					this.getStage().hide();
			}
					
		} catch (Exception e) {
			if (tentativas > MAX_TENTATIVAS) {
				Utilitarios.atencao("Você atingiu o limite de 3 tentativas");
				cancelar(event);
			} else {
				Utilitarios.atencao(e.getMessage());
			}
		}
	}

	@FXML
	private void entrarKeyPress(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			btnEntrar.fire();
	}
	
	@Override
	public void handle(KeyEvent e) {
		if (!txtUsuario.getText().equals("") && !txtSenha.getText().equals(""))
			if (e.getCode() == KeyCode.ENTER) {
				btnEntrar.fire();	
			}
	}
	
	public Node getNode() throws Exception {
		if(node == null)
			start(SistemaCtrl.getInstance().getStage());
		stage = null;
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		LoginController.stage = stage;
	}
	
	
}
