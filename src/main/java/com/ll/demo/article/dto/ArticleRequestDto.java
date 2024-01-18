package com.ll.demo.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.ll.demo.user.entity.User;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequestDto {
    private Long id;
    private User author;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
//  private String comment;
}
