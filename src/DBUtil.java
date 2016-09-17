import javax.sql.DataSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;


public final class DBUtil
{
	private static final int s_retries = 2;

	public static Connection openConnection( DataSource ds ) throws SQLException
	{
		Connection c = null;
		Statement s = null;

		int retryCount = s_retries;

		do
		{
			try
			{
				c = ds.getConnection();

				s = c.createStatement();
				s.executeQuery( "select 1" );
				s.close();

				return c;
			}
			catch (SQLException ex)
			{
				String sqlState = ex.getSQLState();

				try
				{
					if (s != null)
					{
						s.close();
					}
					if (c != null)
					{
						c.close();
					}
				}
				catch (Exception ignore)
				{
				}

				if ("08S01".equals( sqlState ) || "40001".equals( sqlState ))
				{
					retryCount--;
					log( "Connection was stale; trying again..." );
				}
				else
				{
					retryCount = 0;
					log( "Unable to get a connection: " + ex );
				}
			}
		}
		while (retryCount > 0);

		throw new SQLNonTransientConnectionException( "Could not get a connection" );
	}

    /**
     * hashes a member's password and adds it to the prepared statement
     * @param p the prepared statement to add the password to
     * @param password the unhashed password
     * @param index the index to add the password at
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     */
    public static void hashPassword(PreparedStatement p, String password, int index)
            throws NoSuchAlgorithmException, SQLException
    {
        // generate a random number for the salt
        SecureRandom random = new SecureRandom();
        byte salt[] = new byte[32];
        random.nextBytes(salt);
        p.setBytes(index+1, salt);

        // do the hashing
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.update(password.getBytes());
        byte[] hashedPwd = digest.digest(salt);
        p.setBytes(index, hashedPwd);
    }

	private static void log(String msg)
	{
		System.out.println( msg );
	}
}
