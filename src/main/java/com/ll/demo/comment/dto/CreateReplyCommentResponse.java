package com.ll.demo.comment.dto;

import com.ll.demo.comment.entity.Comment;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReplyCommentResponse {
    private Long id;
    private String content;

    public static CreateReplyCommentResponse of(Comment comment) {
        return CreateReplyCommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }
}
