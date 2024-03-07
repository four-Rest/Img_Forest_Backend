package com.ll.demo.comment.entity;

import com.ll.demo.article.entity.Article;
import com.ll.demo.global.entity.BaseEntity;
import com.ll.demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor // 기본 생성자 추가
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parentComment; // 부모 댓글 필드 추가

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComments; // 자식 댓글 필드 추가

    public void changeComment(Comment comment) {
        this.content = comment.getContent();
    }
}
