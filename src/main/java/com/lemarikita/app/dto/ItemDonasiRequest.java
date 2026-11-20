package com.lemarikita.app.dto;

import com.lemarikita.app.model.JenisPakaian;

public record ItemDonasiRequest(
        JenisPakaian jenisPakaian,
        Integer jumlah,
        String kondisi
) {}
