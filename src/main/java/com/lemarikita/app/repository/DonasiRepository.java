package com.lemarikita.app.repository;

import com.lemarikita.app.model.Donasi;
import com.lemarikita.app.model.StatusDonasi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonasiRepository extends JpaRepository<Donasi, Long> {
    List<Donasi> findByUserIdUserOrderByTanggalDesc(Long idUser);
    List<Donasi> findByStatus(StatusDonasi status);
}
