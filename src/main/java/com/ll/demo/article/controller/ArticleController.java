package com.ll.demo.article.controller;

import com.ll.demo.article.dto.ArticleDetailResponseDto;
import com.ll.demo.article.dto.ArticleListResponseDto;
import com.ll.demo.article.dto.ArticleRequestDto;
import com.ll.demo.article.entity.Article;
import com.ll.demo.article.entity.Image;
import com.ll.demo.article.service.ArticleService;
import com.ll.demo.article.service.ImageService;
import com.ll.demo.article.service.TagService;
import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.global.rq.Rq;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;
    private final ImageService imageService;
    private final MemberService memberService;
    private final TagService tagService;
    private final Rq rq;

    //전체 글 조회
    @GetMapping("")
    public GlobalResponse findAllArticles() {
        List<ArticleListResponseDto> articleListResponseDtoList = articleService.findAllOrderByLikesDesc();
        return GlobalResponse.of("200", "success", articleListResponseDtoList);
    }

    //단일 글 조회
    @GetMapping("/detail/{id}")
    public GlobalResponse showArticle(@PathVariable("id") Long id) {

        Article article = articleService.getArticleById(id);
        ArticleDetailResponseDto articleDetailResponseDto = new ArticleDetailResponseDto(article);
        if (rq.isLoggedIn() && articleService.getLikeByArticleIdAndMemberId(article.getId(), memberService.findByUsername(rq.getUser().getUsername()).getId()) != null) {
            articleDetailResponseDto.setLikeValue(true);
        } else {
            articleDetailResponseDto.setLikeValue(false);
        }

        return GlobalResponse.of("200", "success", articleDetailResponseDto);
    }

    //tag값으로 글 검색
    @GetMapping("/{tagName}")
    public GlobalResponse searchArticlesByTag(@PathVariable("tagName") String tagName) {
        Set<ArticleListResponseDto> articleListResponseDtoSet = tagService.getArticlesByTagName(tagName)
                .stream()
                .map(article -> new ArticleListResponseDto(article))
                .collect(Collectors.toSet());
        return GlobalResponse.of("200", "success", articleListResponseDtoSet);
    }

    // 글 생성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public GlobalResponse createArticle(
            @Valid ArticleRequestDto articleRequestDto,
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
    public GlobalResponse updateArticle(
            @PathVariable("id") Long id,
            Principal principal,
            @Valid ArticleRequestDto articleRequestDto
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
    public GlobalResponse deleteArticle(@PathVariable("id") Long id, Principal principal) throws IOException {

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

    //글 추천
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like/{id}")
    public GlobalResponse likeArticle(@PathVariable("id") Long id, Principal principal) {

        Article article = articleService.getArticleById(id);
        Member member = memberService.findByUsername(principal.getName());

        articleService.like(article, member);

        return GlobalResponse.of("200", "추천되었습니다.");
    }

    //추천 취소
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/like/{id}")
    public GlobalResponse unlikeArticle(@PathVariable("id") Long id, Principal principal) {

        Article article = articleService.getArticleById(id);
        Member member = memberService.findByUsername(principal.getName());

        articleService.unlike(article, member);

        return GlobalResponse.of("200", "추천취소되었습니다.");
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {

        Image image = imageService.getImageByFileName(imageName);
        // 이미지 파일을 byte 배열로 읽어옵니다.
        byte[] imageBytes = Files.readAllBytes(Paths.get(image.getPath(), imageName));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);  // 이미지의 MIME 타입에 따라 변경해야 합니다.

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}