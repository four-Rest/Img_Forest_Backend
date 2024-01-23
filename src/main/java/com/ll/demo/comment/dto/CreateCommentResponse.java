package com.ll.demo.comment.dto;

import com.ll.demo.comment.entity.Comment;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CreateCommentResponse {
    private String content;

    public static CreateCommentResponse of(Comment comment) {
        return CreateCommentResponse.builder()
                .content(comment.getContent())
                .build();
    }
}
