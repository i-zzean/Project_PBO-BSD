package com.lemarikita.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "donasi")
public class Donasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_donasi")
    private Long idDonasi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(name = "tanggal", nullable = false)
    private LocalDateTime tanggal = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusDonasi status = StatusDonasi.MENUNGGU_PENJEMPUTAN;

    @Column(name = "total_berat_gram", nullable = false)
    private Integer totalBeratGram = 0;

    @Column(name = "total_poin_estimasi", nullable = false)
    private Integer totalPoinEstimasi = 0;

    @OneToMany(mappedBy = "donasi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailDonasi> daftarDetail = new ArrayList<>();

    @OneToOne(mappedBy = "donasi", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pengiriman pengiriman;

    @OneToOne(mappedBy = "donasi", cascade = CascadeType.ALL, orphanRemoval = true)
    private VerifikasiPengemasan verifikasiPengemasan;

    public void tambahDetail(DetailDonasi detail) {
        daftarDetail.add(detail);
        detail.setDonasi(this);
        hitungUlangTotal();
    }

    public void hitungUlangTotal() {
        this.totalBeratGram = daftarDetail.stream()
                .mapToInt(DetailDonasi::getSubtotalBeratGram)
                .sum();

        this.totalPoinEstimasi = daftarDetail.stream()
                .mapToInt(d -> d.getJenisPakaian().getPoinSatuan() * d.getJumlah())
                .sum();
    }

    public Long getIdDonasi() { return idDonasi; }
    public void setIdDonasi(Long idDonasi) { this.idDonasi = idDonasi; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }

    public StatusDonasi getStatus() { return status; }
    public void setStatus(StatusDonasi status) { this.status = status; }

    public Integer getTotalBeratGram() { return totalBeratGram; }
    public void setTotalBeratGram(Integer totalBeratGram) { this.totalBeratGram = totalBeratGram; }

    public Integer getTotalPoinEstimasi() { return totalPoinEstimasi; }
    public void setTotalPoinEstimasi(Integer totalPoinEstimasi) { this.totalPoinEstimasi = totalPoinEstimasi; }

    public List<DetailDonasi> getDaftarDetail() { return daftarDetail; }
    public void setDaftarDetail(List<DetailDonasi> daftarDetail) { this.daftarDetail = daftarDetail; }

    public Pengiriman getPengiriman() { return pengiriman; }
    public void setPengiriman(Pengiriman pengiriman) { this.pengiriman = pengiriman; }

    public VerifikasiPengemasan getVerifikasiPengemasan() { return verifikasiPengemasan; }
    public void setVerifikasiPengemasan(VerifikasiPengemasan verifikasiPengemasan) { this.verifikasiPengemasan = verifikasiPengemasan; }
}
