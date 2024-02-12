package com.ll.demo.article.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticlePageResponseDto {

    private List<ArticleListResponseDto> content;
    private int pageNo;  // 현재 페이지 넘버
    private int pageSize;  // 페이지 사이즈(페이지에 포함된 게시물 수)
    private long totalElements; // 데이터 로우 총 갯수
    private int totalPages;  // 페이지의 총 갯수
    private boolean last;
}
