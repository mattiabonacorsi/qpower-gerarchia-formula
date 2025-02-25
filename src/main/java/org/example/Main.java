package org.example;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        String inputVariable = "KPI0001";
        try (Connection conn = connection.getConnection()) {
            if (conn != null) {
                Tracer t = new Tracer(conn);
                t.tracer(inputVariable);
                   /* testClass test = new testClass(conn);
                test.trial(); */
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
