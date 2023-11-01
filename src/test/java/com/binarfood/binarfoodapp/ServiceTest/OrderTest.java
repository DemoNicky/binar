package com.binarfood.binarfoodapp.ServiceTest;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.*;
import com.binarfood.binarfoodapp.Repository.OrderRepository;
import com.binarfood.binarfoodapp.Repository.ProductRepository;
import com.binarfood.binarfoodapp.Repository.UserRepository;
import com.binarfood.binarfoodapp.Service.Impl.OrderServiceImpl;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOrderUserNotFound() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserCode("nonexistentUser");

        ResponseHandling<OrderResponseDTO> response = orderService.createOrder(orderDTO);

        assertEquals("fail to create order", response.getMessage());
        assertEquals("user code not found", response.getErrors());
    }

    @Test
    public void testPaymentOrderNotFound() throws FileNotFoundException, JRException {
        when(orderRepository.findByOrderCode("NonExistentOrder")).thenReturn(Optional.empty());

        ResponseHandling<OrderPaymentResponseDTO> response;
        try {
            response = orderService.payment("NonExistentOrder");
        } catch (FileNotFoundException | JRException e) {
            response = null;
        }

        assertEquals("fail to pay", response.getMessage());
        assertEquals("code not found", response.getErrors());
    }

    @Test
    public void testPaymentOrderAlreadyCompleted() throws FileNotFoundException, JRException {
        Order order = new Order();
        order.setCompleted(true);
        when(orderRepository.findByOrderCode("CompletedOrder")).thenReturn(Optional.of(order));

        ResponseHandling<OrderPaymentResponseDTO> response;
        try {
            response = orderService.payment("CompletedOrder");
        } catch (FileNotFoundException | JRException e) {
            response = null;
        }

        assertEquals("fail to pay", response.getMessage());
        assertEquals("payment already successfull", response.getErrors());
    }


    @Test
    public void testGetOrderSuccess() {
        String userCode = "User123";

        Merchant merchant = new Merchant();
        merchant.setMerchantCode("Merchant123");
        merchant.setMerchantName("Merchant Name");

        Product product = new Product();
        product.setProductCode("Product123");
        product.setProductName("Product Name");
        product.setPrice(new BigDecimal("10.0"));
        product.setMerchant(merchant);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);

        orderDetail.setQuantity(2);

        Order order = new Order();
        order.setOrderCode("Order123");
        order.setDestinationAddress("Address123");
        order.setOrderTime(new Date());
        order.setCompleted(false);
        order.setOrderDetail(Collections.singletonList(orderDetail));

        User user = new User();
        user.setUserCode(userCode);

        when(orderRepository.findByUserCode(userCode)).thenReturn(Optional.of(Collections.singletonList(order)));

        ResponseHandling<List<OrderGetResponseDTO>> response = orderService.getOrder(userCode);

        assertEquals("success get data", response.getMessage());
        assertEquals(1, response.getData().size());

    }

    @Test
    public void testGetOrderUserNotFound() {
        String userCode = "NonExistentUser";
        when(orderRepository.findByUserCode(userCode)).thenReturn(Optional.empty());

        ResponseHandling<List<OrderGetResponseDTO>> response = orderService.getOrder(userCode);

        assertEquals("fail to get data", response.getMessage());
        assertEquals("user with code NonExistentUser not found", response.getErrors());
        assertEquals(null, response.getData());
    }
}
