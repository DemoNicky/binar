package com.binarfood.binarapp.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FindMerchantDTO {

    private String merchantName;

    private String merchantLocation;

    private String merchantCode;

    private boolean open;

}
