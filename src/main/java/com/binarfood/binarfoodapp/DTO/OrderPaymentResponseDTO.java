package com.binarfood.binarfoodapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentResponseDTO {

    private String orderCode;

    private String username;

    private String address;

    private String orderTime;

    private String payment;

    private OrderDetailResponseDTO orderDetailResponseDTO;

}
