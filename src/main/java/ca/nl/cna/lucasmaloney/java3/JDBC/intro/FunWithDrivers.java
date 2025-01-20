package ca.nl.cna.lucasmaloney.java3.JDBC.intro;

import java.sql.*;

public class FunWithDrivers {
    public static void main(String[] args) {
        System.out.println("Fun trying to registers a driver");

        //Option 1: Find the class
        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            System.out.println("Option 1: Find the class worked!");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
        } catch (IllegalAccessException ex) {
            System.out.println("Error: access problem while loading!");
        } catch (InstantiationException ex) {
            System.out.println("Error: unable to instantiate driver!");
        }

        //Option 2: Register the Driver
        try {
            Driver myDriver = new org.mariadb.jdbc.Driver();
            DriverManager.registerDriver(myDriver);
            System.out.println("Option 2: Register the Driver worked!");
        } catch (SQLException ex) {
            System.out.println("Error: unable to load driver class!");
        }

    }
}
