package com.ll.demo.comment.dto;

import com.ll.demo.article.entity.Article;
import com.ll.demo.comment.entity.Comment;
import com.ll.demo.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateReplyCommentRequest {
    @NotNull(message = "대댓글의 아이디는 필수입니다.")
    private Long replyId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    @NotBlank(message = "어떤 멤버인지 확인이 필요합니다.")
    private String username;

    public static Comment toEntity(UpdateReplyCommentRequest request, Member member, Article article) {
        return Comment.builder()
                .id(request.getReplyId())
                .content(request.getContent())
                .member(member)
                .article(article)
                .build();
    }
}
