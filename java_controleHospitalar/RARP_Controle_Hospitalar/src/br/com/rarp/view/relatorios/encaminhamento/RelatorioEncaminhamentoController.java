package br.com.rarp.view.relatorios.encaminhamento;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import br.com.rarp.control.EncaminhamentoCtrl;
import br.com.rarp.control.EntradaPacienteCtrl;
import br.com.rarp.control.EspacoCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.control.UsuarioCtrl;
import br.com.rarp.model.Encaminhamento;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.model.Espaco;
import br.com.rarp.model.Leito;
import br.com.rarp.model.Usuario;
import br.com.rarp.utils.ChartPizzaValue;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnComponents.AutoCompleteComboBox;
import br.com.rarp.view.scnComponents.ImageCard;
import br.com.rarp.view.scnComponents.SelectionNode;
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

public class RelatorioEncaminhamentoController implements Initializable {

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
	private TableView<Encaminhamento> tblEncaminhamentos;

	@FXML
	private TableColumn<Encaminhamento, String> cmnCodigo;

	@FXML
	private TableColumn<Encaminhamento, String> cmnData;

	@FXML
	private TableColumn<Encaminhamento, String> cmnHora;

	@FXML
	private TableColumn<Encaminhamento, String> cmnOrigem;

	@FXML
	private TableColumn<Encaminhamento, String> cmnDestino;

	@FXML
	private TableColumn<Encaminhamento, String> cmnEntradaPaciente;

	@FXML
	private TableColumn<Encaminhamento, String> cmnUsuario;

	@FXML
	private TableColumn<Encaminhamento, String> cmnStatus;

	@FXML
	private AutoCompleteComboBox<Usuario> cmbUsuario;

	@FXML
	private AutoCompleteComboBox<EntradaPaciente> cmbEntradaPaciente;

	@FXML
	private AutoCompleteComboBox<String> cmbStatus;

	@FXML
	private AutoCompleteComboBox<Espaco> cmbOrigem;

	@FXML
	private AutoCompleteComboBox<Espaco> cmbDestino;

	@FXML
	private SelectionNode<ImageCard> pnlOrigem;

	@FXML
	private SelectionNode<ImageCard> pnlDestino;

	@FXML
	void imprimir(ActionEvent event) {
		try {
			JasperReport report = JasperCompileManager
					.compileReport(getClass().getResource("RelatorioEncaminhamento.jrxml").getFile());
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
			params.put("TITLE", "Relatório de Encaminhamentos");
			params.put("QTDE_ENCAMINHAMENTO", tblEncaminhamentos.getItems().size() + "");
			params.put("MED_ENTRADA", getMediaPorEntrada() + "");
			params.put("QTDE_DESATIVADO", getQtdeDesativado() + "");
			params.put("BY_USUARIO", agruparPorUsuario());
			params.put("PathGraficoPizza", "src/br/com/rarp/view/relatorios/GraficoPizza.jasper");

			JasperPrint print = JasperFillManager.fillReport(report, params,
					new JRBeanCollectionDataSource(tblEncaminhamentos.getItems()));
			String outputFilename = "MeuRelatorio.pdf";
			JasperExportManager.exportReportToPdfFile(print, outputFilename);
			Desktop.getDesktop().open(new File("MeuRelatorio.pdf"));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao imprimir relatório\nMotivo: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private int getQtdeDesativado() {
		int i = 0;
		for (Encaminhamento e : tblEncaminhamentos.getItems())
			if (!e.isStatus())
				i++;
		return i;
	}

	private double getMediaPorEntrada() {
		List<EntradaPaciente> entradas = new ArrayList<>();
		for (Encaminhamento e : tblEncaminhamentos.getItems())
			if (entradas.contains(e.getEntradaPaciente()))
				entradas.add(e.getEntradaPaciente());
		if (entradas.size() > 0)
			return tblEncaminhamentos.getItems().size() / entradas.size();
		return 0;
	}

	// private List<ChartPizzaValue> agruparPorResponsável() {
	// List<ChartPizzaValue> values = new ArrayList<>();
	// for(Atendimento a: tblAtendimentos.getItems()) {
	// ChartPizzaValue value = new ChartPizzaValue();
	// if(a.getResponsavel() == null || a.getResponsavel().getNome().equals(""))
	// value.setLegend("Sem Responsavel");
	// else
	// value.setLegend(a.getResponsavel().getNome());
	// value.setValue(1);
	// if(values.contains(value)) {
	// values.get(values.indexOf(value)).setValue(values.get(values.indexOf(value)).getValue()
	// + 1);
	// } else if(values.size() >= 11) {
	// value.setLegend("Outros");
	// if(values.contains(value))
	// values.get(values.indexOf(value)).setValue(values.get(values.indexOf(value)).getValue()
	// + 1);
	// else
	// values.add(value);
	// } else {
	// values.add(value);
	// }
	// }
	// for(ChartPizzaValue value : values)
	// value.setLabel(String.format("%.1f", Utilitarios
	// .getPercentual(tblAtendimentos.getItems().size(), value.getValue())) + " %");
	//
	// return values;
	// }
	//
	private List<ChartPizzaValue> agruparPorUsuario() {
		List<ChartPizzaValue> values = new ArrayList<>();
		for (Encaminhamento e : tblEncaminhamentos.getItems()) {
			ChartPizzaValue value = new ChartPizzaValue();
			if (e.getUsuario() == null || e.getUsuario().getNome().equals(""))
				value.setLegend("Sem Usuario");
			else
				value.setLegend(e.getUsuario().getNome());
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
					Utilitarios.getPercentual(tblEncaminhamentos.getItems().size(), value.getValue())) + " %");

		return values;
	}

	@FXML
	void voltar(ActionEvent event) {
		if (((BorderPane) node.getParent()) != null)
			((BorderPane) node.getParent()).setCenter(null);
	}

	public RelatorioEncaminhamentoController() throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("RelatorioEncaminhamento.fxml"));
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
			tblEncaminhamentos.getItems()
					.setAll(new EncaminhamentoCtrl().consultar(txtDataIni.getValue(), txtDataFin.getValue(),
							txtHoraIni.getLocalTime(), txtHoraFin.getLocalTime(),
							pnlOrigem.getValue() != null ? pnlOrigem.getValue().getLeito() : null,
							pnlDestino.getValue() != null ? pnlDestino.getValue().getLeito() : null,
							cmbEntradaPaciente.getSelectedValue(), cmbUsuario.getSelectedValue(),
							cmbStatus.getSelectedValue()));
			Utilitarios.message("Consulta realizada com sucesso");
		} catch (Exception e) {
			Utilitarios.erro("Erro ao consultar os encaminhamentos.\n" + "Descrição: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resources) {
		prepararTela();
	}

	private void prepararTela() {
		try {
			cmbOrigem.getItems().setAll(new EspacoCtrl().getEspacos());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbEntradaPaciente.getItems().setAll(new EntradaPacienteCtrl().getEntradasPaciente());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cmbDestino.getItems().setAll(new EspacoCtrl().getEspacos());
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
		cmnOrigem.setCellValueFactory(new PropertyValueFactory<>("origem"));
		cmnDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
		cmnUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
		cmnStatus.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Encaminhamento, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Encaminhamento, String> param) {
						String value = "";
						if (param.getValue() != null)
							value = param.getValue().isStatus() ? "Sim" : "Não";
						return new SimpleStringProperty(value);
					}
				});
		cmbOrigem.setOnAction(onChange);
		cmbDestino.setOnAction(onChange);
	}

	EventHandler<ActionEvent> onChange = (event) -> {
		if (event.getSource() == cmbOrigem && cmbOrigem.getValue() != null) {
			pnlOrigem.getItems().clear();
			for (Leito l : cmbOrigem.getValue().getLeitos()) {
				ImageCard img = new ImageCard();
				img.getPathImage().set(getClass().getResource("../../img/patient128x128.png").toString());
				img.setLeito(l);
				pnlOrigem.getItems().add(img);
			}
		}

		if (event.getSource() == cmbDestino && cmbDestino.getValue() != null) {
			pnlDestino.getItems().clear();
			for (Leito l : cmbDestino.getValue().getLeitos()) {
				ImageCard img = new ImageCard();
				img.getPathImage().set(getClass().getResource("../../img/leitos.png").toString());
				img.setLeito(l);
				pnlDestino.getItems().add(img);
			}
		}
	};

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
