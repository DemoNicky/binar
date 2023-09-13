package Challange_3Test;

import org.example.Challenge_3.Menu;
import org.example.Challenge_3.Pesanan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PesananTest {

    @Test
    void testTambahPesananForNewMenu() {
        Menu menu = new Menu("Nasi Goreng", 15000);
        Pesanan pesanan = new Pesanan();
        pesanan.tambahPesanan(menu, 2);
        Assertions.assertEquals(2, pesanan.getDaftarPesanan().get(0).getJumlahPesanan());

    }

    @Test
    void testTambahPesananForExistingMenu() {
        Menu menu = new Menu("Nasi Goreng", 15000);
        Pesanan pesanan = new Pesanan();
        pesanan.tambahPesanan(menu, 2);
        pesanan.tambahPesanan(menu, 1);
        Assertions.assertEquals(3, pesanan.getDaftarPesanan().get(0).getJumlahPesanan());

    }

    @Test
    void testGetTotalHarga() {
        Menu menu1 = new Menu("Nasi Goreng", 15000);
        Menu menu2 = new Menu("Mie Goreng", 13000);
        Pesanan pesanan = new Pesanan();
        pesanan.tambahPesanan(menu1, 2);
        pesanan.tambahPesanan(menu2, 1);
        Assertions.assertEquals(43000, pesanan.getTotalHarga());

    }
}
