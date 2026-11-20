package com.lemarikita.app.repository;

import com.lemarikita.app.model.DetailDonasi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetailDonasiRepository extends JpaRepository<DetailDonasi, Long> {
    List<DetailDonasi> findByDonasiIdDonasi(Long idDonasi);
}
