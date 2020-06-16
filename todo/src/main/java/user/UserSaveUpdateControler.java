package br.ucsal.app.todo.controller;

import br.ucsal.app.todo.dao.UsuarioDAO;
import br.ucsal.app.todo.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = { "/salvaruser", "/editaruser"})
public class UserSaveUpdateControler extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UsuarioDAO udao = new UsuarioDAO();



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String index = request.getParameter("index");

        if( index != null ) {
            long id = Long.parseLong(index);
            Usuario usuario = udao.findById(id);
            request.setAttribute("usuario",usuario);
        }

        request.setAttribute("usuarios", udao.listAll());
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);


    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String ativo = request.getParameter("ativo");

        Usuario usuario = new Usuario();
        if(id!=null && !id.trim().isEmpty()) {
            long i = Long.parseLong(id);
            usuario.setId(i);
        }
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setAtivo(ativo!=null?true:false);


        udao.saveOrUpdate(usuario);

        response.sendRedirect("/usuarios");
    }
}
