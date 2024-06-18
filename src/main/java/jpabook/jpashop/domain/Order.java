package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jms.JmsProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // 안해주면 order 이 되기 때문에 관례상으로 orders 로 지정해줌
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")  // 테이블 명의 id
    private Long id;

    @ManyToOne  //  Order 와 Member는 다대1 관계
    @JoinColumn(name = "member_id")// 맵핑을 뭘로 할거냐  (외래키 이름이 member_id 가 된다.)
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne // Delivery 와 Order 1대1 매핑
    @JoinColumn(name = "delivery_id")  // Order 와 Delivery 사이에서 연관관계의 주인은 Order 안의 Delivery
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING) // enum 타입 상태 지정
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]
}
