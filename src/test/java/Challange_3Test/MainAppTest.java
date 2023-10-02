package Challange_3Test;

import org.example.Challenge_3.DaftarMenu;
import org.example.Challenge_3.MainApp;
import org.example.Challenge_3.Menu;
import org.example.Challenge_3.Pesanan;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MainAppTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final ByteArrayInputStream input = new ByteArrayInputStream("1\n2\n0\n".getBytes());
    private MainApp mainApp;
    private Menu menu;
    private DaftarMenu daftarMenu;

    @BeforeEach
    public void setUpStreams() {
        daftarMenu = new DaftarMenu();
        mainApp = new MainApp();
        menu = new Menu();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        System.setIn(input);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(System.in);
    }

    @Test
    public void testPesanDanBayar() {
        MainApp app = new MainApp();
        String input = "99\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        app.pesanDanBayar();
        assertNotNull(outContent.toString());
    }

//    @Test
//    public void testCetakStrukTransaksi() {
//        MainApp app = new MainApp();
//        app.cetakStrukTransaksi();
//        File file = new File("receipt.txt");
//        assertTrue(file.exists());
//    }

    @Test
    void testCetakStrukTransaksi() throws IOException {
        Pesanan pesanan = mainApp.pesanan;
        Menu menu1 = new Menu("Nasi Goreng", 15000);
        Menu menu2 = new Menu("Mie Goreng", 13000);
        pesanan.tambahPesanan(menu1, 2);
        pesanan.tambahPesanan(menu2, 1);

        mainApp.cetakStrukTransaksi();

        BufferedReader reader = new BufferedReader(new FileReader("receipt.txt"));

        String line1 = reader.readLine();
        String line2 = reader.readLine();
        String line3 = reader.readLine();
        String line4 = reader.readLine();
        String line5 = reader.readLine();
        String line6 = reader.readLine();
        String line7 = reader.readLine();
        String line8 = reader.readLine();
        String line9 = reader.readLine();
        String line10 = reader.readLine();

        assertEquals("========================", line1);
        assertEquals("Struk BinarFud", line2);
        assertEquals("========================", line3);
        assertEquals("Nasi Goreng\t2\tRp. 30000", line4);
        assertEquals("Mie Goreng\t1\tRp. 13000", line5);
        assertEquals("------------------------+", line6);
        assertEquals("Total\t\t3\tRp 43000", line7);
        assertEquals("", line8);
        assertEquals("Pembayaran : BinarCash", line9);
        assertEquals("========================", line10);
    }

    @Test
    public void testTampilkanPesanan() {
        MainApp app = new MainApp();
        app.tampilkanPesanan();
        assertNotNull(outContent.toString());
    }

    @Test
    void testGetTotalHarga() {
        Pesanan pesanan = mainApp.pesanan;
        Menu menu1 = new Menu("Nasi Goreng", 15000);
        Menu menu2 = new Menu("Mie Goreng", 13000);
        pesanan.tambahPesanan(menu1, 2);
        pesanan.tambahPesanan(menu2, 1);
        assertEquals(43000, pesanan.getTotalHarga());
    }

    @Test
    void testTambahPesanan() {
        Pesanan pesanan = mainApp.pesanan;
        Menu menu1 = new Menu("Nasi Goreng", 15000);
        Menu menu2 = new Menu("Mie Goreng", 13000);

        pesanan.tambahPesanan(menu1, 2);
        assertEquals(1, pesanan.getDaftarPesanan().size());

        pesanan.tambahPesanan(menu2, 1);
        assertEquals(2, pesanan.getDaftarPesanan().size());

        pesanan.tambahPesanan(menu1, -1);
        assertEquals(2, pesanan.getDaftarPesanan().size());
    }

    @Test
    void testGetMenuWhenIndexIsValid() {
        Optional<Menu> menu = mainApp.daftarMenu.getMenu(1);
        assertTrue(menu.isPresent());
        assertEquals("Nasi Goreng", menu.get().getNama());
    }

    @Test
    void testGetMenuWhenIndexIsInvalid() {
        Optional<Menu> menu = mainApp.daftarMenu.getMenu(-1);
        assertTrue(menu.isEmpty());
    }

    @Test
    public void testGetDaftarMenu() {
        daftarMenu = mainApp.daftarMenu;
        assertEquals(5, daftarMenu.getDaftarMenu().size());
        assertEquals("Nasi Goreng", daftarMenu.getDaftarMenu().get(0).getNama());
        assertEquals("Mie Goreng", daftarMenu.getDaftarMenu().get(1).getNama());
        assertEquals("Nasi + Ayam", daftarMenu.getDaftarMenu().get(2).getNama());
        assertEquals("Es Teh Manis", daftarMenu.getDaftarMenu().get(3).getNama());
        assertEquals("Es Jeruk", daftarMenu.getDaftarMenu().get(4).getNama());
    }

    @Test
    public void testPesanMenuWhenJumlahPesananKurangDariNol() {
        Assertions.assertDoesNotThrow(() -> {
            System.setIn(new java.io.ByteArrayInputStream("1\n-1\n".getBytes()));
            mainApp.pesanMenu(mainApp.daftarMenu.getMenu(1).get());
        });
    }

    @Test
    public void testMainScreen() {
        Assertions.assertDoesNotThrow(() -> {
            System.setIn(new java.io.ByteArrayInputStream("1\n2\n99\n0\n".getBytes()));
            mainApp.tampilkanMainScreen();
        });
    }
}
