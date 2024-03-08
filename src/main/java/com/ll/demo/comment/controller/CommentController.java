package com.ll.demo.comment.controller;

import com.ll.demo.comment.dto.*;
import com.ll.demo.comment.service.CommentService;
import com.ll.demo.global.response.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
@Tag(name = "Comment", description = "Comment API")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "요청에 실패했습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "인증이 필요합니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "요청이 거부되었습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "리소스를 서버에서 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
})
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/")
    @Operation(summary = "댓글 생성", description = "댓글 생성 시 사용하는 API")
    public GlobalResponse<CreateCommentResponse> create(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        return GlobalResponse.of("201", "success", this.commentService.create(createCommentRequest));
    }

    @PutMapping("{id}")
    @Operation(summary = "댓글 수정", description = "댓글 수정 시 사용하는 API")
    public GlobalResponse<UpdateCommentResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateCommentRequest updateCommentRequest
    ) {
        updateCommentRequest.setCommentId(id);
        return GlobalResponse.of("200", "success", this.commentService.update(updateCommentRequest));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제 시 사용하는 API")
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