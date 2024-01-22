package com.ll.demo.comment.controller;

import com.ll.demo.comment.dto.CreateCommentRequest;
import com.ll.demo.comment.dto.CreateCommentResponse;
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
        return GlobalResponse.of("201", "success", commentService.create(createCommentRequest));
    }
}
