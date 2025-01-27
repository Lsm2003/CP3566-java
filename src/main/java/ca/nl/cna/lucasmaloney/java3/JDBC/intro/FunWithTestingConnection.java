package ca.nl.cna.lucasmaloney.java3.JDBC.intro;

import org.w3c.dom.ls.LSOutput;
import ca.nl.cna.lucasmaloney.java3.JDBC.DatabaseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FunWithTestingConnection {
    public static void main(String[] args) {
        try{
            //Test the connection with a single complete URL
            Connection conn = DriverManager.getConnection(DatabaseProperties.DATABASE_URL_COMPLETE);
            System.out.println("Connection Successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
