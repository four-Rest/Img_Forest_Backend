package com.ll.demo.article.repository;

import com.ll.demo.article.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByTagName(String tagName);
}