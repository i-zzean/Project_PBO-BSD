package com.lemarikita.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucher_donatur")
public class VoucherDonatur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voucher_donatur")
    private Long idVoucherDonatur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voucher", nullable = false)
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(name = "kode_voucher", nullable = false, unique = true, length = 50)
    private String kodeVoucher;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_penggunaan", nullable = false, length = 30)
    private StatusVoucher statusPenggunaan = StatusVoucher.TERSEDIA;

    @Column(name = "tanggal_penukaran", nullable = false)
    private LocalDateTime tanggalPenukaran = LocalDateTime.now();

    public Long getIdVoucherDonatur() { return idVoucherDonatur; }
    public void setIdVoucherDonatur(Long idVoucherDonatur) { this.idVoucherDonatur = idVoucherDonatur; }

    public Voucher getVoucher() { return voucher; }
    public void setVoucher(Voucher voucher) { this.voucher = voucher; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getKodeVoucher() { return kodeVoucher; }
    public void setKodeVoucher(String kodeVoucher) { this.kodeVoucher = kodeVoucher; }

    public StatusVoucher getStatusPenggunaan() { return statusPenggunaan; }
    public void setStatusPenggunaan(StatusVoucher statusPenggunaan) { this.statusPenggunaan = statusPenggunaan; }

    public LocalDateTime getTanggalPenukaran() { return tanggalPenukaran; }
    public void setTanggalPenukaran(LocalDateTime tanggalPenukaran) { this.tanggalPenukaran = tanggalPenukaran; }
}
