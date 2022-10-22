package Minions_DB;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class RemoveVillain {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "am99e3n8");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);
        int villainId = Integer.parseInt(scanner.nextLine());

        //Find name of villain
        PreparedStatement findVillainName = connection.prepareStatement("SELECT name FROM villains WHERE id = ?;");
        findVillainName.setInt(1, villainId);
        ResultSet villainSet = findVillainName.executeQuery();
        if (!villainSet.next()) {
            System.out.println("No such villain was found!");
            return;
        }
        String villainName = villainSet.getString("name");

        //Find number of attached minions to villain
        PreparedStatement findAttachedMinions = connection.prepareStatement("SELECT count(minion_id) count FROM minions_villains WHERE villain_id = ?;");
        findAttachedMinions.setInt(1, villainId);
        ResultSet minionsSet = findAttachedMinions.executeQuery();
        minionsSet.next();

        int attachedMinionsCount = minionsSet.getInt("count");

        //Detach/delete all attached minions to villain from mapped table 'minions_villains'
        PreparedStatement deleteAttachedMinions = connection.prepareStatement("DELETE FROM minions_villains WHERE villain_id = ?;");
        deleteAttachedMinions.setInt(1, villainId);
        deleteAttachedMinions.executeUpdate();

        //Delete the villain
        PreparedStatement deleteVillain = connection.prepareStatement("DELETE FROM villains WHERE id = ?;");
        deleteVillain.setInt(1, villainId);
        deleteVillain.executeUpdate();

        System.out.println(villainName.concat(" was deleted"));
        System.out.println(String.valueOf(attachedMinionsCount).concat(" minions released"));
    }
}
