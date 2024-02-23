package com.ll.demo.article.repository;

import com.ll.demo.article.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
