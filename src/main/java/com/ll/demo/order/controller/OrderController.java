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
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.security.Principal;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;

    // 주문 상세
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public GlobalResponse showDetail(@PathVariable long id, Principal principal) {
       Order order = orderService.findById(id).orElse(null);

       if (order == null) {
           throw new IllegalArgumentException("주문을 찾을 수 없습니다");
       }
       Member member = memberService.findByUsername(principal.getName());

       long restCash = member.getRestCash();

       if(!orderService.actorCanSee(member,order)) {
           throw new IllegalArgumentException("권한이 없습니다");
       }

       return GlobalResponse.of("200","success",order);
    }

}
