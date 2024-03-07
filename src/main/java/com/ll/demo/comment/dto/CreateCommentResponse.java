package com.ll.demo.comment.dto;

import com.ll.demo.comment.entity.Comment;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CreateCommentResponse {
    private Long id;
    private Long parentId; // 대댓글의 부모 댓글 ID
    private String content;

    public static CreateCommentResponse of(Comment comment) {
        Long parentId = null;
        if (comment.getParentComment() != null) {
            parentId = comment.getParentComment().getId();
        }

        return CreateCommentResponse.builder()
                .id(comment.getId())
                .parentId(parentId)
                .content(comment.getContent())
                .build();
    }
}
