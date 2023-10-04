package com.binarfood.binarapp.Controller;

import com.binarfood.binarapp.DTO.*;
import com.binarfood.binarapp.Service.MerchantService;
import com.binarfood.binarapp.Service.ProductService;
import com.binarfood.binarapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

@Component
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;

    private Scanner scanner = new Scanner(System.in);

    public void adminMainMenu(){
        boolean ulang = true;
        while (ulang) {
            System.out.println("Selamat datang ADMIN");
            System.out.println("========================");
            System.out.println("=======Menu Admin=======");
            System.out.println("========================");
            System.out.println("1. Merchant");
            System.out.println("2. Produk");
            System.out.println("3. User");
            System.out.println("4. Keluar Dari Aplikasi");
            System.out.print("Pilih : ");
            int pilih = scanner.nextInt();

            if (pilih == 1) {
                ulang = false;
                System.out.println("Anda memilih Menu Merchant");
                menuMerchantAdmin();
            }else if (pilih == 2){
                ulang = false;
                System.out.println("Anda memilih Menu produk");
                menuProductAdmin();
            }else if (pilih == 3){
                ulang = false;
                System.out.println("Anda memilih Menu User");
                menuUserAdmin();
            }else if (pilih == 4){
                ulang = false;
                System.out.println("Anda telah Keluar dari Aplikasi !!!");
                System.exit(0);
            }else {
                System.out.println("maaf kode yang anda masukan tidak sesuai !!");
            }
        }
    }

    /**
     * //!!!!!!!Merchant!!!!!!!!
     */

    public void menuMerchantAdmin() {
        boolean ulang = true;
        while (ulang){
            System.out.println("========================");
            System.out.println("======Menu Merchan======");
            System.out.println("========================");
            System.out.println("1. Buat Merchant");
            System.out.println("2. Tampilkan Semua Merchant");
            System.out.println("3. Cari Merchant");
            System.out.println("4. Update Merchant");
            System.out.println("5. Hapus Merchat");
            System.out.println("6. Kembali");
            System.out.print("pilih : ");
            int pilih = scanner.nextInt();
            if (pilih == 1) {
                ulang = false;
                createMerchant();
            } else if (pilih == 2) {
                ulang = false;
                findAllMerchant();
            } else if (pilih == 3) {
                ulang = false;
                searchMerchant();
            } else if (pilih == 4) {
                ulang = false;
                updateMerchant();
            } else if (pilih == 5) {
                ulang = false;
                deletedMerchant();
            } else if (pilih == 6) {
                ulang = false;
                adminMainMenu();
            } else {
                System.out.println("pilihan tidak valid");
            }
        }
    }

    //menggunakan safe delete
    public void deletedMerchant(){
        System.out.println("========================");
        System.out.println("=====Deleted Merchant=====");
        System.out.println("========================");
        System.out.println("masukan kode Merchant : ");
        String kode = scanner.next();
        String result = merchantService.deleteMerchant(kode);
        System.out.println(result);
        menuMerchantAdmin();
    }

    public void updateMerchant(){
        System.out.println("========================");
        System.out.println("=====Update Merchant=====");
        System.out.println("========================");
        System.out.print("masukan kode Merchant : ");
        String kode = scanner.next();
        System.out.print("Masukan Nama Merchant : ");
        String nama = scanner.next();
        System.out.print("Masukan Lokasi Merchant : ");
        String lokasi = scanner.next();
        System.out.print("apakah ingin mengaktifkan/open merchant (1 jika iya/2 jika tidak) : ");
        int open = scanner.nextInt();

        String merchant = merchantService.updateMerchant(kode, nama, lokasi, open);
        System.out.println(merchant);
        menuMerchantAdmin();

    }

    public void searchMerchant(){
        System.out.println("========================");
        System.out.println("=====Cari Merchant=====");
        System.out.println("========================");
        System.out.println("masukan kode Merchant : ");
        String kode = scanner.next();
        FindMerchantDTO result = merchantService.findMerchant(kode);
        if (result.equals(null) || result == null){
            System.out.println("Merchant yang anda cari tidak dapat di temukan");
        }
        System.out.println("Nama Merchant\t\t: " + result.getMerchantName());
        System.out.println("Lokasi Merchant\t\t: " + result.getMerchantLocation());
        System.out.println("Kode Merchant \t\t: " + result.getMerchantCode());
        System.out.println("Open\t\t\t\t:" + result.isOpen());
        System.out.println("========================");
        menuMerchantAdmin();
    }

    public void createMerchant(){
        System.out.println("========================");
        System.out.println("====membuat Merchan====");
        System.out.println("========================");
        System.out.print("Masukan Nama Merchant : ");
        String nama = scanner.next();
        System.out.print("Masukan Lokasi Merchant : ");
        String lokasi = scanner.next();

        if (nama.isEmpty() || lokasi.isEmpty()) {
            System.out.println("Nama/lokasi merchant tidak boleh kosong.");
            menuMerchantAdmin();
        }
        CreateMerchantDTO create = CreateMerchantDTO.builder()
                .merchantName(nama)
                .merchantLocation(lokasi)
                .build();
        Boolean returnnn = merchantService.createMerchant(create);
        if (returnnn == true){
            System.out.println("Merchant sudah di Simpan");
            menuMerchantAdmin();
        }else if (returnnn == false){
            System.out.println("Merchant Gagal di Simpan");
            menuMerchantAdmin();
        }
    }

    public void findAllMerchant(){
        System.out.println("========================");
        System.out.println("Menampilkan Semua Merchant");
        System.out.println("========================");

        List<FindAllMerchantDTO> findAllMerchantDTO = merchantService.findAllMerchant();

        if (findAllMerchantDTO.isEmpty()) {
            System.out.println("Tidak ada data merchant yang ditemukan.");
        } else {
            System.out.println("Daftar Merchant:");
            System.out.println("========================");
            for (FindAllMerchantDTO merchant : findAllMerchantDTO) {
                System.out.println("Nama Merchant\t\t: " + merchant.getMerchantName());
                System.out.println("Lokasi Merchant\t\t: " + merchant.getMerchantLocation());
                System.out.println("Kode Merchant \t\t: " + merchant.getMerchantCode());
                System.out.println("Open\t\t\t\t:" + merchant.isOpen());
                System.out.println("========================");
            }

            System.out.println("Semua Merchant telah ditampilkan.\n\n");
            menuMerchantAdmin();
        }
        menuMerchantAdmin();
    }


    /**
     * //!!!!!!!Product!!!!!!!!
     */

    public void menuProductAdmin(){
        while (true){
            System.out.println("========================");
            System.out.println("======Menu Product======");
            System.out.println("========================");
            System.out.println("1. Buat produk");
            System.out.println("2. Tampilkan Semua produk");
            System.out.println("3. Cari produk");
            System.out.println("4. Update Produk");
            System.out.println("5. Hapus Produk");
            System.out.println("6. Kembali");
            System.out.print("pilih : ");
            int pilih = scanner.nextInt();
            if (pilih == 1){
                createProduct();
            }else if (pilih == 2){
                findAllProduct();
            }else if (pilih == 3){
                findProductWithName();
            }else if (pilih == 4){
                updateProduct();
            }else if (pilih == 5){
                deleteProduct();
            }else if (pilih == 6){
                adminMainMenu();
            }else {
                System.out.println("pilihan tidak valid");
            }
        }
    }

    private void deleteProduct() {
        System.out.println("========================");
        System.out.println("=====Delete Product=====");
        System.out.println("========================");
        System.out.println("Masukan Kode Produk : ");
        String kode = scanner.next();
        Boolean returnnn = productService.deleteProduct(kode);
        if (returnnn == true){
            System.out.println("Produk Berhasil Di Hapus");
            menuProductAdmin();
        }else {
            System.out.println("Produk Gagal Di Hapus");
            menuProductAdmin();
        }

    }


    private void updateProduct() {
        System.out.println("========================");
        System.out.println("=====Update Product=====");
        System.out.println("========================");
        System.out.print("masukan kode Produk : ");
        String kode = scanner.next();
        System.out.print("Masukan Nama Produk : ");
        String nama = scanner.next();
        System.out.print("Masukan Harga Produk : ");
        BigDecimal harga = scanner.nextBigDecimal();
        String result = productService.updateProduct(kode, nama, harga);
        System.out.println(result);
        menuProductAdmin();

    }

    private void findProductWithName() {
        System.out.println("========================");
        System.out.println("=====Cari Produk=====");
        System.out.println("========================");
        System.out.print("masukan Nama Produk : ");
        String nama = scanner.next();
        List<FindProdukDTO> foundProducts = productService.findProductWithName(nama);
        if (foundProducts.isEmpty()) {
            System.out.println("Produk yang Anda cari tidak dapat ditemukan");
        } else {
            System.out.println("========================");
            for (FindProdukDTO findProdukDTO : foundProducts) {
                System.out.println("Kode Produk\t\t: " + findProdukDTO.getProductCode());
                System.out.println("Nama Produk\t\t: " + findProdukDTO.getProductName());
                System.out.println("Harga Produk \t\t: " + findProdukDTO.getPrice());
                System.out.println("Kode Merchant\t\t: " + findProdukDTO.getKodeMerchant());
                System.out.println("========================");
            }
        }

        menuProductAdmin();
    }

    public void createProduct(){
        System.out.println("========================");
        System.out.println("====Membuat Produk====");
        System.out.println("========================");
        System.out.println("====Kode Merchant====");
        System.out.println("========================");
        List<FindAllMerchantDTO> findAllMerchantDTO = merchantService.findAllMerchant();

        if (findAllMerchantDTO.isEmpty()) {
            System.out.println("Tidak ada data merchant yang ditemukan.");
        } else {
            System.out.println("Daftar Merchant:");
            System.out.println("========================");
            for (FindAllMerchantDTO merchant : findAllMerchantDTO) {
                System.out.println("Kode Merchant \t\t: " + merchant.getMerchantCode());
                System.out.println("Nama Merchant : " + merchant.getMerchantName() + "||" + " Lokasi Merchant\t\t: " + merchant.getMerchantLocation());
                System.out.println("========================");
            }

            System.out.println("Semua Merchant telah ditampilkan.");
        }
        System.out.print("Masukan Kode Merchant : ");
        String kodeMerchant = scanner.next();

        scanner.nextLine();

        System.out.print("Masukan Nama Produk : ");
        String nama = scanner.nextLine();

        BigDecimal harga = null;

        while (harga == null) {
            System.out.print("Masukan Harga Produk : ");
            if (scanner.hasNextBigDecimal()) {
                harga = scanner.nextBigDecimal();
            } else {
                System.out.println("Harga harus berupa angka. Silakan coba lagi.");
                scanner.next();
            }
        }
        if (nama.isEmpty() || harga.equals(0) || kodeMerchant.isEmpty()) {
            System.out.println("Nama/Harga Produk /KodeMerchant tidak boleh kosong.");
            menuProductAdmin();
        }

        System.out.println(kodeMerchant + nama + harga);
        CreateProductDTO create = CreateProductDTO.builder()
                .productName(nama)
                .price(harga)
                .merchantCode(kodeMerchant)
                .build();
        Boolean returnnn = productService.createProduct(create);
        if (returnnn == true){
            System.out.println("Produk Berhasil di Simpan");
            menuProductAdmin();
        }else if (returnnn == false){
            System.out.println("produk Gagal di Simpan");
            menuProductAdmin();
        }
    }

    public void findAllProduct() {
        System.out.println("========================");
        System.out.println("Menampilkan Semua Produk");
        System.out.println("========================");
        boolean ulang = true;
        int page = 1;
        while (ulang) {
            Pageable pageable = PageRequest.of(page - 1, 10);
            List<FindAllProductDTO> findAllProductDTOS = productService.findAllProduct(pageable);
            if (findAllProductDTOS.isEmpty()) {
                System.out.println("Tidak ada data produk yang ditemukan.");
                ulang = false;
                menuProductAdmin();
            } else {
                System.out.println("Daftar Produk");
                System.out.println("========================");
                for (FindAllProductDTO findAllProductDTO : findAllProductDTOS) {
                    System.out.println("kode Produk : \t\t" + findAllProductDTO.getProductCode());
                    System.out.println("Nama Produk : \t\t" + findAllProductDTO.getProductName());
                    System.out.println("Harga Produk : \t\t" + findAllProductDTO.getPrice());
                    System.out.println("Kode Merchant : \t\t" + findAllProductDTO.getKodeMerchant());
                    System.out.println("========================");
                }
                System.out.println("Semua Produk telah ditampilkan.");
                System.out.println("Saat ini anda berada di halaman ke-" + page);
                System.out.println("Pilih Page (pilih 0 jika keluar): ");
                int pg = scanner.nextInt();
                if (findAllProductDTOS == null || findAllProductDTOS.isEmpty()) {
                    System.out.println("Page Yang anda Pilih kosong");
                } else if (pg < 0) {
                    System.out.println("pilihan tidak boleh kurang dari 1 dan harus angka");
                } else if (pg == 0){
                    menuProductAdmin();
                } else {
                    Integer pgg = Integer.valueOf(pg);
                    page = pgg;
                }

            }
        }
    }

    public void menuUserAdmin() {
        while (true) {
            System.out.println("========================");
            System.out.println("======Menu Product======");
            System.out.println("========================");
            System.out.println("1. Buat User Menjadi Admin");
            System.out.println("2. Cari User");
            System.out.println("3. Hapus User");
            System.out.println("4. Kembali");
            System.out.print("pilih : ");
            int pilih = scanner.nextInt();
            if (pilih == 1) {
                createUserToAdmin();
            } else if (pilih == 2) {
                findUser();
            } else if (pilih == 3) {
                deleteUser();
            } else if (pilih == 4) {
                adminMainMenu();
            } else {
                System.out.println("pilihan Tidak Valid");
            }
        }
    }

    private void deleteUser() {
        System.out.println("========================");
        System.out.println("======Delete User======");
        System.out.println("========================");
        System.out.print("Masukan Id User : ");
        String id = scanner.next();
        Boolean returnn = userService.deleteUser(id);
        if (returnn == true){
            System.out.println("User telah dihapus !!!");
        }else {
            System.out.println("User dengan id :" +id+ " Gagal Di hapus");
        }
        menuUserAdmin();
    }

    private void findUser() {
        System.out.println("========================");
        System.out.println("======Cari User======");
        System.out.println("========================");
        System.out.print("Masukan Username user :");
        String username = scanner.next();
        FindUserDTO returnn = userService.findUser(username);
        System.out.println("========================");
        System.out.println("Username \t\t: " + returnn.getUsername());
        System.out.println("Email \t\t: " + returnn.getEmail());
        System.out.println("Role \t\t: " + returnn.getRole());
        menuUserAdmin();
    }

    public void createUserToAdmin(){
        System.out.println("========================");
        System.out.println("======Ubah User======");
        System.out.println("========================");
        System.out.print("Masukan Id User: ");
        String id = scanner.next();
        Boolean returnn = userService.createUser(id);
        if (returnn == true){
            System.out.println("User Berhasil diubah");
            menuUserAdmin();
        }else {
            System.out.println("User Gagal Di Ubah!!!");
            menuUserAdmin();
        }
    }
}
