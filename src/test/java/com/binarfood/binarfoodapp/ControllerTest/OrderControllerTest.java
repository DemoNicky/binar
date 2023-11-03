package com.binarfood.binarfoodapp.ControllerTest;

import com.binarfood.binarfoodapp.Controller.OrderController;
import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Service.OrderService;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder_Success() {
        OrderDTO orderDTO = new OrderDTO();
        OrderResponseDTO successResponse = new OrderResponseDTO();
        successResponse.setOrderCode("12345");
        successResponse.setUsername("John");
        successResponse.setAddress("123 Main St");
        successResponse.setPayment("Credit Card");

        ResponseHandling<OrderResponseDTO> successHandling = new ResponseHandling<>(successResponse, "Success message", null);

        Mockito.when(orderService.createOrder(orderDTO))
                .thenReturn(successHandling);

        ResponseEntity<ResponseHandling<OrderResponseDTO>> response = orderController.createOrder(orderDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseHandling<OrderResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("12345", responseBody.getData().getOrderCode());
        assertEquals("John", responseBody.getData().getUsername());
        assertEquals("123 Main St", responseBody.getData().getAddress());
        assertEquals("Credit Card", responseBody.getData().getPayment());
    }

    @Test
    public void testCreateOrder_BadRequest() {
        OrderDTO orderDTO = new OrderDTO();

        ResponseHandling<OrderResponseDTO> errorHandling = new ResponseHandling<>(null, "Error message", "Invalid order data");

        Mockito.when(orderService.createOrder(orderDTO))
                .thenReturn(errorHandling);

        ResponseEntity<ResponseHandling<OrderResponseDTO>> response = orderController.createOrder(orderDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResponseHandling<OrderResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Error message", responseBody.getMessage());
        assertEquals("Invalid order data", responseBody.getErrors());
    }

    @Test
    public void testPayment_Success() throws JRException, FileNotFoundException {
        String orderCode = "12345";

        OrderPaymentResponseDTO successResponse = new OrderPaymentResponseDTO();
        successResponse.setOrderCode(orderCode);
        successResponse.setUsername("John");
        successResponse.setAddress("123 Main St");
        successResponse.setPayment("Credit Card");
        successResponse.setOrderTime(String.valueOf(new Date()));

        ResponseHandling<OrderPaymentResponseDTO> successHandling = new ResponseHandling<>(successResponse, "Success message", null);

        Mockito.when(orderService.payment(orderCode))
                .thenReturn(successHandling);

        ResponseEntity<ResponseHandling<OrderPaymentResponseDTO>> response = orderController.payment(orderCode);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseHandling<OrderPaymentResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(orderCode, responseBody.getData().getOrderCode());
        assertEquals("John", responseBody.getData().getUsername());
        assertEquals("123 Main St", responseBody.getData().getAddress());
        assertEquals("Credit Card", responseBody.getData().getPayment());
    }

    @Test
    public void testPayment_NotFound() throws JRException, FileNotFoundException {
        String orderCode = "12345";

        ResponseHandling<OrderPaymentResponseDTO> errorHandling = new ResponseHandling<>(null, "Error message", "Order not found");

        Mockito.when(orderService.payment(orderCode))
                .thenReturn(errorHandling);

        ResponseEntity<ResponseHandling<OrderPaymentResponseDTO>> response = orderController.payment(orderCode);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ResponseHandling<OrderPaymentResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Error message", responseBody.getMessage());
        assertEquals("Order not found", responseBody.getErrors());
    }

    @Test
    public void testGetOrder_Success() {
        String userCode = "12345";
        List<OrderGetResponseDTO> orderResponseList = new ArrayList<>();
        OrderGetResponseDTO orderResponse = new OrderGetResponseDTO();
        orderResponse.setOrderCode("ORD123");
        orderResponse.setAddress("123 Main St");
        orderResponse.setOrderTime(new Date());
        orderResponse.setPaymentStatus("Paid");
        orderResponseList.add(orderResponse);

        ResponseHandling<List<OrderGetResponseDTO>> successHandling = new ResponseHandling<>(orderResponseList, "Success message", null);

        Mockito.when(orderService.getOrder(userCode))
                .thenReturn(successHandling);

        ResponseEntity<ResponseHandling<List<OrderGetResponseDTO>>> response = orderController.getOrder(userCode);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseHandling<List<OrderGetResponseDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.getData().size());
        assertEquals("ORD123", responseBody.getData().get(0).getOrderCode());
        assertEquals("123 Main St", responseBody.getData().get(0).getAddress());
        assertEquals("Paid", responseBody.getData().get(0).getPaymentStatus());
    }

    @Test
    public void testGetOrder_NotFound() {
        String userCode = "12345";

        ResponseHandling<List<OrderGetResponseDTO>> errorHandling = new ResponseHandling<>(null, "Error message", "No orders found");

        Mockito.when(orderService.getOrder(userCode))
                .thenReturn(errorHandling);

        ResponseEntity<ResponseHandling<List<OrderGetResponseDTO>>> response = orderController.getOrder(userCode);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ResponseHandling<List<OrderGetResponseDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Error message", responseBody.getMessage());
        assertEquals("No orders found", responseBody.getErrors());
    }



}
