package com.binarfood.binarapp.Service.Impl;

import com.binarfood.binarapp.DTO.OrderDetailResponseDTO;
import com.binarfood.binarapp.DTO.OrderResponseDTO;
import com.binarfood.binarapp.Entity.Order;
import com.binarfood.binarapp.Entity.OrderDetail;
import com.binarfood.binarapp.Entity.Product;
import com.binarfood.binarapp.Entity.User;
import com.binarfood.binarapp.Repository.OrderDetailRepository;
import com.binarfood.binarapp.Repository.OrderRepository;
import com.binarfood.binarapp.Repository.ProductRepository;
import com.binarfood.binarapp.Repository.UserRepository;
import com.binarfood.binarapp.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OrderDetailResponseDTO orderStuff(Map<String, Integer> produkMap) {
        logger.info("Ordering products: {}", produkMap);
        List<Product> orderedProducts = new ArrayList<>();
        int price = 0;
        int qty = 0;
        OrderDetail orderDetail = new OrderDetail();
        for (Map.Entry<String, Integer> produk : produkMap.entrySet()){
            String productCode = produk.getKey();
            Optional<Product> product = productRepository.findByProductCode(productCode);
            if (product.isPresent()) {
                Product productget = product.get();
                orderedProducts.add(productget);

                int jumlah = produk.getValue();
                qty += jumlah;

                BigDecimal hargaProduk = productget.getPrice();
                int harprod = hargaProduk.intValue();
                price += harprod * jumlah;
            }else {
                throw new NullPointerException("Produk dengan kode " + productCode + " tidak ditemukan");
            }
        }
        BigDecimal totalPrice = new BigDecimal(price);

        orderDetail.setQuantity(qty);
        orderDetail.setTotalPrice(totalPrice);
        orderDetail.setProduct(orderedProducts);
        orderDetailRepository.save(orderDetail);
        logger.info("Order completed. Quantity: {}, Total Price: {}", qty, totalPrice);
        return OrderDetailResponseDTO.builder()
                .quantity(qty)
                .totalPrice(totalPrice)
                .product(orderedProducts)
                .build();

    }

    @Override
    public OrderResponseDTO buyStuff(String alamat, String id) {
        logger.info("Buying stuff with user ID: {} and destination address: {}", id, alamat);

        User user = userRepository.findById(id).get();
        Date date = new Date();
        Order order = new Order();
        order.setOrderTime(date);
        order.setDestinationAddress(alamat);
        order.setCompleted(Boolean.TRUE);
        order.setUser(user);
        orderRepository.save(order);

        logger.info("Purchase completed. Order time: {}, Destination Address: {}", date, alamat);

        return OrderResponseDTO.builder()
                .orderTime(order.getOrderTime())
                .destinationAddress(order.getDestinationAddress())
                .completed(order.getCompleted())
                .build();
    }

}
