package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResultDTO {
    private String codiceProgressivo;
    private String nome;
    private List<ResultDTO> dipendoDa;
    private List<ResultDTO> servoPer;

    public ResultDTO(String codiceProgressivo, String nome) {
        this.codiceProgressivo = codiceProgressivo;
        this.nome = nome;
        this.dipendoDa = new ArrayList<>();
        this.servoPer = new ArrayList<>();
    }

    public List<ResultDTO> getDipendoDa() {
        return dipendoDa;
    }

    public List<ResultDTO> getServoPer() {
        return servoPer;
    }

    @Override
    public String toString() {
        return buildString("", new HashSet<>());
    }

    private String buildString(String indent, Set<String> visited) {
        if (visited.contains(codiceProgressivo)) {
            return indent + codiceProgressivo + " - " + nome + " (circular reference)\n";
        }
        visited.add(codiceProgressivo);
        
        StringBuilder result = new StringBuilder();
        result.append(indent).append(codiceProgressivo);
        if (nome != null && !nome.isEmpty()) {
            result.append(" - ").append(nome);
        }
        result.append("\n");
        
        if (!dipendoDa.isEmpty()) {
            result.append(indent).append("dipende da:\n");
            for (ResultDTO dto : dipendoDa) {
                result.append(indent).append("-").append(dto.buildString(indent + " ", new HashSet<>(visited)));
            }
        }
        
        if (!servoPer.isEmpty()) {
            result.append(indent).append("serve per:\n");
            for (ResultDTO dto : servoPer) {
                result.append(indent).append("-").append(dto.buildString(indent + " ", new HashSet<>(visited)));
            }
        }
        
        return result.toString();
    }
}
