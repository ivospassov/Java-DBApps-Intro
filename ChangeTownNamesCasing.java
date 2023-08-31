package Minions_DB;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class ChangeTownNamesCasing {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "pass");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);
        String searchedCountry = scanner.nextLine();

        PreparedStatement searchTownsByCountry = connection.prepareStatement("UPDATE towns " +
                "SET name = UPPER(name) WHERE country = ?;");
        searchTownsByCountry.setString(1, searchedCountry);
        searchTownsByCountry.executeUpdate();

        //Find the towns, whose names were changed
        PreparedStatement findTownsBeforeChange = connection.prepareStatement("SELECT name FROM towns WHERE country = ?;");
        findTownsBeforeChange.setString(1, searchedCountry);
        ResultSet countriesSet = findTownsBeforeChange.executeQuery();

        int numberOfTowns = 0;
        StringBuilder stringBuilder = new StringBuilder();

        if (countriesSet.next()) {
            numberOfTowns++;
            String firstTown = countriesSet.getString("name");
            stringBuilder.append(String.format("[%s", firstTown));
        } else {
            System.out.println("No town names were affected.");
            return;
        }

        while (countriesSet.next()) {
            String currentTown = countriesSet.getString("name");
            stringBuilder.append(String.format(", %s", currentTown));
            numberOfTowns++;
        }
        stringBuilder.append("]");

        System.out.println(numberOfTowns + " town names were affected.");
        System.out.println(stringBuilder);
    }
}
