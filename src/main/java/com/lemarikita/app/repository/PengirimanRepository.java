package com.lemarikita.app.repository;

import com.lemarikita.app.model.Pengiriman;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PengirimanRepository extends JpaRepository<Pengiriman, Long> {
    Optional<Pengiriman> findByBiteshipOrderId(String biteshipOrderId);
    Optional<Pengiriman> findByNoResi(String noResi);
    Optional<Pengiriman> findByDonasiIdDonasi(Long idDonasi);
}
