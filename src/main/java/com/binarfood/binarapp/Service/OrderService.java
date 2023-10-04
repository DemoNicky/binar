package com.binarfood.binarapp.Service;

import com.binarfood.binarapp.DTO.OrderDetailResponseDTO;
import com.binarfood.binarapp.DTO.OrderResponseDTO;
import com.binarfood.binarapp.Entity.Order;

import java.util.Map;

public interface OrderService {

    OrderDetailResponseDTO orderStuff(Map<String, Integer> produkk);

    OrderResponseDTO buyStuff(String alamat, String id);
}
