package com.example.study.model.entity;

import com.example.study.model.enumclass.OrderType;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ToString(exclude = {"user","orderDetailList"})
@EntityListeners(AuditingEntityListener.class)
@Builder    // Builder 패턴으로 객체 생성
@Accessors(chain = true)    // 생성된 객체에 .setXXX() 형태로 set 함수를 쩜연사자 연결해서 지정 할수 있게 해줌.
public class OrderGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;           // 주문형태 : 일괄, 개별

    private String revAddress;
    private String revName;

    private String paymentType;         // 카드, 현금
    private BigDecimal totalPrice;
    private Integer totalQuantity;
    private LocalDateTime orderAt;
    private LocalDateTime arrivalDate;
    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    // OderGroup : User = N : 1
    @ManyToOne
    private User user;

    // OrderGroup : OrderDetail = 1 : N
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderGroup")
    private List<OrderDetail> orderDetailList;

}
