package com.binarfood.binarapp.DTO;

import com.binarfood.binarapp.Entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FindMerchantAndProductDTO {

    private String merchantName;

    private String merchantLocation;

    private String merchantCode;

    private boolean open;

    private List<Product> products;

}
