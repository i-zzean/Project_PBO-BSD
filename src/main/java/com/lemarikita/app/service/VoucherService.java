package com.lemarikita.app.service;

import com.lemarikita.app.model.*;
import com.lemarikita.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherDonaturRepository voucherDonaturRepository;
    private final UserRepository userRepository;
    private final ProfilRepository profilRepository;

    public VoucherService(VoucherRepository voucherRepository,
                          VoucherDonaturRepository voucherDonaturRepository,
                          UserRepository userRepository,
                          ProfilRepository profilRepository) {
        this.voucherRepository = voucherRepository;
        this.voucherDonaturRepository = voucherDonaturRepository;
        this.userRepository = userRepository;
        this.profilRepository = profilRepository;
    }

    @Transactional(readOnly = true)
    public List<Voucher> ambilVoucherAktif() {
        return voucherRepository.findByMasaBerlakuGreaterThanEqual(LocalDate.now());
    }

    @Transactional
    public VoucherDonatur tukarPoinDenganVoucher(Long idUser, Long idVoucher) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan."));
        Voucher voucher = voucherRepository.findById(idVoucher)
                .orElseThrow(() -> new IllegalArgumentException("Voucher tidak ditemukan."));

        // 1. Validasi masa berlaku voucher
        if (voucher.getMasaBerlaku().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Masa berlaku voucher sudah habis.");
        }

        // 2. Validasi sisa kuota penukaran voucher
        long jumlahSudahDitukar = voucherDonaturRepository.countByVoucher(voucher);
        if (jumlahSudahDitukar >= voucher.getBatasPenggunaan()) {
            throw new IllegalStateException("Kuota penukaran voucher ini sudah habis.");
        }

        // 3. Validasi kecukupan poin donatur
        Profil profil = user.getProfil();
        if (profil.getTotalPoin() < voucher.getPoinDibutuhkan()) {
            throw new IllegalStateException("Poin Anda tidak mencukupi untuk menukar voucher ini.");
        }

        // 4. Potong poin tanpa menurunkan level yang sudah dicapai user
        profil.setTotalPoin(profil.getTotalPoin() - voucher.getPoinDibutuhkan());
        profilRepository.save(profil);

        // 5. Terbitkan kode voucher unik untuk donatur
        String kodeUnik = "VCR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        VoucherDonatur voucherDonatur = new VoucherDonatur();
        voucherDonatur.setUser(user);
        voucherDonatur.setVoucher(voucher);
        voucherDonatur.setKodeVoucher(kodeUnik);
        voucherDonatur.setStatusPenggunaan(StatusVoucher.TERSEDIA);
        voucherDonatur.setTanggalPenukaran(LocalDateTime.now());

        return voucherDonaturRepository.save(voucherDonatur);
    }

    @Transactional(readOnly = true)
    public List<VoucherDonatur> ambilVoucherMilikUser(Long idUser) {
        return voucherDonaturRepository.findByUserIdUserOrderByTanggalPenukaranDesc(idUser);
    }
}
