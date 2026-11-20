package com.lemarikita.app.service;

import com.lemarikita.app.dto.BiteshipWebhookPayload;
import com.lemarikita.app.model.*;
import com.lemarikita.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogistikWebhookService {

    private final PengirimanRepository pengirimanRepository;
    private final DonasiRepository donasiRepository;
    private final RiwayatRepository riwayatRepository;
    private final RewardRepository rewardRepository;
    private final ProfilRepository profilRepository;

    public LogistikWebhookService(PengirimanRepository pengirimanRepository,
                                  DonasiRepository donasiRepository,
                                  RiwayatRepository riwayatRepository,
                                  RewardRepository rewardRepository,
                                  ProfilRepository profilRepository) {
        this.pengirimanRepository = pengirimanRepository;
        this.donasiRepository = donasiRepository;
        this.riwayatRepository = riwayatRepository;
        this.rewardRepository = rewardRepository;
        this.profilRepository = profilRepository;
    }

    @Transactional
    public void prosesWebhookBiteship(BiteshipWebhookPayload payload) {
        Pengiriman pengiriman = pengirimanRepository.findByBiteshipOrderId(payload.order_id())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Order Biteship tidak ditemukan: " + payload.order_id()));

        // Perbarui nomor resi jika baru tersedia saat kurir ditugaskan
        if (payload.courier_waybill_id() != null && !payload.courier_waybill_id().isBlank()) {
            pengiriman.setNoResi(payload.courier_waybill_id());
            pengirimanRepository.save(pengiriman);
        }

        // 1. Catat log perjalanan paket ke tabel Riwayat
        Riwayat riwayat = new Riwayat();
        riwayat.setPengiriman(pengiriman);
        riwayat.setStatus(payload.status().toUpperCase());
        riwayat.setKeterangan(payload.note() != null ? payload.note() : "Pembaruan status dari kurir.");
        riwayatRepository.save(riwayat);

        // 2. Sinkronkan status Donasi berdasarkan status webhook Biteship
        Donasi donasi = pengiriman.getDonasi();
        StatusDonasi statusBaru = konversiStatusBiteship(payload.status());
        donasi.setStatus(statusBaru);
        donasiRepository.save(donasi);

        // 3. Jika paket sudah TERKIRIM ke mitra/panti, berikan poin gamifikasi (idempoten)
        if (statusBaru == StatusDonasi.TERKIRIM) {
            berikanPoinDonasiJikaBelumAda(donasi);
        }
    }

    private void berikanPoinDonasiJikaBelumAda(Donasi donasi) {
        boolean sudahDiberikan = rewardRepository.existsByDonasiAndJenisReward(donasi, JenisReward.POIN_DONASI);
        if (sudahDiberikan) {
            return; // Abaikan jika webhook duplikat
        }

        User donatur = donasi.getUser();
        int poinDidapat = donasi.getTotalPoinEstimasi();

        Reward reward = new Reward();
        reward.setUser(donatur);
        reward.setDonasi(donasi);
        reward.setPoin(poinDidapat);
        reward.setJenisReward(JenisReward.POIN_DONASI);
        rewardRepository.save(reward);

        Profil profil = donatur.getProfil();
        profil.tambahPoin(poinDidapat); // Otomatis menambah total_poin dan memperbarui level
        profilRepository.save(profil);
    }

    private StatusDonasi konversiStatusBiteship(String statusBiteship) {
        if (statusBiteship == null) {
            return StatusDonasi.MENUNGGU_PENJEMPUTAN;
        }
        return switch (statusBiteship.toLowerCase()) {
            case "confirmed", "allocated", "picking_up" -> StatusDonasi.MENUNGGU_PENJEMPUTAN;
            case "picked" -> StatusDonasi.DIJEMPUT_KURIR;
            case "dropping_off" -> StatusDonasi.DALAM_PENGIRIMAN;
            case "delivered" -> StatusDonasi.TERKIRIM;
            case "cancelled", "rejected", "returned", "disposed" -> StatusDonasi.DIBATALKAN;
            default -> StatusDonasi.DALAM_PENGIRIMAN;
        };
    }
}
