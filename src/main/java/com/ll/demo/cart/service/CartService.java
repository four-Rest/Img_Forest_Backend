package com.ll.demo.cart.service;

import com.ll.demo.article.entity.Article;
import com.ll.demo.cart.entity.CartItem;
import com.ll.demo.cart.repository.CartItemRepository;
import com.ll.demo.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartItem addItem(Member member, Article article) {
        CartItem cartItem = CartItem.builder()
                .member(member)
                .article(article)
                .build();

        cartItemRepository.save(cartItem);

        return cartItem;
    }
}