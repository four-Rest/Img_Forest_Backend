package com.ll.demo.article.repository;

import com.ll.demo.article.entity.Article;
import com.ll.demo.article.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByLikesDesc();

    List<Article> findByMemberUsername(String username);

    Page<Article> findByArticleTagsTag(Tag tag, Pageable pageable);
}