package com.binarfood.binarapp.Controller;

import com.binarfood.binarapp.DTO.UserLoginDTO;
import com.binarfood.binarapp.Entity.Role;
import com.binarfood.binarapp.Entity.User;
import com.binarfood.binarapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminController adminController;

    @Autowired
    private UserController userController;

    private Scanner scanner = new Scanner(System.in);

    @PostConstruct
    public void init() throws ParseException {
        pilihAuth();
    }

    public void pilihAuth(){
        boolean ulang = true;
        while (ulang) {
            System.out.println("!!!!!NOTE ADA 2 ROLE USER DAN ADMIN!!!!!");
            System.out.println("Selamat datang di aplikasi Binar-Food \n shilakan pilih : ");
            System.out.println("1. \t Login");
            System.out.println("2. \t Register");
            System.out.print("pilih : ");
            try {
                int pilih = scanner.nextInt();
                scanner.nextLine();

                if (pilih == 1) {
                    ulang = false;
                    System.out.println("Anda memilih login\n\n");
                    login();
                } else if (pilih == 2) {
                    ulang = false;
                    System.out.println("Anda memilih register \n\n");
                    register();
                } else {
                    System.out.println("Maaf kode yang anda masukkan tidak sesuai!!");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Maaf, harap masukkan angka yang sesuai.");
            }
        }
    }

    public void login(){
        boolean ulang = true;
        while (ulang){
            System.out.println("=====login=====");
            System.out.print("Masukan Username \t: ");
            String username = scanner.next();
            System.out.print("Masukan Password \t: ");
            String pass = scanner.next();

            if (username.isEmpty() || pass.isEmpty()){
                System.out.println("Gagal Melakukan login, shilakan coba lagi!!!");
                pilihAuth();
            }else {
                UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                        .username(username)
                        .password(pass)
                        .build();
                Optional<User> returnn = userService.login(userLoginDTO);

                if (!returnn.isPresent() || returnn.equals(null) || returnn == null) {
                    System.out.println("Username/Password tidak valid!!!");
                    login();
                } else {
                    System.out.println("Berhasil Login");
                    if (returnn.get().getRole().equals(Role.ADMIN)){
                        ulang = false;
                        adminController.adminMainMenu();
                    }else {
                        ulang = false;
                        User user = returnn.get();
                        userController.midleDTO.setData(user);
                        userController.userMainMenu();
                    }

                }
            }
        }
    }

    public void register(){
        System.out.println("=====register=====");
        System.out.print("Masukan Username \t: ");
        String username = scanner.next();
        System.out.print("Masukan Email \t: ");
        String email = scanner.next();
        System.out.print("Masukan Password \t: ");
        String pass = scanner.next();

        regex(email);

        if (username.isEmpty() || email.isEmpty() || pass.isEmpty()){
            System.out.println("Gagal Melakukan registrasi, shilakan coba lagi!!!");
            pilihAuth();
        }else {
            String returnn = userService.register(username, email, pass);
            System.out.println(returnn);
            pilihAuth();
        }
    }

    private void regex(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            System.out.println("Email tidak valid!!! \n\n");
            pilihAuth();
        }
    }

}
