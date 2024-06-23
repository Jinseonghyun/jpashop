package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService { // ItemService는 itemRepository 에 단순하게 위임만 하는 클래스

    private final ItemRepository itemRepository;

    // 저장
    @Transactional // 위에가 (readOnly = true) 이기에 해줘야함 안해주면 저장 안됨 (우선권을 가지기 위해 오버라이팅 해버린것 )
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 준영속성 엔티티를 수정하는 방법 1. Dirty Checking (변경 감지)
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId); // id 를 기반으로 실제 db에 있는 영속성 엔티티 찾고 아래에서 값 변경해본다.
//        findItem.changd(name, price, stockQuantity); // 실제로는 의미있는 메소드를 만들어야 한다. 아래처럼 하나씩 set하면 안됨 -> 이렇게 해야 변경 지점이 엔티티로 간다. 엔티티 게벨에서 역추적 가능하게 설계해야함
        findItem.setName(name);   //  값을 파라미터에 넣어서 book 을 가지고 세팅
        findItem.setPrice(price);   // 스프링에 의해서 트랜잭션이 commit 된다. -> 그러면 JPA가 flush를 날린다. : jpa가 영속성 컨텐스에 있는 엔티티 중에 변경된 애 누구지?
        findItem.setStockQuantity(stockQuantity);   // 찾아서 오러니 하고 애가 지금 바뀌었네 하면 바뀐 값으로 업데이트 쿼리를 날려서 업데이트 쳐버림
//        itemRepository.save(findItem); // save 를 해줄 호출할 필요가 없다.
    }
    // 2. Merge (병합) : 준영속성 상태 엔티티를 영속 상태로 변경할 때 사용
    // merge를 가급적 사용하지 않고 최대한 변경 감지로 해주는게 좋다.
    // 위의 변경 감지 방법이 Merge로 작동한 것
    // 동작 방식은 위의 코드와 똑같다.  jpa가 대신 자동으로 해주는  것 -> 모든 값을 바꿔치기
    // 마지막에 찾은값을 return 해주는 것
//    @Transactional
//    public void updateItem(Long itemId, Book param) {
//        Item findItem = itemRepository.findOne(itemId); // id 를 기반으로 실제 db에 있는 영속성 엔티티 찾고 아래에서 값 변경해본다.
//        findItem.setPrice(param.getPrice()); //  값을 파라미터에 넣어서 book 을 가지고 세팅
//        findItem.setName(param.getName());   // 스프링에 의해서 트랜잭션이 commit 된다. -> 그러면 JPA가 flush를 날린다. : jpa가 영속성 컨텐스에 있는 엔티티 중에 변경된 애 누구지?
//        findItem.setStockQuantity(param.getStockQuantity());   // 찾아서 오러니 하고 애가 지금 바뀌었네 하면 바뀐 값으로 업데이트 쿼리를 날려서 업데이트 쳐버림
//        return findItem;
//    }


    // 조회  // Transactional 따로 안해주기에 맨 위 전체 @Transactional(readOnly = true) 가 먹힌다.
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    // 단건 조회
    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

}
