package com.ll.demo.article.repository;

import com.ll.demo.article.entity.Article;
import com.ll.demo.article.entity.Like;
import com.ll.demo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByArticleAndMember(Article article, Member member);
}
