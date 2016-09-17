import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.naming.NamingException;



/**
 * Created by Jake on 9/16/16.
 */
@WebServlet(name = "DBTest")
public class DBTest extends HttpServlet {

    private DataSource m_dataSource;

    /**
     * Sets up a connection with the server
     * @throws ServletException
     */
    public void init()
            throws ServletException {
        try
        {
            InitialContext ctx = new InitialContext();
            m_dataSource = (DataSource)ctx.lookup("java:comp/env/jdbc/MySQLDB");
        }
        //if we cannot open the database
        catch (NamingException ex)
        {
            log("error opening data source: " + ex);
            throw new ServletException("cannot initialize servlet");
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
