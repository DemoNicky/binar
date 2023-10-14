package com.binarfood.binarfoodapp.Service;

import com.binarfood.binarfoodapp.DTO.OrderDTO;
import com.binarfood.binarfoodapp.DTO.OrderResponseDTO;
import com.binarfood.binarfoodapp.DTO.ResponseHandling;

public interface OrderService {
    ResponseHandling<OrderResponseDTO> createOrder(OrderDTO orderDTO);
}
