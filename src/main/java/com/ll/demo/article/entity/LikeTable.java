package com.ll.demo.article.entity;

import com.ll.demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long articleId;

    private Long memberId;

    public LikeTable(Article article, Member member) {
        this.articleId = article.getId();
        this.memberId = member.getId();
    }
}