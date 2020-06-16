package br.ucsal.app.todo.dao;

import br.ucsal.app.todo.model.Tarefa;
import br.ucsal.app.todo.model.Usuario;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Context ctx = null;
    private DataSource ds = null;
    private Connection con = null;

    private static final String SELECT_ALL = "SELECT usuario_id, login, senha, nome, email, ativo from usuarios";
    private static final String UPDATE = "UPDATE usuarios SET login=?, senha=?, nome=?,email=?, ativo=? where usuario_id=?";
    private static final String UPDATE_SENHA = "UPDATE usuarios SET senha=? where usuario_id=?";
    private static final String UPDATE_ATIVO = "UPDATE usuarios SET ativo=? where usuario_id=?";
    private static final String INSERT = "INSERT INTO usuarios ( login, senha, nome, email, ativo ) VALUES ( ?,?,?,?,? )";
    private static final String DELETE = "DELETE FROM usuarios where usuario_id=?";
    private static final String SELECT_BY_ID = "SELECT usuario_id, login, senha, nome, email, ativo from usuarios WHERE usuario_id=?";

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

    public List<Usuario> listAll() {
        List<Usuario> ususarios = new ArrayList<Usuario>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            this.open();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_ALL);
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("usuario_id"));
                usuario.setLogin(rs.getString("login"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setAtivo(rs.getBoolean("ativo"));
                ususarios.add(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(stmt, rs);
        }

        return ususarios;
    }

    public void update(Usuario usuario) {

        PreparedStatement stmt = null;
        try {
            this.open();
            stmt = con.prepareStatement(UPDATE);
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getNome());
            stmt.setString(4, usuario.getEmail());
            stmt.setBoolean(5, usuario.getAtivo());
            stmt.setLong(6, usuario.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(stmt);
        }
    }

    public void update_senha(Usuario usuario) {
        PreparedStatement stmt = null;
        try {
            this.open();
            stmt = con.prepareStatement(UPDATE_SENHA);
            stmt.setString(1,usuario.getSenha());
            stmt.setLong(2, usuario.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(stmt);
        }
    }

    public void update_ativo(Usuario usuario) {
        PreparedStatement stmt = null;
        try {
            this.open();
            stmt = con.prepareStatement(UPDATE_ATIVO);
            stmt.setBoolean(1, usuario.getAtivo());
            stmt.setLong(2, usuario.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(stmt);
        }
    }

    public void save(Usuario usuario) {
        PreparedStatement stmt = null;
        try {
            open();
            stmt = con.prepareStatement(INSERT);
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getNome());
            stmt.setString(4, usuario.getEmail());
            stmt.setBoolean(5, usuario.getAtivo());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(stmt);
        }
    }

    public void delete(Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        this.delete(usuario);
    }

    public void delete(Usuario usuario) {
        PreparedStatement stmt = null;

        try {
            open();
            stmt = con.prepareStatement(DELETE);
            stmt.setLong(1, usuario.getId());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(stmt);
        }
    }

    public Usuario findById(Long id) {
        Usuario usuario = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            open();
            stmt = con.prepareStatement(SELECT_BY_ID);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getLong("usuario_id"));
                usuario.setLogin(rs.getString("login"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));
                usuario.setAtivo(rs.getBoolean("ativo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(stmt, rs);
        }
        return usuario;
    }

    public void saveOrUpdate(Usuario usuario) {
        if(usuario.getId()==null) {
            save(usuario);
        }else {
            update(usuario);
        }
    }
}
