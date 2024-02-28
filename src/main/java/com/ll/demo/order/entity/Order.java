package com.ll.demo.order.entity;

import com.ll.demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(unique = true)
    private String orderNumber;

    private String orderStatus;

    private String paymentMethod;

    private Timestamp orderDate;

    @ManyToOne
    private Member member;

}
