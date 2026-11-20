package com.lemarikita.app.repository;

import com.lemarikita.app.model.Riwayat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RiwayatRepository extends JpaRepository<Riwayat, Long> {
    List<Riwayat> findByPengirimanIdPengirimanOrderByTanggalAsc(Long idPengiriman);
}
