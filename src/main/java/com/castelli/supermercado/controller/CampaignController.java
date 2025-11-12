package com.castelli.supermercado.controller;

import com.castelli.supermercado.service.CampaignService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendCampaign(
            @RequestParam("file") MultipartFile file,
            @RequestParam("message") String messageTemplate) {

        campaignService.processAndSendCampaign(file, messageTemplate);

        String jsonResponse = "{\"status\": \"Started\", \"message\": \"Campanha iniciada com sucesso. O processo continuará em segundo plano.\"}";

        return ResponseEntity.ok(jsonResponse);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body("{\"message\": \"" + ex.getMessage() + "\"}");
    }
}