package com.castelli.supermercado.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class CustomerDTO {

    @CsvBindByName
    private String nome;

    @CsvBindByName
    private String telefone;
}