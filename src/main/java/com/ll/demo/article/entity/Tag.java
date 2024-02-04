package com.ll.demo.article.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tagName;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private Set<ArticleTag> articleTags;
}