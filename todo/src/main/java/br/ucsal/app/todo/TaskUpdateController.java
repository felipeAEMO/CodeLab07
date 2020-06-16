package br.ucsal.app.todo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TaskUpdateController
 */
public class TaskUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TaskUpdateController() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String index = request.getParameter("index");
		Context ctx = null;
		Connection con = null;
		PreparedStatement start = null;
		ResultSet ts = null;
		if(index !=null) {
			try {
				long i = long.parseLong(index);
				
				ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup("jdbc/tarefasDS");
				con = ds.getConnection();
				stmt = con.prepareStatement("SELECT tarefa_id, titulo,descricao,concluida FROM tarefas WHERE tarefa_id=?");
				start.setLong(1, i); 
				
				rs = stmt.executeQuery();
				
				if(rs.next()) {
					Tarefa tarefa = new Tarefa();
					tarefa.setId(rs.getLong("tarefa_id"));
					tarefa.setTitulo(rs.getString("titulo"));
					tarefa.setDescricao(rs.getString("descricao"));
					tarefa.setConcluida(rs.getBoolean("concluida"));
					request.setAttribute("tarefa", tarefa);
				}
			} catch(NumberFormatException nfe) {
				nfe.printStackTrace();
			} catch(NamingException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					rs.close();
					stmt.close();
					con.close();
					ctx.close();
				} catch (SQLExcepion e ) {
					System.out.println("Exception in closing dB resources");
				} catch (NamingException e) {
					System.out.println("Exception in closing Context");
				}
			}
		}	
		request.getRequestDispatcher("form.jsp").foward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String titulo = request.getParameter("titulo");
		String descricao = request.getParameter("descricao");
		String concluida = request.getParameter("concluida");
		
		Tarefa tarefa new Tarefa();
		long i = long.parseLong(id);
		
		tarefa.setId(i);
		tarefa.setTitulo(titulo);
		tarefa.setDescricao(descricao);
		tarefa.setConcluida(concluida != null?true:false);
		
		Context ctx = null;
		Connection con = null;
		PreparedStatement stnt = null;
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("jdbc/tarefaDS");
			con = ds.getConnection();
			stnt = con.prepareStatement("UPDATE tarefas SET titulo=?, descricao=?, concluida=? where tarefa_id=?");
			stnt.setString(1, tarefa.getTitulo());
			stnt.setString(2, tarefa.getDescricao());
			stnt.setBoolean(3, tarefa.getConcluida());
			stnt.setLong(4, tarefa.getId());
			stnt.executeUpdate();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				stnt.close();
				con.close();
				ctx.close();
			} catch (SQLException e) {
				System.out.println("Exception in closing DB resources");
			} catch (NamingException e ) {
				System.out.println("Exception in closing Context");
			}
		}
		response.sendRedirect("/");
	}

}
