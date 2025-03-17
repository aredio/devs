package br.com.rarp.view.relatorios.entradaPaciente;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import br.com.rarp.control.EntradaPacienteCtrl;
import br.com.rarp.control.FuncionarioCtrl;
import br.com.rarp.control.MedicoCtrl;
import br.com.rarp.control.PacienteCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.control.UsuarioCtrl;
import br.com.rarp.enums.Funcao;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Funcionario;
import br.com.rarp.model.Medico;
import br.com.rarp.model.Paciente;
import br.com.rarp.model.Usuario;
import br.com.rarp.utils.ChartPizzaValue;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import jfxtras.scene.control.LocalTimeTextField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RelatorioEntradaController implements Initializable {
	
	private Node node;
	
    @FXML
    private Button btnAtualizar;

    @FXML
    private Button btnImprimir;

    @FXML
    private Button btnVoltar;

    @FXML
    private DatePicker txtDataIni;

    @FXML
    private DatePicker txtDataFin;

    @FXML
    private LocalTimeTextField txtHoraIni;

    @FXML
    private LocalTimeTextField txtHoraFin;

    @FXML
    private AutoCompleteComboBox<Funcionario> cmbEnfermeira;

    @FXML
    private AutoCompleteComboBox<Funcionario> cmbAtendente;

    @FXML
    private AutoCompleteComboBox<Usuario> cmbUsuario;

    @FXML
    private AutoCompleteComboBox<Paciente> cmbPaciente;

    @FXML
    private AutoCompleteComboBox<Medico> cmbMedico;
    
    @FXML
    private AutoCompleteComboBox<String> cmbStatus;

    @FXML
    private TextField txtPreTriagem;

    @FXML
    private TableView<EntradaPaciente> tblEntradas;
    
    @FXML
    private TableColumn<EntradaPaciente, String> cmnCodigo;

    @FXML
    private TableColumn<EntradaPaciente, String> cmnData;

    @FXML
    private TableColumn<EntradaPaciente, String> cmnHora;

    @FXML
    private TableColumn<EntradaPaciente, String> cmnUsuario;

    @FXML
    private TableColumn<EntradaPaciente, String> cmnPaciente;

    @FXML
    private TableColumn<EntradaPaciente, String> cmnMedico;

    @FXML
    private TableColumn<EntradaPaciente, String> cmnEnfermeira;

    @FXML
    private TableColumn<EntradaPaciente, String> cmnPreTriagem;

    @FXML
    private TableColumn<EntradaPaciente, String> cmnStatus;

    @FXML
    void imprimir(ActionEvent event)  {
    	try {
			JasperReport report = JasperCompileManager.compileReport(getClass().getResource("RelatorioEntrada.jrxml").getFile());
			Map<String, Object> params = new HashMap<>();
			params.put("ORG_NAME", SistemaCtrl.getInstance().getOrganizacao().getNome());
			params.put("ORG_CNPJ", "CNPJ: " + SistemaCtrl.getInstance().getOrganizacao().getCnpj());
			String endereco = SistemaCtrl.getInstance().getOrganizacao().getLogradouro();
			if(endereco == null)
				endereco = "";
			if(!endereco.trim().isEmpty())
				endereco += ", ";
			
			if(SistemaCtrl.getInstance().getOrganizacao().getNumero() != null 
					&& !SistemaCtrl.getInstance().getOrganizacao().getNumero().isEmpty())
				endereco += SistemaCtrl.getInstance().getOrganizacao().getNumero();
			
			if(!endereco.trim().isEmpty())
				endereco += ", ";
			
			if(SistemaCtrl.getInstance().getOrganizacao().getBairro() != null 
					&& !SistemaCtrl.getInstance().getOrganizacao().getBairro().isEmpty())
				endereco += SistemaCtrl.getInstance().getOrganizacao().getBairro();
			
			if(!endereco.trim().isEmpty())
				endereco += ", ";
			
			if(SistemaCtrl.getInstance().getOrganizacao().getCidade() != null 
					&& !SistemaCtrl.getInstance().getOrganizacao().getCidade().getNome().isEmpty())
				endereco += SistemaCtrl.getInstance().getOrganizacao().getCidade().getNome();
			
			if(!endereco.trim().isEmpty())
				endereco += ", ";
			
			if(SistemaCtrl.getInstance().getOrganizacao().getCidade() != null
					&& SistemaCtrl.getInstance().getOrganizacao().getCidade().getEstado() != null
					&& !SistemaCtrl.getInstance().getOrganizacao().getCidade().getEstado().getNome().isEmpty())
				endereco += SistemaCtrl.getInstance().getOrganizacao().getCidade().getEstado().getNome();
			
			params.put("ORG_END", endereco);
			params.put("ORG_FONE",
					SistemaCtrl.getInstance().getOrganizacao().getTelefones().size() > 0
							? SistemaCtrl.getInstance().getOrganizacao().getTelefones().get(0).getNumero()
							: "");
			if(SistemaCtrl.getInstance().getOrganizacao().getEmail() != null)
				params.put("ORG_EMAIL", SistemaCtrl.getInstance().getOrganizacao().getEmail());
			else
				params.put("ORG_EMAIL", "");
			params.put("TITLE", "Relatório de Entrada de Pacientes");
			params.put("EntradasPorAtendente", agruparPorAtendente());
			params.put("EntradasPorMedico", agruparPorMedicos());
			params.put("QTDE_ENTRADAS", tblEntradas.getItems().size()+"");
			int qtdeAtendimentos = getQtdeAtendimentos();
			params.put("QTDE_ATENDIMENTO", qtdeAtendimentos + "");
			params.put("MED_ATENDIMENTO", qtdeAtendimentos == 0 ? "0" : (qtdeAtendimentos / tblEntradas.getItems().size()) + "");
			int qtdeEncaminhamentos = getQtdeEncaminhamentos();
			params.put("QTDE_ENCAMINHAMENTO", qtdeEncaminhamentos + "");
			params.put("MED_ENCAMINHAMENTO",  qtdeEncaminhamentos == 0 ? "0" : (qtdeEncaminhamentos / tblEntradas.getItems().size()) + "");
			params.put("QTDE_EM_ANDAMENTO", getQtdeEmAndamento() + "");
			params.put("QTDE_FINALIZADO", getQtdeFinalizado() + "");
			params.put("QTDE_DESATIVADO", getQtdeDesativado() + "");
			params.put("PathGraficoPizza", "src/br/com/rarp/view/relatorios/GraficoPizza.jasper");
			
			JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(tblEntradas.getItems()));
			String outputFilename = "D:\\MeuRelatorio.pdf";
			JasperExportManager.exportReportToPdfFile(print, outputFilename );
			Desktop.getDesktop().open(new File("D:\\MeuRelatorio.pdf"));
    	} catch (Exception e) {
			Utilitarios.erro("Erro ao imprimir relatório\nMotivo: " + e.getMessage());	
			e.printStackTrace();
		}
    }

    private Object getQtdeDesativado() {
    	int result = 0;
		for(EntradaPaciente e: tblEntradas.getItems())
			if (!e.isStatus()) 
				result++;
		return result;
	}

	private int getQtdeFinalizado() {
    	int result = 0;
		for(EntradaPaciente e: tblEntradas.getItems())
			if (e.isStatus() && e.isAlta()) 
				result++;
		return result;
	}

	private int getQtdeEmAndamento() {
		int result = 0;
		for(EntradaPaciente e: tblEntradas.getItems())
			if (e.isStatus() && !e.isAlta()) 
				result++;
		return result;
	}

	private int getQtdeEncaminhamentos() {
    	int result = 0;
		for(EntradaPaciente e: tblEntradas.getItems())
			result += e.getEncaminhamentos() == null ? 0 : e.getEncaminhamentos().size();
		return result;
	}

	private int getQtdeAtendimentos() {
		int result = 0;
		for(EntradaPaciente e: tblEntradas.getItems())
			result += e.getAtendimentos() == null ? 0 : e.getAtendimentos().size();
		return result;
	}

	private Object agruparPorMedicos() {
		List<ChartPizzaValue> values = new ArrayList<>();
		for(EntradaPaciente e: tblEntradas.getItems()) {
			ChartPizzaValue value = new ChartPizzaValue();
			if(e.getMedico() == null || e.getMedico().getNome().equals(""))
				value.setLegend("Sem Médico");
			else
				value.setLegend(e.getMedico().getNome());
			value.setValue(1);
			if(values.contains(value)) {
				values.get(values.indexOf(value)).setValue(values.get(values.indexOf(value)).getValue() + 1);
			} else if(values.size() >= 11) {
				value.setLegend("Outros");
				if(values.contains(value))
					values.get(values.indexOf(value)).setValue(values.get(values.indexOf(value)).getValue() + 1);
				else
					values.add(value);
			} else {
				values.add(value);
			}
		}
		for(ChartPizzaValue value : values)
			value.setLabel(String.format("%.1f", Utilitarios
					.getPercentual(tblEntradas.getItems().size(), value.getValue())) + " %");
		return values;
	}

	private List<ChartPizzaValue> agruparPorAtendente() {
		List<ChartPizzaValue> values = new ArrayList<>();
		for(EntradaPaciente e: tblEntradas.getItems()) {
			ChartPizzaValue value = new ChartPizzaValue();
			if(e.getAtendente() == null || e.getAtendente().getNome().equals(""))
				value.setLegend("Sem atendente");
			else
				value.setLegend(e.getAtendente().getNome());
			value.setValue(1);
			if(values.contains(value)) {
				values.get(values.indexOf(value)).setValue(values.get(values.indexOf(value)).getValue() + 1);
			} else if(values.size() >= 11) {
				value.setLegend("Outros");
				if(values.contains(value))
					values.get(values.indexOf(value)).setValue(values.get(values.indexOf(value)).getValue() + 1);
				else
					values.add(value);
			} else {
				values.add(value);
			}
		}
		for(ChartPizzaValue value : values)
			value.setLabel(String.format("%.1f", Utilitarios
					.getPercentual(tblEntradas.getItems().size(), value.getValue())) + " %");

		return values;
	}

	@FXML
    void voltar(ActionEvent event) {
    	if(((BorderPane) node.getParent()) != null)
			((BorderPane) node.getParent()).setCenter(null);
    }
    
    public RelatorioEntradaController() throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("RelatorioEntrada.fxml"));
			loader.setController(this);
			node = loader.load();
			node.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if(event.getCode() == KeyCode.ESCAPE)
						voltar(new ActionEvent());
				}	
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    @FXML
    void atualizar(ActionEvent event) {
    	EntradaPacienteCtrl entradaPacienteCtrl = new EntradaPacienteCtrl();
    	try {
			tblEntradas.getItems().setAll(entradaPacienteCtrl.consultar(txtDataIni.getValue(), 
					txtDataIni.getValue(), 
					txtHoraIni.getLocalTime(), 
					txtHoraFin.getLocalTime(),
					cmbAtendente.getSelectedValue(),
					cmbEnfermeira.getSelectedValue(),
					cmbMedico.getSelectedValue(),
					cmbPaciente.getSelectedValue(),
					cmbUsuario.getSelectedValue(),
					txtPreTriagem.getText(),
					cmbStatus.getSelectedValue()));
			Utilitarios.message("Consulta realizada com sucesso");
		} catch (Exception e) {
			Utilitarios.erro("Erro ao consultar as entradas de pacientes.\n" + "Descrição: " + e.getMessage());
			e.printStackTrace();
		}
    }

	@Override
	public void initialize(URL url, ResourceBundle resources) {
		prepararTela();
	}

	private void prepararTela() {
		try {
			cmbAtendente.getItems().setAll(new FuncionarioCtrl().getFuncionarios(Funcao.atendente));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbEnfermeira.getItems().setAll(new FuncionarioCtrl().getFuncionarios(Funcao.enfermeira));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbMedico.getItems().setAll(new MedicoCtrl().getMedicos());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbPaciente.getItems().setAll(new PacienteCtrl().getPacientes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbUsuario.getItems().setAll(new UsuarioCtrl().getUsuarios());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbStatus.getItems().addAll("Ativado", "Desativado");
		} catch (Exception e) {
			e.printStackTrace();
		}
		cmnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		cmnData.setCellValueFactory(new PropertyValueFactory<>("dtMovimentacao"));
	    cmnHora.setCellValueFactory(new PropertyValueFactory<>("hrMovimentacao"));
	    cmnUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
	    cmnPaciente.setCellValueFactory(new PropertyValueFactory<>("paciente"));
	    cmnMedico.setCellValueFactory(new PropertyValueFactory<>("medico"));
	    cmnEnfermeira.setCellValueFactory(new PropertyValueFactory<>("enfermeira"));
	    cmnPreTriagem.setCellValueFactory(new PropertyValueFactory<>("preTriagem"));
	    cmnStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EntradaPaciente,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<EntradaPaciente, String> param) {
				String value = "";
				if(param.getValue() != null)
					value = param.getValue().isStatus() ? "Sim" : "Não";
				return new SimpleStringProperty(value);
			}
		});
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
