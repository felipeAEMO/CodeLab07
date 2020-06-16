import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/status")
public class Status extends HttpServlet {
	private static final long serialVersionUID = 1L;

	InitialContext ic;


	protected void service(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException
	    {
	        PrintStream out = new PrintStream(response.getOutputStream());
	        try
	        {
	            DataSource ds = (DataSource)ic.lookup("jdbc/tarefasDS");
	            Connection conn = ds.getConnection();
	            Statement stmt = conn.createStatement();
	            for(ResultSet rs = stmt.executeQuery("SELECT 'DATABASE - OK' FROM INFORMATION_SCHEMA.SYSTEM_USERS"); rs.next(); out.println(rs.getString(1)));
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	    }

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			ic = new InitialContext();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}