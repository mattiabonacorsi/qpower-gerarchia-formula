package org.example.utils;

import org.example.model.Formula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseUtils {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
    private final Connection conn;

    public DatabaseUtils(Connection conn) {
        this.conn = conn;
    }

    public Optional<String> getEntityId(String code, EntityType entityType) {
        String query = createEntityQuery(entityType);
        return executeQuery(query, code);
    }

    public Optional<String> getCodeFromEntity(String entityId, EntityType entityType) {
        String query = createCodeQuery(entityType);
        return executeQuery(query, entityId);
    }

    public List<Formula> getFormulas(String entityId) {
        List<Formula> formulas = new ArrayList<>();
        String query = "SELECT formula, entitaPrincipaleId, entitaPrincipale FROM formula WHERE entitaPrincipaleId = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, entityId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    formulas.add(new Formula(
                        rs.getString("entitaPrincipaleId"),
                        rs.getString("entitaPrincipale"),
                        rs.getString("formula")
                    ));
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching formulas for entityId: {}", entityId, e);
            throw new DatabaseException("Failed to fetch formulas", e);
        }
        return formulas;
    }

    private String createEntityQuery(EntityType entityType) {
        return switch (entityType) {
            case KPI, KPI_NUMERATORE, KPI_DENOMINATORE -> 
                "SELECT kpiId FROM kpi WHERE codiceProgressivo = ?";
            case CONTATORE, ONERE_ELEMENTO -> 
                "SELECT contatoreId FROM contatore WHERE codiceProgressivo = ?";
            case INDEX -> 
                "SELECT indiceId FROM indici WHERE codiceProgressivo = ?";
        };
    }

    private String createCodeQuery(EntityType entityType) {
        return switch (entityType) {
            case KPI, KPI_NUMERATORE, KPI_DENOMINATORE -> 
                "SELECT codiceProgressivo FROM kpi WHERE kpiId = ?";
            case CONTATORE, ONERE_ELEMENTO -> 
                "SELECT codiceProgressivo FROM contatore WHERE contatoreId = ?";
            case INDEX -> 
                "SELECT codiceProgressivo FROM indici WHERE indiceId = ?";
        };
    }

    private Optional<String> executeQuery(String query, String param) {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing query: {} with param: {}", query, param, e);
            throw new DatabaseException("Failed to execute query", e);
        }
        return Optional.empty();
    }

    public Connection getConnection() {
        return conn;
    }
} 