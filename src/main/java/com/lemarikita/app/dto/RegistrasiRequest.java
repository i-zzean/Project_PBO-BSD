package com.lemarikita.app.dto;

public record RegistrasiRequest(
        String nama,
        String email,
        String noTelp,
        String password,
        String username
) {}
