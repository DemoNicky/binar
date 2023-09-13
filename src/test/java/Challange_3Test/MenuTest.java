package Challange_3Test;

import org.example.Challenge_3.Menu;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test class Menu")
public class MenuTest {
    Menu menu;

    @BeforeEach
    void objct(){
        menu = new Menu();
    }

    @Test
    public void testConstructor() {
        Menu menu = new Menu("Nasi Goreng", 15000);
        assertEquals("Nasi Goreng", menu.getNama());
        assertEquals(15000, menu.getHarga());
        assertEquals(0, menu.getJumlahPesanan());
    }

    @Test
    public void testToString() {
        Menu menu = new Menu("Mie Goreng", 13000);
        String expectedString = "Mie Goreng\t\t| 13000";
        assertEquals(expectedString, menu.toString());
    }
}

