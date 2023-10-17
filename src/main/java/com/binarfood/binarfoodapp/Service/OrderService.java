package com.binarfood.binarfoodapp.Service;

import com.binarfood.binarfoodapp.DTO.*;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;

public interface OrderService {
    ResponseHandling<OrderResponseDTO> createOrder(OrderDTO orderDTO);

    ResponseHandling<OrderPaymentResponseDTO> payment(String code) throws FileNotFoundException, JRException;

    ResponseHandling<List<OrderGetResponseDTO>> getOrder(String code);
}
