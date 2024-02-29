package com.ll.demo.order.dto;


import lombok.Getter;

@Getter
public class OrderRequestDto {
    private String username;
    private Long articleId;
    private int orderPrice;
    private String orderNumber;  // 백 자체로 난수생성해야되나..
    private String paymentMethod;
}
