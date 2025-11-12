package com.castelli.supermercado.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class CustomerDTO {

    // O nome da coluna no CSV deve ser "nome"
    @CsvBindByName
    private String nome;

    // O nome da coluna no CSV deve ser "telefone"
    @CsvBindByName
    private String telefone;
}