package com.castelli.supermercado.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiFullRequestDTO {

    @JsonProperty("session")
    private String session;

    @JsonProperty("number")
    private String number;

    @JsonProperty("text")
    private String text;

    @JsonProperty("isGroup")
    private boolean isGroup = false;
}