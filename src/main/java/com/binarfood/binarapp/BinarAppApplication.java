package com.binarfood.binarapp;

import com.binarfood.binarapp.Controller.LoginController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.binarfood.binarapp")
public class BinarAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BinarAppApplication.class, args);
	}

}
