package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 싱글 테이블 전략  (strategy 로 전략을 잡자.)
@DiscriminatorColumn(name = "dtype") // 만약 book 을 읽을 때 book 이면 어떻게 할거야 ? (즉 객체가 들어올 때 어떻게 할거야)
@Getter @Setter// 대도록 setter 은 넣지 않는 것이 좋다. // 변경을 할려면 아래 비즈니스 로직 처럼 핵심 비즈니스 로직을 가지고 변경하도록 하자 예) addStock, removeStock -> 객체지향적이다.
public abstract class Item {  // 상속 클래스

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    //상속 관계 매핑
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    // 위에서 데이터를 가지고 있기에 여기서 비즈니스 메서드가 있는게 좋다! -> 그래야 응집력 있다. -> 관리하기 좋다

    /**
     * 재고 수량인 stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity; // 재고 수량을 증가시킨다.
    }

    /**
     * 재고 수량인 stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) { // 감소 로직은 항상 0을 신경써야 한다 주의!!!
            throw new NotEnoughStockException("need more stock"); // 수량이 0 보다 작으면 예외 터진다.
        }
        this.stockQuantity = restStock; // 남은 수량이 현재 재고
    }
}
