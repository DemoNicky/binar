package com.binarfood.binarapp.DTO;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@Builder
public class CreateMerchantDTO {

    @NotNull
    private String merchantName;

    @NotNull
    private String merchantLocation;

}
