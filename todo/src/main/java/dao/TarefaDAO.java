package br.ucsal.app.todo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import br.ucsal.app.todo.model.Tarefa;

public class TarefaDAO {
	
	private Context ctx = null;
	private DataSource ds = null;
	private Connection con = null;
	
	private static final String SELECT_ALL = "SELECT tarefa_id, titulo, descricao, concluida FROM tarefas";
	private static final String UPDATE = "UPDATE tarefas SET titulo=?, descricao=?, concluida=? where tarefa_id=?";
	private static final String INSERT = "INSERT INTO tarefas ( titulo, descricao, concluida ) VALUES ( ?,?,? )";
	private static final String DELETE = "DELETE FROM tarefas where tarefa_id=?";
	private static final String SELECT_BY_ID = "SELECT tarefa_id, titulo,descricao, concluida FROM tarefas WHERE tarefa_id=?";

			
	public void open() throws Exception {
	 	ctx = new InitialContext();
	 	ds = (DataSource) ctx.lookup("jdbc/tarefasDS");
		con = ds.getConnection();
	}
	
	private void close(Statement stmt) {
		close(stmt,null);
	}
	
	private void close(Statement stmt, ResultSet rs ) {
		try {
			if(rs!=null) rs.close();
			if(stmt!=null)stmt.close();
			if(con!=null)con.close();
			if(ctx!=null)ctx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public List<Tarefa> listAll() {
		List<Tarefa> tarefas = new ArrayList<Tarefa>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			this.open();
			stmt = con.createStatement();
			rs = stmt.executeQuery(SELECT_ALL);
			while (rs.next()) {
				Tarefa tarefa = new Tarefa();
				tarefa.setId(rs.getLong("tarefa_id"));
				tarefa.setTitulo(rs.getString("titulo"));
				tarefa.setDescricao(rs.getString("descricao"));
				tarefa.setConcluida(rs.getBoolean("concluida"));
				tarefas.add(tarefa);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(stmt, rs);
		}

		return tarefas;
	}

	public void update(Tarefa tarefa) {

		PreparedStatement stmt = null;
		try {
			this.open();
			stmt = con.prepareStatement(UPDATE);
			stmt.setString(1, tarefa.getTitulo());
			stmt.setString(2, tarefa.getDescricao());
			stmt.setBoolean(3, tarefa.getConcluida());
			stmt.setLong(4, tarefa.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(stmt);
		}
	}

	public void save(Tarefa tarefa) {
		PreparedStatement stmt = null;
		try {
			open();
			stmt = con.prepareStatement(INSERT);
			stmt.setString(1, tarefa.getTitulo());
			stmt.setString(2, tarefa.getDescricao());
			stmt.setBoolean(3, tarefa.getConcluida());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(stmt);
		}
	}

	public void delete(Long id) {
		Tarefa tarefa = new Tarefa();
		tarefa.setId(id);
		this.delete(tarefa);
	}

	public void delete(Tarefa tarefa) {
		PreparedStatement stmt = null;

		try {
			open();
			stmt = con.prepareStatement(DELETE);
			stmt.setLong(1, tarefa.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(stmt);
		}
	}

	public Tarefa findById(Long id) {
		Tarefa tarefa = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			open();
			stmt = con.prepareStatement(SELECT_BY_ID);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				tarefa = new Tarefa();
				tarefa.setId(rs.getLong("tarefa_id"));
				tarefa.setTitulo(rs.getString("titulo"));
				tarefa.setDescricao(rs.getString("descricao"));
				tarefa.setConcluida(rs.getBoolean("concluida"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(stmt, rs);
		}
		return tarefa;
	}

	public void saveOrUpdate(Tarefa tarefa) {
		if(tarefa.getId()==null) {
			save(tarefa);
		}else {
			update(tarefa);
		}
	}

}
