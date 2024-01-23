package com.ll.demo.comment.service;

import com.ll.demo.article.entity.Article;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.comment.dto.*;
import com.ll.demo.comment.entity.Comment;
import com.ll.demo.comment.repository.CommentRepository;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateCommentResponse create(CreateCommentRequest request) {
        Member member = this.verifyMember(request.getMemberId());
        Article article = this.verifyArticle(request.getArticleId());

        // TODO: JWT를 parse해서 해당 정보로 member를 가져올지에 대한 논의 필요.
        Comment saved = this.commentRepository.save(CreateCommentRequest.toEntity(request, member, article));

        return CreateCommentResponse.of(saved);
    }

    @Transactional
    public UpdateCommentResponse update(UpdateCommentRequest request) {
        Member member = this.verifyMember(request.getMemberId());
        Article article = this.verifyArticle(request.getArticleId());

        Comment saveComment = this.verifyComment(request.getCommentId());
        saveComment.changeComment(UpdateCommentRequest.toEntity(request, member, article));

        return UpdateCommentResponse.of(this.commentRepository.save(saveComment));
    }

    /**
     * TODO: 논리적 삭제 구현에 대해서 상의 필요
     * @param request
     * @return
     */
    @Transactional
    public DeleteCommentResponse delete(DeleteCommentRequest request) {
        this.verifyMember(request.getMemberId());
        this.verifyArticle(request.getArticleId());

        Comment saveComment = this.verifyComment(request.getCommentId());
        saveComment.setRemovedTime(LocalDateTime.now());

        return DeleteCommentResponse.of(this.commentRepository.save(saveComment));
    }

    private Member verifyMember(Long id) {
        return this.memberRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("멤버가 존재하지 않습니다.")
        );
    }

    private Article verifyArticle(Long id) {
        return this.articleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
    }

    private Comment verifyComment(Long id) {
        return this.commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
    }
}
