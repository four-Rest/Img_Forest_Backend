package com.ll.demo.cart.controller;


import com.ll.demo.article.entity.Article;
import com.ll.demo.article.service.ArticleService;
import com.ll.demo.cart.dto.CartDto;
import com.ll.demo.cart.entity.CartItem;
import com.ll.demo.cart.service.CartService;
import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ArticleService articleService;
    private final MemberService memberService;

    // 장바구니 추가
    @PostMapping("/add/{id}")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse addArticle(@PathVariable long id, Principal principal) {

        Article article = articleService.findById(id);

        Member member = memberService.findByUsername(principal.getName());
        cartService.addItem(member,article);

        List<CartItem> cartItems = cartService.findByBuyer(member);
        return GlobalResponse.of("200","장바구니 담기 성공",cartItems);
    }

    // 장바구니 삭제
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse remove(@PathVariable long id, Principal principal) {

        Article article = articleService.findById(id);

        Member member = memberService.findByUsername(principal.getName());

        cartService.removeItem(member,article);
        List<CartItem> cartItems = cartService.findByBuyer(member);
        return GlobalResponse.of("200","장바구니 삭제 성공",cartItems);
    }

    // 장바구니 리스트
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse showList(Principal principal) {
        Member member = memberService.findByUsername(principal.getName());
        List<CartItem> cartItems = cartService.findByBuyerOrderByIdDesc(member);

        // 장바구니 아이템 총 가격 계산
        long totalPrice = cartItems
                .stream()
                .map(CartItem::getArticle)
                .mapToLong(Article::getPrice)
                .sum();

        CartDto cartDto = new CartDto();

        cartDto.setCartItems(cartItems);
        cartDto.setTotalPrice(totalPrice);
        return GlobalResponse.of("200","장바구니 조회 성공",cartDto);
    }
}
