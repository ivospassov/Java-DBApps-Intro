package Minions_DB;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class AddMinion {
    private final static String DEFAULT_EVILNESS_FACTOR = "evil";
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "am99e3n8");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);
        String[] minionData = scanner.nextLine().split("\\s+");
        String[] villainData = scanner.nextLine().split("\\s+");

        //Minion Data
        String minionName = minionData[1];
        int minionAge = Integer.parseInt(minionData[2]);
        String minionTown = minionData[3];

        //VillainData
        String villainName = villainData[1];

        //Check if such town exists in the database; Insert if not
        PreparedStatement searchTown = connection.prepareStatement("SELECT name FROM towns WHERE name = ?;");
        searchTown.setString(1, minionTown);
        ResultSet townSet = searchTown.executeQuery();

        if (!townSet.next()) {
            PreparedStatement insertNewTown = connection.prepareStatement("INSERT INTO towns(name)VALUES(?)");
            insertNewTown.setString(1, minionTown);
            insertNewTown.executeUpdate();
            System.out.printf("Town %s was added to the database.\n", minionTown);
        }

        //Add villain if he/she does not exist in the database
        PreparedStatement searchVillain = connection.prepareStatement("SELECT name FROM villains WHERE name = ?;");
        searchVillain.setString(1, villainName);
        ResultSet villainSet = searchVillain.executeQuery();

        if (!villainSet.next()) {
            PreparedStatement insertNewVillain = connection.prepareStatement("INSERT INTO villains(name, evilness_factor)VALUES(?, ?);");
            insertNewVillain.setString(1, villainName);
            insertNewVillain.setString(2, DEFAULT_EVILNESS_FACTOR);
            insertNewVillain.executeUpdate();
            System.out.printf("Villain %s was added to the database.\n", villainName);
        } else {
            System.out.printf("Villain %s already exists in the database.\n", villainName);
        }

        //Find id of villain to attach the new minion to it
        PreparedStatement findVillainId = connection.prepareStatement("SELECT id FROM villains WHERE name = ?;");
        findVillainId.setString(1, villainName);
        ResultSet villainIdSet = findVillainId.executeQuery();

        villainIdSet.next();
        int villainId = villainIdSet.getInt("id");

        //Find town_id to insert it in minions table
        PreparedStatement findTownId = connection.prepareStatement("SELECT id FROM towns WHERE name = ?;");
        findTownId.setString(1, minionTown);
        ResultSet townIdSet = findTownId.executeQuery();
        townIdSet.next();
        int townId = townIdSet.getInt("id");

        PreparedStatement addNewMinion = connection.prepareStatement("INSERT INTO minions(name, age, town_id)VALUES(?, ?, ?);");
        addNewMinion.setString(1, minionName);
        addNewMinion.setInt(2, minionAge);
        addNewMinion.setInt(3, townId);
        addNewMinion.executeUpdate();

        //Get id of minion to attach it to the new villain
        PreparedStatement findNewMinionId = connection.prepareStatement("SELECT id FROM minions WHERE name = ?;");
        findNewMinionId.setString(1, minionName);
        ResultSet minionIdSet = findNewMinionId.executeQuery();
        minionIdSet.next();
        int minionId = minionIdSet.getInt("id");

        //Insert into mapped table the minion and villain id
        PreparedStatement insertIntoMappedTable = connection.prepareStatement("INSERT INTO minions_villains(minion_id, villain_id)VALUES(?, ?);");
        insertIntoMappedTable.setInt(1, minionId);
        insertIntoMappedTable.setInt(2, villainId);
        insertIntoMappedTable.executeUpdate();
        System.out.printf("Successfully added %s to be minion of %s.", minionName, villainName);

        /*
        SAMPLE INPUT:
        Minion: Cathleen 20 Liverpool
        Villain: Gru
         */
    }
}
