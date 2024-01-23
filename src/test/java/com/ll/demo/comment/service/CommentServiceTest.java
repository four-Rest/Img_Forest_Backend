package com.ll.demo.comment.service;

import com.ll.demo.article.entity.Article;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.comment.dto.CreateCommentRequest;
import com.ll.demo.comment.dto.CreateCommentResponse;
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
        articleRepository.deleteAll();
        commentRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        articleRepository.deleteAll();
        commentRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("댓글 생성 테스트 메서드")
    @Test
    @Transactional
    void createCommentTest() {
        // given
        Member member = Member.builder()
                .username("dc-choi")
                .password("dc-choi")
                .nickname("dc-choi")
                .email("ddagae0805@gmail.com")
                .build();
        memberRepository.save(member);
        memberRepository.flush();

        Article article = Article.builder()
                .member(member)
                .content("qwer")
                .paid(true)
                .price(1L)
                .build();
        articleRepository.save(article);
        articleRepository.flush();

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
}