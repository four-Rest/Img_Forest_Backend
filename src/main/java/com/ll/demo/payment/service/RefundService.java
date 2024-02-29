package com.ll.demo.payment.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RefundService {

    public String getToken(String restApiKey, String restApiSecret) {
        return "";
    }

    public void refundWithToken(String token, String orderNumber, String message) {
    }
}
