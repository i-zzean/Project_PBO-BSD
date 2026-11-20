package com.lemarikita.app.dto;

import com.lemarikita.app.model.Donasi;
import java.time.LocalDateTime;

public record DonasiResponse(
        Long idDonasi,
        Long idUser,
        LocalDateTime tanggal,
        String status,
        Integer totalBeratGram,
        Integer totalPoinEstimasi,
        String kodeKurir,
        String biteshipOrderId,
        String noResi,
        String statusVerifikasiKemasan
) {
    public static DonasiResponse fromEntity(Donasi donasi) {
        return new DonasiResponse(
                donasi.getIdDonasi(),
                donasi.getUser().getIdUser(),
                donasi.getTanggal(),
                donasi.getStatus().name(),
                donasi.getTotalBeratGram(),
                donasi.getTotalPoinEstimasi(),
                donasi.getPengiriman() != null ? donasi.getPengiriman().getKodeKurir() : null,
                donasi.getPengiriman() != null ? donasi.getPengiriman().getBiteshipOrderId() : null,
                donasi.getPengiriman() != null ? donasi.getPengiriman().getNoResi() : null,
                donasi.getVerifikasiPengemasan() != null
                        ? donasi.getVerifikasiPengemasan().getHasilVerifikasi().name()
                        : null
        );
    }
}
