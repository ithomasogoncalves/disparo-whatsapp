package com.castelli.supermercado.service;

import com.castelli.supermercado.dto.CustomerDTO;
import com.castelli.supermercado.util.CsvParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class CampaignService {

    private static final Logger log = LoggerFactory.getLogger(CampaignService.class);

    private final CsvParserUtil csvParser;
    private final WhatsAppClient whatsAppClient;

    public CampaignService(CsvParserUtil csvParser, WhatsAppClient whatsAppClient) {
        this.csvParser = csvParser;
        this.whatsAppClient = whatsAppClient;
    }

    @Async
    public void processAndSendCampaign(MultipartFile file, String messageTemplate) {
        log.info("INICIANDO CAMPANHA ASSÍNCRONA...");

        List<CustomerDTO> customers = csvParser.parse(file);
        log.info("Encontrados {} clientes no CSV.", customers.size());

        int sentCount = 0;
        int failedCount = 0;
        List<String> failedNumbers = new ArrayList<>();

        for (CustomerDTO customer : customers) {
            String personalizedMessage = messageTemplate.replace("{nome}", customer.getNome());

            boolean success = whatsAppClient.sendMessage(customer.getTelefone(), personalizedMessage);

            if (success) {
                sentCount++;
            } else {
                failedCount++;
                failedNumbers.add(customer.getTelefone());
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        log.info("CAMPANHA ASSÍNCRONA FINALIZADA. Sucessos: {}, Falhas: {}", sentCount, failedCount);
        if(failedCount > 0) {
            log.warn("Números que falharam: {}", failedNumbers);
        }
    }
}