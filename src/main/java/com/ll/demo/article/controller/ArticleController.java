package com.ll.demo.article.controller;

import com.ll.demo.article.dto.ArticleDetailResponseDto;
import com.ll.demo.article.dto.ArticleRequestDto;
import com.ll.demo.article.entity.Article;
import com.ll.demo.article.entity.Image;
import com.ll.demo.article.entity.Tag;
import com.ll.demo.article.service.ArticleService;
import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;

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

    //단일 글 조회
    @GetMapping("/{id}")
    public GlobalResponse show(@PathVariable("id") Long id) {

        Article article = articleService.getArticleById(id);

        return GlobalResponse.of("200", "success",
                ArticleDetailResponseDto.builder()
                        .article(article)
                        .build());
    }

    // 글 생성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public GlobalResponse create(
            @Valid @RequestBody ArticleRequestDto articleRequestDto,
            Principal principal) throws IOException {
        // 사용자 인증 정보 가져오기
        Member member = memberService.findByUsername(principal.getName());

        if (member == null) {
            return GlobalResponse.of("401", "로그인이 필요한 서비스입니다.");
        }

        articleService.create(articleRequestDto, member);

        return GlobalResponse.of("201", "Article created");
    }

    //글 수정
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public GlobalResponse update(
            @PathVariable("id") Long id,
            Principal principal,
            @Valid @RequestBody ArticleRequestDto articleRequestDto
    ) throws IOException {
        Article article = articleService.getArticleById(id);
        Member member = memberService.findByUsername(principal.getName());

        //권한 확인
        if (member == null) {
            return GlobalResponse.of("401", "로그인이 필요한 서비스입니다.");
        } else if (article.getMember().getId() != member.getId()) {
            return GlobalResponse.of("403", "수정 권한이 없습니다.");
        }

        //유료 아티클의 경우 이미지 수정이 불가능함
        if (article.isPaid()) {
            articleService.modifyPaidArticle(article, articleRequestDto);
        }
        //무료 아티클의 경우 이미지 수정이 가능함
        else {
            articleService.modifyUnpaidArticle(article, articleRequestDto);
        }
        return GlobalResponse.of("200", "수정되었습니다.");
    }


    //글 삭제
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public GlobalResponse delete(@PathVariable("id") Long id, Principal principal) {

        Article article = articleService.getArticleById(id);
        Member member = memberService.findByUsername(principal.getName());

        //권한 확인
        if (member == null) {
            return GlobalResponse.of("401", "로그인이 필요한 서비스입니다.");
        } else if (article.getMember().getId() != member.getId()) {
            return GlobalResponse.of("403", "삭제 권한이 없습니다.");
        }

        articleService.delete(article);
        return GlobalResponse.of("200", "Article deleted");
    }
}