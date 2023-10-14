package com.binarfood.binarfoodapp.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MerchantRequestDTO {
    @NotBlank(message = "Merchant name cannot be blank")
    private String merchantName;

    @NotBlank(message = "Merchant Location cannot be blank")
    private String merchantLocation;

}
