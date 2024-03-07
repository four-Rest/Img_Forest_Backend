package com.ll.demo.comment.dto;

import com.ll.demo.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListCommentResponse {
    private Long id;
    private Long parentId; // 대댓글의 부모 댓글 ID
    private String content;
    private String username;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime removedTime;

    public ListCommentResponse(Comment comment){
        this.id = comment.getId();
        this.parentId = comment.getParentComment() != null ? comment.getParentComment().getId() : null; // 부모 댓글 ID 설정
        this.content = comment.getContent();
        this.username = comment.getMember().getUsername();
        this.createdDate = comment.getCreatedTime();
        this.modifiedDate = comment.getModifiedTime();
        this.removedTime = comment.getRemovedTime();
    }
}
