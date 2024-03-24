package com.ll.demo.order.service;

import com.ll.demo.cart.entity.CartItem;
import com.ll.demo.cart.service.CartService;
import com.ll.demo.cash.entity.CashLog;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.service.MemberService;
import com.ll.demo.order.entity.Order;
import com.ll.demo.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final MemberService memberService;

    @Transactional
    public Order createFromCart(Member buyer) {
        List<CartItem> cartItems = cartService.findItemsByBuyer(buyer);

        Order order = Order.builder()
                .buyer(buyer)
                .build();

        // 카트에 아이템을 주문에 담고, 카트 아이템 삭제
        cartItems.stream()
                .forEach(order::addItem);
        orderRepository.save(order);
        cartItems.stream()
                .forEach(cartService::delete);
        return order;
    }
    public void payByCashOnly(Order order) {
        Member buyer = order.getBuyer();
        long restCash = buyer.getRestCash();
        long payPrice = order.calcPayPrice();

        if (payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }

        memberService.addCash(buyer, payPrice * -1, CashLog.EvenType.사용__예치금_주문결제);

        payDone(order);
    }
    private void payDone(Order order) {
        order.setPaymentDone();
    }
}