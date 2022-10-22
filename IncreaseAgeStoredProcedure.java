package Minions_DB;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class IncreaseAgeStoredProcedure {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "am99e3n8");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        //Create a stored procedure, which increases age
        CallableStatement createStoredProcedure = connection.prepareCall("CREATE PROCEDURE `usp_get_older`(`minion_id` INT) " +
                "BEGIN " +
                    "UPDATE minions " +
                    "SET age = age + 1 " +
                    "WHERE id = minion_id; " +
                "END ");

        createStoredProcedure.executeUpdate();
    }
}
