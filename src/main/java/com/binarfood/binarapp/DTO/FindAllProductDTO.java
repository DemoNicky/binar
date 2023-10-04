package com.binarfood.binarapp.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FindAllProductDTO {

    private String productCode;

    private String productName;

    private BigDecimal price;

    private String kodeMerchant;
}
