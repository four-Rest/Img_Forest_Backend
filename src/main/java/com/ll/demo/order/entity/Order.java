package com.ll.demo.order.entity;

import com.ll.demo.cart.entity.CartItem;
import com.ll.demo.global.entity.BaseEntity;
import com.ll.demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
@Table(name = "order_")
public class Order extends BaseEntity {
    @ManyToOne
    private Member buyer;


    // 주문은 하나, 주문에 포함된 하나하나의 상품들을 orderItem
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime payDate; // 결제일
    private LocalDateTime cancelDate; // 취소일
    private LocalDateTime refundDate; // 환불일


    public void addItem(CartItem cartItem) {

        if(buyer.hasArticle(cartItem.getArticle())) {
            throw new IllegalArgumentException("이미 구매한 이미지입니다.");
        }
        OrderItem orderItem = OrderItem.builder()
                .order(this)
                .article(cartItem.getArticle())
                .build();

        orderItems.add(orderItem);
    }

    public long calcPayPrice() {
        return orderItems.stream()
                .mapToLong(OrderItem::getPayPrice)
                .sum();
    }

    public void setPaymentDone() {
        payDate = LocalDateTime.now();

        orderItems.stream()
                .forEach(OrderItem::setPaymentDone);
    }

    public void setCancelDone() {
        cancelDate = LocalDateTime.now();
    }

    public void setRefundDone() {
        refundDate = LocalDateTime.now();
    }


    //결제 이름 가져오기(article.id를 String으로 변경 후 OrderName으로 지정)
    public String getName() {
        String name = String.valueOf(orderItems.get(0).getArticle().getId());

        if(orderItems.size() > 1) {
            name += " 외 %건".formatted(orderItems.size() - 1);
        }
        return name;
    }

    // 결제 코드 생성 및 가져오기
    public String getCode() {
        // yyyy-MM-dd 형식의 DateTimeFormatter 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // LocalDateTime 객체를 문자열로 변환
        return getPayDate().format(formatter) + "__" + getId();
    }

    public boolean isPayable() {
        if (payDate != null) return false;
        if (cancelDate != null) return false;

        return true;
    }


    public String getForPrintPayStatus() {
        if (payDate != null)
            return "결제완료(" + payDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";

        if (cancelDate != null) return "-";

        return "결제대기";
    }

    public String getForPrintCancelStatus() {
        if (cancelDate != null)
            return "취소완료(" + cancelDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";

        if (!isCancelable()) return "취소불가능";

        return "취소가능";
    }

    public String getForPrintRefundStatus() {
        if (refundDate != null)
            return "환불완료(" + refundDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";

        if (payDate == null) return "-";
        if (!isCancelable()) return "-";

        return "환불가능";
    }


    public boolean isPayDone() {
        return payDate != null;
    }

    public boolean isCancelable() {
        if (cancelDate != null) return false;

        // 결제일자로부터 1시간 지나면 취소 불가능
        if (payDate != null && payDate.plusHours(1).isBefore(LocalDateTime.now())) return false;

        return true;
    }
}