package org.example.Challenge_2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MainApp {
    private Scanner scanner;
    private DaftarMenu daftarMenu;
    private Pesanan pesanan;

    public MainApp() {
        scanner = new Scanner(System.in);
        daftarMenu = new DaftarMenu();
        pesanan = new Pesanan();
    }

    public void tampilkanMainScreen() {
        boolean exit = false;

        while (!exit) {
            daftarMenu.tampilkanDaftarMenu();
            System.out.print("Pilih: ");
            int pilihan = scanner.nextInt();

            switch (pilihan) {
                case 99:
                    pesanDanBayar();
                    break;
                case 0:
                    System.out.println("Terima kasih!");
                    exit = true;
                    break;
                default:
                    if (pilihan >= 1 && pilihan <= daftarMenu.jumlahMenu) {
                        pesanMenu(pilihan);
                    } else {
                        tampilkanKesalahan("Pilihan tidak valid");
                    }
                    break;
            }
        }
    }

    public void pesanMenu(int nomorMenu) {
        System.out.println("========================");
        System.out.println("Berapa pesanan anda");
        System.out.println("========================");

        Menu menu = daftarMenu.getMenu(nomorMenu - 1);
        System.out.println(menu.getNama() + "\t\t| " + menu.getHarga());
        System.out.print("Jumlah: ");

        int jumlah = scanner.nextInt();

        if (jumlah > 0) {
            menu.setJumlahPesanan(jumlah);
            pesanan.tambahPesanan(jumlah, menu.getHarga());
        } else {
            tampilkanKesalahan("Jumlah pesanan tidak valid");
        }
    }

    public void pesanDanBayar() {
        int jumlahPesananTotal = 0;

        System.out.println("========================");
        System.out.println("Konfirmasi dan Pembayaran");
        System.out.println("========================");

        for (int i = 0; i < daftarMenu.jumlahMenu; i++) {
            Menu menu = daftarMenu.getMenu(i);
            int jumlahPesanan = menu.getJumlahPesanan();

            if (jumlahPesanan > 0) {
                int subtotal = menu.getHarga() * jumlahPesanan;

                System.out.println(menu.getNama() + "\t" + jumlahPesanan + "\tRp. " + subtotal);
            }
            jumlahPesananTotal += jumlahPesanan;
        }
        boolean exit = false;

        while (!exit) {

            System.out.println("------------------------");
            System.out.println("Total\t\t" +jumlahPesananTotal + "\tRp. " + pesanan.getTotalHarga());
            System.out.println("1. Konfirmasi dan Bayar");
            System.out.println("2. Kembali Ke Menu Utama");
            System.out.println("0. Keluar Aplikasi");
            System.out.print("Pilih: ");

            int pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    tampilkanPesanan();
                    cetakStrukTransaksi();
                    exit = true;
                    break;
                case 2:
                    exit = true;
                    break;
                case 0:
                    System.out.println("Terima kasih!");
                    System.exit(0);
                    break;
                default:
                    tampilkanKesalahan("Pilihan tidak valid");
                    break;
            }
        }
    }

    public void cetakStrukTransaksi() {
        int jumlahPesananTotal = 0;

        System.out.println("========================");
        System.out.println("Cetak Struk Transaksi");
        System.out.println("========================");

        try {
            FileWriter fileWriter = new FileWriter("receipt.txt");
            fileWriter.write("========================\n");
            fileWriter.write("Struk BinarFud\n");
            fileWriter.write("========================\n");

            for (int i = 0; i < daftarMenu.jumlahMenu; i++) {
                Menu menu = daftarMenu.getMenu(i);
                int jumlahPesanan = menu.getJumlahPesanan();

                if (jumlahPesanan > 0) {
                    int subtotal = menu.getHarga() * jumlahPesanan;

                    fileWriter.write(menu.getNama() + "\t" + jumlahPesanan + "\tRp. " + subtotal + "\n");
                }
                jumlahPesananTotal += jumlahPesanan;
            }

            fileWriter.write("------------------------+\n");
            fileWriter.write("Total\t\t" + jumlahPesananTotal + "\tRp " + pesanan.getTotalHarga() + "\n\n");
            fileWriter.write("Pembayaran : BinarCash\n");
            fileWriter.write("========================\n");
            fileWriter.write("Simpan struk ini sebagai\nbukti pembayaran\n");
            fileWriter.write("========================\n");
            fileWriter.close();

            System.out.println("Struk transaksi berhasil dicetak!");
        } catch (IOException e) {
            tampilkanKesalahan(e.getMessage());
        }
    }

    public void tampilkanPesanan() {
        int jumlahPesananTotal = 0;

        System.out.println("========================");
        System.out.println("Pesanan Anda");
        System.out.println("========================");

        for (int i = 0; i < daftarMenu.jumlahMenu; i++) {
            Menu menu = daftarMenu.getMenu(i);
            int jumlahPesanan = menu.getJumlahPesanan();

            if (jumlahPesanan > 0) {
                System.out.println(menu.getNama() + "\t" + jumlahPesanan + "\tRp. " + (jumlahPesanan * menu.getHarga()));
            }
        }

        System.out.println("------------------------+");
        System.out.println("Total\t\t" + jumlahPesananTotal + "\tRp " + pesanan.getTotalHarga());
        System.out.println("========================");
        System.out.println("Simpan struk ini sebagai\nbukti pembayaran");
        System.out.println("========================\n\n");

    }

    public void tampilkanKesalahan(String pesan) {
        System.out.println("Error: " + pesan);
    }

    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.tampilkanMainScreen();
    }
}
