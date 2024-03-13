package com.ll.demo.payment.entity;


import com.ll.demo.payment.enums.PayStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;

    private PayStatus status;

    // imp_uid(결제 고유 번호)
    private String paymentUid;

    public void changePaymentBySuccess(PayStatus status, String paymentUid) {
        this.status = status;
        this.paymentUid = paymentUid;
    }

}
