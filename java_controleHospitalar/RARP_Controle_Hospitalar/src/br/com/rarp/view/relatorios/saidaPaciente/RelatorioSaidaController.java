package br.com.rarp.view.relatorios.saidaPaciente;

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
import br.com.rarp.control.SaidaPacienteCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.control.UsuarioCtrl;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.SaidaPaciente;
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

public class RelatorioSaidaController implements Initializable {

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
	private LocalTimeTextField txtHoraIni;

	@FXML
	private DatePicker txtDataFin;

	@FXML
	private LocalTimeTextField txtHoraFin;

	@FXML
	private TableView<SaidaPaciente> tblSaidas;

	@FXML
	private TableColumn<SaidaPaciente, String> cmnCodigo;

	@FXML
	private TableColumn<SaidaPaciente, String> cmnData;

	@FXML
	private TableColumn<SaidaPaciente, String> cmnHora;

	@FXML
	private TableColumn<SaidaPaciente, String> cmnEntradaPaciente;

	@FXML
	private TableColumn<SaidaPaciente, String> cmnEstadoPaciente;

	@FXML
	private TableColumn<SaidaPaciente, String> cmnUsuario;

	@FXML
	private TableColumn<SaidaPaciente, String> cmnStatus;

	@FXML
	private AutoCompleteComboBox<EntradaPaciente> cmbEntradaPaciente;

	@FXML
	private AutoCompleteComboBox<Usuario> cmbUsuario;

	@FXML
	private TextField txtEstadoPaciente;

	@FXML
	private AutoCompleteComboBox<String> cmbStatus;

	@FXML
	void imprimir(ActionEvent event) {
		try {
			JasperReport report = JasperCompileManager
					.compileReport(getClass().getResource("RelatorioSaida.jrxml").getFile());
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
			params.put("TITLE", "Relatório de Atendimentos");
			params.put("BY_USUARIO", agruparPorUsuario());
			params.put("QTDE_SAIDA", tblSaidas.getItems().size() + "");
			params.put("QTDE_DESATIVADO", getQtdeDesativado() + "");
			params.put("PathGraficoPizza", "src/br/com/rarp/view/relatorios/GraficoPizza.jasper");

			JasperPrint print = JasperFillManager.fillReport(report, params,
					new JRBeanCollectionDataSource(tblSaidas.getItems()));
			String outputFilename = "MeuRelatorio.pdf";
			JasperExportManager.exportReportToPdfFile(print, outputFilename);
			Desktop.getDesktop().open(new File("MeuRelatorio.pdf"));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao imprimir relatório\nMotivo: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private int getQtdeDesativado() {
		int result = 0;
		for (SaidaPaciente s : tblSaidas.getItems())
			if (!s.isStatus())
				result++;
		return result;
	}

	private List<ChartPizzaValue> agruparPorUsuario() {
		List<ChartPizzaValue> values = new ArrayList<>();
		for (SaidaPaciente s : tblSaidas.getItems()) {
			ChartPizzaValue value = new ChartPizzaValue();
			if (s.getUsuario() == null || s.getUsuario().getNome().equals(""))
				value.setLegend("Sem Usuario");
			else
				value.setLegend(s.getUsuario().getNome());
			value.setValue(1);
			if (values.contains(value)) {
				values.get(values.indexOf(value)).setValue(values.get(values.indexOf(value)).getValue() + 1);
			} else if (values.size() >= 11) {
				value.setLegend("Outros");
				if (values.contains(value))
					values.get(values.indexOf(value)).setValue(values.get(values.indexOf(value)).getValue() + 1);
				else
					values.add(value);
			} else {
				values.add(value);
			}
		}
		for (ChartPizzaValue value : values)
			value.setLabel(
					String.format("%.1f", Utilitarios.getPercentual(tblSaidas.getItems().size(), value.getValue()))
							+ " %");

		return values;
	}

	@FXML
	void voltar(ActionEvent event) {
		if (((BorderPane) node.getParent()) != null)
			((BorderPane) node.getParent()).setCenter(null);
	}

	public RelatorioSaidaController() throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("RelatorioSaida.fxml"));
			loader.setController(this);
			node = loader.load();
			node.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.ESCAPE)
						voltar(new ActionEvent());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void atualizar(ActionEvent event) {
		try {
			tblSaidas.getItems()
					.setAll(new SaidaPacienteCtrl().consultar(txtDataIni.getValue(), txtDataFin.getValue(),
							txtHoraIni.getLocalTime(), txtHoraFin.getLocalTime(), cmbEntradaPaciente.getSelectedValue(),
							cmbUsuario.getSelectedValue(), txtEstadoPaciente.getText(), cmbStatus.getSelectedValue()));
			Utilitarios.message("Consulta realizada com sucesso");
		} catch (Exception e) {
			Utilitarios.erro("Erro ao consultar os atendimentos.\n" + "Descrição: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resources) {
		prepararTela();
	}

	private void prepararTela() {
		try {
			cmbEntradaPaciente.getItems().setAll(new EntradaPacienteCtrl().getEntradasPaciente());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbStatus.getItems().addAll("Ativado", "Desativado");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbUsuario.getItems().setAll(new UsuarioCtrl().getUsuarios());
		} catch (Exception e) {
			e.printStackTrace();
		}
		cmnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		cmnData.setCellValueFactory(new PropertyValueFactory<>("dtMovimentacao"));
		cmnHora.setCellValueFactory(new PropertyValueFactory<>("hrMovimentacao"));
		cmnEntradaPaciente.setCellValueFactory(new PropertyValueFactory<>("entradaPaciente"));
		cmnUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
		cmnEstadoPaciente.setCellValueFactory(new PropertyValueFactory<>("estadoPaciente"));
		cmnStatus.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<SaidaPaciente, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<SaidaPaciente, String> param) {
						String value = "";
						if (param.getValue() != null)
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
