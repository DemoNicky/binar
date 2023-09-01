package org.example.Challenge_2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
