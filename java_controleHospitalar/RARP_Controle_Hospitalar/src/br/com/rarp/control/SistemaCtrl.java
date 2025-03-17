package br.com.rarp.control;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.com.rarp.enums.TipoMovimentacao;
import br.com.rarp.model.Configuracoes;
import br.com.rarp.model.Espaco;
import br.com.rarp.model.Leito;
import br.com.rarp.model.Organizacao;
import br.com.rarp.model.Tela;
import br.com.rarp.model.Usuario;
import br.com.rarp.model.dao.AtendimentoDAO;
import br.com.rarp.model.dao.CargoDAO;
import br.com.rarp.model.dao.CidadeDAO;
import br.com.rarp.model.dao.Conexao;
import br.com.rarp.model.dao.ConfiguracoesDAO;
import br.com.rarp.model.dao.ConvenioDAO;
import br.com.rarp.model.dao.EncaminhamentoDAO;
import br.com.rarp.model.dao.EntradaPacienteDAO;
import br.com.rarp.model.dao.OrganizacaoDAO;
import br.com.rarp.model.dao.EspacoDAO;
import br.com.rarp.model.dao.EspecialidadeDAO;
import br.com.rarp.model.dao.EstadoDAO;
import br.com.rarp.model.dao.FuncionarioDAO;
import br.com.rarp.model.dao.LeitoDAO;
import br.com.rarp.model.dao.LimpezaDAO;
import br.com.rarp.model.dao.MedicoDAO;
import br.com.rarp.model.dao.MovimentacaoDAO;
import br.com.rarp.model.dao.PacienteDAO;
import br.com.rarp.model.dao.PerfilUsuarioDAO;
import br.com.rarp.model.dao.PessoaDAO;
import br.com.rarp.model.dao.PessoaFisicaDAO;
import br.com.rarp.model.dao.PessoaJuridicaDAO;
import br.com.rarp.model.dao.Propriedades;
import br.com.rarp.model.dao.ReceitaMedicaDAO;
import br.com.rarp.model.dao.SaidaPacienteDAO;
import br.com.rarp.model.dao.SintomaDAO;
import br.com.rarp.model.dao.TelaDAO;
import br.com.rarp.model.dao.TelefoneDAO;
import br.com.rarp.model.dao.UsuarioDAO;
import br.com.rarp.utils.MyAppointmentGroup;
import br.com.rarp.view.scnConexao.ConexaoController;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SistemaCtrl {
	private static final SistemaCtrl INSTANCE = new SistemaCtrl();
	
	private Usuario usuarioSessao;
	
	private Conexao conexao;
	
	private SistemaCtrl() {
		getPropriedades();
	}
	
	public Configuracoes getConfiguracoes() {
		return Configuracoes.getInstance();
	}
	
	public Organizacao getOrganizacao() {
		return Organizacao.getINSTANCE();
	}
	
	public void salvarConfiguracoes() throws Exception {
		new OrganizacaoDAO().salvar();
		new ConfiguracoesDAO().salvar();
	}
	
	
	public boolean podeLiberar(String tela, TipoMovimentacao tipo) {
		if (usuarioSessao != null && usuarioSessao.getPerfilUsuario() != null && usuarioSessao.getPerfilUsuario().getTelas().size() > 0) {
			switch (tipo) {
			case acesso:
				boolean permitido = false;
				for(Tela t: usuarioSessao.getPerfilUsuario().getTelas())
					if(t.getNome().equals(tela))
						permitido = t.isStatus();
				return permitido;

			case insercao:
				boolean podeInserir = false;
				for(Tela t: usuarioSessao.getPerfilUsuario().getTelas())
					if(t.getNome().equals(tela))
						podeInserir = t.isPodeInserir();
				return podeInserir;

			case alteracao:
				boolean podeAlterar = false;
				for(Tela t: usuarioSessao.getPerfilUsuario().getTelas())
					if(t.getNome().equals(tela))
						podeAlterar = t.isPodeAlterar();
				return podeAlterar;

			case visualizaco:
				boolean podeVisualizar = false;
				for(Tela t: usuarioSessao.getPerfilUsuario().getTelas())
					if(t.getNome().equals(tela))
						podeVisualizar = t.isPodeVisualizar();
				return podeVisualizar;

			case desativacao:
				boolean podeDesativar = false;
				for(Tela t: usuarioSessao.getPerfilUsuario().getTelas())
					if(t.getNome().equals(tela))
						podeDesativar = t.isPodeDesativar();
				return podeDesativar;
			}
		}
		return !getConfiguracoes().isControleAcesso();
	}
	
	public static SistemaCtrl getInstance() {
		return INSTANCE;
	}
	
	public List<Tela> getTelas() {
		List<Tela> telas = new ArrayList<>();
		telas.add(new Tela(1, "manutencaoUsuario", "Manutenção de Usuário"));
		telas.add(new Tela(2, "manutencaoPerfilUsuario", "Manutenção de Perfil de Usuário"));
		telas.add(new Tela(3, "manutencaoEspaco", "Manutenção de Espaço"));
		telas.add(new Tela(4, "manutencaoFuncionario", "Manutenção de Funcionario"));
		telas.add(new Tela(5, "manutencaoCargo", "Manutenção de Cargo"));
		telas.add(new Tela(6, "manutencaoCidade", "Manutenção de Cidade"));
		telas.add(new Tela(7, "manutencaoPaciente", "Manutenção de Paciente"));
		telas.add(new Tela(8, "manutencaoConvenio", "Manutenção de Convênio"));
		telas.add(new Tela(9, "manutencaoEspecialidade", "Manutenção de Especialidade"));
		telas.add(new Tela(10, "manutencaoMedico", "Manutenção de Especialidade"));
		
		telas.add(new Tela(11, "controleEntradaPaciente", "Controle de Entrada de Paciente"));
		telas.add(new Tela(12, "controleEncaminhamento", "Controle de Encaminhamentos"));
		telas.add(new Tela(13, "controleAtendimento", "Controle de Atendimentos"));
		telas.add(new Tela(14, "controleSaida", "Controle de Saida de Paciente"));
		telas.add(new Tela(15, "controleLimpeza", "Controle de Limpeza"));
		telas.add(new Tela(16, "controleAcesso", "Controle de Acesso"));
		telas.add(new Tela(17, "controleConsulta", "Consulta Online"));
		
		telas.add(new Tela(18, "relatorioEntradaPaciente", "Relatório de Entrada de Paciente"));
		telas.add(new Tela(19, "relatorioEncaminhamento", "Relatório de Encaminhamentos"));
		telas.add(new Tela(20, "relatorioAtendimento", "Relatório de Atendimentos"));
		telas.add(new Tela(21, "relatorioSaida", "Relatório de Saida de Paciente"));
		telas.add(new Tela(22, "relatorioLimpeza", "Relatório de Limpeza"));
		telas.add(new Tela(23, "relatorioConsulta", "Relatório de Consultas Online"));
		return telas;
	}
	
	public void liberarControleAcesso() throws Exception {
		if(!podeLiberar("controleAcesso", TipoMovimentacao.acesso))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarManutencaoUsuario(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("manutencaoUsuario", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarManutencaoPerfilUsuario(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("manutencaoPerfilUsuario", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarManutencaoEspaco(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("manutencaoEspaco", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarManutencaoCargo(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("manutencaoCargo", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarManutencaoFuncionario(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("manutencaoFuncionario", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarManutencaoCidade(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("manutencaoCidade", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarManutencaoPaciente(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("manutencaoPaciente", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarManutencaoConvenio(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("manutencaoConvenio", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarManutencaoEspecialidade(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("manutencaoEspecialidade", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarManutencaoMedico(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("manutencaoMedico", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarControleEncaminhamento(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("controleEncaminhamento", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarControleEntradaPaciente(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("controleEntradaPaciente", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarControleAtendimento(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("controleAtendimento", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarControleSaida(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("controleSaida", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarControleLimpeza(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("controleLimpeza", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public void liberarRelatorioLimpeza(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("relatorioLimpeza", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}

	public void liberarRelatorioSaida(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("relatorioSaida", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}

	public void liberarRelatorioEntrada(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("relatorioEntrada", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}

	public void liberarRelatorioEncaminhamento(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("relatorioEncaminhamento", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}

	public void liberarRelatorioAtendimento(TipoMovimentacao tipoMovimentacao) throws Exception {
		if(!podeLiberar("relatorioAtendimento", tipoMovimentacao))
			throw new Exception("Acesso negado a essa área");
	}
	
	public Propriedades getPropriedades() {
		return Propriedades.getInstance();
	}
	
	public Conexao getConexao() throws Exception {
		if(conexao == null)
			conexao = new Conexao();
		conexao.getConexao();
		return conexao;
	}
	
	public void configuraConexao() throws Exception {
		try {
			SistemaCtrl.getInstance().getConexao().getConexao();
		} catch (Exception e) {
			
			try {
				ConexaoController conexaoController = new ConexaoController();
				conexaoController.configurar();
				
				SistemaCtrl.getInstance().getConexao().getConexao();
			}catch(Exception e2) {
				System.out.println(e.getMessage());
				new Conexao().criarDataBase();
			}
		}
	}

	public Stage getStage() {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.APPLICATION_MODAL);
		return stage;
	}
	
	public boolean tabelaExiste(String nome) throws ClassNotFoundException, SQLException, Exception {
		DatabaseMetaData dbm = SistemaCtrl.getInstance().getConexao().getConexao().getMetaData();
		ResultSet rs = dbm.getTables(null, null, nome, null);
		return rs.next();		
	}
	
	public List<MyAppointmentGroup> getApontamentos() {
		List<MyAppointmentGroup> apontamentos = new ArrayList<>();
		apontamentos.add(new MyAppointmentGroup("group0", "#AC725E"));
		apontamentos.add(new MyAppointmentGroup("group1", "#D06B64"));
		apontamentos.add(new MyAppointmentGroup("group2", "#F83A22"));
		apontamentos.add(new MyAppointmentGroup("group3", "#FA573C"));
		apontamentos.add(new MyAppointmentGroup("group4", "#FF7537"));
		apontamentos.add(new MyAppointmentGroup("group5", "#FFAD46"));
		apontamentos.add(new MyAppointmentGroup("group6", "#42D692"));
		apontamentos.add(new MyAppointmentGroup("group7", "#16A765"));
		apontamentos.add(new MyAppointmentGroup("group8", "#7BD148"));
		apontamentos.add(new MyAppointmentGroup("group9", "#B3DC6C"));
		apontamentos.add(new MyAppointmentGroup("group10", "#FBE983"));
		apontamentos.add(new MyAppointmentGroup("group11", "#FAD165"));
		apontamentos.add(new MyAppointmentGroup("group12", "#92E1C0"));
		apontamentos.add(new MyAppointmentGroup("group13", "#9FE1E7"));
		apontamentos.add(new MyAppointmentGroup("group14", "#9FC6E7"));
		apontamentos.add(new MyAppointmentGroup("group15", "#4986E7"));
		apontamentos.add(new MyAppointmentGroup("group16", "#9A9CFF"));
		apontamentos.add(new MyAppointmentGroup("group17", "#B99AFF"));
		apontamentos.add(new MyAppointmentGroup("group18", "#C2C2C2"));
		apontamentos.add(new MyAppointmentGroup("group19", "#CABDBF"));
		apontamentos.add(new MyAppointmentGroup("group20", "#CCA6AC"));
		apontamentos.add(new MyAppointmentGroup("group21", "#F691B2"));
		apontamentos.add(new MyAppointmentGroup("group22", "#CD74E6"));
		apontamentos.add(new MyAppointmentGroup("group23", "#A47AE2"));
		return apontamentos;
	}
	
	public void criarTabelas() throws ClassNotFoundException, SQLException, Exception {
		PerfilUsuarioDAO.criarTabela();
		EstadoDAO.criarTabela();
		CidadeDAO.criarTabela();
		PessoaDAO.criarTabela();
		TelefoneDAO.criarTabela();
		PessoaFisicaDAO.criarTabela();
		PessoaJuridicaDAO.criarTabela();
		ConvenioDAO.criarTabela();
		CargoDAO.criarTabela();
		FuncionarioDAO.criarTabela();
		TelaDAO.criarTabela();
		UsuarioDAO.criarTabela();
		EspacoDAO.criarTabela();
		PacienteDAO.criarTabela();
		LeitoDAO.criarTabela();
		EspecialidadeDAO.criarTabela();
		MedicoDAO.criarTabela();
		ConfiguracoesDAO.criarTabela();
		OrganizacaoDAO.criarTabela();
		MovimentacaoDAO.criarTabela();
		EntradaPacienteDAO.criarTabela();
		ReceitaMedicaDAO.criarTabela();
		AtendimentoDAO.criarTabela();
		SintomaDAO.criarTabela();
		EncaminhamentoDAO.criarTabela();
		SaidaPacienteDAO.criarTabela();
		LimpezaDAO.criarTabela();
		//SQLDAO sqldao = new SQLDAO();
		//sqldao.executarSQLFile("cidades_estados.sql");
	}

	public Usuario getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(Usuario usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	public void getConfiguracoesDB() throws Exception {
		// TODO Auto-generated method stub 
		new ConfiguracoesDAO().getConfiguracoes();
	}
	
	public void getOrganizacaoDB() throws Exception {
		new OrganizacaoDAO().getOrganizacao();
	}

	public Espaco getRecepcao() {
		Espaco espaco = new Espaco();
		espaco.setAndar("Não definido");
		espaco.setBloco("Não definido");
		List<Leito> leitos = new ArrayList<>();
		leitos.add(new Leito(1));
		espaco.setLeitos(leitos);
		espaco.setNome("Recepção");
		espaco.setStatus(true);
		return espaco;
	}

	public void criarRegistrosPadroes() throws Exception {
		TelaDAO.criarRegistrosPadroes();
		PerfilUsuarioDAO.criarRegistrosPadroes();
		EstadoDAO.criarRegistrosPadroes();
		CidadeDAO.criarRegistrosPadroes();
	}

}
