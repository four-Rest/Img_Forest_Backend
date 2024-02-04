package com.ll.demo.article.dto;

import com.ll.demo.article.entity.Article;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleListResponseDto {
    private boolean paid;
    private String imgFilePath;
    private String imgFileName;
    private Long id;
    private int likes;

    public ArticleListResponseDto(Article article) {
        this.imgFilePath = article.getImage().getPath();
        this.paid = article.isPaid();
        this.imgFileName = article.getImage().getFileName();
        this.id = article.getId();
        this.likes = article.getLikes();
    }
}