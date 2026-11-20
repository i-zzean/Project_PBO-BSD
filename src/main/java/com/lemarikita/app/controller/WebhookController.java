package com.lemarikita.app.controller;

import com.lemarikita.app.dto.BiteshipWebhookPayload;
import com.lemarikita.app.service.LogistikWebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private final LogistikWebhookService webhookService;

    public WebhookController(LogistikWebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/biteship")
    public ResponseEntity<Object> terimaWebhookBiteship(@RequestBody BiteshipWebhookPayload payload) {
        webhookService.prosesWebhookBiteship(payload);
        return ResponseEntity.ok(Map.of("message", "Webhook berhasil diproses"));
    }
}
