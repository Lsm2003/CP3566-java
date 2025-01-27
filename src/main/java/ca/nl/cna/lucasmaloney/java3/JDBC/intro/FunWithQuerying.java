package ca.nl.cna.lucasmaloney.java3.JDBC.intro;

import java.sql.*;
import ca.nl.cna.lucasmaloney.java3.JDBC.DatabaseProperties;

public class FunWithQuerying {
    public static final String DB_NAME = "/example_employee";
    public static final String QUERY = "SELECT id, first, last, age FROM Employees";

    public static void main(String[] args) {

        try{
            Connection conn = DriverManager.getConnection(
                    DatabaseProperties.DATABASE_URL + DB_NAME, DatabaseProperties.DATABASE_USER, DatabaseProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);

            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                System.out.print("ID: " + rs.getInt("id"));
                System.out.print(", Age: " + rs.getInt("age"));
                System.out.print(", First: " + rs.getString("first"));
                System.out.println(", Last: " + rs.getString("last"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
