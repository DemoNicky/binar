package org.example.Challenge_2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pesanan {
    private int jumlahPesanan;
    private int totalHarga;

    public Pesanan() {
        this.jumlahPesanan = 0;
        this.totalHarga = 0;
    }

    public void tambahPesanan(int jumlah, int harga) {
        jumlahPesanan += jumlah;
        totalHarga += (jumlah * harga);
    }
}

