package br.com.rarp.view.scnConexao;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnAcesso.AcessoController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConexaoController extends Application implements Initializable {
	@FXML // fx:id="txtHost"
	private TextField txtHost; // Value injected by FXMLLoader

	@FXML // fx:id="txtPorta"
	private TextField txtPorta; // Value injected by FXMLLoader

	@FXML // fx:id="txtUser"
	private TextField txtUser; // Value injected by FXMLLoader

	@FXML // fx:id="txtSenha"
	private PasswordField txtSenha; // Value injected by FXMLLoader

	@FXML // fx:id="btnAplicar"
	private Button btnAplicar; // Value injected by FXMLLoader

	@FXML // fx:id="btnCancelar"
	private Button btnCancelar; // Value injected by FXMLLoader

	private static Stage stage;

	private Node node;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtHost.setText(SistemaCtrl.getInstance().getPropriedades().getHost());
		txtPorta.setText(SistemaCtrl.getInstance().getPropriedades().getPorta());
		txtUser.setText(SistemaCtrl.getInstance().getPropriedades().getUser());
		txtSenha.setText(SistemaCtrl.getInstance().getPropriedades().getPassword());
	}

	public Node getNode() throws Exception {
		if(node == null)
			start(SistemaCtrl.getInstance().getStage());
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	@SuppressWarnings("static-access")
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Conexao.fxml"));
		setNode(loader.load());
		stage.setScene(new Scene((Parent) getNode()));
		this.stage = stage;
	}

	public void configurar() throws Exception {
		start(new Stage(StageStyle.DECORATED));
		stage.setResizable(false);
		stage.showAndWait();
	}

	@FXML
	void btnAplicarAction(ActionEvent event) {
		try {
			SistemaCtrl.getInstance().getPropriedades().setHost(txtHost.getText());
			SistemaCtrl.getInstance().getPropriedades().setPorta(txtPorta.getText());
			SistemaCtrl.getInstance().getPropriedades().setUser(txtUser.getText());
			SistemaCtrl.getInstance().getPropriedades().setPassword(txtSenha.getText());
			SistemaCtrl.getInstance().getPropriedades().setPropriedades();
			SistemaCtrl.getInstance().getPropriedades().getPropriedades();
			Utilitarios.message("Configurações do servidor de banco de dados gravadas com sucesso.");
			
			if(stage != null)
				stage.hide();
		} catch (Exception e) {
			Utilitarios.atencao("Falha ao fravar configurações do servidor de banco de dados.");
		}
	}

	@FXML
	void btnCancelarAction(ActionEvent event) {
		if (getStage() != null) 
			getStage().hide();
		else {
			AcessoController.setPageIndex(0);
		}
			
	}
	
	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		ConexaoController.stage = stage;
	}
}
