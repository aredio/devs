package br.com.rarp.view.scnOpcoes;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.rarp.control.CidadeCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Cidade;
import br.com.rarp.model.Configuracoes;
import br.com.rarp.model.Organizacao;
import br.com.rarp.model.Telefone;
import br.com.rarp.model.dao.OrganizacaoDAO;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.MaskTextField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;

public class OpcoesController extends Application implements Initializable {

    @FXML
    private TextField txtLogradouro;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtBairro;

    @FXML
    private AutoCompleteComboBox<Cidade> cmbCidade;

    @FXML
    private MaskTextField txtCNPJ;

    @FXML
    private TextField txtComplemento;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtRazaoSocial;

    @FXML
    private MaskTextField txtTelefone;

    @FXML
    private ImageViewButton btnAdd;

    @FXML
    private ImageViewButton btnRemove;

    @FXML
    private ListView<Telefone> lsTelefones;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private IntegerTextField txtCodigoRARP;

    @FXML
    private Button btnAplicar;

    @FXML
    private Button btnCancelar;
    
    @FXML
    private MaskTextField txtEmail;

	private static Stage stage;
	
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


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			preparaTela();
			preencherTela();
		} catch (Exception e) {
			Utilitarios.erro("Erro ao abri tela de opções: " + e.getMessage());
		}
	}

	private void preencherTela() {
		Organizacao empresa = SistemaCtrl.getInstance().getOrganizacao();
		txtNome.setText(empresa.getNome());
		txtRazaoSocial.setText(empresa.getRazaoSocial());
		txtCNPJ.setText(empresa.getCnpj());
		txtLogradouro.setText(empresa.getLogradouro());
		txtNumero.setText(empresa.getNumero());
		txtComplemento.setText(empresa.getComplemento());
		cmbCidade.setValue(empresa.getCidade());
		txtEmail.setText(empresa.getEmail());
		txtBairro.setText(empresa.getBairro());
		lsTelefones.getItems().setAll(empresa.getTelefones());
		Configuracoes configuracoes = SistemaCtrl.getInstance().getConfiguracoes();
		txtUsuario.setText(configuracoes.getUsuario());
		txtSenha.setText(configuracoes.getSenha());
		txtCodigoRARP.setText(String.valueOf(configuracoes.getCodigoRARP()));
	}

	@Override
	public void start(Stage pstage) throws Exception {
		// TODO Auto-generated method stub
		stage = pstage;
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Opcoes.fxml"))));
		stage.setResizable(false);
		stage.showAndWait();
	}

	@FXML
	private void gravar(ActionEvent e) {
		try {
			preencherConfiguracoes();
			SistemaCtrl.getInstance().salvarConfiguracoes();
			new OrganizacaoDAO().salvar();
			stage.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Utilitarios.erro(e1.toString());
		}
	}

	@FXML
	private void voltar(ActionEvent e) {
		stage.hide();
	}

	public void preparaTela() {
		try {
			cmbCidade.getItems().setAll(new CidadeCtrl().getCidades());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void preencherConfiguracoes() {
		SistemaCtrl.getInstance().getConfiguracoes().setSenha(txtSenha.getText());
		SistemaCtrl.getInstance().getConfiguracoes().setUsuario(txtUsuario.getText());
		SistemaCtrl.getInstance().getConfiguracoes().setCodigoRARP(txtCodigoRARP.getValue());
		SistemaCtrl.getInstance().getOrganizacao().setNome(txtNome.getText());
		SistemaCtrl.getInstance().getOrganizacao().setRazaoSocial(txtRazaoSocial.getText());
		SistemaCtrl.getInstance().getOrganizacao().setCnpj(txtCNPJ.getText());
		SistemaCtrl.getInstance().getOrganizacao().setLogradouro(txtLogradouro.getText());
		SistemaCtrl.getInstance().getOrganizacao().setNumero(txtNumero.getText());
		SistemaCtrl.getInstance().getOrganizacao().setComplemento(txtComplemento.getText());
		SistemaCtrl.getInstance().getOrganizacao().setCidade(cmbCidade.getSelectedValue());
		SistemaCtrl.getInstance().getOrganizacao().setEmail(txtEmail.getText());
		SistemaCtrl.getInstance().getOrganizacao().setBairro(txtBairro.getText());
		SistemaCtrl.getInstance().getOrganizacao().setTelefones(lsTelefones.getItems());
	}
}
