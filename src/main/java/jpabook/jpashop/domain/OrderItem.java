package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // PROTECTED 범위의 빈 생성자 자동 생성 -> 다른 곳에서 객체 생성 못하도록
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 당시 가격
    private int count; // 주문 당시 수량

    // 위에서 NoArgsConstructor 대신해준다.
    //jpa 는 protected 까지 허용해준다. 다른 곳에서 생성 못하도록 막아주자
//    public OrderItem() {
//    }

    //==생성 메서드==//
    // static 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 아이템을 세팅해주고
        orderItem.setOrderPrice(orderPrice); // 가격을 넣어주고
        orderItem.setCount(count); // 수량 넣어준다

        item.removeStock(count); // 넘어온 수량만큼 아이템의 재고를 까줍니다.
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);// 상품의 재고를 원래대로 늘리는게 목표 // 원래대로 다시 늘려준다.
    }

    //==조회 로직==//
    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
