package Minions_DB;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class GetMinionNames {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "pass");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);
        int villainId = Integer.parseInt(scanner.nextLine());

        PreparedStatement extractVillain = connection.prepareStatement("SELECT name FROM villains " +
                "WHERE id = ?;");
        extractVillain.setInt(1, villainId);
        ResultSet villainSet = extractVillain.executeQuery();

        if (!villainSet.next()) {
            System.out.printf("No villain with ID %d exists in the database.\n", villainId);
            return;
        }

        PreparedStatement extractMinions = connection.prepareStatement("SELECT m.name, m.age FROM minions m " +
                "JOIN minions_villains mv ON mv.minion_id LIKE m.id " +
                "WHERE villain_id = ?;");
        extractMinions.setInt(1, villainId);

        ResultSet minionsSet = extractMinions.executeQuery();

        //Print villain name
        System.out.println("Villain: " + villainSet.getString("name"));

        int counter = 1;
        while (minionsSet.next()) {
            System.out.printf("%d. %s %s\n", counter, minionsSet.getString("name"), minionsSet.getInt("age"));
            counter++;
        }
    }
}
