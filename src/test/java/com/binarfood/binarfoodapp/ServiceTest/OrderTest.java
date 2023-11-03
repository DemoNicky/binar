package com.binarfood.binarfoodapp.ServiceTest;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.*;
import com.binarfood.binarfoodapp.Repository.OrderDetailRepository;
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
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
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

    @Mock
    private OrderDetailRepository orderDetailRepository;

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

    @Test
    public void testGetCustomFileDTOS_Success() {
        OrderServiceImpl orderConverter = new OrderServiceImpl();
        List<ProductOrderResponseDTO> productOrderResponseDTOS = new ArrayList<>();

        List<CustomFileDTO> result = orderConverter.getCustomFileDTOS(productOrderResponseDTOS);

        assertNotNull(result);
        assertEquals(productOrderResponseDTOS.size(), result.size());

        for (int i = 0; i < result.size(); i++) {
            ProductOrderResponseDTO source = productOrderResponseDTOS.get(i);
            CustomFileDTO customFileDTO = result.get(i);

            assertEquals(source.getProductCode(), customFileDTO.getProductCode());
            assertEquals(source.getProductName(), customFileDTO.getProductName());
            assertEquals(source.getPrice(), customFileDTO.getPrice());
            assertEquals(source.getMerchantName(), customFileDTO.getMerchantName());
            assertEquals(source.getQty(), customFileDTO.getQty());
        }
    }

    @Test
    public void testGetCustomFileDTOS_EmptyList() {
        OrderServiceImpl orderConverter = new OrderServiceImpl();
        List<ProductOrderResponseDTO> productOrderResponseDTOS = new ArrayList<>();

        List<CustomFileDTO> result = orderConverter.getCustomFileDTOS(productOrderResponseDTOS);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetProductOrderResponseDTOS_Success() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        Order order = mock(Order.class);
        List<OrderDetail> orderDetails = new ArrayList<>();
        when(order.getOrderDetail()).thenReturn(orderDetails);

        List<ProductOrderResponseDTO> result = orderService.getProductOrderResponseDTOS(order);

        assertNotNull(result);
        assertEquals(orderDetails.size(), result.size());

        for (int i = 0; i < result.size(); i++) {
            OrderDetail source = orderDetails.get(i);
            ProductOrderResponseDTO responseDTO = result.get(i);

            assertEquals(source.getProduct().getProductCode(), responseDTO.getProductCode());
            assertEquals(source.getProduct().getProductName(), responseDTO.getProductName());
            assertEquals(source.getProduct().getPrice(), responseDTO.getPrice());
            assertEquals(source.getProduct().getMerchant().getMerchantCode(), responseDTO.getMerchantCode());
            assertEquals(source.getProduct().getMerchant().getMerchantName(), responseDTO.getMerchantName());
            assertEquals(source.getQuantity(), responseDTO.getQty());
        }
    }

    @Test
    public void testGetProductOrderResponseDTOS_EmptyList() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        Order order = mock(Order.class);

        when(order.getOrderDetail()).thenReturn(new ArrayList<>());

        List<ProductOrderResponseDTO> result = orderService.getProductOrderResponseDTOS(order);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTotalPrice_Success() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        List<ProductOrderResponseDTO> productOrderResponseDTOS = new ArrayList<>();

        BigDecimal totalPrice = orderService.getTotalPrice(productOrderResponseDTOS);

        assertNotNull(totalPrice);
        assertEquals(BigDecimal.ZERO, totalPrice);
    }

    @Test
    public void testGetTotalPrice_EmptyList() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        List<ProductOrderResponseDTO> productOrderResponseDTOS = new ArrayList<>();

        BigDecimal totalPrice = orderService.getTotalPrice(productOrderResponseDTOS);

        assertNotNull(totalPrice);
        assertEquals(BigDecimal.ZERO, totalPrice);
    }

    @Test
    public void testGetAnInt_Success() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        List<ProductOrderResponseDTO> productOrderResponseDTOS = new ArrayList<>();

        int qty = orderService.getAnInt(productOrderResponseDTOS);
        int expectedQty = 0;
        for (ProductOrderResponseDTO dto : productOrderResponseDTOS) {
            expectedQty += dto.getQty();
        }

        assertEquals(expectedQty, qty);
    }

    @Test
    public void testGetAnInt_EmptyList() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        List<ProductOrderResponseDTO> productOrderResponseDTOS = new ArrayList<>();

        int qty = orderService.getAnInt(productOrderResponseDTOS);

        assertEquals(0, qty);
    }

    @Test
    public void testGetBigDecimal_Success() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(createOrderDetail(BigDecimal.valueOf(10), 3, createProduct(BigDecimal.valueOf(10))));
        orderDetails.add(createOrderDetail(BigDecimal.valueOf(15), 2, createProduct(BigDecimal.valueOf(15))));

        when(orderDetailRepository.findAll()).thenReturn(orderDetails);

        BigDecimal result = orderService.getBigDecimal(orderDetails);

        BigDecimal expectedTotal = BigDecimal.valueOf(10 * 3 + 15 * 2);
        assertEquals(expectedTotal, result);
    }

    private OrderDetail createOrderDetail(BigDecimal price, int quantity, Product product) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setTotalPrice(price.multiply(BigDecimal.valueOf(quantity)));
        orderDetail.setQuantity(quantity);
        orderDetail.setProduct(product);
        return orderDetail;
    }

    private Product createProduct(BigDecimal price) {
        Product product = new Product();
        product.setPrice(price);
        return product;
    }

    @Test
    public void testGetQty_Success() {
        OrderDetail orderDetail1 = createOrderDetail(5);
        OrderDetail orderDetail2 = createOrderDetail(3);
        OrderDetail orderDetail3 = createOrderDetail(2);

        List<OrderDetail> orderDetails = Arrays.asList(orderDetail1, orderDetail2, orderDetail3);

        int expectedQty = 5 + 3 + 2;

        int result = orderService.getQty(orderDetails);
        assertEquals(expectedQty, result);
    }

    @Test
    public void testGetQty_EmptyList() {
        List<OrderDetail> orderDetails = new ArrayList<>();

        int expectedQty = 0;

        int result = orderService.getQty(orderDetails);
        assertEquals(expectedQty, result);
    }

    private OrderDetail createOrderDetail(int quantity) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setQuantity(quantity);
        return orderDetail;
    }

}
