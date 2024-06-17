package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 싱글 테이블 전략  (strategy 로 전략을 잡자.)
@DiscriminatorColumn(name = "dtype") // 만약 book 을 읽을 때 book 이면 어떻게 할거야 ? (즉 객체가 들어올 때 어떻게 할거야)
@Getter @Setter
public abstract class Item {  // 상속 클래스

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    //상속 관계 매핑
    private String name;
    private int price;
    private int stockQuantity;
}
