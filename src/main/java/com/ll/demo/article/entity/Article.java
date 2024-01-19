package com.ll.demo.article.entity;

import com.ll.demo.global.entity.BaseEntity;
import com.ll.demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private Member author;

    private Boolean paid;

    private Long price;

    @OneToOne(mappedBy = "article", cascade = CascadeType.REMOVE)
    private Image image;

}
