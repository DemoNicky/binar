package com.binarfood.binarfoodapp.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderDetailResponseDTO {

    private int quantity;

    private BigDecimal totalPrice;

    private List<ProductOrderResponseDTO> productOrderResponseDTO;

}
