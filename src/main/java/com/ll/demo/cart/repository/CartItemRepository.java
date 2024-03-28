package com.ll.demo.cart.repository;

import com.ll.demo.cart.entity.CartItem;
import com.ll.demo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    List<CartItem> findByBuyer(Member buyer);
}
