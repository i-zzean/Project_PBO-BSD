package com.lemarikita.app.model;

public enum JenisPakaian {
    KAOS(200, 10),
    KEMEJA(250, 15),
    CELANA_JEANS(600, 25),
    JAKET(800, 35),
    GAUN(400, 20),
    LAINNYA(300, 10);

    private final int beratSatuanGram;
    private final int poinSatuan;

    JenisPakaian(int beratSatuanGram, int poinSatuan) {
        this.beratSatuanGram = beratSatuanGram;
        this.poinSatuan = poinSatuan;
    }

    public int getBeratSatuanGram() {
        return beratSatuanGram;
    }

    public int getPoinSatuan() {
        return poinSatuan;
    }
}
