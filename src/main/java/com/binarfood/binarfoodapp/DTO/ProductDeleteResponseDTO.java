package com.binarfood.binarfoodapp.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDeleteResponseDTO {

    private String message;

    private String errors;
}
