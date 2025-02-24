package org.example;

import java.util.List;

public class ResultDTO {
    private String codiceProgressivo;
    private List<ResultDTO> dipendoDa;
    private List<ResultDTO> servoPer;

    public ResultDTO(String code){
        this.codiceProgressivo = code;
    }

}
