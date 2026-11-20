package com.lemarikita.app.controller;

import com.lemarikita.app.dto.BuatDonasiRequest;
import com.lemarikita.app.dto.DonasiResponse;
import com.lemarikita.app.model.Donasi;
import com.lemarikita.app.model.VerifikasiPengemasan;
import com.lemarikita.app.service.BiteshipService;
import com.lemarikita.app.service.DonasiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donasi")
public class DonasiController {

    private final DonasiService donasiService;
    private final BiteshipService biteshipService;

    public DonasiController(DonasiService donasiService, BiteshipService biteshipService) {
        this.donasiService = donasiService;
        this.biteshipService = biteshipService;
    }

    @GetMapping("/cek-ongkir")
    public ResponseEntity<Object> cekOngkir(
            @RequestParam String asal,
            @RequestParam String tujuan,
            @RequestParam int beratGram) {
        return ResponseEntity.ok(biteshipService.cekOngkosKirim(asal, tujuan, beratGram));
    }

    @PostMapping
    public ResponseEntity<DonasiResponse> buatDonasi(@RequestBody BuatDonasiRequest request) {
        Donasi donasi = donasiService.buatDonasi(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(DonasiResponse.fromEntity(donasi));
    }

    @GetMapping("/user/{idUser}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<DonasiResponse>> riwayatDonasiUser(@PathVariable Long idUser) {
        List<DonasiResponse> daftar = donasiService.ambilRiwayatDonasiUser(idUser).stream()
                .map(DonasiResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(daftar);
    }

    @PatchMapping("/{idDonasi}/verifikasi")
    public ResponseEntity<Object> verifikasiKemasan(
            @PathVariable Long idDonasi,
            @RequestParam Long idAdmin,
            @RequestParam boolean valid,
            @RequestParam(defaultValue = "") String catatan) {
        VerifikasiPengemasan hasil = donasiService.verifikasiKemasanOlehAdmin(idDonasi, idAdmin, valid, catatan);
        return ResponseEntity.ok(Map.of(
                "idVerifikasi", hasil.getIdVerifikasi(),
                "hasilVerifikasi", hasil.getHasilVerifikasi().name(),
                "catatan", hasil.getCatatan() != null ? hasil.getCatatan() : ""
        ));
    }
}
