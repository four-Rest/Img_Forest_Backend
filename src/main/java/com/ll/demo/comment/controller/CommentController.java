package com.ll.demo.comment.controller;

import com.ll.demo.comment.dto.*;
import com.ll.demo.comment.service.CommentService;
import com.ll.demo.global.response.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
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
}
