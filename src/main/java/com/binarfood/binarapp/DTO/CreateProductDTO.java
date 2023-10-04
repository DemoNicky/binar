package com.binarfood.binarapp.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CreateProductDTO {

    private String productName;

    private BigDecimal price;

    private String merchantCode;

}
