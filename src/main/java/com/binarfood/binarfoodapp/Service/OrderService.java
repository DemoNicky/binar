package com.binarfood.binarfoodapp.Service;

import com.binarfood.binarfoodapp.DTO.*;

import java.util.List;

public interface OrderService {
    ResponseHandling<OrderResponseDTO> createOrder(OrderDTO orderDTO);

    ResponseHandling<OrderPaymentResponseDTO> payment(String code);

    ResponseHandling<List<OrderGetResponseDTO>> getOrder(String code);
}
