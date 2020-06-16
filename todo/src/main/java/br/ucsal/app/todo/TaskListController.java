package br.ucsal.app.todo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import br.ucsal.app.todo.model.Tarefa;

/**
 * Servlet implementation class TaskListController
 */
@WebServlet("/")
public class TaskListController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		
		Context ctx  = null;
		Connection con  = null;
		Statement stmt = null;
		ResultSet	rs = null;
		
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("jdbc/tarefasDS");
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT tarefa_id, titulo,descricao, concluida FROM tarefas");
			
			List<Tarefa> tarefas =  new ArrayList<Tarefa>();
			
			while (rs.next()) {
				Tarefa tarefa = new Tarefa();
				tarefa.setId(rs.getLong("tarefa_id"));
				tarefa.setTitulo(rs.getString("titulo"));
				tarefa.setDescricao(rs.getString("descricao"));
				tarefa.setConcluida(rs.getBoolean("concluida"));
				tarefas.add(tarefa);
			}
			
			request.getSession().getServletContext().setAttribute("tarefas",tarefas);
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				stmt.close();
				con.close();
				ctx.close();
			} catch (SQLException e) {
				System.out.println("Exception in closing DB resources");
			} catch (NamingException e) {
				System.out.println("Exception in closing Context");
			}
			
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}