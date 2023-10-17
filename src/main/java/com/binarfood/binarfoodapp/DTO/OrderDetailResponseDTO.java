package com.binarfood.binarfoodapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponseDTO {

    private int quantity;

    private BigDecimal totalPrice;

    private List<ProductOrderResponseDTO> productOrderResponseDTO;

}
