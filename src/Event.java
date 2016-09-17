/**
 * Created by Jake on 9/16/16.
 */

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


public class Event extends HttpServlet
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
            InitialContext ctx = new InitialContext( );
            m_dataSource = ( DataSource ) ctx.lookup( "java:comp/env/jdbc/MySQLDB" );
        }
        //if we cannot open the database
        catch ( NamingException ex )
        {
            log( "error opening data source: " + ex );
            ex.printStackTrace( );
            throw new ServletException( "cannot initialize servlet" + ex.getCause( ) );
        }

    }
    public void doGet( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        try
        {

            Class.forName("com.mysql.jdbc.Driver");

            Connection con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/HackCMU2016", "root",  "321blast" );

            resp.setContentType( "application/vnd.api+json" );
            PrintWriter out = resp.getWriter();

            int uid = Integer.parseInt( req.getParameter( "uid" ) );

            PreparedStatement p = con.prepareStatement( "SELECT event_id from attendance where user_id = ? union SELECT event_id from events where user_id = ?" );
            p.setInt( 1,  uid);
            p.setInt( 2,  uid);

            ResultSet rs = p.executeQuery();
            while(rs.next())
            {
                int eid = rs.getInt( 1 );
                PreparedStatement e = con.prepareStatement( "select event_id, eventname, location, goTime, price, description, user_id from events where event_id = ? " );

                e.setInt( 1, eid);

                ResultSet event = p.executeQuery( );

                PreparedStatement people = con.prepareStatement( "select username from users T1 inner join events T2 on T1.user_id=T2.user_id and event_id = ?" );

                people.setInt( 1, eid );

                ResultSet rsvp = people.executeQuery();

                if ( event.next( ) && rsvp.next() )
                {
                }
                //call other helper
            }
            out.print( "Get got sonny" );
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

    public void doPost( HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");

            Connection con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/HackCMU2016", "root",  "321blast" );


            addEvent(con, req.getParameter( "eventname" ), req.getParameter( "location" ), req.getParameter( "goTime" ), req.getParameter( "price" ), req.getParameter( "description" ), req.getParameter( "uid") );
            //TODO Add redirect

        }
        catch ( SQLException e )
        {
            log("sql exception: " + e);
            throw new ServletException(e);
        }
        catch ( ClassNotFoundException e )
        {
            log( "Class Not Found Exception: " );
            throw new ServletException(e);
        }
    }

    private void addEvent(Connection con, String name, String where, String when, String price, String description, String uid)
        throws ServletException
    {
        try
        {
            PreparedStatement p = con.prepareStatement( "insert into events(eventname, location, goTime, price, description, user_id ) values(?, ?,?,?,?,?)" );

            p.setString( 1, name );
            p.setString( 2,  where);
            p.setString( 3, when );
            p.setString( 4, price );
            p.setString( 5, description );
            p.setInt( 6,  Integer.parseInt( uid ));

            p.execute();

        }
        catch ( SQLException e )
        {
            log("sql exception: " + e);
            throw new ServletException(e);
        }
    }


}
