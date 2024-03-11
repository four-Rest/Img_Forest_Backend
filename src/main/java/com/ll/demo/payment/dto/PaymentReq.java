package com.ll.demo.payment.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentReq {

    private Long memberId;
    private Long ArticleId;
    private int amount;   // 결제 금액
    private Long orderNumber;
    private String paymentMethod;
    private String merchantUid;
}
