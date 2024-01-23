package com.ll.demo.comment.dto;

import com.ll.demo.comment.entity.Comment;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DeleteCommentResponse {
    private Long id;
    private String content;

    public static DeleteCommentResponse of(Comment comment) {
        return DeleteCommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }
}
