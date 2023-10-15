package com.binarfood.binarfoodapp.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderPaymentResponseDTO {

    private String orderCode;

    private String username;

    private String address;

    private Date orderTime;

    private String payment;

    private OrderDetailResponseDTO orderDetailResponseDTO;

}
