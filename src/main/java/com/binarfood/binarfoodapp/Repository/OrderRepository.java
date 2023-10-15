package com.binarfood.binarfoodapp.Repository;

import com.binarfood.binarfoodapp.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByOrderCode(String code);

    @Query("SELECT o FROM Order o WHERE o.user.userCode = :userCode")
    Optional<List<Order>> findByUserCode(@Param("userCode") String userCode);

}
