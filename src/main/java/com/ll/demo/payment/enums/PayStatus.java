package com.ll.demo.payment.enums;

public enum PayStatus {

    SUCCESS("결제 완료"),
    CANCEL("결제 취소"),
    WAITING_FOR_PAYMENT("결제 대기");

    private String status;

    public String getStatus() {
        return status;
    }

    PayStatus(String status) {
        this.status = status;
    }
}
