package com.castelli.supermercado.util;

import com.castelli.supermercado.dto.CustomerDTO;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvParserUtil {

    public List<CustomerDTO> parse(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo CSV está vazio.");
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            return new CsvToBeanBuilder<CustomerDTO>(reader)
                    .withType(CustomerDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(0)
                    .build()
                    .parse();

        } catch (Exception ex) {
            throw new RuntimeException("Falha ao processar o arquivo CSV: " + ex.getMessage(), ex);
        }
    }
}