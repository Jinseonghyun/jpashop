package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue // 시퀀스 값을 사용하기 위함
    @Column(name = "member_id") // Fk(외래키)가 MEMBER_ID
    private Long id;

    private String username;

    @Embedded // 내장 타입을 포함했다.
    private Address address;

    @OneToMany(mappedBy = "member") // 멤버의 입장에서 list 는 하나의 회원이 여러개의 상품을 주문하기 때문에 1대다 관계
    private List<Order> orders = new ArrayList<>();
}
// 어떤 애의 값이 바뀌었을 때 저 FK 를 바꿀거야 라고 지정해주면 되는데 그게 연관관계의 주인
// 연관관계의 주인은 FK 가 가까운곳으로 하면됨  (Member 와 비교해 볼 때 Order 에 Fk 있다. 연관관계의 주인)
// 주인은 그대로 두고 반대로 Member 는 주인 아니고 거울이기에 mappedBy 첨부  (읽기 전용)
// 누구에 의해서 맵핑이 됐나여?? Order table에 있는 member로 맵핑할거야!!