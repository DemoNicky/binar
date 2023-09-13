package Challange_3Test;

import org.example.Challenge_3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DaftarMenuTest {

    DaftarMenu daftarMenu;

    @BeforeEach
    public void objct(){
        daftarMenu = new DaftarMenu();
    }
    @Test
    void testGetMenuWhenIndexIsValid() {
        Optional<Menu> menu = daftarMenu.getMenu(1);
        assertTrue(menu.isPresent());
        assertEquals("Nasi Goreng", menu.get().getNama());
    }

    @Test
    void testGetMenuWhenIndexIsInvalid() {
        Optional<Menu> menu = daftarMenu.getMenu(-1);
        assertTrue(menu.isEmpty());
    }

    @Test
    public void testGetDaftarMenu() {
        DaftarMenu daftarMenu = new DaftarMenu();
        List<Menu> daftar = daftarMenu.getDaftarMenu();
        assertEquals(5, daftar.size());
        assertEquals("Nasi Goreng", daftar.get(0).getNama());
        assertEquals("Mie Goreng", daftar.get(1).getNama());
        assertEquals("Nasi + Ayam", daftar.get(2).getNama());
        assertEquals("Es Teh Manis", daftar.get(3).getNama());
        assertEquals("Es Jeruk", daftar.get(4).getNama());
    }
}
