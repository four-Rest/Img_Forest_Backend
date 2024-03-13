package com.ll.demo.order.service;


import com.ll.demo.article.entity.Article;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.member.entity.Member;
import com.ll.demo.order.dto.OrderRequestDto;
import com.ll.demo.order.entity.Order;
import com.ll.demo.order.repository.OrderRepository;
import com.ll.demo.payment.entity.Payment;
import com.ll.demo.payment.enums.PayStatus;
import com.ll.demo.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ArticleRepository articleRepository;
    private final PaymentRepository paymentRepository;

    public Order saveOrderAndPayment(Member member,Article article) {

        // 임시 결제내역 생성
        Payment payment = Payment.builder()
                .price(1000L)
                .status(PayStatus.WAITING_FOR_PAYMENT)
                .build();

        paymentRepository.save(payment);

        // 주문 생성
        Order order = Order.builder()
                .member(member)
                .price(1000L)
                .article(article)
                .orderUid(UUID.randomUUID().toString())
                .payment(payment)
                .orderDate(new Timestamp(System.currentTimeMillis()))
                .build();

        return orderRepository.save(order);
    }


}
