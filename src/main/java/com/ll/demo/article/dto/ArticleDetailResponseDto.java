package com.ll.demo.article.dto;

import com.ll.demo.article.entity.Article;
import com.ll.demo.article.entity.Image;
import com.ll.demo.article.entity.Tag;
import com.ll.demo.comment.dto.ListCommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ArticleDetailResponseDto {
    private String content;
    private String username;
    private boolean paid;
    private Long price;
    private List<String> tags;
    private String imgFileName;
    private List<ListCommentResponse> listCommentResponses;

    public ArticleDetailResponseDto(Article article) {
        this.content = article.getContent();
        this.username = article.getMember().getUsername();
        this.paid = article.isPaid();
        this.price = article.getPrice();
        this.tags = article.getTags().stream()
                .map(tag -> tag.getTagName())
                .collect(Collectors.toList());
        this.imgFileName = article.getImage().getFileName();

        this.listCommentResponses = article.getCommentList().stream()
                .map(comment -> new ListCommentResponse(comment))
                .collect(Collectors.toList());
    }
}