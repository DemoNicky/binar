package org.example.Challenge_3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class MainApp {
    private final Scanner scanner;
    private final DaftarMenu daftarMenu;
    private final Pesanan pesanan;

    public MainApp() {
        scanner = new Scanner(System.in);
        daftarMenu = new DaftarMenu();
        pesanan = new Pesanan();
    }

    public void tampilkanMainScreen() {
        boolean exit = false;
        while (!exit) {
            daftarMenu.tampilkanDaftarMenu();
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
                    Optional<Menu> menu = daftarMenu.getMenu(pilihan);
                    if (menu.isPresent()) {
                        pesanMenu(menu.get());
                    } else {
                        System.out.println("Error: Pilihan tidak valid");
                    }
                    break;
            }
        }
    }

    public void pesanMenu(Menu menu) {
        System.out.println("========================");
        System.out.println("Berapa pesanan anda");
        System.out.println("========================");

        System.out.println(menu.toString());
        System.out.print("Jumlah: ");

        int jumlah = scanner.nextInt();

        if (jumlah > 0) {
            pesanan.tambahPesanan(menu, jumlah);
            System.out.println("Pesanan berhasil ditambahkan!");
        } else {
            System.out.println("Error: Jumlah pesanan tidak valid");
        }
    }

    public void pesanDanBayar() {
        System.out.println("========================");
        System.out.println("Konfirmasi dan Pembayaran");
        System.out.println("========================");

        if (pesanan.getDaftarPesanan().isEmpty()) {
            System.out.println("Error: Belum Ada Pesanan!");
            return;
        }

        int jumlahPesananTotal = 0;

        for (Menu menu : pesanan.getDaftarPesanan()) {
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
            System.out.println("Total\t\t" + jumlahPesananTotal + "\tRp. " + pesanan.getTotalHarga());
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
                    System.out.println("Error: Pilihan tidak valid");
                    break;
            }
        }
    }

    public void cetakStrukTransaksi() {
        System.out.println("========================");
        System.out.println("Cetak Struk Transaksi");
        System.out.println("========================");

        try {
            FileWriter fileWriter = new FileWriter("receipt.txt");
            fileWriter.write("========================\n");
            fileWriter.write("Struk BinarFud\n");
            fileWriter.write("========================\n");

            for (Menu menu : pesanan.getDaftarPesanan()) {
                int jumlahPesanan = menu.getJumlahPesanan();

                if (jumlahPesanan > 0) {
                    int subtotal = menu.getHarga() * jumlahPesanan;

                    fileWriter.write(menu.getNama() + "\t" + jumlahPesanan + "\tRp. " + subtotal + "\n");
                }
            }

            fileWriter.write("------------------------+\n");
            fileWriter.write("Total\t\t" + pesanan.getDaftarPesanan()
                    .stream().mapToInt(menu -> menu.getJumlahPesanan()).sum() + "\tRp " + pesanan.getTotalHarga() + "\n\n");
            fileWriter.write("Pembayaran : BinarCash\n");
            fileWriter.write("========================\n");
            fileWriter.write("Simpan struk ini sebagai\nbukti pembayaran\n");
            fileWriter.write("========================\n");
            fileWriter.close();

            System.out.println("Struk transaksi berhasil dicetak!");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void tampilkanPesanan() {
        System.out.println("========================");
        System.out.println("Pesanan Anda");
        System.out.println("========================");

        pesanan.getDaftarPesanan().stream().forEach(menu -> {
            int jumlahPesanan = menu.getJumlahPesanan();

            if (jumlahPesanan > 0) {
                System.out.println(menu.getNama() + "\t" + jumlahPesanan + "\tRp. " + (jumlahPesanan * menu.getHarga()));
            }
        });

        System.out.println("------------------------+");
        System.out.println("Total\t\t" + pesanan.getDaftarPesanan()
                .stream().mapToInt(menu -> menu.getJumlahPesanan()).sum() + "\tRp " + pesanan.getTotalHarga());
        System.out.println("========================");
        System.out.println("Simpan struk ini sebagai\nbukti pembayaran");
        System.out.println("========================\n\n");
    }

    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.tampilkanMainScreen();
    }
}