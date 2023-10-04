package com.binarfood.binarapp.Controller;

import com.binarfood.binarapp.DTO.*;
import com.binarfood.binarapp.Entity.Product;
import com.binarfood.binarapp.Service.MerchantService;
import com.binarfood.binarapp.Service.OrderService;
import com.binarfood.binarapp.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ProductService productService;

    private Scanner scanner = new Scanner(System.in);

    public MidleDTO midleDTO = new MidleDTO();

    Map<String, Integer> produkk = new HashMap<>();

    public void userMainMenu(){
        boolean ulang = true;
        while (ulang) {
            System.out.println("========================");
            System.out.println("=======Menu User=======");
            System.out.println("========================");
            System.out.println("Selamat datang " + midleDTO.getData().getUsername());
            System.out.println("1. Cari Merchant");
            System.out.println("2. Cari Produk Dan Beli");
            System.out.println("3. Keluar Dari Aplikasi");
            System.out.print("Pilih : ");
            int pilih = scanner.nextInt();

            if (pilih == 1) {
                ulang = false;
                searchMerchant();
            }else if (pilih == 2){
                ulang = false;
                System.out.println("Anda memilih Menu produk");
                searchProduct();
            }else if (pilih == 3){
                ulang = false;
                System.out.println("Terima Kasih Telah Menggunakan Aplikasi Kami ");
            }else {
                System.out.println("maaf kode yang anda masukan tidak sesuai !!");
            }
        }
    }

    private void searchMerchant() {
        System.out.println("========================");
        System.out.println("=====Cari Merchant=====");
        System.out.println("========================");
        System.out.print("Masukan Nama Merchant : ");
        String nama = scanner.next();
        List<FindMerchantAndProductDTO> result = merchantService.findMerchantUser(nama);
        for (FindMerchantAndProductDTO merchantAndProductDTO : result){
            System.out.println("Kode Merchant : " + merchantAndProductDTO.getMerchantCode());
            System.out.println("Nama Merchant : " + merchantAndProductDTO.getMerchantName());
            System.out.println("Lokasi Merchant : " + merchantAndProductDTO.getMerchantLocation());
            System.out.println("Buka : " + merchantAndProductDTO.isOpen());
            System.out.println("Produk Di Merchant : ");
            System.out.println("========================");
            for (Product product : merchantAndProductDTO.getProducts()) {
                System.out.println("Kode Produk\t\t: " + product.getProductCode());
                System.out.println("Nama Produk\t\t: " + product.getProductName());
                System.out.println("Harga Produk\t: " + product.getPrice());
                System.out.println("========================");
            }
            System.out.println("========================");
        }
        userMainMenu();
    }

    private void searchProduct(){
        int no = 1;
        System.out.println("========================");
        System.out.println("=====Cari Produk=====");
        System.out.println("========================");
        System.out.println("Masukan Nama Produk : ");
        String nama = scanner.next();
        List<FindProdukDTO> foundProducts = productService.findProductWithName(nama);
        if (foundProducts.isEmpty()) {
            System.out.println("Produk yang Anda cari tidak dapat ditemukan");
            userMainMenu();
        } else {
            System.out.println("========================");
            for (FindProdukDTO findProdukDTO : foundProducts) {
                System.out.println("Nomor Produk : " + no++);
                System.out.println("Kode Produk\t\t: " + findProdukDTO.getProductCode());
                System.out.println("Nama Produk\t\t: " + findProdukDTO.getProductName());
                System.out.println("Harga Produk\t: " + findProdukDTO.getPrice());
                System.out.println("Kode Merchant\t: " + findProdukDTO.getKodeMerchant());
                System.out.println("========================");
            }
        }
        System.out.print("Apakah Anda Ingin Membeli Produk(Y untuk ya/N untuk tidak : )");
        String pilih = scanner.next();
        if (pilih.equalsIgnoreCase("y")){
            buyItem(foundProducts);
        }else {
            userMainMenu();
        }
    }

    private void buyItem(List<FindProdukDTO> produk) {

        System.out.println("========================");
        System.out.println("=====Beli Produk=====");
        System.out.println("========================");
        boolean ulang = true;
        while (ulang){
            System.out.print("Pilih Nomor Produk : ");
            int number = scanner.nextInt();
            int num = number - 1;
            System.out.print("Jumlah Produk : ");
            int jumlah = scanner.nextInt();
            if (jumlah <= 0){
                System.out.println("Maaf jumlah tidak valid !!!");
            }else {
                produkk.put(produk.get(num).getProductCode(), jumlah);
                System.out.print("Apakah ingin Membeli produk lagi ? (Y/N) : ");
                String pilih = scanner.next();
                if (pilih.equalsIgnoreCase("y")){
                    ulang = true;
                    searchProduct();
                }else {
                    ulang = false;
                }
            }
        }
        OrderDetailResponseDTO responseDTO = orderService.orderStuff(produkk);
        if (responseDTO != null){
            orderPayment(responseDTO);
        }

    }

    public void orderPayment(OrderDetailResponseDTO responseDTO){
        System.out.println("========================");
        System.out.println("==Konfirmasi Pembayaran==");
        System.out.println("========================");
        for (Product products : responseDTO.getProduct()) {
            System.out.println("Kode\t||\tNama\t||\tHarga");
            System.out.println(products.getProductCode() + "\t||\t" + products.getProductName()+ "\t||\t" + products.getPrice());
            System.out.println("========================");
        }
        System.out.println("Total Harga : " + responseDTO.getTotalPrice());
        System.out.println("Jumlah Item : " + responseDTO.getQuantity());
        System.out.println("Konfirmasi Pembelian Apakah anda ingin Melanjutkan Pembayaran ? (Y/N)");
        String pilih = scanner.next();
        if (pilih.equalsIgnoreCase("y")){
            System.out.println("Masukan Alamat Kamu : ");
            String alamat = scanner.next();
            OrderResponseDTO order = orderService.buyStuff(alamat, midleDTO.getData().getId());

            detailTransaksi(responseDTO, order);
            System.out.println("");
        }else {
            userMainMenu();
        }

    }

    private void detailTransaksi(OrderDetailResponseDTO responseDTO, OrderResponseDTO order) {
        System.out.println("========================");
        System.out.println("====Detail Pembayaran====");
        System.out.println("========================");
        System.out.println("Tanggal Pembelian : " + order.getOrderTime());
        System.out.println("Alamat : " + order.getDestinationAddress());
        for (Product products : responseDTO.getProduct()) {
            System.out.println("Nama\t\t||\tHarga");
            System.out.println(products.getProductName()+ "\t||\t" + products.getPrice());
            System.out.println("========================");
        }
        System.out.println("Jumlah Total Item : " + responseDTO.getQuantity());
        System.out.println("Total Harga : " + responseDTO.getTotalPrice());
        if (order.getCompleted() == true || order.getCompleted().equals(true)){
            String status = "Lunas";
            System.out.println("Status Pembayaran : " + status);
        }

    }


}
