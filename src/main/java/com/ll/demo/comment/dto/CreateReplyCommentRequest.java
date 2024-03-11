package com.ll.demo.comment.dto;

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
public class CreateReplyCommentRequest {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
    @NotNull(message = "어떤 멤버인지 확인이 필요합니다.")
    private String username;

    public static Comment toEntity(CreateReplyCommentRequest request, Member member) {
        return Comment.builder()
                .content(request.getContent())
                .member(member)
                .build();
    }
}