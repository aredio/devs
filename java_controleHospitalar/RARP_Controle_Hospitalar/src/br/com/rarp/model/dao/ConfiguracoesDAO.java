package br.com.rarp.model.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import br.com.rarp.annotations.IgnorarField;
import br.com.rarp.control.SistemaCtrl;
import br.com.rarp.model.Configuracoes;

public class ConfiguracoesDAO {

	public static void criarTabela() throws Exception {

		Statement st = SistemaCtrl.getInstance().getConexao().getConexao().createStatement();
		String sql;
		sql = "CREATE TABLE IF NOT EXISTS ";
		sql += "configuracoes(";
		sql += "chave character varying, ";
		sql += "valor character varying, ";
		sql += " arquivo bit(1)[], ";
		sql += "CONSTRAINT unique_chave UNIQUE (chave)";
		sql += ");";

		try {
			st.executeUpdate(sql);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Falha ao criar tabela de configura�oes");

		}

		String sql2 = "SELECT proname  FROM pg_proc where   proname = 'AtualizaValor'";

		PreparedStatement ps = SistemaCtrl.getInstance().getConexao().getConexao().prepareStatement(sql2);

		ResultSet rs = ps.executeQuery();

		if (!rs.next()) {

			sql = "";

			sql += "CREATE FUNCTION public.\"AtualizaValor\"() ";
			sql += "RETURNS trigger ";
			sql += "LANGUAGE 'plpgsql' ";
			sql += "COST 100.0 ";
			sql += "VOLATILE NOT LEAKPROOF ";
			sql += "AS $BODY$ ";
			sql += "begin ";
			sql += "if exists (select chave from configuracoes ";
			sql += "where chave = NEW.chave) then ";
			sql += "update configuracoes set valor = NEW.valor where chave=new.chave; ";
			sql += "return null; ";
			sql += "else ";
			sql += "return NEW; ";
			sql += "end if; ";
			sql += "return true; ";
			sql += "end; ";
			sql += "$BODY$; ";

			try {
				st.executeUpdate(sql);
			} catch (Exception e) {
				// TODO: handle exception
				throw new Exception("Falha ao criar function trigger de configura�oes");
			}
			sql = "";

			sql += "ALTER FUNCTION public.\"AtualizaValor\"() ";
			sql += "OWNER TO postgres;";
			try {
				st.executeUpdate(sql);
			} catch (Exception e) {
				// TODO: handle exception
				throw new Exception("Falha ao alterar function trigger de configura�oes");
			}
			sql = "";
			sql += "CREATE TRIGGER \"ValidaInsert\" ";
			sql += "BEFORE INSERT ";
			sql += "ON public.configuracoes ";
			sql += "FOR EACH ROW ";
			sql += "EXECUTE PROCEDURE public.\"AtualizaValor\"(); ";

			try {
				st.executeUpdate(sql);
			} catch (Exception e) {
				// TODO: handle exception
				throw new Exception("Falha ao criar tabela de configura�oes");
			}
		}

	}

	public void getConfiguracoes() throws Exception {

		// SistemaCtrl.getInstance().getConfiguracoes().setControlerAcesso(true);
		
		 PreparedStatement ps;
	        Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
	        Configuracoes configuracoes = Configuracoes.getInstance();
	        try {
	        	String sql = "SELECT * FROM configuracoes " ;
	            ps = conexao.prepareStatement(sql);
	            
	            ResultSet rs = ps.executeQuery();
	        	while(rs.next()){
	            	String chave =  rs.getString("chave");
	            	String valor = rs.getString("valor");
	            	
	            	Field field = configuracoes.getClass().getDeclaredField(chave);
	            	field.setAccessible(true);
	            	
	            	if (field.getType() == boolean.class)
	            		try {
	            			field.set(configuracoes, Boolean.valueOf(valor));
	            		}catch (Exception e) {
							// TODO: handle exception
	            			field.set(configuracoes,false);
						}
	            				
	            	if (field.getType() == String.class)
	            		try {
	            			field.set(configuracoes, valor);
	            		}catch (Exception e) {
							// TODO: handle exception
	            			field.set(configuracoes, "");
						}
	            		
	            	if (field.getType() == double.class)
	            		try {
	            			field.set(configuracoes, Double.parseDouble(valor));
	            		}catch (Exception e) {
							// TODO: handle exception
	            			field.set(configuracoes, 0);
						}
	            		
	            	if (field.getType() == int.class)
	            		try {
	            			field.set(configuracoes, Integer.parseInt(valor));
	            		}catch (Exception e) {
							// TODO: handle exception
	            			field.set(configuracoes,0);
						}
	            	
	            	
	            	if (field.getType() == long.class)
	            		try {
	            			field.set(configuracoes, Long.parseLong(valor));
	            		}catch (Exception e) {
							// TODO: handle exception
	            			field.set(configuracoes,0);
						}	
	   
	            }
	            ps.close();
	        } catch(Exception e) {
	        	e.printStackTrace();
				throw new Exception("Erro a obter Configuracoes");
			} finally{
	            //conexao.close();
	        }
		

	}

	public void salvar() throws Exception {
		PreparedStatement ps;
		Connection conexao = SistemaCtrl.getInstance().getConexao().getConexao();
		try {
			/**
			 * INSERT INTO public.medico_especialidade( codigo_medico, codigo_especialidade)
			 * select 1,1 where not exists (select codigo_medico, codigo_especialidade from
			 * public.medico_especialidade where codigo_medico = 1 and codigo_especialidade
			 * =1 )
			 */

			String sql;
			sql = "INSERT INTO configuracoes("; 
			sql += "chave,valor) ";
			sql += " VALUES ";
			Configuracoes configuracoes = Configuracoes.getInstance();
			int i = 0;
			for (Field field : configuracoes.getClass().getDeclaredFields()) {
				
				// @formatter:off
				 
				// @formatter:on
	

				if (field.getDeclaredAnnotation(IgnorarField.class) == null) {
					field.setAccessible(true);
					if (i != 0)
						sql += ", ";

					sql += "('"+ field.getName() +"', '"+field.get(configuracoes)+"')";
					i++;
				}
			}

			ps = conexao.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao salvar Configuracoes");
		} finally {

			//conexao.close();
		}

	}

}
