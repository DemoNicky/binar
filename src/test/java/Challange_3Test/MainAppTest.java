package Challange_3Test;

import org.example.Challenge_3.MainApp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class MainAppTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final ByteArrayInputStream input = new ByteArrayInputStream("1\n2\n0\n".getBytes());

    @BeforeEach
    public void setUpStreams() {
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

    @Test
    public void testCetakStrukTransaksi() {
        MainApp app = new MainApp();
        app.cetakStrukTransaksi();
        File file = new File("receipt.txt");
        assertTrue(file.exists());
    }

    @Test
    public void testTampilkanPesanan() {
        MainApp app = new MainApp();
        app.tampilkanPesanan();
        assertNotNull(outContent.toString());
    }

}
