package com.lemarikita.app.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "nama", nullable = false, length = 100)
    private String nama;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "no_telp", nullable = false, length = 20)
    private String noTelp;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "peran", nullable = false, length = 20)
    private PeranUser peran = PeranUser.DONATUR;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profil profil;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Donasi> daftarDonasi = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reward> daftarReward = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<VoucherDonatur> daftarVoucherDonatur = new ArrayList<>();

    public Long getIdUser() { return idUser; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public PeranUser getPeran() { return peran; }
    public void setPeran(PeranUser peran) { this.peran = peran; }

    public Profil getProfil() { return profil; }
    public void setProfil(Profil profil) { this.profil = profil; }

    public List<Donasi> getDaftarDonasi() { return daftarDonasi; }
    public void setDaftarDonasi(List<Donasi> daftarDonasi) { this.daftarDonasi = daftarDonasi; }

    public List<Reward> getDaftarReward() { return daftarReward; }
    public void setDaftarReward(List<Reward> daftarReward) { this.daftarReward = daftarReward; }

    public List<VoucherDonatur> getDaftarVoucherDonatur() { return daftarVoucherDonatur; }
    public void setDaftarVoucherDonatur(List<VoucherDonatur> daftarVoucherDonatur) { this.daftarVoucherDonatur = daftarVoucherDonatur; }
}
