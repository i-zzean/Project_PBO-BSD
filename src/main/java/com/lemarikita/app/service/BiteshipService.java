package com.lemarikita.app.service;

import com.lemarikita.app.model.Donasi;
import com.lemarikita.app.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class BiteshipService {

    private final RestClient restClient;

    public record HasilOrderBiteship(
            String orderId,
            String waybillId,
            BigDecimal ongkosKirim
    ) {}

    public BiteshipService(@Value("${biteship.api.key}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.biteship.com")
                .defaultHeader("Authorization", apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * 1. Cek ongkos kirim berbagai kurir berdasarkan kode pos & total berat paket
     */
    public Object cekOngkosKirim(String kodePosAsal, String kodePosTujuan, int totalBeratGram) {
        Map<String, Object> body = Map.of(
                "origin_postal_code", Integer.parseInt(kodePosAsal),
                "destination_postal_code", Integer.parseInt(kodePosTujuan),
                "couriers", "jne,sicepat,anteraja,jnt",
                "items", List.of(Map.of(
                        "name", "Paket Donasi Pakaian",
                        "description", "Pakaian layak pakai LemariKita",
                        "value", 50000,
                        "weight", Math.max(totalBeratGram, 100),
                        "quantity", 1
                ))
        );

        try {
            Map<?, ?> response = restClient.post()
                    .uri("/v1/rates/couriers")
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            return response != null && response.get("pricing") != null
                    ? response.get("pricing")
                    : List.of();
        } catch (RestClientResponseException ex) {
            throw new IllegalStateException("Gagal mengecek ongkir Biteship: " + ex.getResponseBodyAsString());
        }
    }

    /**
     * 2. Buat pesanan penjemputan kurir otomatis dan ambil order_id, resi, serta tarif asli
     */
    public HasilOrderBiteship buatOrderPenjemputan(
            User donatur,
            Donasi donasi,
            String kodeKurir,
            String kodeLayananKurir,
            String alamatPengirim,
            String kodePosPengirim,
            String alamatPenerima,
            String kodePosPenerima) {

        Map<String, Object> body = Map.ofEntries(
                Map.entry("shipper_contact_name", donatur.getNama()),
                Map.entry("shipper_contact_phone", donatur.getNoTelp()),
                Map.entry("shipper_contact_email", donatur.getEmail()),
                Map.entry("shipper_organization", "LemariKita"),
                Map.entry("origin_contact_name", donatur.getNama()),
                Map.entry("origin_contact_phone", donatur.getNoTelp()),
                Map.entry("origin_address", alamatPengirim),
                Map.entry("origin_postal_code", Integer.parseInt(kodePosPengirim)),
                Map.entry("destination_contact_name", "Mitra Penerima LemariKita"),
                Map.entry("destination_contact_phone", "081200000000"),
                Map.entry("destination_address", alamatPenerima),
                Map.entry("destination_postal_code", Integer.parseInt(kodePosPenerima)),
                Map.entry("courier_company", kodeKurir),
                Map.entry("courier_type", kodeLayananKurir),
                Map.entry("delivery_type", "now"),
                Map.entry("items", List.of(Map.of(
                        "name", "Paket Donasi Pakaian (" + donasi.getDaftarDetail().size() + " jenis)",
                        "value", 50000,
                        "weight", Math.max(donasi.getTotalBeratGram(), 100),
                        "quantity", 1
                )))
        );

        try {
            Map<?, ?> response = restClient.post()
                    .uri("/v1/orders")
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
                throw new IllegalStateException("Respons order Biteship tidak valid.");
            }

            String orderId = String.valueOf(response.get("id"));

            String waybillId = null;
            if (response.get("courier") instanceof Map<?, ?> courierMap && courierMap.get("waybill_id") != null) {
                waybillId = String.valueOf(courierMap.get("waybill_id"));
            }

            BigDecimal price = response.get("price") != null
                    ? new BigDecimal(String.valueOf(response.get("price")))
                    : BigDecimal.ZERO;

            return new HasilOrderBiteship(orderId, waybillId, price);
        } catch (RestClientResponseException ex) {
            throw new IllegalStateException("Gagal membuat order Biteship: " + ex.getResponseBodyAsString());
        }
    }
}
