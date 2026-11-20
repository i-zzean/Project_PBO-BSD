package com.lemarikita.app.repository;

import com.lemarikita.app.model.HasilVerifikasi;
import com.lemarikita.app.model.VerifikasiPengemasan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VerifikasiPengemasanRepository extends JpaRepository<VerifikasiPengemasan, Long> {
    Optional<VerifikasiPengemasan> findByDonasiIdDonasi(Long idDonasi);
    List<VerifikasiPengemasan> findByHasilVerifikasi(HasilVerifikasi hasilVerifikasi);
}
