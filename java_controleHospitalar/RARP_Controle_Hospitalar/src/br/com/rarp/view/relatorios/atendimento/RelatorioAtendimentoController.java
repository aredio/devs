package br.com.rarp.view.relatorios.atendimento;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import br.com.rarp.control.AtendimentoCtrl;
import br.com.rarp.control.EntradaPacienteCtrl;
import br.com.rarp.control.FuncionarioCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.control.UsuarioCtrl;
import br.com.rarp.enums.Funcao;
import br.com.rarp.enums.StatusAtendimento;
import br.com.rarp.model.Atendimento;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Funcionario;
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

public class RelatorioAtendimentoController implements Initializable {

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
	private DatePicker txtDataIniAtend;

	@FXML
	private DatePicker txtDataFinAtend;

	@FXML
	private LocalTimeTextField txtHoraIniAtend;

	@FXML
	private LocalTimeTextField txtHoraFinAtend;

	@FXML
	private TableView<Atendimento> tblAtendimentos;

	@FXML
	private TableColumn<Atendimento, String> cmnCodigo;

	@FXML
	private TableColumn<Atendimento, String> cmnData;

	@FXML
	private TableColumn<Atendimento, String> cmnHora;

	@FXML
	private TableColumn<Atendimento, String> cmnDataAtendimento;

	@FXML
	private TableColumn<Atendimento, String> cmnHoraInicial;

	@FXML
	private TableColumn<Atendimento, String> cmnHoraFinal;

	@FXML
	private TableColumn<Atendimento, String> cmnEntradaPaciente;

	@FXML
	private TableColumn<Atendimento, String> cmnResponsavel;

	@FXML
	private TableColumn<Atendimento, String> cmnStatusAtendimento;

	@FXML
	private TableColumn<Atendimento, String> cmnStatus;

	@FXML
	private AutoCompleteComboBox<EntradaPaciente> cmbEntradaPaciente;

	@FXML
	private AutoCompleteComboBox<StatusAtendimento> cmbStatusAtendimento;

	@FXML
	private AutoCompleteComboBox<Funcionario> cmbResponsavel;

	@FXML
	private AutoCompleteComboBox<String> cmbStatus;

	@FXML
	private AutoCompleteComboBox<Usuario> cmbUsuario;

	@FXML
	void imprimir(ActionEvent event) {
		try {
			JasperReport report = JasperCompileManager
					.compileReport(getClass().getResource("RelatorioAtendimento.jrxml").getFile());
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
			params.put("BY_RESPONSAVEL", agruparPorResponsável());
			params.put("QTDE_ATENDIMENTO", tblAtendimentos.getItems().size() + "");
			params.put("MED_RESPONSAVEL", getMediaPorResponsavel() + "");
			params.put("QTDE_EM_ABERTO", getQtdeEmAberto() + "");
			params.put("QTDE_EM_ANDAMENTO", getQtdeEmAndamento() + "");
			params.put("QTDE_REALIZADO", getQtdeRealizado() + "");
			params.put("QTDE_DESATIVADO", getQtdeDesativado() + "");
			params.put("PathGraficoPizza", "src/br/com/rarp/view/relatorios/GraficoPizza.jasper");

			JasperPrint print = JasperFillManager.fillReport(report, params,
					new JRBeanCollectionDataSource(tblAtendimentos.getItems()));
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
		for (Atendimento a : tblAtendimentos.getItems())
			if (!a.isStatus())
				result++;
		return result;
	}

	private int getQtdeEmAndamento() {
		int result = 0;
		for (Atendimento a : tblAtendimentos.getItems())
			if (a.isStatus() && a.getStatusAtendimento() == StatusAtendimento.emAndamento)
				result++;
		return result;
	}

	private int getQtdeRealizado() {
		int result = 0;
		for (Atendimento a : tblAtendimentos.getItems())
			if (a.isStatus() && a.getStatusAtendimento() == StatusAtendimento.realizado)
				result++;
		return result;
	}

	private int getQtdeEmAberto() {
		int result = 0;
		for (Atendimento a : tblAtendimentos.getItems())
			if (a.isStatus() && a.getStatusAtendimento() == StatusAtendimento.emAberto)
				result++;
		return result;
	}

	private double getMediaPorResponsavel() {
		List<Funcionario> funcionarios = new ArrayList<>();
		for (Atendimento a : tblAtendimentos.getItems()) {
			if (!funcionarios.contains(a.getResponsavel()))
				funcionarios.add(a.getResponsavel());
		}
		return tblAtendimentos.getItems().size() > 0 ? tblAtendimentos.getItems().size() / funcionarios.size() : 0;
	}

	private List<ChartPizzaValue> agruparPorResponsável() {
		List<ChartPizzaValue> values = new ArrayList<>();
		for (Atendimento a : tblAtendimentos.getItems()) {
			ChartPizzaValue value = new ChartPizzaValue();
			if (a.getResponsavel() == null || a.getResponsavel().getNome().equals(""))
				value.setLegend("Sem Responsavel");
			else
				value.setLegend(a.getResponsavel().getNome());
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
			value.setLabel(String.format("%.1f",
					Utilitarios.getPercentual(tblAtendimentos.getItems().size(), value.getValue())) + " %");

		return values;
	}

	private List<ChartPizzaValue> agruparPorUsuario() {
		List<ChartPizzaValue> values = new ArrayList<>();
		for (Atendimento a : tblAtendimentos.getItems()) {
			ChartPizzaValue value = new ChartPizzaValue();
			if (a.getUsuario() == null || a.getUsuario().getNome().equals(""))
				value.setLegend("Sem Usuario");
			else
				value.setLegend(a.getUsuario().getNome());
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
			value.setLabel(String.format("%.1f",
					Utilitarios.getPercentual(tblAtendimentos.getItems().size(), value.getValue())) + " %");

		return values;
	}

	@FXML
	void voltar(ActionEvent event) {
		if (((BorderPane) node.getParent()) != null)
			((BorderPane) node.getParent()).setCenter(null);
	}

	public RelatorioAtendimentoController() throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("RelatorioAtendimento.fxml"));
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
		AtendimentoCtrl atendimentoCtrl = new AtendimentoCtrl();
		try {
			tblAtendimentos.getItems()
					.setAll(atendimentoCtrl.consultar(txtDataIni.getValue(), txtDataFin.getValue(),
							txtHoraIni.getLocalTime(), txtHoraFin.getLocalTime(), txtDataIniAtend.getValue(),
							txtHoraIniAtend.getLocalTime(), txtDataFinAtend.getValue(), txtHoraFinAtend.getLocalTime(),
							cmbEntradaPaciente.getSelectedValue(), cmbResponsavel.getSelectedValue(),
							cmbUsuario.getSelectedValue(), cmbStatusAtendimento.getSelectedValue(),
							cmbStatus.getSelectedValue()));
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
			cmbResponsavel.getItems().setAll(new FuncionarioCtrl().getFuncionarios(Funcao.atendente));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbEntradaPaciente.getItems().setAll(new EntradaPacienteCtrl().getEntradasPaciente());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbStatusAtendimento.getItems().setAll(StatusAtendimento.values());
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
		cmnDataAtendimento.setCellValueFactory(new PropertyValueFactory<>("dataAtendimento"));
		cmnEntradaPaciente.setCellValueFactory(new PropertyValueFactory<>("entradaPaciente"));
		cmnHoraFinal.setCellValueFactory(new PropertyValueFactory<>("horaFim"));
		cmnHoraInicial.setCellValueFactory(new PropertyValueFactory<>("horaIni"));
		cmnResponsavel.setCellValueFactory(new PropertyValueFactory<>("responsavel"));
		cmnStatusAtendimento.setCellValueFactory(new PropertyValueFactory<>("statusAtendimento"));
		cmnStatus.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Atendimento, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Atendimento, String> param) {
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
