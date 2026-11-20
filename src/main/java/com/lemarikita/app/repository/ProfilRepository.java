package com.lemarikita.app.repository;

import com.lemarikita.app.model.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProfilRepository extends JpaRepository<Profil, Long> {
    Optional<Profil> findByUserIdUser(Long idUser);
    boolean existsByUsername(String username);

    // Mengambil 10 donatur dengan poin tertinggi untuk fitur Leaderboard
    List<Profil> findTop10ByOrderByTotalPoinDesc();
}
