package com.ll.demo.article.controller;

import com.ll.demo.article.dto.ArticleRequestDto;
import com.ll.demo.article.entity.Article;
import com.ll.demo.article.service.ArticleService;
import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;

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

    //글 삭제
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public GlobalResponse delete(@PathVariable("id") Long id, Principal principal) {

        Article article = articleService.getArticleById(id);
        Member member = memberService.findByUsername(principal.getName());

        //권한 확인
        if (member == null) {
            return GlobalResponse.of("401", "로그인이 필요한 서비스입니다.");
        }
        else if (article.getMember().getId() != member.getId()) {
            return GlobalResponse.of("403", "삭제 권한이 없습니다.");
        }

        articleService.delete(article);
        return GlobalResponse.of("200", "Article deleted");
    }
}
