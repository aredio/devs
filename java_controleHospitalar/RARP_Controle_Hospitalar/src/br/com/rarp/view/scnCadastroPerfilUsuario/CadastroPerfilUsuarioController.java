package br.com.rarp.view.scnCadastroPerfilUsuario;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.rarp.control.PerfilUsuarioCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Tela;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnComponents.SwitchButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CadastroPerfilUsuarioController extends Application implements Initializable {
	@FXML private TextField txtCodigo;
	@FXML private TextField txtNome;
	@FXML private ListView<Tela> lvTelas;
	@FXML private ListView<Tela> lvTelasPermitidas;
	@FXML private CheckBox ckbInserir;
	@FXML private CheckBox ckbAlterar;
	@FXML private CheckBox ckbVisualizar;
	@FXML private CheckBox ckbDesativar;
	@FXML private Button btnSelectAll;
	@FXML private Button btnRemove;
	@FXML private Button btnRemoveAll;
	@FXML private Button btnAdd;
	@FXML private Button btnAddAll;
	@FXML private Button btnSalvar;
	@FXML private Button btnVoltar;
	@FXML private SwitchButton sbAtivado;
	
	private static PerfilUsuarioCtrl perfilUsuarioCtrl;
	private static boolean visualizando;
	private static Stage stage;
	
	@Override
	public void start(Stage stage) throws Exception {
		setStage(stage);
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CadastroPerfilUsuario.fxml"))));
		stage.setTitle("Cadastro de Perfil de Usuário");
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getTarget() instanceof Button && event.getCode() == KeyCode.ENTER)
					((Button) event.getTarget()).arm();
				
				if(event.getCode() == KeyCode.ESCAPE)
					voltar(new ActionEvent());
			}
		});
		stage.setMinWidth(680);
		stage.setMinHeight(490);
	}
	
	private void limparCampos() {
		ckbAlterar.setSelected(false);
		ckbDesativar.setSelected(false);
		ckbInserir.setSelected(false);
		ckbVisualizar.setSelected(false);
		
		txtCodigo.clear();
		txtNome.clear();
		
		btnRemoveAll.arm();
		for(Tela tela: lvTelas.getSelectionModel().getSelectedItems()) {
			tela.setPodeAlterar(false);
			tela.setPodeInserir(false);
			tela.setPodeDesativar(false);
			tela.setPodeVisualizar(false);
		}
		
		sbAtivado.switchOnProperty().set(true);
	}
	
	public static void setStage(Stage stage) {
		CadastroPerfilUsuarioController.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}
	
	@SuppressWarnings("static-access")
	public void alterar(PerfilUsuarioCtrl perfilUsuarioCtrl) throws Exception {
		this.perfilUsuarioCtrl = perfilUsuarioCtrl;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		adicionarTelas();
		
		sbAtivado.switchOnProperty().set(true);
		if(perfilUsuarioCtrl != null && perfilUsuarioCtrl.getPerfilUsuario() != null)
			preencheTela();
		
		lvTelas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lvTelasPermitidas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lvTelasPermitidas.getSelectionModel().getSelectedItems().addListener(changeListener);
		
		lvTelas.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		                add(new ActionEvent());
		            }
		        }
		    }
		});
		
		lvTelasPermitidas.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		                remove(new ActionEvent());
		            }
		        }
		    }
		});
		
		if(visualizando)
			bloquearCampos();
	}
	
	private void bloquearCampos() {
		txtCodigo.setDisable(true);
		txtNome.setDisable(true);
		btnAdd.setDisable(true);
		btnAddAll.setDisable(true);
		btnRemove.setDisable(true);
		btnRemoveAll.setDisable(true);
		btnSelectAll.setDisable(true);
		btnSalvar.setDisable(true);
		sbAtivado.setDisable(true);
		ckbAlterar.setDisable(true);
		ckbDesativar.setDisable(true);
		ckbInserir.setDisable(true);
		ckbVisualizar.setDisable(true);
	}

	ListChangeListener<Tela> changeListener = new ListChangeListener<Tela>() {
		@Override
		public void onChanged(javafx.collections.ListChangeListener.Change<? extends Tela> c) {
			lvTelasSelecionadas();
		}
	};
	
	private void adicionarTelas() {
		lvTelas.setItems(FXCollections.observableArrayList(SistemaCtrl.getInstance().getTelas()));
	}

	public void inserir() throws Exception {
		perfilUsuarioCtrl = null;
		visualizando = false;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();		
	}
	
	@FXML
	private void selectAll(ActionEvent event) {
		lvTelasPermitidas.getSelectionModel().selectAll();
	}
	
	@FXML
	private void addAll(ActionEvent event) {
		lvTelasPermitidas.getItems().addAll(lvTelas.getItems());
		lvTelas.getItems().clear();
	}
	
	@FXML
	private void removeAll(ActionEvent event) {
		lvTelas.getItems().addAll(lvTelasPermitidas.getItems());
		lvTelasPermitidas.getItems().clear();
	}
	
	@FXML
	private void add(ActionEvent event) {
		lvTelasPermitidas.getItems().addAll(lvTelas.getSelectionModel().getSelectedItems());
		lvTelas.getItems().removeAll(lvTelas.getSelectionModel().getSelectedItems());
	}
	
	@FXML
	private void remove(ActionEvent event) {
		lvTelas.getItems().addAll(lvTelasPermitidas.getSelectionModel().getSelectedItems());
		lvTelasPermitidas.getItems().removeAll(lvTelasPermitidas.getSelectionModel().getSelectedItems());
	}
	
	@FXML
	private void salvar() {
		preencherObjeto();
		try {
			if(perfilUsuarioCtrl.salvar()) {
				Utilitarios.message("Perfil de usuário salvo com sucesso.");
				limparCampos();
			}
		} catch (Exception e) {
			Utilitarios.erro("Erro ao salvar perfil de usuario.\n"
						   + "Descrição: " + e.getMessage());
		}
	}
	
	private void preencherObjeto() {
		if (perfilUsuarioCtrl == null)
			perfilUsuarioCtrl = new PerfilUsuarioCtrl();
		
		if (perfilUsuarioCtrl.getPerfilUsuario() == null)
			perfilUsuarioCtrl.novoPerfilUsuario();
		
		perfilUsuarioCtrl.getPerfilUsuario().setCodigo(Utilitarios.strToInt(txtCodigo.getText()));
		perfilUsuarioCtrl.getPerfilUsuario().setNome(txtNome.getText());
		
		//Adiciona telas permitidas
		for(Tela tela: lvTelasPermitidas.getItems()) {
			tela.setStatus(true);
			perfilUsuarioCtrl.getPerfilUsuario().getTelas().add(tela);
		}
		//Adiciona telas n�o permitidas
		for(Tela tela: lvTelas.getItems()) {
			tela.setStatus(false);
			tela.setPodeAlterar(false);
			tela.setPodeInserir(false);
			tela.setPodeVisualizar(false);
			tela.setPodeDesativar(false);
			perfilUsuarioCtrl.getPerfilUsuario().getTelas().add(tela);
		}
		perfilUsuarioCtrl.getPerfilUsuario().setStatus(sbAtivado.switchOnProperty().get());
	}
	
	@FXML
	private void lvTelasSelecionadas() {
		if(lvTelasPermitidas.getSelectionModel().getSelectedItem() != null)
			ckbInserir.setSelected(lvTelasPermitidas.getSelectionModel().getSelectedItem().isPodeInserir());
		if(lvTelasPermitidas.getSelectionModel().getSelectedItem() != null)
			ckbAlterar.setSelected(lvTelasPermitidas.getSelectionModel().getSelectedItem().isPodeAlterar());
		if(lvTelasPermitidas.getSelectionModel().getSelectedItem() != null)
			ckbVisualizar.setSelected(lvTelasPermitidas.getSelectionModel().getSelectedItem().isPodeVisualizar());
		if(lvTelasPermitidas.getSelectionModel().getSelectedItem() != null)
			ckbDesativar.setSelected(lvTelasPermitidas.getSelectionModel().getSelectedItem().isPodeDesativar());
	}
	
	@FXML
	private void ckbClick(ActionEvent event) {
		for(Tela tela: lvTelasPermitidas.getSelectionModel().getSelectedItems()) { 
			tela.setPodeInserir(ckbInserir.isSelected());
			tela.setPodeAlterar(ckbAlterar.isSelected());
			tela.setPodeVisualizar(ckbVisualizar.isSelected());
			tela.setPodeDesativar(ckbDesativar.isSelected());
		}	
	}
	
	@FXML
	private void voltar(ActionEvent event) {
		perfilUsuarioCtrl = null;
		stage.hide();
    	visualizando = false;
	}

	private void preencheTela() {
		txtNome.setText(perfilUsuarioCtrl.getPerfilUsuario().getNome());
		txtCodigo.setText(perfilUsuarioCtrl.getPerfilUsuario().getCodigo() + "");
		for(Tela tela: perfilUsuarioCtrl.getPerfilUsuario().getTelas()){
			if(lvTelas.getItems().contains(tela))
				if(tela.isStatus()) {
					lvTelas.getItems().set(lvTelas.getItems().indexOf(tela), tela);
					lvTelas.getSelectionModel().select(tela);
					add(new ActionEvent());
				} else {
					lvTelas.getItems().set(lvTelas.getItems().indexOf(tela), tela);
				}
		}
		sbAtivado.switchOnProperty().set(perfilUsuarioCtrl.getPerfilUsuario().isStatus());
	}

	@SuppressWarnings("static-access")
	public void visualizar(PerfilUsuarioCtrl perfilUsuarioCtrl) throws Exception {
		this.perfilUsuarioCtrl = perfilUsuarioCtrl;
		visualizando = true;
		start(SistemaCtrl.getInstance().getStage());
		stage.setResizable(false);
		stage.showAndWait();
	}

}
