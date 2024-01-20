package com.ll.demo.article.dto;

import com.ll.demo.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequestDto {
    private Long id;
    private Member member;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
//  private String comment;
}
