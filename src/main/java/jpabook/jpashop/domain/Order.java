package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jms.JmsProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "orders") // 안해주면 order 이 되기 때문에 관례상으로 orders 로 지정해줌
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")  // 테이블 명의 id
    private Long id;

    @ManyToOne(fetch = LAZY)  //  Order 와 Member는 다대1 관계
    @JoinColumn(name = "member_id")// 맵핑을 뭘로 할거냐  (외래키 이름이 member_id 가 된다.)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) // Delivery 와 Order 1대1 매핑
    @JoinColumn(name = "delivery_id")  // Order 와 Delivery 사이에서 연관관계의 주인은 Order 안의 Delivery
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING) // enum 타입 상태 지정
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    //==연관관계 편의 메서드==//
    //양방향인데 한개의 코드안에서 해결

    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//

    /**
     * 주문 생성
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { // ... 문법 여러개를 넘긴다.!
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); // 주문 상태
        order.setOrderDate(LocalDateTime.now()); // 주문 시간 정보
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소 (재고를 원래 수량으로 다시 올려줘야함)
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) { // 딜리버리 상태가 == 이미 배송 완료상태 이면 (COMP 배송 완료)
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCLE); // 배송 완료 상태 아니면 주문 취소
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();  // 각각의 상품 주문 취소
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
// 아래의 코드를 stream으로 정리
//        return orderItems.stream() .mapToInt(OrderItem::getTotalPrice).sum(); // stream 으로 바꾸고mapToInt하고 sum하면 위와 같다.
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;



    }

}
