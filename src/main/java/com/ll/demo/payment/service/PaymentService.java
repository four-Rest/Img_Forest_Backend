package com.ll.demo.payment.service;


import com.ll.demo.article.entity.Article;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.repository.MemberRepository;
import com.ll.demo.member.service.MemberService;
import com.ll.demo.order.dto.OrderRequestDto;
import com.ll.demo.order.entity.Order;
import com.ll.demo.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orderRepository;
    private final ArticleRepository articleRepository;

    public void saveOrder(Member member, OrderRequestDto orderRequestDto) {
        Article article = articleRepository.findArticleById(orderRequestDto.getArticleId())
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));
        Order order = Order.builder()
                .member(member)
                .article(article)
                .orderNumber(orderRequestDto.getOrderNumber())
                .orderDate(new Timestamp(System.currentTimeMillis()))
                .paymentMethod(orderRequestDto.getPaymentMethod()).build();

        orderRepository.save(order);

    }
}
