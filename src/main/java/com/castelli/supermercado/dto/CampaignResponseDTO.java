package com.castelli.supermercado.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CampaignResponseDTO {

    private String status;
    private int sent;
    private int failed;
    private List<String> errors;
}