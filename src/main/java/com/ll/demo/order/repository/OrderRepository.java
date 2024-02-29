package com.ll.demo.order.repository;

import com.ll.demo.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Long, Order> {

}
