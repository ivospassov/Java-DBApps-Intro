package Minions_DB;

import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class IncreaseMinionAge {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "am99e3n8");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);

        int[] minionIds = Arrays
                .stream(scanner.nextLine().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();


        for (int id : minionIds) {
            PreparedStatement updateMinionData = connection.prepareStatement("UPDATE minions " +
                    "SET name = LOWER(name), age = age + 1 " +
                    "WHERE id = ?;");
            updateMinionData.setInt(1, id);
            updateMinionData.executeUpdate();
        }

        PreparedStatement extractAllMinionsData = connection.prepareStatement("SELECT name, age FROM minions");
        ResultSet resultSet = extractAllMinionsData.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d\n", resultSet.getString("name"), resultSet.getInt("age"));
        }
    }
}
