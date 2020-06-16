package br.ucsal.app.todo.controller;

import br.ucsal.app.todo.model.Usuario;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "loginfilter", urlPatterns = {"/usuarios", "/tarefas", "/userprofile", "/editar", "/salvar"})
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpSession session = ((HttpServletRequest) req).getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if(usuario == null) {
            session.setAttribute("erro", "Você não está registrado");
            ((HttpServletResponse) resp).sendRedirect("/usuarios.jsp");
        } else {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
