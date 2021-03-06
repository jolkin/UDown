/**
 * Created by Jake on 9/16/16.
 */

import HelperFunctions.HelperFunctions;

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


public class Events extends HttpServlet
{
    private DataSource m_dataSource;

    /**
     * Sets up a connection with the server
     * @throws ServletException
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

            PreparedStatement p = con.prepareStatement( "SELECT event_id from events" );


            ResultSet rs = p.executeQuery();
            while(!rs.isClosed())
            {
                if(rs.next())
                {
                    int eid = rs.getInt( 1 );
                    PreparedStatement e = con.prepareStatement( "SELECT event_id, eventname, location, goTime, price, description, user_id FROM events WHERE event_id = ? " );

                    e.setInt( 1, eid );

                    ResultSet event = e.executeQuery( );

                    PreparedStatement people = con.prepareStatement( "SELECT username FROM users T1 INNER JOIN events T2 ON T1.user_id=T2.user_id AND event_id = ?" );

                    people.setInt( 1, eid );

                    ResultSet rsvp = people.executeQuery( );

                    if ( event.next( ) && rsvp.next( ) )
                    {
                        String name = event.getString( "eventname" );
                        HelperFunctions.eventToJson( event, rsvp, out );
                    }
                    //call other helper
                }
                else
                {
                    rs.close();
                }
            }
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


            int eid = addEvent(con, req.getParameter( "eventname" ), req.getParameter( "location" ), req.getParameter( "goTime" ), req.getParameter( "price" ), req.getParameter( "description" ), req.getParameter( "uid") );

            //add interests to table here
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

    private int addEvent(Connection con, String name, String where, String when, String price, String description, String uid)
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

            p = con.prepareStatement( "select max(event_id) from events" );
            ResultSet rs = p.executeQuery();
            rs.next();
            return rs.getInt( 1 );


        }
        catch ( SQLException e )
        {
            log("sql exception: " + e);
            throw new ServletException(e);
        }
    }


}
