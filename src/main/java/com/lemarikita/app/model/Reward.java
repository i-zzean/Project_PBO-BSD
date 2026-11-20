package com.lemarikita.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reward")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reward")
    private Long idReward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    // Opsional untuk bonus non-donasi, tetapi mencegah pemberian poin ganda untuk donasi yang sama
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_donasi")
    private Donasi donasi;

    @Column(name = "poin", nullable = false)
    private Integer poin;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_reward", nullable = false, length = 30)
    private JenisReward jenisReward;

    @Column(name = "tanggal", nullable = false)
    private LocalDateTime tanggal = LocalDateTime.now();

    public Long getIdReward() { return idReward; }
    public void setIdReward(Long idReward) { this.idReward = idReward; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Donasi getDonasi() { return donasi; }
    public void setDonasi(Donasi donasi) { this.donasi = donasi; }

    public Integer getPoin() { return poin; }
    public void setPoin(Integer poin) { this.poin = poin; }

    public JenisReward getJenisReward() { return jenisReward; }
    public void setJenisReward(JenisReward jenisReward) { this.jenisReward = jenisReward; }

    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }
}
