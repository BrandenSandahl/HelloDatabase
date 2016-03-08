package com.theIronYard;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {

        useH2();

        usePostGreSQL();


    }





    public static void useH2() throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:h2:./main"); //this object call links to the database

        Statement stmt = conn.createStatement(); //links a statement object to our current DB

        stmt.execute("CREATE TABLE IF NOT EXISTS players (id IDENTITY, name VARCHAR, health DOUBLE, is_alive BOOLEAN, score INT)");
        stmt.execute("INSERT INTO players VALUES (NULL, 'Bob', 7.5, true, 50)");
        stmt.execute("UPDATE players SET health = 10.0 WHERE name = 'Bob'");
        stmt.execute("DELETE FROM players WHERE name = 'Bob'");

        /*
        //this is example of the wrong way to do this.
        //String name = "Alice";
        String name = "', 10.0, true, 50); DROP TABLE players; --"; //<-- malicious user could use injection attack !!
        stmt.execute(String.format("INSERT INTO players VALUES (NULL, '%s', 10.0, true, 50)", name));

         */

        //Good way to to this
        PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO players VALUES (NULL, ?, ?, true, 50)");  //prepare
        stmt2.setString(1, "Alice"); //then enter options where the ? are at
        stmt2.setDouble(2, 10.0);
        stmt2.execute();


        ResultSet results = stmt.executeQuery("SELECT * FROM players");


        while (results.next()) {
            String name = results.getString("name");
            double health = results.getDouble("health");
            int score = results.getInt("score");
            System.out.printf("%s %s %s\n", name, health, score);
        }


        stmt.execute("DROP TABLE players");


        conn.close();
    }

    public static void usePostGreSQL() throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hellodb");

        Statement stmt = conn.createStatement(); //links a statement object to our current DB

        stmt.execute("CREATE TABLE IF NOT EXISTS players (id SERIAL, name VARCHAR, health DECIMAL, is_alive BOOLEAN, score INT)");
        stmt.execute("INSERT INTO players VALUES (DEFAULT, 'Bob', 7.5, true, 50)");
        stmt.execute("UPDATE players SET health = 10.0 WHERE name = 'Bob'");
        stmt.execute("DELETE FROM players WHERE name = 'Bob'");

        /*
        //this is example of the wrong way to do this.
        //String name = "Alice";
        String name = "', 10.0, true, 50); DROP TABLE players; --"; //<-- malicious user could use injection attack !!
        stmt.execute(String.format("INSERT INTO players VALUES (NULL, '%s', 10.0, true, 50)", name));

         */

        //Good way to to this
        PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO players VALUES (DEFAULT, ?, ?, true, 50)");  //prepare
        stmt2.setString(1, "Alice"); //then enter options where the ? are at
        stmt2.setDouble(2, 10.0);
        stmt2.execute();


        ResultSet results = stmt.executeQuery("SELECT * FROM players");


        while (results.next()) {
            String name = results.getString("name");
            double health = results.getDouble("health");
            int score = results.getInt("score");
            System.out.printf("%s %s %s\n", name, health, score);
        }


       // stmt.execute("DROP TABLE players");


        conn.close();
    }


}
