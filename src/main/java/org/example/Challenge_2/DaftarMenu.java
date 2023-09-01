package org.example.Challenge_2;

import java.util.ArrayList;

public class DaftarMenu {
    private ArrayList<Menu> daftarMenu;
    int jumlahMenu;

    public DaftarMenu() {
        this.jumlahMenu = 5;
        this.daftarMenu = new ArrayList<Menu>();

        daftarMenu.add(new Menu("Nasi Goreng", 15000));
        daftarMenu.add(new Menu("Mie Goreng", 13000));
        daftarMenu.add(new Menu("Nasi + Ayam", 18000));
        daftarMenu.add(new Menu("Es Teh Manis", 3000));
        daftarMenu.add(new Menu("Es Jeruk", 5000));
    }

    public void tampilkanDaftarMenu() {
        System.out.println("========================");
        System.out.println("Selamat datang di BinarFud");
        System.out.println("========================");
        System.out.println("Silahkan pilih makanan :");

        int i = 1;
        for (int j = 0; j < jumlahMenu; j++) {
            Menu menu = daftarMenu.get(j);
            System.out.println(i + ". " + menu.getNama() + "\t\t| " + menu.getHarga());
            i++;
        }

        System.out.println("99. Pesan Dan Bayar");
        System.out.println("0. Keluar Aplikasi");
    }

    public Menu getMenu(int i){
        return daftarMenu.get(i);
    }
}
