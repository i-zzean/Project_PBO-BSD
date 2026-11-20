package com.lemarikita.app.repository;

import com.lemarikita.app.model.Donasi;
import com.lemarikita.app.model.JenisReward;
import com.lemarikita.app.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByUserIdUserOrderByTanggalDesc(Long idUser);

    // Mencegah pemberian poin ganda jika webhook Biteship terkirim lebih dari sekali
    boolean existsByDonasiAndJenisReward(Donasi donasi, JenisReward jenisReward);
}
