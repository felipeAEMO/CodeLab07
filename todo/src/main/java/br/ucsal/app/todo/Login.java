package br.ucsal.app.todo.controller;

import br.ucsal.app.todo.dao.UsuarioDAO;
import br.ucsal.app.todo.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.hash.Hashing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/login")
public class Login extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsuarioDAO dao = new UsuarioDAO();
        String usuario = request.getParameter("login");
        String password = request.getParameter("password");
        boolean login = false;
        Usuario usuario1 = null;
        List<Usuario> usuarios = dao.listAll();
        
        String passcryptografada = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
       
        System.out.println("SENHA CRYPTOGRAFADA: " + passcryptografada);
        
        for (Usuario user : usuarios) {
            if (user.getLogin().equals(usuario) && user.getSenha().equals(passcryptografada)) {
                usuario1 = user;
                login = true;
                request.getSession().setAttribute("usuario", usuario1);
                break;
            }
        }

        if(login) {
            ((HttpServletResponse) response).sendRedirect("/tarefas");
        } else {
            response.sendRedirect("/index.jsp?erro=Erro de login e password");
        }

    }


}
