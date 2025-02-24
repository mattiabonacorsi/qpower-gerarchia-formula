package org.example.utils;

public enum EntityType {
    KPI,
    KPI_NUMERATORE,
    KPI_DENOMINATORE,
    CONTATORE,
    INDEX,
    ONERE_ELEMENTO;

    public static EntityType fromCode(String code) {
        if (code.startsWith("KPI")) return KPI;
        if (code.startsWith("CON")) return CONTATORE;
        if (code.startsWith("IDX")) return INDEX;
        return null;
    }

    public static EntityType fromDbValue(String value) {
        switch (value.toUpperCase()) {
            case "KPI_NUMERATORE":
            case "KPI_DENOMINATORE":
                return KPI;
            case "ONERE_ELEMENTO":
                return CONTATORE;
            default:
                return valueOf(value);
        }
    }
} 