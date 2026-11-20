package com.lemarikita.app.dto;

public record BiteshipWebhookPayload(
        String order_id,
        String courier_waybill_id,
        String status,
        String note
) {}
