package com.ll.demo.payment.enums;

public enum PayStatus {

    SUCCESS("결제 완료"),
    REFUND("환불 완료"),
    WAITING_FOR_PAYMENT("결제 대기");

    private String status;

    public String getStatus() {
        return status;
    }

    PayStatus(String status) {
        this.status = status;
    }
}
