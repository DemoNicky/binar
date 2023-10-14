package com.binarfood.binarfoodapp.Controller;

import com.binarfood.binarfoodapp.DTO.OrderDTO;
import com.binarfood.binarfoodapp.DTO.OrderResponseDTO;
import com.binarfood.binarfoodapp.DTO.ResponseHandling;
import com.binarfood.binarfoodapp.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<OrderResponseDTO>>createOrder(@RequestBody OrderDTO orderDTO){
        ResponseHandling<OrderResponseDTO> response = orderService.createOrder(orderDTO);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
