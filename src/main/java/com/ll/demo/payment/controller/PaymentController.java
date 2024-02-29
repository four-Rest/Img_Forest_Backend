package com.ll.demo.payment;


import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.service.MemberService;
import com.ll.demo.order.dto.OrderRequestDto;
import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final MemberService memberService;

    private IamportClient iamportClient;

    @Value("${iamport.api.key}")
    private String restApiKey;

    @Value("${iamport.api.secret}")
    private String restApiSecret;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(restApiKey,restApiSecret);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/payment")
    public GlobalResponse paymentComplete(Principal principal, @RequestBody OrderRequestDto orderRequestDto) {
        String orderNumber = String.valueOf(orderRequestDto.getOrderNumber());

        try {
            String username = principal.getName();
            Member member = memberService.findByUsername(username);
            

        }

    }
}
