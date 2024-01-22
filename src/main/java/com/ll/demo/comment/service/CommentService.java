package com.ll.demo.comment.service;

import com.ll.demo.article.entity.Article;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.comment.dto.CreateCommentRequest;
import com.ll.demo.comment.dto.CreateCommentResponse;
import com.ll.demo.comment.entity.Comment;
import com.ll.demo.comment.repository.CommentRepository;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateCommentResponse create(CreateCommentRequest request) {
        Member member = this.memberRepository.findById(request.getMemberId()).orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));
        Article article = this.articleRepository.findById(request.getArticleId()).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // TODO: JWT를 parse해서 해당 정보로 member를 가져올지에 대한 논의 필요.
        Comment saved = this.commentRepository.save(CreateCommentRequest.toEntity(request, member, article));

        return CreateCommentResponse.of(saved);
    }
}
