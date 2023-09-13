package org.example.Challenge_3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaftarMenu {
    private List<Menu> daftarMenu;

    public DaftarMenu() {
        this.daftarMenu = new ArrayList<>();
        this.daftarMenu.add(new Menu("Nasi Goreng", 15000));
        this.daftarMenu.add(new Menu("Mie Goreng", 13000));
        this.daftarMenu.add(new Menu("Nasi + Ayam", 18000));
        this.daftarMenu.add(new Menu("Es Teh Manis", 3000));
        this.daftarMenu.add(new Menu("Es Jeruk", 5000));
    }

    public void tampilkanDaftarMenu() {
        System.out.println("========================");
        System.out.println("Selamat datang di BinarFud");
        System.out.println("========================");
        System.out.println("Silahkan pilih makanan :");
        daftarMenu.forEach(menu -> System.out.println((daftarMenu.indexOf(menu) + 1) + ". " + menu.toString()));
        System.out.println("99. Pesan Dan Bayar");
        System.out.println("0. Keluar Aplikasi");
        System.out.print("Pilih: ");
    }

    public Optional<Menu> getMenu(int i) {
        try {
            if (i < 1 || i > daftarMenu.size()) {
                throw new IndexOutOfBoundsException("Indeks menu tidak valid!");
            }
            return Optional.of(daftarMenu.get(i - 1));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Menu> getDaftarMenu() {
        return daftarMenu;
    }
}
