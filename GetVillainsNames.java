package Minions_DB;

import java.sql.*;
import java.util.Properties;

public class GetVillainsNames {
    public static void main(String[] args) throws SQLException {

        //GET VILLAINS' NAMES BASED ON NUMBER OF MINIONS
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "pass");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement prepareStatement = connection.prepareStatement("SELECT v.name, count(minion_id) count FROM villains v " +
                "JOIN minions_villains mv ON v.id = mv.villain_id " +
                "GROUP BY mv.villain_id " +
                "HAVING count > 15 " +
                "ORDER BY count DESC;");

        ResultSet resultSet = prepareStatement.executeQuery();
        while (resultSet.next()) {
            System.out.printf("%s %d\n",
                    resultSet.getString("name"),
                    resultSet.getInt("count"));
        }
    }
}
