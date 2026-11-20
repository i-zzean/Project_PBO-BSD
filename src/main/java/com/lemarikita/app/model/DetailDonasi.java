package com.lemarikita.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detail_donasi")
public class DetailDonasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detail")
    private Long idDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_donasi", nullable = false)
    private Donasi donasi;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_pakaian", nullable = false, length = 30)
    private JenisPakaian jenisPakaian;

    @Column(name = "jumlah", nullable = false)
    private Integer jumlah;

    @Column(name = "kondisi", nullable = false, length = 50)
    private String kondisi;

    @Column(name = "subtotal_berat_gram", nullable = false)
    private Integer subtotalBeratGram;

    @PrePersist
    @PreUpdate
    public void hitungSubtotalBerat() {
        if (this.jenisPakaian != null && this.jumlah != null) {
            this.subtotalBeratGram = this.jenisPakaian.getBeratSatuanGram() * this.jumlah;
        }
    }

    public Long getIdDetail() { return idDetail; }
    public void setIdDetail(Long idDetail) { this.idDetail = idDetail; }

    public Donasi getDonasi() { return donasi; }
    public void setDonasi(Donasi donasi) { this.donasi = donasi; }

    public JenisPakaian getJenisPakaian() { return jenisPakaian; }
    public void setJenisPakaian(JenisPakaian jenisPakaian) {
        this.jenisPakaian = jenisPakaian;
        hitungSubtotalBerat();
    }

    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
        hitungSubtotalBerat();
    }

    public String getKondisi() { return kondisi; }
    public void setKondisi(String kondisi) { this.kondisi = kondisi; }

    public Integer getSubtotalBeratGram() { return subtotalBeratGram; }
    public void setSubtotalBeratGram(Integer subtotalBeratGram) { this.subtotalBeratGram = subtotalBeratGram; }
}
