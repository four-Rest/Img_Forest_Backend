package com.ll.demo.order.controller;


import com.ll.demo.article.entity.Article;
import com.ll.demo.article.service.ArticleService;
import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.service.MemberService;
import com.ll.demo.order.dto.OrderRequestDto;
import com.ll.demo.order.entity.Order;
import com.ll.demo.order.service.OrderService;
import com.ll.demo.payment.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ArticleService articleService;


    // 결제 완료 후 주문내역 저장
    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public GlobalResponse saveOrder(Principal principal, @RequestBody PaymentDto paymentDto) {
        String username = principal.getName();
        Member member = memberService.findByUsername(username);
        Article article = articleService.getArticleById(paymentDto.getArticleId());
        Order order = orderService.saveOrderAndPayment(member,article);
        String message = order.getOrderUid();

        return GlobalResponse.of("200",message);

    }

}
