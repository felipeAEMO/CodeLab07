package br.ucsal.app.todo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ucsal.app.todo.model.Tarefa;


@WebServlet("/salvar")
public class TaskSaveController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String titulo = request.getParameter("titulo");
		String descricao = request.getParameter("descricao");
		String concluida = request.getParameter("concluida");
		
		Tarefa tarefa = new Tarefa();
		tarefa.setTitulo(titulo);
		tarefa.setDescricao(descricao);
		tarefa.setConcluida( concluida!=null?true:false );
		
		Context ctx = null;
		Connection con = null;
		PreparedStatement start = null;
		ResultSet ts = null;
		
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("jdbc/tarefaDS");
			con = ds.getConnection();
			start = con.prepareStatement("INSERT INTO tarefas(titulo, descricao,concluida)VALUES(?,?,?);")
			start.setString(1, tarefa.getTitulo());
			start.setString(2, tarefa.getDescricao());
			start.setBoolean(3, tarefa.getConcluida());
			start.execute();
		} catch(NaningException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				start.close();
				con.close();
				ctx.close();
			} catch(SQLException e) {
				System.out.println("Exception in closing DB resources");
			} catch (NaningException e) {
				System.out.println("Exception in closing Context");
			}
		}
		response.sendRedirect("/")
}