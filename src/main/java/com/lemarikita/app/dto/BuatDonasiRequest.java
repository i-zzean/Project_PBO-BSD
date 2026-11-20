package com.lemarikita.app.dto;

import java.util.List;

public record BuatDonasiRequest(
        Long idUser,
        List<ItemDonasiRequest> items,
        boolean checklistTerkonfirmasi,
        String fotoKemasan,
        String kodeKurir,
        String kodeLayananKurir,
        String alamatPengirim,
        String kodePosPengirim,
        String alamatPenerima,
        String kodePosPenerima
) {}
