package com.ll.demo.article.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값은 출력하지 않도록 설정
public class ArticlePageResponseDto {

    @JsonProperty("content")
    private List<ArticleListResponseDto> content;

    @JsonProperty("pageNo")
    private int pageNo;  // 현재 페이지 넘버

    @JsonProperty("pageSize")
    private int pageSize;  // 페이지 사이즈(페이지에 포함된 게시물 수)

    @JsonProperty("totalElements")
    private long totalElements; // 데이터 로우 총 갯수

    @JsonProperty("totalPages")
    private int totalPages;  // 페이지의 총 갯수

    @JsonProperty("last")
    private boolean last;
}
