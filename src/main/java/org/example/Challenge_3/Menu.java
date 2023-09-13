package org.example.Challenge_3;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    private String nama;
    private int harga;
    private int jumlahPesanan;

    public Menu(String nama, int harga) {
        this.nama = nama;
        this.harga = harga;
        this.jumlahPesanan = 0;
    }

    public String toString() {
        return nama + "\t\t| " + harga;
    }
}