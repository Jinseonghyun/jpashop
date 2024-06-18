package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @Setter
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany  // 카테고리도 리스트로 아이템을 가지고, 아이템도 리스트로 카테고리를 가진다.
    @JoinTable(name = "category_item",  // 다대다는 JoinTable 이 필요하다. 중간테이블 카테고리 아이템
            joinColumns = @JoinColumn(name = "category_id"), // 중간테이블에 있는 카테고리 id
            inverseJoinColumns = @JoinColumn(name = "item_id"))  // category_item 테이블에 아이템쪽으로 들어가는
    private List<Item> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();;
}
