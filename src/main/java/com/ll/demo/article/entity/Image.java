package com.ll.demo.article.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@RequiredArgsConstructor
@Getter
@SuperBuilder(toBuilder = true)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String path;

    @JsonIgnore
    @OneToOne
    private Article article;

    public void modifyFileName(String fileName) {
        this.fileName = fileName;
    }

    public void modifyPath(String path) {
        this.path = path;
    }
}