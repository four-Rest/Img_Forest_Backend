package com.ll.demo.comment.dto;

import com.ll.demo.article.entity.Article;
import com.ll.demo.comment.entity.Comment;
import com.ll.demo.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DeleteReplyCommentRequest {
    @NotNull(message = "대댓글의 아이디는 필수입니다.")
    private Long replyId;

    @NotNull(message = "어떤 게시글의 아이디인지 확인이 필요합니다.")
    private Long articleId;

    @NotNull(message = "부모 댓글 아이디는 필수입니다.")
    private Long parentCommentId;

    @NotNull(message = "어떤 멤버인지 확인이 필요합니다.")
    private String username;

    public static Comment toEntity(DeleteReplyCommentRequest request, Member member, Article article) {
        Comment comment = Comment.builder()
                .id(request.getReplyId())
                .member(member)
                .article(article)
                .build();

        return comment;
    }
}
