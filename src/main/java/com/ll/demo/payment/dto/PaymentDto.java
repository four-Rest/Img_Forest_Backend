package com.ll.demo.payment.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentReq {

    private Long memberId;
    private Long ArticleId;
    private Long amount;   // 결제 금액
    private Long orderUid;
    private String merchantUid;
}
