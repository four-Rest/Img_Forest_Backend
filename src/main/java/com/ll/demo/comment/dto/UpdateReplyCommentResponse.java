package com.ll.demo.comment.dto;

import com.ll.demo.comment.entity.Comment;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UpdateReplyCommentResponse {
    private Long id;
    private String content;

    public static UpdateReplyCommentResponse of(Comment comment) {
        return UpdateReplyCommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }
}