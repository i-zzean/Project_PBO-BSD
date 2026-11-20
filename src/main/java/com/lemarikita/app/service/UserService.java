package com.lemarikita.app.service;

import com.lemarikita.app.model.PeranUser;
import com.lemarikita.app.model.Profil;
import com.lemarikita.app.model.User;
import com.lemarikita.app.repository.ProfilRepository;
import com.lemarikita.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfilRepository profilRepository;

    public UserService(UserRepository userRepository, ProfilRepository profilRepository) {
        this.userRepository = userRepository;
        this.profilRepository = profilRepository;
    }

    @Transactional
    public User registrasiDonatur(String nama, String email, String noTelp, String password, String username) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email sudah terdaftar.");
        }
        if (profilRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username sudah digunakan.");
        }

        User user = new User();
        user.setNama(nama);
        user.setEmail(email);
        user.setNoTelp(noTelp);
        user.setPassword(password);
        user.setPeran(PeranUser.DONATUR);

        Profil profil = new Profil();
        profil.setUsername(username);
        profil.setLevel(1);
        profil.setTotalPoin(0);
        profil.setUser(user);
        user.setProfil(profil);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email atau password salah."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Email atau password salah.");
        }

        // Memastikan data profil termuat sebelum sesi transaksi berakhir
        if (user.getProfil() != null) {
            user.getProfil().getUsername();
        }

        return user;
    }

    @Transactional(readOnly = true)
    public List<Profil> ambilLeaderboard() {
        return profilRepository.findTop10ByOrderByTotalPoinDesc();
    }
}
