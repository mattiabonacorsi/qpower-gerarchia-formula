package org.example;

import java.sql.*;
import java.util.*;

import org.example.model.*;
import org.example.utils.*;
import org.slf4j.*;

public class Tracer {
    private static final Logger logger = LoggerFactory.getLogger(Tracer.class);
    private final DatabaseUtils dbUtils;

    public Tracer(Connection conn) {
        this.dbUtils = new DatabaseUtils(conn);
    }

    public void tracer(String codice) {
        logger.info("Starting trace for code: {}", codice);
        ResultDTO result = new ResultDTO(codice);
        try {
            findDependencies(result, codice, new HashSet<>());
            findDependents(result, codice, new HashSet<>());
            System.out.println(result);
        } catch (Exception e) {
            logger.error("Error tracing dependencies for code: {}", codice, e);
            throw new TracingException("Failed to trace dependencies", e);
        }
    }

    private void findDependencies(ResultDTO result, String codice, Set<String> visited) {
        if (!shouldProcessDependency(codice, visited)) return;
        visited.add(codice);

        EntityType entityType = EntityType.fromCode(codice);
        if (entityType == null) return;

        processEntityDependencies(result, codice, entityType, visited);
    }

    private boolean shouldProcessDependency(String codice, Set<String> visited) {
        return codice != null && !visited.contains(codice);
    }

    private void processEntityDependencies(ResultDTO result, String codice, EntityType entityType, Set<String> visited) {
        dbUtils.getEntityId(codice, entityType).ifPresent(entityId -> {
            List<Formula> formulas = dbUtils.getFormulas(entityId);
            Set<String> allDependencies = new HashSet<>();

            if (entityType == EntityType.INDEX && formulas.isEmpty()) {
                logger.debug("Index {} has no formulas", codice);
                return;
            }

            for (Formula formula : formulas) {
                processSingleFormula(result, formula, allDependencies, visited);
            }
        });
    }

    private void processSingleFormula(ResultDTO result, Formula formula, Set<String> allDependencies, Set<String> visited) {
        String[] dependencies = FormulaParser.extractDependencies(formula.formula());
        for (String dep : dependencies) {
            if (!allDependencies.contains(dep)) {
                allDependencies.add(dep);
                ResultDTO dependency = new ResultDTO(dep);
                result.getDipendoDa().add(dependency);
                findDependencies(dependency, dep, visited);
            }
        }
    }

    private void findDependents(ResultDTO result, String codice, Set<String> visited) {
        if (!shouldProcessDependency(codice, visited)) return;
        visited.add(codice);

        String query = "SELECT f.entitaPrincipaleId, f.entitaPrincipale FROM formula f WHERE f.formula LIKE ?";
        try (PreparedStatement stmt = dbUtils.getConnection().prepareStatement(query)) {
            stmt.setString(1, "%" + codice + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    processDependent(result, rs, visited);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding dependents for code: {}", codice, e);
            throw new DatabaseException("Failed to find dependents", e);
        }
    }

    private void processDependent(ResultDTO result, ResultSet rs, Set<String> visited) throws SQLException {
        String entityId = rs.getString("entitaPrincipaleId");
        String entityType = rs.getString("entitaPrincipale");
        
        dbUtils.getCodeFromEntity(entityId, EntityType.fromDbValue(entityType))
            .ifPresent(dependentCode -> {
                if (!visited.contains(dependentCode)) {
                    ResultDTO dependent = new ResultDTO(dependentCode);
                    result.getServoPer().add(dependent);
                    findDependents(dependent, dependentCode, visited);
                }
            });
    }
}
