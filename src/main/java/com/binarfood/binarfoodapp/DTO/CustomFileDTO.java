package com.binarfood.binarfoodapp.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CustomFileDTO {

    private String orderCode;

    private String username;

    private String address;

    private String orderTime;

    private String payment;

    private int quantity;

    private BigDecimal totalPrice;

    private String productCode;

    private String productName;

    private BigDecimal price;

    private String merchantName;

    private int qty;

}
