package com.castelli.supermercado.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WhatsAppClient {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppClient.class);

    private final RestTemplate restTemplate;

    private final String apiBaseUrl;
    private final String instanceName;
    private final String apiToken;

    public WhatsAppClient(RestTemplate restTemplate,
                          @Value("${whatsapp.api.base-url}") String apiBaseUrl,
                          @Value("${whatsapp.api.instance-name}") String instanceName,
                          @Value("${whatsapp.api.token}") String apiToken) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
        this.instanceName = instanceName;
        this.apiToken = apiToken;
    }

    public boolean sendMessage(String to, String personalizedMessage) {

        String fullUrl = UriComponentsBuilder.fromHttpUrl(apiBaseUrl)
                .path("/whatsapp/send-message")
                .toUriString();

        String phoneNumber = to;
        if (phoneNumber != null && phoneNumber.length() == 13 && phoneNumber.charAt(4) == '9') {
            log.info("Nono dígito encontrado em {}. Removendo...", phoneNumber);
            // Recria a string: "55XX" (índices 0-3) + "XXXX-XXXX" (índices 5 em diante)
            phoneNumber = phoneNumber.substring(0, 4) + phoneNumber.substring(5);
        }

        String jid = phoneNumber;
        if (jid != null && !jid.endsWith("@c.us")) {
            jid = jid.trim() + "@c.us";
        }

        String jsonBody = String.format(
                "{\"session\": \"%s\", \"number\": \"%s\", \"text\": \"%s\", \"isGroup\": false}",
                this.instanceName,
                jid,
                personalizedMessage.replace("\"", "\\\"")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        try {
            log.info("Enviando para {} via URL (JSON Manual): {}", jid, fullUrl);
            log.info("Payload (JSON String): {}", jsonBody);

            restTemplate.exchange(
                    fullUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            log.info("SUCESSO AO ENVIAR PARA {}!", jid);
            return true;

        } catch (RestClientException e) {
            log.error("FALHA AO ENVIAR PARA {}: {}", jid, e.getMessage());
            return false;
        }
    }
}