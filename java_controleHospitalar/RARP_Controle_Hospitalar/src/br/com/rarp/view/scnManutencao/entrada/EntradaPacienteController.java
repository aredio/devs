package br.com.rarp.view.scnManutencao.entrada;

import java.time.format.DateTimeFormatter;

import br.com.rarp.control.EntradaPacienteCtrl;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.enums.TipoCampo;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.EntradaPaciente;
import br.com.rarp.utils.Campo;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.scnControleEntrada.ControleEntradaController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class EntradaPacienteController extends ManutencaoController {

	@SuppressWarnings("unchecked")
	@Override
	public void prepararTela() throws Exception {
		getLblTitle().setText("Controle de Entrada de Pacientes");
		getLblTitle().setTextFill(Paint.valueOf("#000000"));
		getLblTitle().setStyle("-fx-background-color: #FFD700;"
				+ "-fx-font-weight: bold;"
				+ "-fx-color: white");
		
		TableColumn<EntradaPaciente, String> codigo = new TableColumn<>("Código");
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		
		TableColumn<EntradaPaciente, String> data = new TableColumn<>("Data");
		data.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EntradaPaciente,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<EntradaPaciente, String> param) {
				String value = "";
				if(param.getValue() != null && param.getValue().getDtMovimentacao() != null) {
					value = param.getValue().getDtMovimentacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<EntradaPaciente, String> hora = new TableColumn<>("Hora");
		hora.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EntradaPaciente,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<EntradaPaciente, String> param) {
				String value = "";
				if(param.getValue() != null && param.getValue().getHrMovimentacao() != null) {
					value = param.getValue().getHrMovimentacao().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<EntradaPaciente, String> paciente = new TableColumn<>("Paciente");
		paciente.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EntradaPaciente,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<EntradaPaciente, String> param) {
				String value = "";
				if(param.getValue() != null && param.getValue().getPaciente() != null) {
					value = param.getValue().getPaciente().getNome();
				}
				return new SimpleStringProperty(value);
			}
		});
		
		TableColumn<EntradaPaciente, String> atendente = new TableColumn<>("Atendente");
		atendente.setCellValueFactory(new PropertyValueFactory<>("atendente"));
		
		codigo.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.1));
		data.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.13));
		hora.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.13));
		paciente.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.3));
		atendente.prefWidthProperty().bind(tblManutencao.widthProperty().multiply(0.3));
		
		tblManutencao.getColumns().addAll(codigo, data, hora, paciente, atendente);
		tblManutencao.setEditable(false);
		adicionarCampos();
		cmbCampo.getSelectionModel().select(0);
		cmbCampo.getOnAction().handle(new ActionEvent());
	}

	private void adicionarCampos() {
		cmbCampo.getItems().add(new Campo("ENT.codigo", "Codigo", TipoCampo.numerico));
		cmbCampo.getItems().add(new Campo("MOV.data", "Data da entrada", TipoCampo.data));
		cmbCampo.getItems().add(new Campo("PAC.nome", "Nome do(a) Paciente", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("MED.nome", "Nome do(a) Médico(a)", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("ENF.nome", "Nome do(a) Enfermeiro(a)", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("ATE.nome", "Nome do(a) Atendente(a)", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("USU.nome", "Nome do(a) Usuario(a)", TipoCampo.texto));
		cmbCampo.getItems().add(new Campo("ENT.status", "Ativado", TipoCampo.booleano));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pesquisar() {
		EntradaPacienteCtrl entradaPacienteCtrl = new EntradaPacienteCtrl();
		try {
			tblManutencao.setItems(entradaPacienteCtrl.consultar(cmbCampo.getSelectionModel().getSelectedItem(),
					cmbComparacao.getSelectionModel().getSelectedItem(),
					cmbCampo.getSelectionModel().getSelectedItem().getTipo() == TipoCampo.booleano ? cmbTermo.getValue()
							: txtTermo.getText()));
		} catch (Exception e) {
			Utilitarios.erro("Erro ao pesquisar as entradas de paciente.\n" + "Descrição: " + e.getMessage());
		}
	}

	@Override
	public void inserir() {
		try {
			SistemaCtrl.getInstance().liberarControleEntradaPaciente(TipoMovimentacao.insercao);
			ControleEntradaController controler = new ControleEntradaController();
			controler.inserir();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void alterar() {
		try {
			SistemaCtrl.getInstance().liberarControleEntradaPaciente(TipoMovimentacao.alteracao);
			ControleEntradaController controller = new ControleEntradaController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				EntradaPacienteCtrl entradaPacienteCtrl = new EntradaPacienteCtrl();
				entradaPacienteCtrl.setEntradaPaciente(tblManutencao.getSelectionModel().getSelectedItem());
				controller.alterar(entradaPacienteCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void visualizar() {
		try {
			SistemaCtrl.getInstance().liberarControleEntradaPaciente(TipoMovimentacao.visualizaco);
			ControleEntradaController controller = new ControleEntradaController();
			if (tblManutencao.getSelectionModel().getSelectedItem() == null)
				Utilitarios.erro("Nenhum registro foi selecionado");
			else {
				EntradaPacienteCtrl entradaPacienteCtrl = new EntradaPacienteCtrl();
				entradaPacienteCtrl.setEntradaPaciente(tblManutencao.getSelectionModel().getSelectedItem());
				controller.visualizar(entradaPacienteCtrl);
			}
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

}
