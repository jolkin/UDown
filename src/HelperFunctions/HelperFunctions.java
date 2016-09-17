package HelperFunctions;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Gabriel on 9/17/2016.
 */
public class HelperFunctions
{
    public static void userToJson(ResultSet resultSet, PrintWriter printWriter)
    {
        try
        {
            printWriter.write(
                "{" +
                    "\"user_id\":\"" + resultSet.getString("user_id") + "\"," +
                    "\"username\":\"" + resultSet.getString("username") + "\"," +
                    "\"email\":\"" + resultSet.getString("email") + "\"," +
                    "\"phone\":\"" + resultSet.getString("phoneNum") + "\"" +
                "}"
            );
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }

    public static void userInterestsToJson(ResultSet resultSet, PrintWriter printWriter)
    {
        try
        {
            printWriter.write(
                "{"
            );
            int counter = 1;
            do
            {
                if(counter != 1)
                    printWriter.write(
                        ","
                    );
                printWriter.write(
                    "\"" + resultSet.getString("user_id") + "\":\"" + resultSet.getString("interest") + "\""
                );
                counter++;
            }while(resultSet.next());
            printWriter.write(
                "}"
            );
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }

    public static void eventToJson(ResultSet resultSet1, ResultSet resultSet2, PrintWriter printWriter)
    {
        try
        {
            printWriter.write(
                "{" +
                    "\"event_id\":\"" + resultSet1.getString("event_id") + "\"," +
                    "\"eventname\":\"" + resultSet1.getString("eventname") + "\"," +
                    "\"location\":\"" + resultSet1.getString("location") + "\"," +
                    "\"goTime\":\"" + resultSet1.getString("goTime") + "\"," +
                    "\"price\":\"" + resultSet1.getString("price") + "\"," +
                    "\"description\":\"" + resultSet1.getString("description") + "\"," +
                    "\"user_id\":\"" + resultSet1.getString("user_id") + "\"," +
                    "\"attendees\":{"
            );
            int counter = 1;
            do
            {
                if(counter != 1)
                    printWriter.write(",");
                printWriter.write(
                        "\"" + (counter++) + "\":\"" + resultSet2.getString("username") + "\""
                );
            }while(resultSet2.next());
            printWriter.write(
                    "}" +
                "}"
            );
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
