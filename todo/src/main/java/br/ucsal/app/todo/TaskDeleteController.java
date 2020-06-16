package br.ucsal.app.todo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import br.ucsal.app.todo.model.Tarefa;

/**
 * Servlet implementation class TaskDeleteController
 */
@WebServlet("/excluir")
public class TaskDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Tarefa> tarefas = (List<Tarefa>) request.getSession().getServletContext().getAttribute("tarefas");
		String index = request.getParameter("index");
		if( index != null ) {
			try {
				long i = Long.parseLong(index);
				Context ctx = null;
				Connection con = null;
				PreparedStatement stmt = null;
				try {
					ctx = new InitialContext();
					DataSource ds = (DataSource) ctx.lookup("jdbc/tarefaDS");
					con = ds.getConnection();
					stmt = con.prepareStatement("DELETE FROM  tarefas where tarefa_id=?");
					stmt.setLong(1,i);
					stmt.execute();
				}catch (NamingException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}finaly{
					try {
						stmt.close();
						con.close();
						ctx.close();
					} catch (SQLException e) {
						System.out.println("Exception in closing DB resources");
					} catch (NamingException e) {
						System.out.println("Exception in closing Context");
					}
				}
				
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		response.sendRedirect("/");


	}

}