package com.lemarikita.app.service;

import com.lemarikita.app.dto.BuatDonasiRequest;
import com.lemarikita.app.dto.ItemDonasiRequest;
import com.lemarikita.app.model.*;
import com.lemarikita.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonasiService {

    private static final int BONUS_POIN_PACKING_RAPI = 25;

    private final DonasiRepository donasiRepository;
    private final UserRepository userRepository;
    private final ProfilRepository profilRepository;
    private final VerifikasiPengemasanRepository verifikasiRepository;
    private final RewardRepository rewardRepository;
    private final BiteshipService biteshipService;

    public DonasiService(DonasiRepository donasiRepository,
                         UserRepository userRepository,
                         ProfilRepository profilRepository,
                         VerifikasiPengemasanRepository verifikasiRepository,
                         RewardRepository rewardRepository,
                         BiteshipService biteshipService) {
        this.donasiRepository = donasiRepository;
        this.userRepository = userRepository;
        this.profilRepository = profilRepository;
        this.verifikasiRepository = verifikasiRepository;
        this.rewardRepository = rewardRepository;
        this.biteshipService = biteshipService;
    }

    @Transactional
    public Donasi buatDonasi(BuatDonasiRequest request) {
        if (!request.checklistTerkonfirmasi()) {
            throw new IllegalStateException("Donatur wajib menyetujui checklist standar pengemasan terlebih dahulu.");
        }
        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Keranjang donasi tidak boleh kosong.");
        }

        User donatur = userRepository.findById(request.idUser())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan."));

        // 1. Inisiasi entitas Donasi dan hitung otomatis berat & estimasi poin
        Donasi donasi = new Donasi();
        donasi.setUser(donatur);
        donasi.setStatus(StatusDonasi.MENUNGGU_PENJEMPUTAN);

        for (ItemDonasiRequest itemReq : request.items()) {
            DetailDonasi detail = new DetailDonasi();
            detail.setJenisPakaian(itemReq.jenisPakaian());
            detail.setJumlah(itemReq.jumlah());
            detail.setKondisi(itemReq.kondisi());
            donasi.tambahDetail(detail);
        }

        // 2. Panggil API Biteship Sandbox secara otomatis untuk membuat order & nomor resi
        BiteshipService.HasilOrderBiteship hasilOrder = biteshipService.buatOrderPenjemputan(
                donatur,
                donasi,
                request.kodeKurir(),
                request.kodeLayananKurir(),
                request.alamatPengirim(),
                request.kodePosPengirim(),
                request.alamatPenerima(),
                request.kodePosPenerima()
        );

        // 3. Simpan bukti packing mandiri donatur
        VerifikasiPengemasan verifikasi = new VerifikasiPengemasan();
        verifikasi.setDonasi(donasi);
        verifikasi.setChecklistTerkonfirmasi(true);
        verifikasi.setFotoKemasan(request.fotoKemasan());
        verifikasi.setHasilVerifikasi(HasilVerifikasi.MENUNGGU_REVIEW);
        donasi.setVerifikasiPengemasan(verifikasi);

        // 4. Simpan data logistik & tracking hasil respons asli Biteship
        Pengiriman pengiriman = new Pengiriman();
        pengiriman.setDonasi(donasi);
        pengiriman.setKodeKurir(request.kodeKurir());
        pengiriman.setAlamatPengirim(request.alamatPengirim());
        pengiriman.setKodePosPengirim(request.kodePosPengirim());
        pengiriman.setAlamatPenerima(request.alamatPenerima());
        pengiriman.setKodePosPenerima(request.kodePosPenerima());
        pengiriman.setOngkosKirim(hasilOrder.ongkosKirim());
        pengiriman.setBiteshipOrderId(hasilOrder.orderId());
        pengiriman.setNoResi(hasilOrder.waybillId());

        Riwayat riwayatAwal = new Riwayat();
        riwayatAwal.setPengiriman(pengiriman);
        riwayatAwal.setStatus("ORDER_CREATED");
        riwayatAwal.setKeterangan("Pesanan penjemputan berhasil dibuat di Biteship.");
        pengiriman.getDaftarRiwayat().add(riwayatAwal);

        donasi.setPengiriman(pengiriman);

        return donasiRepository.save(donasi);
    }

    @Transactional
    public VerifikasiPengemasan verifikasiKemasanOlehAdmin(Long idDonasi, Long idAdmin, boolean valid, String catatan) {
        User admin = userRepository.findById(idAdmin)
                .orElseThrow(() -> new IllegalArgumentException("Admin tidak ditemukan."));
        if (admin.getPeran() != PeranUser.ADMIN) {
            throw new IllegalStateException("Hanya akun dengan peran ADMIN yang dapat memverifikasi kemasan.");
        }

        VerifikasiPengemasan verifikasi = verifikasiRepository.findByDonasiIdDonasi(idDonasi)
                .orElseThrow(() -> new IllegalArgumentException("Data verifikasi pengemasan tidak ditemukan."));

        verifikasi.setAdmin(admin);
        verifikasi.setCatatan(catatan);
        verifikasi.setTanggal(LocalDateTime.now());
        verifikasi.setHasilVerifikasi(valid ? HasilVerifikasi.VALID : HasilVerifikasi.DITOLAK);

        Donasi donasi = verifikasi.getDonasi();
        if (valid && !rewardRepository.existsByDonasiAndJenisReward(donasi, JenisReward.BONUS_PACKING_RAPI)) {
            User donatur = donasi.getUser();

            Reward bonus = new Reward();
            bonus.setUser(donatur);
            bonus.setDonasi(donasi);
            bonus.setPoin(BONUS_POIN_PACKING_RAPI);
            bonus.setJenisReward(JenisReward.BONUS_PACKING_RAPI);
            rewardRepository.save(bonus);

            Profil profil = donatur.getProfil();
            profil.tambahPoin(BONUS_POIN_PACKING_RAPI);
            profilRepository.save(profil);
        }

        return verifikasiRepository.save(verifikasi);
    }

    @Transactional(readOnly = true)
    public List<Donasi> ambilRiwayatDonasiUser(Long idUser) {
        return donasiRepository.findByUserIdUserOrderByTanggalDesc(idUser);
    }
}
