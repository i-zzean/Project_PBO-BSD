package com.lemarikita.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "riwayat")
public class Riwayat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_riwayat")
    private Long idRiwayat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pengiriman", nullable = false)
    private Pengiriman pengiriman;

    @Column(name = "tanggal", nullable = false)
    private LocalDateTime tanggal = LocalDateTime.now();

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

    public Long getIdRiwayat() { return idRiwayat; }
    public void setIdRiwayat(Long idRiwayat) { this.idRiwayat = idRiwayat; }

    public Pengiriman getPengiriman() { return pengiriman; }
    public void setPengiriman(Pengiriman pengiriman) { this.pengiriman = pengiriman; }

    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}
