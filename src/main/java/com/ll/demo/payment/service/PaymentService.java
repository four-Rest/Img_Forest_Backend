package com.ll.demo.payment.service;


import com.ll.demo.order.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    public void saveOrder(Long userId, OrderRequestDto orderRequestDto) {
    }
}
