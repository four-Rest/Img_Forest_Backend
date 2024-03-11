package com.ll.demo.payment.dto;


import com.ll.demo.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PaymentRes {

    private int amount;
    private Timestamp orderDate;
    private String paymentMethod;
    private Member member;

}
