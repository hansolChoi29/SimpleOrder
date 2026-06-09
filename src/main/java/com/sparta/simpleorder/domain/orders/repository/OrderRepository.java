package com.sparta.simpleorder.domain.orders.repository;

import com.sparta.simpleorder.domain.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
