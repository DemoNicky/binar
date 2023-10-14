package com.binarfood.binarfoodapp.Repository;

import com.binarfood.binarfoodapp.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
