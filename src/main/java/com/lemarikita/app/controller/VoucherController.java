package com.lemarikita.app.controller;

import com.lemarikita.app.model.Voucher;
import com.lemarikita.app.model.VoucherDonatur;
import com.lemarikita.app.service.VoucherService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping
    public ResponseEntity<List<Voucher>> daftarVoucherAktif() {
        return ResponseEntity.ok(voucherService.ambilVoucherAktif());
    }

    @PostMapping("/tukar")
    public ResponseEntity<Object> tukarVoucher(
            @RequestParam Long idUser,
            @RequestParam Long idVoucher) {
        VoucherDonatur hasil = voucherService.tukarPoinDenganVoucher(idUser, idVoucher);
        return ResponseEntity.ok(Map.of(
                "kodeVoucher", hasil.getKodeVoucher(),
                "statusPenggunaan", hasil.getStatusPenggunaan().name(),
                "tanggalPenukaran", hasil.getTanggalPenukaran()
        ));
    }

    @GetMapping("/user/{idUser}")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> voucherMilikUser(@PathVariable Long idUser) {
        var daftar = voucherService.ambilVoucherMilikUser(idUser).stream()
                .map(vd -> Map.of(
                        "kodeVoucher", vd.getKodeVoucher(),
                        "namaMitra", vd.getVoucher().getNamaMitra(),
                        "jenisVoucher", vd.getVoucher().getJenisVoucher(),
                        "statusPenggunaan", vd.getStatusPenggunaan().name()
                ))
                .toList();
        return ResponseEntity.ok(daftar);
    }
}
