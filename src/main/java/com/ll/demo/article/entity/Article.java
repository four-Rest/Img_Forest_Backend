package com.ll.demo.article.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.demo.comment.entity.Comment;
import com.ll.demo.global.entity.BaseEntity;
import com.ll.demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private Member member;

    private boolean paid;

    private Long price;

    @JsonIgnore
    @OneToOne(mappedBy = "article", cascade = CascadeType.REMOVE)
    private Image image;

    @ManyToMany
    private Set<Tag> tags;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;


}
