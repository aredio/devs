package br.com.rarp.view.main.scnMain;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.control.UsuarioCtrl;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.utils.Utilitarios;
import br.com.rarp.view.relatorios.atendimento.RelatorioAtendimentoController;
import br.com.rarp.view.relatorios.encaminhamento.RelatorioEncaminhamentoController;
import br.com.rarp.view.relatorios.entradaPaciente.RelatorioEntradaController;
import br.com.rarp.view.relatorios.limpeza.RelatorioLimpezaController;
import br.com.rarp.view.relatorios.saidaPaciente.RelatorioSaidaController;
import br.com.rarp.view.scnAcesso.AcessoController;
import br.com.rarp.view.scnConsulta.ConsultaController;
import br.com.rarp.view.scnLogin.LoginController;
import br.com.rarp.view.scnManutencao.ManutencaoController;
import br.com.rarp.view.scnManutencao.atendimento.AtendimentoController;
import br.com.rarp.view.scnManutencao.cargo.CargoController;
import br.com.rarp.view.scnManutencao.cidade.CidadeController;
import br.com.rarp.view.scnManutencao.convenio.ConvenioController;
import br.com.rarp.view.scnManutencao.encaminhamento.EncaminhamentoController;
import br.com.rarp.view.scnManutencao.entrada.EntradaPacienteController;
import br.com.rarp.view.scnManutencao.espaco.EspacoController;
import br.com.rarp.view.scnManutencao.especialidade.EspecialidadeController;
import br.com.rarp.view.scnManutencao.funcionario.FuncionarioController;
import br.com.rarp.view.scnManutencao.limpeza.LimpezaController;
import br.com.rarp.view.scnManutencao.medico.MedicoController;
import br.com.rarp.view.scnManutencao.paciente.PacienteController;
import br.com.rarp.view.scnManutencao.perfilUsuario.PerfilUsuarioController;
import br.com.rarp.view.scnManutencao.saida.SaidaController;
import br.com.rarp.view.scnManutencao.usuario.UsuarioController;
import br.com.rarp.view.scnOpcoes.OpcoesController;
import br.com.rarp.view.scnSplash.SplashController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class MainController extends Application implements Initializable {

    @FXML
    private BorderPane pnMain;

    @FXML
    private MenuBar mnbMenu;

    @FXML
    private Menu mnArquivo;

    @FXML
    private MenuItem mniOpcoes;

    @FXML
    private MenuItem mniSair;

    @FXML
    private Menu mnCadastros;

    @FXML
    private MenuItem mniFuncionarios;

    @FXML
    private MenuItem mniPacientes;

    @FXML
    private MenuItem mniMedicos;

    @FXML
    private MenuItem mniLeitos;

    @FXML
    private MenuItem mniCidades;

    @FXML
    private MenuItem mniEspecialidade;

    @FXML
    private Menu mnMovimentacoes;

    @FXML
    private Menu mnRelatorios;

    @FXML
    private Menu mnSeguranca;

    @FXML
    private CheckMenuItem mniControleAcesso;

    @FXML
    private MenuItem mniUsuario;

    @FXML
    private MenuItem mniPerfilUsuario;

    @FXML
    private MenuItem mniTrocarUsuario;

    @FXML
    private Menu mnSobre;

    @FXML
    private ToolBar tlbBarraFerramentas;

    @FXML
    private Button btnFuncionarios;

    @FXML
    private Button btnPacientes;

    @FXML
    private Button btnMedicos;

    @FXML
    private Button btnLeitos;

    @FXML
    private Button btnEntrada;

    @FXML
    private Button btnSaida;

    @FXML
    private Button btnAjuda;

    @FXML
    private Button btnSair;

    @FXML
    private ImageView imgControleAcesso;

    @FXML
    private Label lblUsuarioSessao;

    @FXML
    private Label lblRelogio;

    @FXML
	private AnchorPane pnlContent;

    @FXML
    private ImageView imgMain;
    
	private ManutencaoController manutencao;

	@Override
	public void start(Stage stage) throws Exception {
		try {
			SplashController splash = new SplashController();
			splash.abrir(1);
			SistemaCtrl.getInstance().configuraConexao();
			SistemaCtrl.getInstance().criarTabelas();
			SistemaCtrl.getInstance().criarRegistrosPadroes();
			SistemaCtrl.getInstance().getConfiguracoesDB();
			SistemaCtrl.getInstance().getOrganizacaoDB();
			splash.getStage().close();
			if (SistemaCtrl.getInstance().getConfiguracoes().isControleAcesso()) {
				UsuarioCtrl usuarioCtrl = new UsuarioCtrl();
				if (!usuarioCtrl.isEmpty()) {					
					AcessoController acessoController = new AcessoController();
					if (!acessoController.logar())
						System.exit(0);
				} else {
					SistemaCtrl.getInstance().getConfiguracoes().setControleAcesso(false);
				}
			}	
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					SistemaCtrl.getInstance().getPropriedades().setPropriedades();
				}
			});
			splash.getStage().show();
			stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Main.fxml"))));
			stage.setTitle("RARP Controle Hospitalar - Sistema de controle hospitalar");
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/br/com/rarp/view/img/UnderComputer-38(2).png")));
			stage.setIconified(true);
			stage.setMaximized(true);

			try {
				SistemaCtrl.getInstance().getConexao().getConexao();
			} catch (Exception e) {
				try {
					SistemaCtrl.getInstance().getConexao().criarDataBase();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				stage.show();
			}
			SistemaCtrl.getInstance().criarTabelas();
			splash.getStage().close();
			stage.show();
			stage.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if(event.getTarget() instanceof Button && event.getCharacter().equals("\r"))
						((Button) event.getTarget()).fire();
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void abrir() {
		launch();
	}

	public void ativarDesativarControleAcesso() {
		if (mniControleAcesso.isSelected()) {
			if (SistemaCtrl.getInstance().getUsuarioSessao() == null) {
				LoginController login = new LoginController();
				try {
					if (!login.logar())
						System.exit(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				SistemaCtrl.getInstance().liberarControleAcesso();
				SistemaCtrl.getInstance().getConfiguracoes().setControleAcesso(false);
				imgControleAcesso.setImage(
						new Image(getClass().getResource("..\\..\\img\\security-system-desativada-16x16.png").toString()));
				SistemaCtrl.getInstance().setUsuarioSessao(null);
			} catch (Exception e) {
				Utilitarios.erro(e.getMessage());
			}
		}
		if(SistemaCtrl.getInstance().getUsuarioSessao() == null) {
			SistemaCtrl.getInstance().getConfiguracoes().setControleAcesso(false);
			mniControleAcesso.setSelected(false);
			imgControleAcesso.setImage(new Image(getClass().getResource("..\\..\\img\\security-system-desativada-16x16.png").toString()));
			lblUsuarioSessao.setText("");
		} else {
			imgControleAcesso.setImage(new Image(getClass().getResource("..\\..\\img\\security-system-ativada-16x16.png").toString()));
			SistemaCtrl.getInstance().getConfiguracoes().setControleAcesso(true);
			mniControleAcesso.setSelected(true);
			lblUsuarioSessao.setText(SistemaCtrl.getInstance().getUsuarioSessao().getNome());
		}
		if(!pnMain.getCenter().equals(pnlContent))
			pnMain.setCenter(null);
		try {
			SistemaCtrl.getInstance().salvarConfiguracoes();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
		}
	}

	@FXML
	private void trocarUsuario(ActionEvent event) {
		if(mniControleAcesso.isSelected()) {
			LoginController login = new LoginController();
			try {
				if(login.logar()) {
					lblUsuarioSessao.setText(SistemaCtrl.getInstance().getUsuarioSessao().getNome());
					pnMain.setCenter(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mniControleAcesso.setSelected(true);
			ativarDesativarControleAcesso();
		}
	}

	public void sair() {
		SistemaCtrl.getInstance().getPropriedades().setPropriedades();
		Platform.exit();
		System.exit(0);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mniControleAcesso.setSelected(SistemaCtrl.getInstance().getConfiguracoes().isControleAcesso());
		ativarDesativarControleAcesso();
		mniControleAcesso.fire();
		initRelogio();
		
		pnlContent.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				imgMain.setFitHeight(pnlContent.getHeight());
			}
		});
		
		pnlContent.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				imgMain.setFitWidth(pnlContent.getWidth());
			}
		});
		
		pnMain.centerProperty().addListener(new ChangeListener<Node>() {

			@Override
			public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
				if(newValue == null) {
					pnMain.setCenter(imgMain);
					focarToolBar(true);
					tlbBarraFerramentas.requestFocus();
					btnFuncionarios.requestFocus();
				}
			}
		});
	}
	
	private void focarToolBar(boolean value) {
		btnAjuda.setFocusTraversable(value);
		btnEntrada.setFocusTraversable(value);
		btnFuncionarios.setFocusTraversable(value);
		btnLeitos.setFocusTraversable(value);
		btnMedicos.setFocusTraversable(value);
		btnPacientes.setFocusTraversable(value);
		btnSaida.setFocusTraversable(value);
		btnSair.setFocusTraversable(value);
	}

	private void initRelogio() {
		KeyFrame frame = new KeyFrame(Duration.millis(1000), e -> atualizaHora());
		Timeline timeline = new Timeline(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	private void atualizaHora() {
		lblRelogio.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date().getTime()));
	}
	
	@FXML
	private void manterUsuario(ActionEvent event) {
		try {
			SistemaCtrl.getInstance().liberarManutencaoUsuario(TipoMovimentacao.acesso);
			manutencao = new UsuarioController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
	
    @FXML
    private void manterConvenios(ActionEvent event) {
		try {
			SistemaCtrl.getInstance().liberarManutencaoConvenio(TipoMovimentacao.acesso);
			manutencao = new ConvenioController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
    }

	@FXML
	private void manterPerfilUsuario() {
		try {
			SistemaCtrl.getInstance().liberarManutencaoPerfilUsuario(TipoMovimentacao.acesso);
			manutencao = new PerfilUsuarioController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
	
    @FXML
    private void manterCargos(ActionEvent event) {
		try {
			SistemaCtrl.getInstance().liberarManutencaoCargo(TipoMovimentacao.acesso);
			manutencao = new CargoController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
    }
    
    @FXML
	private void controlarEntrada(ActionEvent event) {
		try {
			SistemaCtrl.getInstance().liberarControleEntradaPaciente(TipoMovimentacao.acesso);
			manutencao = new EntradaPacienteController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
    
    @FXML
    void controlarAtendimento(ActionEvent event) {
    	try {
			SistemaCtrl.getInstance().liberarControleAtendimento(TipoMovimentacao.acesso);
			manutencao = new AtendimentoController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
    }

    @FXML
    void controlarEncaminhamento(ActionEvent event) {
    	try {
			SistemaCtrl.getInstance().liberarControleEncaminhamento(TipoMovimentacao.acesso);
			manutencao = new EncaminhamentoController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
    }

    @FXML
    void controlarLimpeza(ActionEvent event) {
    	try {
			SistemaCtrl.getInstance().liberarControleLimpeza(TipoMovimentacao.acesso);
			manutencao = new LimpezaController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
    }
    
    @FXML
    void relatorioAtendimento(ActionEvent event) {
    	try {
    		SistemaCtrl.getInstance().liberarRelatorioAtendimento(TipoMovimentacao.acesso);
    		RelatorioAtendimentoController relatorioAtendimento = new RelatorioAtendimentoController();
			pnMain.setCenter(relatorioAtendimento.getNode());
			focarToolBar(false);
			relatorioAtendimento.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro("Erro ao abrir relatório de atendimentos");
			e.printStackTrace();
		}    	
    }

    @FXML
    void relatorioEncaminhamento(ActionEvent event) {
    	try {
    		SistemaCtrl.getInstance().liberarRelatorioEncaminhamento(TipoMovimentacao.acesso);
    		RelatorioEncaminhamentoController relatorioEncaminhamento = new RelatorioEncaminhamentoController();
			pnMain.setCenter(relatorioEncaminhamento.getNode());
			focarToolBar(false);
			relatorioEncaminhamento.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro("Erro ao abrir relatório de encaminhamento");
			e.printStackTrace();
		}  
    }

    @FXML
    void relatorioEntrada(ActionEvent event) {
    	try {
    		SistemaCtrl.getInstance().liberarRelatorioEntrada(TipoMovimentacao.acesso);
    		RelatorioEntradaController relatorioEntrada = new RelatorioEntradaController();
			pnMain.setCenter(relatorioEntrada.getNode());
			focarToolBar(false);
			relatorioEntrada.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro("Erro ao abrir relatório de entrada de pacientes");
			e.printStackTrace();
		}
    }
    
    @FXML
    void relatorioSaida(ActionEvent event) {
    	try {
    		SistemaCtrl.getInstance().liberarRelatorioSaida(TipoMovimentacao.acesso);
    		RelatorioSaidaController relatorioSaida = new RelatorioSaidaController();
			pnMain.setCenter(relatorioSaida.getNode());
			focarToolBar(false);
			relatorioSaida.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro("Erro ao abrir relatório de saida de pacientes");
			e.printStackTrace();
		}
    }
    
    @FXML
    void relatorioLimpeza(ActionEvent event) {
    	try {
    		SistemaCtrl.getInstance().liberarRelatorioLimpeza(TipoMovimentacao.acesso);
    		RelatorioLimpezaController relatorioLimpeza = new RelatorioLimpezaController();
			pnMain.setCenter(relatorioLimpeza.getNode());
			focarToolBar(false);
			relatorioLimpeza.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro("Erro ao abrir relatório de saida de pacientes");
			e.printStackTrace();
		}
    }

    @FXML
    void controlarSaida(ActionEvent event) {
    	try {
			SistemaCtrl.getInstance().liberarControleSaida(TipoMovimentacao.acesso);
			manutencao = new SaidaController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
    }

	@FXML
	private void manterEspacos(ActionEvent event) {
		try {
			SistemaCtrl.getInstance().liberarManutencaoEspaco(TipoMovimentacao.acesso);
			manutencao = new EspacoController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}

    @FXML
    private void manterMedicos(ActionEvent event) {
    	try {
			SistemaCtrl.getInstance().liberarManutencaoMedico(TipoMovimentacao.acesso);
			manutencao = new MedicoController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
    }

    @FXML
    private void manterPacientes(ActionEvent event) {
    	try {
			SistemaCtrl.getInstance().liberarManutencaoPaciente(TipoMovimentacao.acesso);
			manutencao = new PacienteController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
    }

	@FXML
	private void manterFuncionario(ActionEvent event) {
		try {
			SistemaCtrl.getInstance().liberarManutencaoFuncionario(TipoMovimentacao.acesso);
			manutencao = new FuncionarioController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@FXML
	private void manterEspecialidade(ActionEvent event){
		try {
			SistemaCtrl.getInstance().liberarManutencaoEspecialidade(TipoMovimentacao.acesso);
			manutencao = new EspecialidadeController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
	}
	
    @FXML
    private void manterCidades(ActionEvent event) {
		try {
			SistemaCtrl.getInstance().liberarManutencaoCidade(TipoMovimentacao.acesso);
			manutencao = new CidadeController();
			pnMain.setCenter(manutencao.getNode());
			focarToolBar(false);
			manutencao.getNode().requestFocus();
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
			e.printStackTrace();
		}
    }
    
    
    @FXML
    private void opcoes(ActionEvent event) {
    	try {
			new OpcoesController().start(SistemaCtrl.getInstance().getStage());
		} catch (Exception e) {
			Utilitarios.erro(e.getMessage());
		}
    }
    
    
    
    @FXML
    private void consultaOnline(ActionEvent event) {
    	new ConsultaController().abrir();
    }
}
