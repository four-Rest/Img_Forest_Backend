package com.ll.demo.article.repository;

import com.ll.demo.article.entity.LikeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeTableRepository extends JpaRepository<LikeTable, Long> {

    Optional<LikeTable> getLikeByArticleIdAndMemberId(Long articleId, Long memberId);
}
