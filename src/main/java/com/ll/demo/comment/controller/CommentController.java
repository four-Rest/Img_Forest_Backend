package com.ll.demo.comment.controller;

import com.ll.demo.comment.dto.*;
import com.ll.demo.comment.service.CommentService;
import com.ll.demo.global.response.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/")
    public GlobalResponse<CreateCommentResponse> create(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        return GlobalResponse.of("201", "success", this.commentService.create(createCommentRequest));
    }

    @PutMapping("{id}")
    public GlobalResponse<UpdateCommentResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateCommentRequest updateCommentRequest
    ) {
        updateCommentRequest.setCommentId(id);
        return GlobalResponse.of("200", "success", this.commentService.update(updateCommentRequest));
    }

    @DeleteMapping("{id}")
    public GlobalResponse<DeleteCommentResponse> delete(
            @PathVariable("id") Long id,
            @Valid @RequestBody DeleteCommentRequest deleteCommentRequest
    ) {
        deleteCommentRequest.setCommentId(id);
        return GlobalResponse.of("200", "success", this.commentService.delete(deleteCommentRequest));
    }

    @PostMapping("/{parentId}/reply")
    public GlobalResponse<CreateCommentResponse> createReply(
            @PathVariable("parentId") Long parentId,
            @Valid @RequestBody CreateReplyCommentRequest createReplyCommentRequest
    ) {
        CreateCommentResponse response = this.commentService.createReply(parentId, createReplyCommentRequest);
        return GlobalResponse.of("201", "success", response);
    }


//
//    @PutMapping("/{parentId}/reply/{replyId}")
//    public GlobalResponse<UpdateReplyCommentResponse> updateReply(
//            @PathVariable("parentId") Long parentId,
//            @PathVariable("replyId") Long replyId,
//            @Valid @RequestBody UpdateReplyCommentRequest updateReplyCommentRequest
//    ) {
//        UpdateReplyCommentResponse response = this.commentService.updateReply(updateReplyCommentRequest);
//        return GlobalResponse.of("200", "success", response);
//    }
//
//    @DeleteMapping("/{parentId}/reply/{replyId}")
//    public GlobalResponse<DeleteReplyCommentResponse> deleteReply(
//            @PathVariable("parentId") Long parentId,
//            @PathVariable("replyId") Long replyId
//    ) {
//        commentService.deleteReply(parentId, replyId);
//        Comment deletedReply = commentService.getDeletedReply(replyId);
//
//        return GlobalResponse.of("200", "success", DeleteReplyCommentResponse.of(deletedReply));
//    }
}