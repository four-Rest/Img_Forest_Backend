package com.ll.demo.order.entity;

import com.ll.demo.article.entity.Article;
import com.ll.demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;


@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(unique = true)
    private String orderNumber;

    private String paymentMethod;

    private Timestamp orderDate;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Article article;

}
