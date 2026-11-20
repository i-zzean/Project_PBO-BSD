package com.lemarikita.app.dto;

import com.lemarikita.app.model.User;

public record UserResponse(
        Long idUser,
        String nama,
        String email,
        String noTelp,
        String peran,
        String username,
        Integer level,
        Integer totalPoin
) {
    public static UserResponse fromEntity(User user) {
        boolean punyaProfil = user.getProfil() != null;
        return new UserResponse(
                user.getIdUser(),
                user.getNama(),
                user.getEmail(),
                user.getNoTelp(),
                user.getPeran().name(),
                punyaProfil ? user.getProfil().getUsername() : user.getNama(),
                punyaProfil ? user.getProfil().getLevel() : 0,
                punyaProfil ? user.getProfil().getTotalPoin() : 0
        );
    }
}
