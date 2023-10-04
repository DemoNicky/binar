package com.binarfood.binarapp.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindAllMerchantDTO {

    private String merchantName;

    private String merchantLocation;

    private String merchantCode;

    private boolean open;

}
