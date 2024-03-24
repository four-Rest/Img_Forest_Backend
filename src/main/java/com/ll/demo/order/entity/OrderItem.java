package com.ll.demo.order.entity;

import com.ll.demo.article.entity.Article;
import com.ll.demo.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class OrderItem extends BaseEntity {
    @ManyToOne
    private Order order;
    @ManyToOne
    private Article article;

    public long getPayPrice() {
        return article.getPrice();
    }
}