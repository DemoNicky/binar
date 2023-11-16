package com.binarfood.binarfoodapp.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class OrderDTO {

    private String destinationAddress;

    @NotBlank(message = "order item cant null")
    private List<OrderItemDTO> orderItem;
}
