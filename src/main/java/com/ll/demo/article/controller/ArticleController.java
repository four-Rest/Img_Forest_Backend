package com.ll.demo.article.controller;

import com.ll.demo.article.dto.ArticleRequestDto;
import com.ll.demo.article.entity.Article;
import com.ll.demo.article.service.ArticleService;
import com.ll.demo.global.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    //전체 글 조회
    @GetMapping("")
    public GlobalResponse findAll(@RequestParam(defaultValue = "0") int page) {

        Page<Article> articles = articleService.findAll(page);
        return GlobalResponse.of("200", "paging success", articles);
    }

    //글 작성
//    @PostMapping("")
//    public String write(ArticleRequestDto articleRequestDto, Authentication authentication) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        articleService.create(articleRequestDto, userDetails.getUsername());
//
//        return "redirect:/";
//    }
}
