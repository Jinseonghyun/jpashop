package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    // RequiredArgsConstructor final이 붙었기에 자동으로 다 생성
    // 아래 엔티티 조회 때문에 의존관계를 넣어줘야 해서 작성해줘야 한다.
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository; // 회원에 대해서 주문에서 id 값을 꺼내기 위해
    private final ItemRepository itemRepository; // 회원에 대해서 주문에서 id 값을 꺼내기 위해

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        return order.getId();

    }
    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) { // 주문 취소할 때 id값만 넘어오게
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);// 먼저 id를 찾는다.
        // 주문 추소
        order.cancel(); // 찾은 id의 주문 취소해준다.
    }

    /**
     * 주문 검색
     */
/*    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
*/

}
