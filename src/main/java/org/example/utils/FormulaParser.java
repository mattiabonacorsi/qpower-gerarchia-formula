package org.example.utils;

import java.util.HashSet;
import java.util.Set;

public class FormulaParser {
    private static final String REGEX = "[{}()+\\-/*<> ]";
    private static final Set<String> VALID_PREFIXES = Set.of("CON", "KPI", "IDX");

    public static String[] extractDependencies(String formula) {
        if (formula == null) return new String[0];
        
        String[] parts = formula.split(REGEX);
        Set<String> dependencies = new HashSet<>();

        for (String part : parts) {
            if (!part.isEmpty()) {
                String processedPart = processVariable(part);
                if (isValidVariable(processedPart)) {
                    dependencies.add(processedPart);
                }
            }
        }
        return dependencies.toArray(new String[0]);
    }

    private static String processVariable(String variable) {
        // For IDX variables, strip everything after the second underscore
        if (variable.startsWith("IDX")) {
            int secondUnderscore = variable.indexOf('_', variable.indexOf('_') + 1);
            return secondUnderscore != -1 ? variable.substring(0, secondUnderscore) : variable;
        }
        return stripSuffix(variable);
    }

    private static String stripSuffix(String variable) {
        int idx = variable.indexOf('_');
        return idx != -1 ? variable.substring(0, idx) : variable;
    }

    private static boolean isValidVariable(String var) {
        if (var == null || var.trim().isEmpty()) return false;
        return VALID_PREFIXES.stream().anyMatch(var::startsWith);
    }
} 