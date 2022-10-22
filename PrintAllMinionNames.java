package Minions_DB;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class PrintAllMinionNames {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "am99e3n8");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        //TASK: print the first + 1, last - 1, first + 2, last - 2 ... name in order 'first record, last record'; ENSURE UNIQUE NAMES
        PreparedStatement getMinionsInAscOrder = connection.prepareStatement("SELECT name FROM minions");
        PreparedStatement getMinionsInDescOrder = connection.prepareStatement("SELECT name FROM minions ORDER BY id DESC");

        ResultSet minionsAsc = getMinionsInAscOrder.executeQuery();
        ResultSet minionsDesc = getMinionsInDescOrder.executeQuery();

        Set<String> minionNames = new LinkedHashSet<>();

        while (minionsAsc.next()) {
            minionsDesc.next();

            String currentAscMinion = minionsAsc.getString("name");
            String currentDescMinion = minionsDesc.getString("name");

            if (minionNames.contains(currentAscMinion) || minionNames.contains(currentDescMinion)) {
                break;
            }
            minionNames.add(currentAscMinion);
            minionNames.add(currentDescMinion);
        }

        for (String name : minionNames) {
            System.out.println(name);
        }
    }
}
