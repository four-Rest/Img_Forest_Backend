package com.ll.demo.comment.service;

import com.ll.demo.article.entity.Article;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.comment.dto.*;
import com.ll.demo.comment.repository.CommentRepository;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("댓글 생성 단위 테스트")
    @Test
    @Transactional
    void createCommentTest() {
        // given
        Member member = this.member();
        Article article = this.article(member);

        CreateCommentRequest build = CreateCommentRequest.builder()
                .articleId(article.getId())
                .memberId(member.getId())
                .content("test 댓글")
                .build();

        // when
        CreateCommentResponse createCommentResponse = this.commentService.create(build);

        // then
        assertThat(createCommentResponse.getContent()).isEqualTo(build.getContent());
    }

    @DisplayName("댓글 수정 단위 테스트")
    @Test
    @Transactional
    void updateCommentTest() {
        // given
        Member member = this.member();
        Article article = this.article(member);

        CreateCommentRequest createCommentRequest = CreateCommentRequest.builder()
                .articleId(article.getId())
                .memberId(member.getId())
                .content("test 댓글")
                .build();
        CreateCommentResponse createCommentResponse = this.commentService.create(createCommentRequest);

        UpdateCommentRequest updateCommentRequest = UpdateCommentRequest.builder()
                .commentId(createCommentResponse.getId())
                .content(createCommentResponse.getContent())
                .articleId(createCommentRequest.getArticleId())
                .memberId(createCommentRequest.getMemberId())
                .build();
        // when
        UpdateCommentResponse updateCommentResponse = this.commentService.update(updateCommentRequest);

        // then
        assertThat(updateCommentResponse.getId()).isEqualTo(updateCommentRequest.getCommentId());
        assertThat(updateCommentResponse.getContent()).isEqualTo(updateCommentRequest.getContent());
    }

    @DisplayName("댓글 삭제 단위 테스트")
    @Test
    @Transactional
    void deleteCommentTest() {
        // given
        Member member = this.member();
        Article article = this.article(member);

        CreateCommentRequest createCommentRequest = CreateCommentRequest.builder()
                .articleId(article.getId())
                .memberId(member.getId())
                .content("test 댓글")
                .build();
        CreateCommentResponse createCommentResponse = this.commentService.create(createCommentRequest);

        DeleteCommentRequest deleteCommentRequest = DeleteCommentRequest.builder()
                .commentId(createCommentResponse.getId())
                .content(createCommentResponse.getContent())
                .articleId(createCommentRequest.getArticleId())
                .memberId(createCommentRequest.getMemberId())
                .build();
        // when
        DeleteCommentResponse deleteCommentResponse = this.commentService.delete(deleteCommentRequest);

        // then
        assertThat(createCommentResponse.getId()).isEqualTo(deleteCommentResponse.getId());
        assertThat(createCommentResponse.getContent()).isEqualTo(deleteCommentResponse.getContent());
    }

    private Member member() {
        Member member = Member.builder()
                .username("dc-choi")
                .password("dc-choi")
                .nickname("dc-choi")
                .email("ddagae0805@gmail.com")
                .build();
        memberRepository.save(member);
        memberRepository.flush();

        return member;
    }

    private Article article(Member member) {
        Article article = Article.builder()
                .member(member)
                .content("qwer")
                .paid(true)
                .price(1L)
                .build();
        articleRepository.save(article);
        articleRepository.flush();

        return article;
    }
}