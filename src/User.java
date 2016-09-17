import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import HelperFunctions.*;

/**
 * Created by Jake on 9/16/16.
 */
public class User extends HttpServlet
{
    private DataSource m_dataSource;

    /**
     * Sets up a connection with the server
     * @throws javax.servlet.ServletException
     */
    public void init()
            throws ServletException
    {
        try
        {
            InitialContext ctx = new InitialContext();
            m_dataSource = (DataSource)ctx.lookup("java:comp/env/jdbc/MySQLDB");

        }
        //if we cannot open the database
        catch (NamingException ex)
        {
            log("error opening data source: " + ex);
            ex.printStackTrace();
            throw new ServletException( "cannot initialize servlet" + ex.getCause());
        }
    }

    public void doGet( HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException
    {
        try
        {
            //Connection con = DBUtil.openConnection( m_dataSource );

            Class.forName("com.mysql.jdbc.Driver");

            Connection con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/HackCMU2016", "root",  "321blast" );

            resp.setContentType( "application/vnd.api+json" );
            PrintWriter out = resp.getWriter();
            PreparedStatement p = con.prepareStatement( "select user_id, username, email, phoneNum from users where user_id = ?" );

            //int uid = Integer.parseInt( req.getParameter( "uid" ) );
            int uid = 1;
            p.setInt( 1, uid);
            ResultSet rs = p.executeQuery();
            if(rs.next())
            {
                HelperFunctions.userToJson(rs, out);
            }
            //call gabriel's helper function
            rs.close();

            p = con.prepareStatement( "SELECT event_id from attendance where user_id = ? union SELECT event_id from events where user_id = ?" );
            p.setInt( 1,  uid);
            p.setInt( 2,  uid);


            rs = p.executeQuery();
            while(!rs.isClosed())
            {
                rs.next();
                int eid = rs.getInt( 1 );
                PreparedStatement e = con.prepareStatement( "select event_id, eventname, location, goTime, price, description, user_id from events where event_id = ? " );

                e.setInt( 1, eid );

                ResultSet event = p.executeQuery( );

                PreparedStatement people = con.prepareStatement( "select username from users T1 inner join events T2 on T1.user_id=T2.user_id and event_id = ?" );

                people.setInt( 1, eid );

                ResultSet rsvp = people.executeQuery();

                if ( event.next( ) && rsvp.next())
                {
                    HelperFunctions.eventToJson(event, rsvp, out);
                }
                //call other helper
            }

            p = con.prepareStatement( "select interest from userInterests where user_id = ?" );

            p.setInt( 1, uid );

            rs = p.executeQuery();
            //call helper here
            rs.close();
            con.close();

        }
        catch ( SQLException e )
        {
            log( "SQL Exception: " );
            throw new ServletException(e);
        }
        catch ( ClassNotFoundException e )
        {
            log( "Class Not Found Exception: " );
            throw new ServletException(e);
        }

    }
}
