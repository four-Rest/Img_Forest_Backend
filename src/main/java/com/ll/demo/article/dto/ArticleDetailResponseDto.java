package com.ll.demo.article.dto;

import com.ll.demo.article.entity.Article;
import com.ll.demo.article.entity.Image;
import com.ll.demo.article.entity.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ArticleDetailResponseDto {
    private String content;
    private String username;
    private boolean paid;
    private Long price;
    private Set<Tag> tags;
    private String imgFileName;

    public ArticleDetailResponseDto(Article article) {
        this.content = article.getContent();
        this.username = article.getMember().getUsername();
        this.paid = article.isPaid();
        this.price = article.getPrice();
        this.tags = article.getTags();
        this.imgFileName = article.getImage().getFileName();
    }
}