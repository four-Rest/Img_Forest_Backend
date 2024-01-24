package com.ll.demo.comment.dto;

import com.ll.demo.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListCommentResponse {
    private String content;
    private String username;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ListCommentResponse(Comment comment){
        this.content = comment.getContent();
        this.username = comment.getMember().getUsername();
        this.createdDate = comment.getCreatedTime();
        this.modifiedDate = comment.getModifiedTime();
    }
}
