package com.ll.demo.article.dto;

import com.ll.demo.article.entity.Tag;
import com.ll.demo.member.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class ArticleRequestDto {
    private String content;
    private Set<Tag> tags;
    private MultipartFile multipartFile;
}
