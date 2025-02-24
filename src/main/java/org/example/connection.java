package org.example;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class connection {
    private static String user,password,url;
    static {
        try {
            Properties properties = new Properties();
            FileInputStream configFile = new FileInputStream("src/main/resources/config.properties");
            properties.load(configFile);

            url = properties.getProperty("db.url");
            user = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}
