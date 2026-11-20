package com.lemarikita.app.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voucher")
    private Long idVoucher;

    @Column(name = "nama_mitra", nullable = false, length = 100)
    private String namaMitra;

    @Column(name = "jenis_voucher", nullable = false, length = 50)
    private String jenisVoucher;

    @Column(name = "nilai_potongan", nullable = false, precision = 12, scale = 2)
    private BigDecimal nilaiPotongan;

    @Column(name = "masa_berlaku", nullable = false)
    private LocalDate masaBerlaku;

    @Column(name = "batas_penggunaan", nullable = false)
    private Integer batasPenggunaan;

    @Column(name = "syarat_ketentuan", columnDefinition = "TEXT")
    private String syaratKetentuan;

    @Column(name = "poin_dibutuhkan", nullable = false)
    private Integer poinDibutuhkan;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    private List<VoucherDonatur> daftarVoucherDonatur = new ArrayList<>();

    public Long getIdVoucher() { return idVoucher; }
    public void setIdVoucher(Long idVoucher) { this.idVoucher = idVoucher; }

    public String getNamaMitra() { return namaMitra; }
    public void setNamaMitra(String namaMitra) { this.namaMitra = namaMitra; }

    public String getJenisVoucher() { return jenisVoucher; }
    public void setJenisVoucher(String jenisVoucher) { this.jenisVoucher = jenisVoucher; }

    public BigDecimal getNilaiPotongan() { return nilaiPotongan; }
    public void setNilaiPotongan(BigDecimal nilaiPotongan) { this.nilaiPotongan = nilaiPotongan; }

    public LocalDate getMasaBerlaku() { return masaBerlaku; }
    public void setMasaBerlaku(LocalDate masaBerlaku) { this.masaBerlaku = masaBerlaku; }

    public Integer getBatasPenggunaan() { return batasPenggunaan; }
    public void setBatasPenggunaan(Integer batasPenggunaan) { this.batasPenggunaan = batasPenggunaan; }

    public String getSyaratKetentuan() { return syaratKetentuan; }
    public void setSyaratKetentuan(String syaratKetentuan) { this.syaratKetentuan = syaratKetentuan; }

    public Integer getPoinDibutuhkan() { return poinDibutuhkan; }
    public void setPoinDibutuhkan(Integer poinDibutuhkan) { this.poinDibutuhkan = poinDibutuhkan; }

    public List<VoucherDonatur> getDaftarVoucherDonatur() { return daftarVoucherDonatur; }
    public void setDaftarVoucherDonatur(List<VoucherDonatur> daftarVoucherDonatur) { this.daftarVoucherDonatur = daftarVoucherDonatur; }
}
