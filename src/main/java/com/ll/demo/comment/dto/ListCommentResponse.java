package com.ll.demo.comment.dto;

import com.ll.demo.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListCommentResponse {
    private Long id;
    private Long parentCommentId;
    private String content;
    private String username;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime removedTime;
    private List<ListCommentResponse> childComments = new ArrayList<>();

    public ListCommentResponse(Comment comment) {
        this.id = comment.getId();
        this.parentCommentId = comment.getParentComment() != null ? comment.getParentComment().getId() : null;
        this.content = comment.getContent();
        this.username = comment.getMember().getUsername();
        this.createdDate = comment.getCreatedTime();
        this.modifiedDate = comment.getModifiedTime();
        this.removedTime = comment.getRemovedTime();
    }
}
