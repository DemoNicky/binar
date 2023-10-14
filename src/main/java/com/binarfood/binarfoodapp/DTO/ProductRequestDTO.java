package com.binarfood.binarfoodapp.DTO;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequestDTO {

    @NotBlank(message = "Product name cannot be blank")
    private String productName;

    @DecimalMin(value = "0.01", message = "Price must be greater than or equal to 0.01")
    private BigDecimal price;

    @NotBlank(message = "Merchant code cannot be blank")
    @Size(min = 4, max = 10, message = "Merchant code must be between 3 and 10 characters")
    private String merchantCode;

}
