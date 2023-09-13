package org.example.Challenge_3;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pesanan {
    private List<Menu> daftarPesanan;

    public Pesanan() {
        this.daftarPesanan = new ArrayList<>();
    }

    public void tambahPesanan(Menu menu, int jumlah) {
        try {
            if (jumlah <= 0) {
                throw new IllegalArgumentException("Jumlah pesanan tidak valid!");
            }
            if (daftarPesanan.contains(menu)) {
                daftarPesanan.stream()
                        .filter(data -> data.equals(menu))
                        .findFirst()
                        .ifPresent(data -> data.setJumlahPesanan(data.getJumlahPesanan() + jumlah));
            } else {
                menu.setJumlahPesanan(jumlah);
                daftarPesanan.add(menu);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public int getTotalHarga() {
        return daftarPesanan.stream()
                .mapToInt(menu -> menu.getHarga() * menu.getJumlahPesanan())
                .sum();
    }

    public List<Menu> getDaftarPesanan() {
        return daftarPesanan;
    }
}