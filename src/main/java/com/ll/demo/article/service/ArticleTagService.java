package com.ll.demo.article.service;

import com.ll.demo.article.entity.Article;
import com.ll.demo.article.entity.ArticleTag;
import com.ll.demo.article.entity.Tag;
import com.ll.demo.article.repository.ArticleTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleTagService {

    private final ArticleTagRepository articleTagRepository;
    private final TagService tagService;

    @Transactional
    public void update(Article article, String tagString) {
        Set<Tag> tags = tagService.parseTagStringIntoSet(tagString);
        for (Tag tag : tags) {
            ArticleTag articleTag = new ArticleTag(article, tag);
            articleTagRepository.save(articleTag);
        }
    }
}