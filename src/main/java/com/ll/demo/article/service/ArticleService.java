package com.ll.demo.article.service;

import com.ll.demo.article.dto.ArticleRequestDto;
import com.ll.demo.article.dto.ArticleListResponseDto;
import com.ll.demo.article.entity.Article;
import com.ll.demo.article.entity.Image;
import com.ll.demo.article.entity.LikeTable;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.article.repository.LikeTableRepository;
import com.ll.demo.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ImageService imageService;
    private final TagService tagService;
    private final ArticleTagService articleTagService;
    private final LikeTableRepository likeTableRepository;

    @Transactional
    public void create(ArticleRequestDto articleRequestDto, Member member) throws IOException {

        Article article = new Article();
        article.setContent(articleRequestDto.getContent());
        article.setMember(member);
        article.setLikes(0);
        String tagString = articleRequestDto.getTagString();
        if (articleRequestDto.getTagString() != null) {

            articleTagService.update(article, tagString);

        }

        if (articleRequestDto.getMultipartFile() != null) {

            Image image = imageService.create(article, articleRequestDto.getMultipartFile());
            article.setImage(image);
            articleRepository.save(article);

        } else {
            throw new IllegalArgumentException("적어도 하나의 이미지를 업로드해야 합니다.");
        }
    }

    public List<ArticleListResponseDto> findAllOrderByLikesDesc() {
        List<Article> articles = articleRepository.findAllByOrderByLikesDesc();
        return articles.stream()
                .map(ArticleListResponseDto::new)
                .collect(Collectors.toList());
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
    public void delete(Article article) throws IOException {
        Image image = article.getImage();
        imageService.delete(image);
        articleRepository.delete(article);
    }

    @Transactional
    public void modifyPaidArticle(Article article, ArticleRequestDto articleRequestDto) {
        //내용과 태그 변경
        article.setContent(articleRequestDto.getContent());
        if (articleRequestDto.getTagString() != null) {
            articleTagService.update(article, articleRequestDto.getTagString());
        }
    }

    @Transactional
    public void modifyUnpaidArticle(Article article, ArticleRequestDto articleRequestDto) throws IOException {
        //내용과 태그 변경
        article.setContent(articleRequestDto.getContent());

        if (articleRequestDto.getTagString() != null) {
            articleTagService.update(article, articleRequestDto.getTagString());
        }
        if (articleRequestDto.getMultipartFile() != null) {
            //이미지 교체
            imageService.modify(article.getImage(), articleRequestDto.getMultipartFile());
        }
    }

    public LikeTable getLikeByArticleIdAndMemberId(Long articleId, Long memberId) {
        Optional<LikeTable> opLike = likeTableRepository.getLikeByArticleIdAndMemberId(articleId, memberId);
        if (opLike.isPresent()) {
            return opLike.get();
        }
        return null;
    }

    @Transactional
    public void like(Article article, Member member) {

        if (getLikeByArticleIdAndMemberId(article.getId(), member.getId()) == null) {
            LikeTable likeTable = new LikeTable(article, member);
            likeTableRepository.save(likeTable);
            article.setLikes(article.getLikes() + 1);
        }
    }

    @Transactional
    public void unlike(Article article, Member member) {

        LikeTable likeTable = getLikeByArticleIdAndMemberId(article.getId(), member.getId());

        if (likeTable != null) {
            likeTableRepository.delete(likeTable);
            article.setLikes(article.getLikes() - 1);
        }
    }
}