package com.binarfood.binarfoodapp.Service.Impl;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.Order;
import com.binarfood.binarfoodapp.Entity.OrderDetail;
import com.binarfood.binarfoodapp.Entity.Product;
import com.binarfood.binarfoodapp.Entity.User;
import com.binarfood.binarfoodapp.Repository.OrderRepository;
import com.binarfood.binarfoodapp.Repository.ProductRepository;
import com.binarfood.binarfoodapp.Repository.UserRepository;
import com.binarfood.binarfoodapp.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseHandling<OrderResponseDTO> createOrder(OrderDTO orderDTO) {
        ResponseHandling<OrderResponseDTO> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByUserCode(orderDTO.getUserCode());
        if (!user.isPresent()) {
            response.setMessage("fail to create order");
            response.setErrors("user code not found");
            return response;
        }
        Order order = new Order();
        order.setOrderCode(getKode());
        order.setOrderTime(new Date());
        order.setCompleted(false);
        order.setDestinationAddress(orderDTO.getDestinationAddress());

        List<OrderDetail> orderDetails = getOrderDetails(orderDTO);

        if (orderDetails.isEmpty() || orderDetails == null) {
            response.setMessage("fail to order");
            response.setErrors("product not found");
            return response;
        }

        order.setOrderDetail(orderDetails);

        order.setUser(user.get());
        orderRepository.save(order);

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderCode(order.getOrderCode());
        responseDTO.setUsername(order.getUser().getUsername());
        responseDTO.setAddress(order.getDestinationAddress());
        responseDTO.setOrderTime(order.getOrderTime());
        responseDTO.setPayment("unpaid");

        List<ProductOrderResponseDTO> productResponseDTOS = getProductOrderResponseDTOS(orderDetails);

        int qty = getQty(orderDetails);

        BigDecimal totalPrice = getBigDecimal(orderDetails);

        OrderDetailResponseDTO orderDetailResponseDTO = new OrderDetailResponseDTO();
        orderDetailResponseDTO.setTotalPrice(totalPrice);
        orderDetailResponseDTO.setQuantity(qty);
        orderDetailResponseDTO.setProductOrderResponseDTO(productResponseDTOS);
        responseDTO.setOrderDetailResponseDTO(orderDetailResponseDTO);
        response.setMessage("success to order");
        response.setData(responseDTO);
        return response;
    }

    @Override
    public ResponseHandling<OrderPaymentResponseDTO> payment(String code) {
        Optional<Order> order = orderRepository.findByOrderCode(code);
        ResponseHandling<OrderPaymentResponseDTO> response = new ResponseHandling<>();
        if (!order.isPresent()){
            response.setMessage("fail to pay");
            response.setErrors("code not found");
            return response;
        }
        Order order1 = order.get();
        order1.setCompleted(true);
        orderRepository.save(order1);

        OrderPaymentResponseDTO orderPaymentResponse = new OrderPaymentResponseDTO();
        orderPaymentResponse.setOrderCode(order1.getOrderCode());
        orderPaymentResponse.setUsername(order1.getUser().getUsername());
        orderPaymentResponse.setAddress(order1.getDestinationAddress());
        orderPaymentResponse.setOrderTime(order1.getOrderTime());
        orderPaymentResponse.setPayment("paid");

        List<ProductOrderResponseDTO> productOrderResponseDTOS = getProductOrderResponseDTOS(order1);

        int qty = getAnInt(productOrderResponseDTOS);
        BigDecimal totalPrice = getTotalPrice(productOrderResponseDTOS);

        OrderDetailResponseDTO responseDTO = new OrderDetailResponseDTO();
        responseDTO.setQuantity(qty);
        responseDTO.setTotalPrice(totalPrice);
        responseDTO.setProductOrderResponseDTO(productOrderResponseDTOS);
        orderPaymentResponse.setOrderDetailResponseDTO(responseDTO);
        response.setData(orderPaymentResponse);
        response.setMessage("successfully paid the product !!!");
        return response;
    }

    @Override
    public ResponseHandling<List<OrderGetResponseDTO>> getOrder(String usercode) {
        Optional<List<Order>> order = orderRepository.findByUserCode(usercode);
        ResponseHandling<List<OrderGetResponseDTO>> response = new ResponseHandling<>();
        if (!order.isPresent()){
            response.setMessage("fail to get data");
            response.setErrors("user with code " +usercode+ " not found");
            return response;
        }
        List<Order> orderList = order.get();
        List<OrderGetResponseDTO> orders = orderList.stream().map((p)->{
            OrderGetResponseDTO responseDTO = new OrderGetResponseDTO();
            responseDTO.setOrderCode(p.getOrderCode());
            responseDTO.setAddress(p.getDestinationAddress());
            responseDTO.setOrderTime(p.getOrderTime());
            responseDTO.setPaymentStatus(p.getCompleted() ? "Paid" : "Unpaid");

            List<ProductOrderResponseDTO> productOrderResponse = new ArrayList<>();
            for (OrderDetail orderDetail : p.getOrderDetail()){
                ProductOrderResponseDTO responseDTO1 = new ProductOrderResponseDTO();
                responseDTO1.setProductCode(orderDetail.getProduct().getProductCode());
                responseDTO1.setProductName(orderDetail.getProduct().getProductName());
                responseDTO1.setPrice(orderDetail.getProduct().getPrice());
                responseDTO1.setMerchantCode(orderDetail.getProduct().getMerchant().getMerchantCode());
                responseDTO1.setMerchantName(orderDetail.getProduct().getMerchant().getMerchantName());
                responseDTO1.setQty(orderDetail.getQuantity());
                productOrderResponse.add(responseDTO1);
            }

            OrderDetailResponseDTO orderDetailResponse = new OrderDetailResponseDTO();
            int qty = 0;
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (OrderDetail orderDetail : p.getOrderDetail()){
                qty += orderDetail.getQuantity();
                BigDecimal quantity = new BigDecimal(orderDetail.getQuantity());
                totalPrice.multiply(quantity);
            }
            orderDetailResponse.setQuantity(qty);
            orderDetailResponse.setTotalPrice(totalPrice);
            orderDetailResponse.setProductOrderResponseDTO(productOrderResponse);
            responseDTO.setOrderDetailResponseDTO(orderDetailResponse);
            return responseDTO;
        }).collect(Collectors.toList());

        response.setData(orders);
        response.setMessage("success get data");
        return response;
    }

    private List<ProductOrderResponseDTO> getProductOrderResponseDTOS(Order order1) {
        List<ProductOrderResponseDTO> productOrderResponseDTOS = order1.getOrderDetail().stream().map((p)->{
            ProductOrderResponseDTO responseDTO = new ProductOrderResponseDTO();
            responseDTO.setProductCode(p.getProduct().getProductCode());
            responseDTO.setProductName(p.getProduct().getProductName());
            responseDTO.setPrice(p.getProduct().getPrice());
            responseDTO.setMerchantCode(p.getProduct().getMerchant().getMerchantCode());
            responseDTO.setMerchantName(p.getProduct().getMerchant().getMerchantName());
            responseDTO.setQty(p.getQuantity());
            return responseDTO;
        }).collect(Collectors.toList());
        return productOrderResponseDTOS;
    }

    private BigDecimal getTotalPrice(List<ProductOrderResponseDTO> productOrderResponseDTOS) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ProductOrderResponseDTO responseDTO: productOrderResponseDTOS){
            BigDecimal productPrice = responseDTO.getPrice();
            BigDecimal quantity = new BigDecimal(responseDTO.getQty());
            BigDecimal total = productPrice.multiply(quantity);
            totalPrice = totalPrice.add(total);
        }
        return totalPrice;
    }

    private int getAnInt(List<ProductOrderResponseDTO> productOrderResponseDTOS) {
        int qty = 0;
        for (ProductOrderResponseDTO res : productOrderResponseDTOS){
            qty += res.getQty();
        }
        return qty;
    }

    private List<OrderDetail> getOrderDetails(OrderDTO orderDTO) {
        return orderDTO.getOrderItem().stream().map((p) -> {
            Optional<Product> product = productRepository.findByProductCode(p.getProductCode());
            Product product1 = product.get();
            if (!product.isPresent()) {
                return null;
            }
            BigDecimal priceTotal = product.get().getPrice().multiply(BigDecimal.valueOf(p.getQuantity()));
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(p.getQuantity());
            orderDetail.setTotalPrice(priceTotal);
            orderDetail.setProduct(product1);
            return orderDetail;
        }).collect(Collectors.toList());
    }

    private List<ProductOrderResponseDTO> getProductOrderResponseDTOS(List<OrderDetail> orderDetails) {
        List<ProductOrderResponseDTO> productResponseDTOS = orderDetails.stream().map((p) -> {
            ProductOrderResponseDTO productOrderResponseDTO = new ProductOrderResponseDTO();
            productOrderResponseDTO.setProductCode(p.getProduct().getProductCode());
            productOrderResponseDTO.setProductName(p.getProduct().getProductName());
            productOrderResponseDTO.setPrice(p.getProduct().getPrice());
            productOrderResponseDTO.setMerchantCode(p.getProduct().getMerchant().getMerchantCode());
            productOrderResponseDTO.setMerchantName(p.getProduct().getMerchant().getMerchantName());
            return productOrderResponseDTO;
        }).collect(Collectors.toList());
        return productResponseDTOS;
    }

    private BigDecimal getBigDecimal(List<OrderDetail> orderDetails) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderDetail detail : orderDetails) {
            BigDecimal productPrice = detail.getProduct().getPrice();
            BigDecimal quantity = new BigDecimal(detail.getQuantity());
            BigDecimal total = productPrice.multiply(quantity);
            totalPrice = totalPrice.add(total);
        }
        return totalPrice;
    }

    private int getQty(List<OrderDetail> orderDetails) {
        int qty = 0;
        for (OrderDetail orderDetail : orderDetails) {
            qty += orderDetail.getQuantity();
        }
        return qty;
    }

    private String getKode() {
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 5);
        return kode;
    }

}
