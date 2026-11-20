package com.lemarikita.app.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pengiriman")
public class Pengiriman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pengiriman")
    private Long idPengiriman;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_donasi", nullable = false, unique = true)
    private Donasi donasi;

    @Column(name = "biteship_order_id", unique = true, length = 100)
    private String biteshipOrderId;

    @Column(name = "no_resi", length = 100)
    private String noResi;

    @Column(name = "kode_kurir", nullable = false, length = 30)
    private String kodeKurir;

    @Column(name = "alamat_pengirim", nullable = false, columnDefinition = "TEXT")
    private String alamatPengirim;

    @Column(name = "kode_pos_pengirim", nullable = false, length = 10)
    private String kodePosPengirim;

    @Column(name = "alamat_penerima", nullable = false, columnDefinition = "TEXT")
    private String alamatPenerima;

    @Column(name = "kode_pos_penerima", nullable = false, length = 10)
    private String kodePosPenerima;

    @Column(name = "ongkos_kirim", precision = 12, scale = 2)
    private BigDecimal ongkosKirim;

    @OneToMany(mappedBy = "pengiriman", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Riwayat> daftarRiwayat = new ArrayList<>();

    public Long getIdPengiriman() { return idPengiriman; }
    public void setIdPengiriman(Long idPengiriman) { this.idPengiriman = idPengiriman; }

    public Donasi getDonasi() { return donasi; }
    public void setDonasi(Donasi donasi) { this.donasi = donasi; }

    public String getBiteshipOrderId() { return biteshipOrderId; }
    public void setBiteshipOrderId(String biteshipOrderId) { this.biteshipOrderId = biteshipOrderId; }

    public String getNoResi() { return noResi; }
    public void setNoResi(String noResi) { this.noResi = noResi; }

    public String getKodeKurir() { return kodeKurir; }
    public void setKodeKurir(String kodeKurir) { this.kodeKurir = kodeKurir; }

    public String getAlamatPengirim() { return alamatPengirim; }
    public void setAlamatPengirim(String alamatPengirim) { this.alamatPengirim = alamatPengirim; }

    public String getKodePosPengirim() { return kodePosPengirim; }
    public void setKodePosPengirim(String kodePosPengirim) { this.kodePosPengirim = kodePosPengirim; }

    public String getAlamatPenerima() { return alamatPenerima; }
    public void setAlamatPenerima(String alamatPenerima) { this.alamatPenerima = alamatPenerima; }

    public String getKodePosPenerima() { return kodePosPenerima; }
    public void setKodePosPenerima(String kodePosPenerima) { this.kodePosPenerima = kodePosPenerima; }

    public BigDecimal getOngkosKirim() { return ongkosKirim; }
    public void setOngkosKirim(BigDecimal ongkosKirim) { this.ongkosKirim = ongkosKirim; }

    public List<Riwayat> getDaftarRiwayat() { return daftarRiwayat; }
    public void setDaftarRiwayat(List<Riwayat> daftarRiwayat) { this.daftarRiwayat = daftarRiwayat; }
}
