package com.castelli.supermercado.service;

import com.castelli.supermercado.dto.ApiFullRequestDTO;
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
            phoneNumber = phoneNumber.substring(0, 4) + phoneNumber.substring(5);
        }

        String jid = phoneNumber;
        if (jid != null && !jid.endsWith("@c.us")) {
            jid = jid.trim() + "@c.us";
        }

        ApiFullRequestDTO requestDTO = new ApiFullRequestDTO();
        requestDTO.setSession(this.instanceName);
        requestDTO.setNumber(jid);
        requestDTO.setText(personalizedMessage);
        requestDTO.setGroup(false);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiToken);

        HttpEntity<ApiFullRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);

        try {
        log.info("Enviando para {} via URL: {}", jid, fullUrl);
        restTemplate.postForEntity(fullUrl, requestEntity, String.class);

        log.info("SUCESSO AO ENVIAR PARA {}!", jid);
        return true;

        } catch (RestClientException e) {
        log.error("FALHA AO ENVIAR PARA {}: {}", jid, e.getMessage());
        return false;
    }
    }
}
