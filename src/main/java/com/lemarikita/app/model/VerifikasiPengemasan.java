package com.lemarikita.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verifikasi_pengemasan")
public class VerifikasiPengemasan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_verifikasi")
    private Long idVerifikasi;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_donasi", nullable = false, unique = true)
    private Donasi donasi;

    // Nullable saat donatur baru mengunggah bukti; diisi ketika admin memverifikasi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin")
    private User admin;

    @Column(name = "checklist_terkonfirmasi", nullable = false)
    private Boolean checklistTerkonfirmasi = false;

    @Column(name = "foto_kemasan", columnDefinition = "TEXT")
    private String fotoKemasan;

    @Enumerated(EnumType.STRING)
    @Column(name = "hasil_verifikasi", nullable = false, length = 30)
    private HasilVerifikasi hasilVerifikasi = HasilVerifikasi.MENUNGGU_REVIEW;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;

    @Column(name = "tanggal", nullable = false)
    private LocalDateTime tanggal = LocalDateTime.now();

    public Long getIdVerifikasi() { return idVerifikasi; }
    public void setIdVerifikasi(Long idVerifikasi) { this.idVerifikasi = idVerifikasi; }

    public Donasi getDonasi() { return donasi; }
    public void setDonasi(Donasi donasi) { this.donasi = donasi; }

    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }

    public Boolean getChecklistTerkonfirmasi() { return checklistTerkonfirmasi; }
    public void setChecklistTerkonfirmasi(Boolean checklistTerkonfirmasi) { this.checklistTerkonfirmasi = checklistTerkonfirmasi; }

    public String getFotoKemasan() { return fotoKemasan; }
    public void setFotoKemasan(String fotoKemasan) { this.fotoKemasan = fotoKemasan; }

    public HasilVerifikasi getHasilVerifikasi() { return hasilVerifikasi; }
    public void setHasilVerifikasi(HasilVerifikasi hasilVerifikasi) { this.hasilVerifikasi = hasilVerifikasi; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }
}
