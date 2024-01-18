package com.ll.demo.article.entity;

import com.ll.demo.global.entity.BaseEntity;
import com.ll.demo.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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

    private String content;
    private User user;
    private Boolean paid;
    private Long price;

}
