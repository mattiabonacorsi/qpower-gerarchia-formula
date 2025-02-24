package org.example.utils;

import java.util.*;

public class FormulaParser {
    private static final String REGEX = "[{}()+\\-/*<> ]";

    public static String[] extractDependencies(String formula) {
        if (formula == null) return new String[0];
        
        String[] parts = formula.split(REGEX);
        Set<String> dependencies = new HashSet<>();

        for (String part : parts) {
            if (!part.isEmpty()) {
                // Only strip suffix if it's not an IDX
                String processedPart = part.startsWith("IDX") ? part : stripSuffix(part);
                if (isValidVariable(processedPart)) {
                    dependencies.add(processedPart);
                }
            }
        }
        return dependencies.toArray(new String[0]);
    }

    private static String stripSuffix(String variable) {
        int idx = variable.indexOf('_');
        return idx != -1 ? variable.substring(0, idx) : variable;
    }

    private static boolean isValidVariable(String var) {
        return var != null && !var.trim().isEmpty() && 
               (var.startsWith("CON") || var.startsWith("KPI") || var.startsWith("IDX"));
    }
} 