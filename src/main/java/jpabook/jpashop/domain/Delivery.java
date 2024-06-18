package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY) // Delivery 와 Order 1대1 매핑  (Delivery 와 속 Order 은 거울)
    private Order order;  // order와의 관계를 가짐

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // enum 타입은 Enumerated 꼭 넣어야한다.  타입을  ORDINAL(기본값), STRING 정할 수 있다. // ORDINAL 은 1,2,3,4 이게 숫자로 들어감 그래도 다른 값이 중간에 오면 꼬인다. 무조건 STRING
    private DeliveryStatus status; // READY, COMP   //  STRING을 써야  READY, COMP 중간에 XXX 들어가서 READY, XXX, COMP 가 되어서 순서에 밀리는게 없다. (장애 예방)
}
