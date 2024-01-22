package com.ll.demo.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.demo.article.entity.Article;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.comment.dto.CreateCommentRequest;
import com.ll.demo.comment.dto.CreateCommentResponse;
import com.ll.demo.comment.repository.CommentRepository;
import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

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

    @DisplayName("댓글 생성 API 테스트")
    @Test
    void createCommentTest() throws Exception {
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

        String content = objectMapper.writeValueAsString(build);
        // when
        mvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8"))
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    GlobalResponse response = objectMapper.readValue(responseContent, GlobalResponse.class);

                    assertThat(response.getResultCode()).isEqualTo(String.valueOf(HttpStatus.CREATED.value()));
                    assertThat(response.getMsg()).isEqualTo("success");
                });
    }
}