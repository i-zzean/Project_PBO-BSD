package com.lemarikita.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profil")
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profil")
    private Long idProfil;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Column(name = "total_poin", nullable = false)
    private Integer totalPoin = 0;

    public void tambahPoin(int poinBaru) {
        this.totalPoin += poinBaru;
        this.level = (this.totalPoin / 500) + 1;
    }

    public Long getIdProfil() { return idProfil; }
    public void setIdProfil(Long idProfil) { this.idProfil = idProfil; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getTotalPoin() { return totalPoin; }
    public void setTotalPoin(Integer totalPoin) { this.totalPoin = totalPoin; }
}
