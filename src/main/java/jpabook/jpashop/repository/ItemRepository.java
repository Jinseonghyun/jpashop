package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 스프링에서 제공하는 @Component scan 의 대상이 되서 자동으로 스프링 빈으로 관리됨
@RequiredArgsConstructor  // final 붙은 필드를 기반으로 아래 생성자 자동으로 만들어줌
public class ItemRepository {

    private final EntityManager em; // JPA 에서 @PersistenceContex 대신에 스프링이 생성한 매니저를 EntityManager 에 주입해줌

    // 상품 저장
    public void save(final Item item) { // item 은 처음 저장할 때는 id가 없다.
        if (item.getId() == null) { // 만약 이미 아이템(id)이  있다는 건 db에 등록된 걸 가져온 것
            em.persist(item); // id 가 없기 때문에 jpa가 제공하는 persist 를 사용해서 등록
        } else {        // id가 없다는 것은 완전히 새로 생성한 객체라는 것 (persist(item) 을 통해서 신규로 등록하는 것)
            em.merge(item); // merge 업데이트 같은 느낌 (신규 등록이 아닌 db에서 가져온 원래 있던걸 업데이트 하는 느낌)
        }
    }

    // 아이템 하나 조회 (단건 조회)
    public Item findOne(final Long id) {
        return em.find(Item.class, id);
    }

    // 아이템 여러개 찾는 것 (여러개 찾는 거기에 JPQL 작성해야 한다.)
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
