package org.example;

import java.util.Scanner;

public class chalange_1 {

    //objek scanner di gunakan untuk membaca masukan dari user
    public static Scanner scanner = new Scanner(System.in);

    //array di bawah berisi menu makanan dan bertipe data string
    public static String[] menu = {"Nasi Goreng", "Mie Goreng", "Nasi + Ayam", "Es Teh Manis", "Es Jeruk"};

    //array di bawah berisi harga dan bertipe data int
    public static int[] hargaMenu = {15000, 13000, 18000, 3000, 5000};

    //array di bawah di gunakan untuk menampung jumlah order dan mewakili jumlah pesanan di menu
    //menggunkan menu.length untuk memastikan bahwa ukuran array sesuai dengan jumlah elemen array di array menu
    public static int[] jumlahOrder = new int[menu.length];

    //variabel di bawah adalah variabel global yang di gunakan untuk menghitung total dari harga pada beberapa method
    public static int total = 0;


    public static void main(String[] args) {
        mainMenu();
    }

    /**
     * method main menu/menu utama
     */
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
        //variable di bawah menggunakan objek Scanner, dan mengambil data langsung dari keyboard ke dalam variable
        int pilihan = scanner.nextInt();
        qtyPesanan(pilihan);

    }

    /**
     * method di bawah di gunakan untuk menetapkan berapa baanyak pesanan
     * @param pilih
     */
    public static void qtyPesanan(Integer pilih){
        //di bawah adalah perkondisian, ketika parameter pilih terisi 99 makan akan hit ke method konfAndPayment
        //dan jika memilih 0 maka akan keluar dari program
        //jika memilih angka lebih dari 1 dan kurang dari menu.length atau kurang dari 5 akan mendirect ke method menuJumlahPesanan
        //dan jika pilihan tidak ada yang benar akan muncul output pilihan tidak valid dan direct ke method mainMenu
        if (pilih == 99){
            konfAndPayment();
        } else if (pilih == 0) {
            System.out.println("Terima kasih!");
            System.exit(0);
        } else if (pilih >= 1 && pilih <= menu.length) {
            menuJumlahPesanan(pilih);
        }else {
            System.out.println("Pilihan tidak Valid !!!");
            mainMenu();
        }
    }

    /**
     * method untuk menu jumlah pesanan
     * @param pilihanMenu
     */
    public static void menuJumlahPesanan(Integer pilihanMenu){
        //variabel ulang digunakan pada while loop jika true maka akan di ulang lagi dan jika ulang false maka perulangan terhenti dan kembali ke menu utama
        boolean ulang = true;
        while (ulang) {
            //variabel di bawah di gunakan untuk mengurangi angka untuk di gunakan di array
            int pilihYangDiKurang = pilihanMenu - 1;
            System.out.println("================================");
            System.out.println("Berapa pesanan anda");
            System.out.println("================================");
            //output yang di hasilkan di ambil dari array menu dan harga menu yang di deklarasikan oleh variabel pilihYangDiKurang
            System.out.println(menu[pilihYangDiKurang] + "\t\t| " + hargaMenu[pilihYangDiKurang]);
            System.out.print("qty: ");
            int jumlah = scanner.nextInt();
            //perkondisian jika jumlah lebih dari 0 maka akan di eksekusi dan jikakurang dari 0 maka akan muncul pilihan tidak valid
            // dan akan mengulang perintah dari awal pada method menuJumlah Pesanan
            if (jumlah > 0) {
                //jumlah order akan terisi jumlah dan di deklarasikan pada array menu yang sama pada array menu
                jumlahOrder[pilihYangDiKurang] += jumlah;
                ulang = false;
                mainMenu();
            } else {
                System.out.println("Pilihan Tidak Valid!!!");
            }
        }
    }

    /**
     * method untuk konfirmasi pembayaran
     */
    public static void konfAndPayment(){
        System.out.println("========================");
        System.out.println("Konfirmasi & pembayaran");
        System.out.println("========================");
        //for loop di gunakan untuk perulangan ketika ada lebih dari satu menu yang di pilih, maka akan banyak menu yang tampil
        for (int i = 0; i < menu.length; i++) {
            if (jumlahOrder[i] > 0){
                //variabel di bawah adalah perkalian untuk menghitung sub total per barang
                int subTotal = hargaMenu[i] * jumlahOrder[i];
                System.out.println(menu[i] + "\t" + jumlahOrder[i] + "\tRp. " + subTotal);
                //variabel total untuk menghitung seluruh sub total
                total += subTotal;
            }
        }
        System.out.println("------------------------+");
        System.out.println("Total \t\tRp " + total);
        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali Ke menu utama");
        System.out.println("0. Keluar aplikasi");
        System.out.print("Pilih: ");
        int pilih = scanner.nextInt();
        if (pilih == 1){
            cetakStrukTransaksi();
        } else if (pilih == 2) {
            mainMenu();
        } else if (pilih == 0) {
            System.exit(0);
        }
    }

    /**
     * method untuk mencetak struk transaksi
     */
    public static void cetakStrukTransaksi(){
        System.out.println("========================");
        System.out.println("Struk BinarFud");
        System.out.println("========================");
        System.out.println("Terima kasih sudah memesan di Binarfud");
        System.out.println("Dibawah ini adalah pesanan anda\n");
        //for loop di gunakan untuk perulangan ketika ada lebih dari satu menu yang di pilih, maka akan banyak menu yang tampil
        for (int i = 0; i < menu.length; i++) {
            if (jumlahOrder[i] > 0){
                //variabel di bawah adalah perkalian untuk menghitung sub total per barang
                int y = hargaMenu[i] * jumlahOrder[i];
                System.out.println(menu[i] + "\t" + jumlahOrder[i] + "\tRp. " + y);
            }
        }
        System.out.println("------------------------+");
        //total di ambil dari variabel global yang di kalikan pada method konfAndPayment
        System.out.println("Total \t\tRp " + total);
        System.out.println("\nPembayaran : BinarCash");
        System.out.println("========================");
        System.out.println("Simpan struk ini sebagai\nbukti pembayaran");
    }
}
