package org.example;

import java.util.ArrayList;
import java.util.List;

public class ResultDTO {
    private String codiceProgressivo;
    private List<ResultDTO> dipendoDa;
    private List<ResultDTO> servoPer;

    public ResultDTO(String codiceProgressivo) {
        this.codiceProgressivo = codiceProgressivo;
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
        StringBuilder result = new StringBuilder();
        result.append(codiceProgressivo).append("\n");
        result.append("dipende da: \n");
        if (!dipendoDa.isEmpty()) {
            for (ResultDTO dto : dipendoDa) {
                result.append("-").append(dto.codiceProgressivo).append(" ");
            }
        } else {
            result.append("None");
        }
        result.append("\n");
        result.append("serve per:\n ");
        if (!servoPer.isEmpty()) {
            for (ResultDTO dto : servoPer) {
                result.append(dto.codiceProgressivo).append(" ");
            }
        } else {
            result.append("None");
        }
        return result.toString();
    }
}
