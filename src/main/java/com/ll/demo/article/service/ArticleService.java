package com.ll.demo.article.service;

import com.ll.demo.article.dto.ArticleRequestDto;
import com.ll.demo.article.entity.Article;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional
    public void create(ArticleRequestDto articleRequestDto, Member member) {
        Article article = new Article();
        article.setContent(articleRequestDto.getContent());
        article.setMember(member);
    }

    public Page<Article> findAll(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return articleRepository.findAll(pageable);
    }

    public Article getArticleById(Long id) {
        Optional<Article> opArticle = articleRepository.findById(id);
        if (opArticle.isPresent()) {
            return opArticle.get();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void delete(Article article) {
        articleRepository.delete(article);
    }

    public void modifyPaidArticle(Article article, ArticleRequestDto articleRequestDto) {
        article.setContent(articleRequestDto.getContent());
        article.setTags(articleRequestDto.getTags());
    }

    public void modifyUnpaidArticle(Article article, ArticleRequestDto articleRequestDto) {
        article.setContent(articleRequestDto.getContent());
        article.setTags(articleRequestDto.getTags());
    }
}
