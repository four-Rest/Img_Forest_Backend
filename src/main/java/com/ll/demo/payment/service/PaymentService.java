package com.ll.demo.payment.service;


import com.ll.demo.article.entity.Article;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.member.entity.Member;
import com.ll.demo.order.dto.OrderRequestDto;
import com.ll.demo.order.entity.Order;
import com.ll.demo.order.repository.OrderRepository;
import com.ll.demo.payment.dto.PaymentDto;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orderRepository;
    private final ArticleRepository articleRepository;


//    public IamportResponse<Payment> paymentByCallback(PaymentDto paymentDto) {
//
//    }

}
