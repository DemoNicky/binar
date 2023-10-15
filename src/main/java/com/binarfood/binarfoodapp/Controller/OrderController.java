package com.binarfood.binarfoodapp.Controller;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(
            path = "/{ordercode}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<OrderPaymentResponseDTO>>payment(@PathVariable("ordercode")String code){
        ResponseHandling<OrderPaymentResponseDTO> response = orderService.payment(code);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/{usercode}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<OrderGetResponseDTO>>>getOrder(@PathVariable("usercode")String code){
        ResponseHandling<List<OrderGetResponseDTO>> response = orderService.getOrder(code);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
