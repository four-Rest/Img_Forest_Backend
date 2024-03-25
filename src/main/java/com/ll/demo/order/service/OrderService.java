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

    @Transactional
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

    @Transactional
    public void refund(Order order) {
        long payPrice = order.calcPayPrice();

        memberService.addCash(order.getBuyer(), payPrice, CashLog.EvenType.환불__예치금_주문결제);

        order.setCancelDone();
        order.setRefundDone();
    }

    // 주문과 금액이 일치하지 않으면 예외처리
    public boolean checkPayPrice(Order order, long payPrice) {
        if(order.calcPayPrice() != payPrice) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다");
        }
        return true;
    }

    // PayPrice가 부족하면 부족한만큼 restCash이용해서 결제 가능, 불가능하다면 예외 발생
    public void checkCanPay(Order order, long pgPayPrice) {
        if(!canPay(order,pgPayPrice))
            throw new IllegalArgumentException("PG 결제 금액 혹은 예치금이 부족하여 결제할 수 없습니다.");
    }

    public boolean canPay(Order order,long pgPayPrice) {
        long restCash = order.getBuyer().getRestCash();

        return order.calcPayPrice() <= restCash + pgPayPrice;
    }


    // 토스페이먼츠로 결제하는 기능 , 부족한 금액은 자동으로 예치금에서 차감
    @Transactional
    public void payByTossPayments(Order order, long pgPayPrice) {
        Member buyer = order.getBuyer();
        long restCash = buyer.getRestCash();
        long payPrice = order.calcPayPrice();

        long useRestCash = payPrice - pgPayPrice;

        memberService.addCash(buyer, pgPayPrice, CashLog.EvenType.충전__토스페이먼츠);
        memberService.addCash(buyer, pgPayPrice * -1, CashLog.EvenType.사용__토스페이먼츠_주문결제);

        // transactional이기 때문에 이 작업이 안되면 위에 작업 다 취소
        if (useRestCash > 0) {
            if (useRestCash > restCash) {
                throw new RuntimeException("예치금이 부족합니다.");
            }
            memberService.addCash(buyer, useRestCash * -1, CashLog.EvenType.사용__예치금_주문결제);
        }
        payDone(order);
    }
}