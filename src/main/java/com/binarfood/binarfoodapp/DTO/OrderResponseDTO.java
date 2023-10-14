package com.binarfood.binarfoodapp.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {

    private String orderCode;

    private String username;

    private String address;

    private Date orderTime;

    private String payment;

    private OrderDetailResponseDTO orderDetailResponseDTO;

}
