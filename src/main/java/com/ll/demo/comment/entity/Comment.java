package com.ll.demo.comment.entity;

import com.ll.demo.article.entity.Article;
import com.ll.demo.global.entity.BaseEntity;
import com.ll.demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    // 부모 댓글과의 관계는 단방향으로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parentComment;

    // 자식 댓글과의 관계는 단방향으로 변경
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> childComments;

    public void changeComment(Comment comment) {
        this.content = comment.getContent();
    }

    // 자식 댓글 추가 메서드
    public void addChildComment(Comment childComment) {
        this.childComments.add(childComment);
    }

    // 자식 댓글 삭제 메서드
    public void removeChildComment(Comment childComment) {
        this.childComments.remove(childComment);
    }
}
