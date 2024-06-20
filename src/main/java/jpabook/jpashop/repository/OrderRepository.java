package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.awt.*;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    // 주문 저장
    public void save(Order order) {
        em.persist(order);
    }

    // 주문 단건 조회
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // 주문 검색으로 조회
//    public List<Order> findAll() {OrederSearch orderSearch} {}

}
