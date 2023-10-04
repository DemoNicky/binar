package com.binarfood.binarapp.DTO;

import com.binarfood.binarapp.Entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDetailResponseDTO {

    private Integer quantity;

    private BigDecimal totalPrice;

    private List<Product> product;

}
