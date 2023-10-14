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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

        BigDecimal totalPrice = getBigDecimal(productResponseDTOS);

        int qty = getQty(orderDetails);

        OrderDetailResponseDTO orderDetailResponseDTO = new OrderDetailResponseDTO();
        orderDetailResponseDTO.setTotalPrice(totalPrice);
        orderDetailResponseDTO.setQuantity(qty);
        orderDetailResponseDTO.setProductOrderResponseDTO(productResponseDTOS);
        responseDTO.setOrderDetailResponseDTO(orderDetailResponseDTO);
        response.setMessage("success to order");
        response.setData(responseDTO);
        return response;
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

    private BigDecimal getBigDecimal(List<ProductOrderResponseDTO> productResponseDTOS) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ProductOrderResponseDTO detail : productResponseDTOS) {
            totalPrice = totalPrice.add(detail.getPrice());
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
