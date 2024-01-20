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
@Builder
public class ArticleDetailResponseDto {
    Article article;

    Image image;

    Set<Tag> tags;
}