package br.com.rarp.view.scnConsulta;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import br.com.rarp.control.CosultaCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.control.Wai;
import br.com.rarp.model.EntradaPacienteWS;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
import br.com.rarp.view.scnComponents.IntegerTextField;
import br.com.rarp.view.scnComponents.TextFieldFormatter;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ConsultaController extends Application implements Initializable {


    @FXML // fx:id="txtPesquisa"
    private TextFieldFormatter  txtPesquisa; // Value injected by FXMLLoader

    @FXML // fx:id="btnPesquisar"
    private Button btnPesquisar; // Value injected by FXMLLoader

    @FXML // fx:id="tbvResultado"
    private TableView<EntradaPacienteWS> tbvResultado; // Value injected by FXMLLoader

    @FXML
    private TableColumn<EntradaPacienteWS, String> tbcDataEntrada;

    @FXML
    private TableColumn<EntradaPacienteWS, String> tbcHospital;

    @FXML
    private TableColumn<EntradaPacienteWS, String> tblMedico;

    @FXML
    private TableColumn<EntradaPacienteWS, String> tbcDescricaoMedica;

    
    @FXML
    private Button btnVoltar;
    
    @FXML
    private Button btnImprimir;
    
    @FXML
    private AutoCompleteComboBox<String> cmbTipoDocumento;
    
    @FXML // fx:id="txtSerie"
    private IntegerTextField txtSerie; // Value injected by FXMLLoader

    @FXML // fx:id="txtNumerecao"
    private IntegerTextField txtNumerecao; // Value injected by FXMLLoader
     
    private static Stage stage;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		
		list.add("CPF");
		list.add("RG");
		
		cmbTipoDocumento.setItems(FXCollections.observableList(list));
		
		
		tipoDocumentoChange();
		
//		TableColumn<EntradaPaciente, String> data = new TableColumn<>("Data");
//		paciente.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EntradaPaciente,String>, ObservableValue<String>>() {
//					
//					@Override
//					public ObservableValue<String> call(CellDataFeatures<EntradaPaciente, String> param) {
//						String value = "";
//						if(param.getValue() != null && param.getValue().getPaciente() != null) {
//							value = param.getValue().getPaciente().getNome();
//						}
//						return new SimpleStringProperty(value);
//					}
//				});
		
		try {
			prepararTela();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	@Override
	
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage = SistemaCtrl.getInstance().getStage();
																	 
		primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("ConsultaOnline.fxml"))));
		stage = primaryStage;
		primaryStage.setMaximized(true);
		primaryStage.showAndWait();
		
		
	}
	
	@SuppressWarnings("static-access")
	public void abrir() {
		try {
			start(this.stage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	@FXML 
	private void consultar() {
		
		
			/**
			 * 
			 *
			ConsultaSOAP consultaSOAP = new ConsultaSOAP();
			Consulta consulta = consultaSOAP.getConsultaSOAPPort();
			consulta.sevidorOn(SistemaCtrl.getInstance().getConfiguracoes().getUsuarioRARP());
			PessoaFisica pessoaFisica = new PessoaFisica();
			if ( cmbTipoDocumento.getSelectionModel().getSelectedIndex() == 0) {
				pessoaFisica.setCep(txtPesquisa.getText());
			}
			if ( cmbTipoDocumento.getSelectionModel().getSelectedIndex() == 1) {
				pessoaFisica.setRg(txtPesquisa.getText());
			}
			if ( cmbTipoDocumento.getSelectionModel().getSelectedIndex() == 2) {
				pessoaFisica.setSUS(txtPesquisa.getText());
			}
			
			Requisicao requisicao = new Requisicao();
			requisicao.setPessoaFisica(pessoaFisica);
			requisicao.setSerie(txtSerie.getValue());
			requisicao.setNumeracao(txtNumerecao.getValue());
			List<Resposta>  list = consulta.consultar(requisicao);
			
			if ((list != null) && (list.size() > 0)) {
				Utilitarios.message("soufoda");
			}
		*/
		Wai wai = new Wai();
		try {
		//	wai.abrir();
			CosultaCtrl cosultaCtrl= new CosultaCtrl();
			br.com.rarp.model.PessoaFisica pf = new br.com.rarp.model.PessoaFisica();
			if ( cmbTipoDocumento.getSelectionModel().getSelectedIndex() == 0) {
				
				pf.setCpf(txtPesquisa.getText());
				cosultaCtrl.getPessoaFisica().setCpf(pf.getCpfSemMascara());
			}
			if ( cmbTipoDocumento.getSelectionModel().getSelectedIndex() == 1) {
				cosultaCtrl.getPessoaFisica().setRg(txtPesquisa.getText());
			}
			if ( cmbTipoDocumento.getSelectionModel().getSelectedIndex() == 2) {
				cosultaCtrl.getPessoaFisica().setSUS(txtPesquisa.getText());
			}
				
			tbvResultado.getItems().setAll(cosultaCtrl.consultar());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Utilitarios.atencao("Falha ao Consultar "+e.getMessage());
		}finally {
			wai.fechar();
		}
		
		
	}
	
	@FXML
	private void tipoDocumentoChange() {
		 cmbTipoDocumento.valueProperty().addListener(new ChangeListener<String>() {
		        public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String t, String t1) {
		        	txtPesquisa.setText("");
		    		if ( cmbTipoDocumento.getSelectionModel().getSelectedIndex() == 0) {
		    			txtPesquisa.setMask("###.###.###-##");
		    		}
		    		if ( cmbTipoDocumento.getSelectionModel().getSelectedIndex() == 1) {
		    			txtPesquisa.setMask("AAAAAAAAAAAAAAA");
		    		}
		    		if ( cmbTipoDocumento.getSelectionModel().getSelectedIndex() == 2) {
		    			txtPesquisa.setMask("############/####-#");
		    		}
		        }    
		    });
		
		
	}
	
	@FXML
	private void volta() {
		try {
			if (stage !=  null)
					stage.hide();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	public void prepararTela() throws Exception {
		   tbcDataEntrada.setCellValueFactory(new PropertyValueFactory<>("dtMovimentacao"));

		   tbcHospital.setCellValueFactory(new PropertyValueFactory<>("Hospital"));

		   tblMedico.setCellValueFactory(new PropertyValueFactory<>("medico"));

		   tbcDescricaoMedica.setCellValueFactory(new PropertyValueFactory<>("Descrioes"));
		    
	}
	
}
