package org.example;

import java.sql.*;

public class testClass {
    Connection conn;

    public testClass(Connection conn) {
        this.conn = conn;

    }
    public void trial() {
        String updateQuery = "UPDATE FORMULA SET formula = '${ KPI0007_H24P - CON0002_EAA_H }' " +
                "WHERE entitaPrincipaleId = '2690b98b-f2bf-4903-8dd6-0bfa2af50c07'";

        try (Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(updateQuery);
            System.out.println(rowsAffected + " row(s) updated");

            // Verify the update
            String verifyQuery = "SELECT formula FROM FORMULA WHERE entitaPrincipaleId = '2690b98b-f2bf-4903-8dd6-0bfa2af50c07'";
            try (ResultSet rs = stmt.executeQuery(verifyQuery)) {
                if (rs.next()) {
                    System.out.println("Updated formula: " + rs.getString("formula"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }
    }}

/*
* ES 2:  Input KPI0001
Rispetto all'esempio 1:
- KPI0001 è collegato a 2 formule,
- ha risultati (in entrambe le parti a 2 livelli)
- Uso anche di INDICI (hanno una sintassi leggermente diversa)
Risultato:
KPI0001
dipende da:
- CON0142
- CON0152
- CON0166
     dipende da:
      - CON0001
      - CON0002
- CON0364
- IDX_CO2
serve per:
- KPI0002
     dipende da:
      - KPI0004
- KPI0003
*
*
CON0140 - Tipologia olio grezzo lavorato Raffineria 2
dipende da:
        - CON0001 - Contatore Sensore Clima QUIX
- CON0002 - Contatore Sensore Ufficio QUIX
serve per:
        - CON0141 - PROVA

*/

