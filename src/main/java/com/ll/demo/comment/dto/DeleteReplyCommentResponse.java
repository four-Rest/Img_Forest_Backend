package com.ll.demo.comment.dto;

import com.ll.demo.comment.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DeleteReplyCommentResponse {
    private Long id;
    private String content;
    private LocalDateTime removedTime;

    public static DeleteReplyCommentResponse of(Comment comment) {
        return DeleteReplyCommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .removedTime(comment.getRemovedTime())
                .build();
    }
}
