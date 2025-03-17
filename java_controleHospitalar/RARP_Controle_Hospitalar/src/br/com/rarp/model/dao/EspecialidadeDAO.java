package br.com.rarp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Especialidade;
import br.com.rarp.model.Medico;

public class EspecialidadeDAO {
	public static void criarTabela() throws ClassNotFoundException, SQLException, Exception {
	
		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "especialidade(";
		sql += "codigo SERIAL NOT NULL PRIMARY KEY, ";
		sql += "nome varchar(250), ";
		sql += "observacoes VARCHAR, ";
		sql += "status boolean ";
		sql  += ")";
		st.executeUpdate(sql);
	}
	
	public void salvar(Especialidade especialidade) throws Exception {
		if(especialidade.getCodigo() == 0)
			inserir(especialidade);
		else
			alterar(especialidade);
	}

	private void inserir(Especialidade especialidade) throws Exception{
			PreparedStatement ps;
			Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			
			String sql = "INSERT INTO especialidade(nome,observacoes,status) VALUES(?,?,?)";
			ps = conexao.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, especialidade.getNome());
			ps.setString(2, especialidade.getObservacoes());
			ps.setBoolean(3, especialidade.isStatus());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				especialidade.setCodigo(rs.getInt("codigo"));
			
			ps.close();
		}catch(Exception e){
			throw new Exception("Erro a salvar Especialidade");
			
			
		}finally {
		
			//conexao.close();
		}
	}
	
	private void  alterar(Especialidade especialidade) throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			
			String sql = "UPDATE especialidade SET nome=?,  observacoes=?,  status=? WHERE codigo=?";
			ps = conexao.prepareStatement(sql);
			ps.setString(1, especialidade.getNome());
			ps.setString(2, especialidade.getObservacoes());
			ps.setBoolean(3, especialidade.isStatus());
			ps.setInt(4, especialidade.getCodigo());
			ps.executeUpdate();
			ps.close();
		}catch(Exception e){
			throw new Exception("Erro a alterar Especialidade");
		} finally {
			//conexao.close();
		}
	}
	
	public void deletar(Especialidade especialidade) throws Exception {
		PreparedStatement ps;
		Conexao conexao = SistemaCtrl.getInstance().getConexao();
		try {
			
			String sql = "DELETE especialidade WHERE codigo=?";
			ps = conexao.getConexao().prepareStatement(sql);
			ps.setInt(1, especialidade.getCodigo());
			ps.executeUpdate();
			ps.close();
		}catch(Exception e){
			throw new Exception("Erro a deletar Especialidade");
			
			
		} finally {
			//conexao.getConexao().close();
		}
	}

	public List<Especialidade> consultar(String campo, String comparacao, String termo) throws Exception {
		List<Especialidade> especialidades = new ArrayList<>();
        PreparedStatement ps;
        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
        try {
        	String sql = "SELECT * FROM especialidade WHERE " + campo + comparacao + termo;
            ps = conexao.prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
        	while(rs.next()){
            	Especialidade Especialidade = new Especialidade();
            	Especialidade.setCodigo(rs.getInt("codigo"));
            	Especialidade.setNome(rs.getString("nome"));
            	Especialidade.setObservacoes(rs.getString("observacoes"));
            	Especialidade.setStatus(rs.getBoolean("status"));
            	especialidades.add(Especialidade);
            }
            ps.close();
        } catch(Exception e) {
        	e.printStackTrace();
			throw new Exception("Erro a consultar Especialidade");
		} finally{
            //conexao.close();
        }
		return especialidades;
	}
	
	public List<Especialidade> getEspecialidesByMedico(Medico medico) throws Exception {
		PreparedStatement ps;
		List<Especialidade>  especialidades =  new ArrayList<>() ;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
	
		try {
			/**
			 * nome,status,codigo_medico
			 */
			
			String sql = " select * from especialidade where codigo in ("
					+ "  select codigo_especialidade from medico_especialidade where codigo_medico = ? )";
			ps = conexao.prepareStatement(sql);
			ps.setInt(1, medico.getCodigoMedico());
			ResultSet rs = ps.executeQuery();
			while ((rs.next()) ) {
					
				Especialidade Especialidade = new Especialidade();
            	Especialidade.setCodigo(rs.getInt("codigo"));
            	Especialidade.setNome(rs.getString("nome"));
            	Especialidade.setObservacoes(rs.getString("observacoes"));
            	Especialidade.setStatus(rs.getBoolean("status"));
            	especialidades.add(Especialidade);
				
			}
		}catch (Exception e) {
			// TODO: handle exception 
			System.out.println(e.getMessage());
			throw new Exception("falha ao consultar especialidades do medico");
		}
		
		return especialidades;

	}
}
