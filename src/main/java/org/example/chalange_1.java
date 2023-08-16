package org.example;

import java.util.Scanner;

public class chalange_1 {

    public static Scanner scanner = new Scanner(System.in);
    public static String[] menu = {"Nasi Goreng", "Mie Goreng", "Nasi + Ayam", "Es Teh Manis", "Es Jeruk"};
    public static int[] hargaMenu = {15000, 13000, 18000, 3000, 5000};
    public static int[] jumlahOrder = new int[menu.length];

    public static int total = 0;


    public static void main(String[] args) {
        mainMenu();
    }

    public static void mainMenu(){
        System.out.println("================================");
        System.out.println("Selamat datang di BinarFud");
        System.out.println("================================");
        System.out.println("Silahkan pilih makanan :");
        System.out.println("1. Nasi Goreng\t\t| 15.000");
        System.out.println("2. Mie Goreng\t\t| 13.000");
        System.out.println("3. Nasi + Ayam\t\t| 18.000");
        System.out.println("4. Es Teh Manis\t\t| 3.000");
        System.out.println("5. Es Jeruk \t\t| 5.000");
        System.out.println("99. Pesan Dan Bayar");
        System.out.println("0. Keluar Aplikasi");
        System.out.print("pilih : ");
        int pilihan = scanner.nextInt();
        qtyPesanan(pilihan);

    }

    public static void qtyPesanan(Integer pilih){
        int x = pilih - 1;
        if (pilih == 99){
            konfAndPayment();
        } else if (pilih == 0) {
            System.out.println("Terima kasih!");
            System.exit(0);
        } else if (pilih >= 1 && pilih <= menu.length) {
            System.out.println("================================");
            System.out.println("Berapa pesanan anda");
            System.out.println("================================");
            System.out.println(menu[x] + "\t\t| " + hargaMenu[x]);
            System.out.print("qty: ");
            int jumlah = scanner.nextInt();
            if (jumlah > 0){
                jumlahOrder[x] += jumlah;
                mainMenu();
            }else {
                System.out.println("Pilihan Tidak Valid!!!");
            }
        }
    }

    public static void konfAndPayment(){
        System.out.println("========================");
        System.out.println("Konfirmasi & pembayaran");
        System.out.println("========================");

        for (int i = 0; i < menu.length; i++) {
            if (jumlahOrder[i] > 0){
                int y = hargaMenu[i] * jumlahOrder[i];
                System.out.println(menu[i] + "\t" + jumlahOrder[i] + "\tRp. " + y);
                total += y;
            }
        }
        System.out.println("------------------------+");
        System.out.println("Total \t\tRp " + total);
        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali Ke menu utama");
        System.out.println("0. Keluar aplikasi");
        System.out.print("Pilih: ");
        int q = scanner.nextInt();
        if (q == 1){
            cetakStrukTransaksi();
        } else if (q == 2) {
            mainMenu();
        } else if (q == 0) {
            System.exit(0);
        }
    }

    public static void cetakStrukTransaksi(){
        System.out.println("========================");
        System.out.println("Struk BinarFud");
        System.out.println("========================");
        System.out.println("Terima kasih sudah memesan di Binarfud");
        System.out.println("Dibawah ini adalah pesanan anda\n");
        for (int i = 0; i < menu.length; i++) {
            if (jumlahOrder[i] > 0){
                int y = hargaMenu[i] * jumlahOrder[i];
                System.out.println(menu[i] + "\t" + jumlahOrder[i] + "\tRp. " + y);
            }
        }
        System.out.println("------------------------+");
        System.out.println("Total \t\tRp " + total);
        System.out.println("\nPembayaran : BinarCash");
        System.out.println("========================");
        System.out.println("Simpan struk ini sebagai\nbukti pembayaran");
    }
}
