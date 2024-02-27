package com.ll.demo.payment;


import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private IamportClient iamportClient;

    @Value("${iamport.api.key}")
    private String restApiKey;

    @Value("${iamport.api.secret}")
    private String restApiSecret;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(restApiKey,restApiSecret);
    }
}
