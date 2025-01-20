package ca.nl.cna.lucasmaloney.java3.JDBC.intro;

import ca.nl.cna.lucasmaloney.java3.JDBC.DatabaseProperties;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

public class FunWithCreatingDBs {

    public static void main(String[] args) {
        // Open a connection
        try(Connection conn = DriverManager.getConnection(
                DatabaseProperties.DATABASE_URL, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
        ) {
            String sql = "CREATE DATABASE STUDENTS";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
